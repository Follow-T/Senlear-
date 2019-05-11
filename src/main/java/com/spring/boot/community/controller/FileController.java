package com.spring.boot.community.controller;

import com.spring.boot.community.domin.Article;
import com.spring.boot.community.service.ArticleService;
import com.spring.boot.community.tools.AliOssUtils;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.spring.boot.community.domin.Comment;
import com.spring.boot.community.domin.Reply;
import com.spring.boot.community.domin.User;
import com.spring.boot.community.service.CommentService;
import com.spring.boot.community.service.ReplyService;
import com.spring.boot.community.service.UserService;
import com.spring.boot.community.tools.JsonData;


import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.*;

/**
 * 文件服务器
 * Created by wangfan on 2018-12-24 下午 4:10.
 */
@RestController
public class FileController {
	
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CommentService commentService;

    @Autowired
    private ArticleService articleService;

	@Autowired
	private ReplyService replyService;
	
    @Value("${fs.dir}")
    private String fileDir;
    @Value("${fs.uuidName}")
    private Boolean uuidName;
    @Value("${fs.useSm}")
    private Boolean useSm;
    @Value("${fs.useNginx}")
    private Boolean useNginx;
    @Value("${fs.nginxUrl}")
    private String nginxUrl;

    //上传文件
    @ResponseBody
    @PostMapping("/file/upload")
    public Map fileupload(@RequestParam MultipartFile file,String filepath) {
        if (fileDir == null) {
            fileDir = "/";
        }
        if (!fileDir.endsWith("/")) {
            fileDir += "/";
        }
        if (!filepath.endsWith("/")) {
        	filepath += "/";
        }
        // 文件原始名称
        String originalFileName = file.getOriginalFilename();
        String suffix = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
        String prefix = originalFileName.substring(0, originalFileName.lastIndexOf("."));
        // 保存到磁盘
        User loginuser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	    String username=loginuser.getUsername();
  
       
        try {
        	AliOssUtils a =new AliOssUtils();
            String path=a.upload(file,"senlear/user/"+username+filepath+originalFileName);
            System.out.println(path);
            Map rs = getRS(200, "上传成功", path);
           
            return rs;
        } catch (Exception e) {
            e.printStackTrace();
            return getRS(500, e.getMessage());
        }
    }  
    
    //上传头像
    @ResponseBody
    @PostMapping("/{username}/upload")
    public JsonData upload(@RequestParam MultipartFile file,@PathVariable("username") String username) {
    	JsonData jsonData;
		if (SecurityContextHolder.getContext().getAuthentication() !=null && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
				 &&  !SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")) {
			User loginuser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal(); 
			if(loginuser.getUsername().equals(username)){
		        // 文件原始名称
		        String originalFileName = file.getOriginalFilename();
		        String suffix = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
		        String prefix = originalFileName.substring(0, originalFileName.lastIndexOf("."));
		        String path = "user/"+username+"/avatar/" + originalFileName;
		        try { 
		            System.out.println(path);
		            AliOssUtils a =new AliOssUtils();
		            String avatar=a.upload(file,fileDir+path);
					loginuser.setAvatar(avatar);
					List<Comment> comments=commentService.ListCommentByUserid(loginuser.getId()+"");
					for(Comment comment:comments) {
						comment.setUseravatar(avatar);
						commentService.SaveComment(comment);
					}
					List<Reply> replies=replyService.ListReplyByUserid(loginuser.getId());
					for(Reply reply:replies) {
						reply.setUseravatar(avatar);
						replyService.saveReply(reply);
					}
					userService.saveUser(loginuser);
		            jsonData=new JsonData("200", "Success", "上传成功!", "tg");       
		        } catch (Exception e) {
		            e.printStackTrace();
		            jsonData=new JsonData("201", "Error", "上传出错!", "tg");
		            return jsonData;
		        }
			}else {
				jsonData=new JsonData("201", "Error", "操作用户不为本人!", "tg");
			}
				
		}else {
			jsonData=new JsonData("202", "Error", "请先登录!", "tg");
		}
		return jsonData;
        
    }

   
    /**
     * 查看原文件
     */
    @GetMapping("/file/senlear/{y}/{m}/{d}/{file:.+}")
    public String fileSm(@PathVariable("y") String y, @PathVariable("m") String m, @PathVariable("d") String d, @PathVariable("file") String filename, HttpServletResponse response) {
        String filePath = "senlear/" + y + "/" + m + "/" + d + "/" + filename;
        if (useNginx) {
            if (nginxUrl == null) {
                nginxUrl = "/";
            }
            if (!nginxUrl.endsWith("/")) {
                nginxUrl += "/";
            }
            String newName;
            try {
                newName = URLEncoder.encode(filePath, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                newName = filePath;
            }
            return "redirect:" + nginxUrl + newName;
        }
        /*if (fileDir == null) {
            fileDir = "/";
        }
        if (!fileDir.endsWith("/")) {
            fileDir += "/";
        }*/
        outputFile(filePath, response);
        return null;
    }

    // 输出文件流
    private void outputFile(String filepath, HttpServletResponse response) {
        // 判断文件是否存在
        File inFile = new File("https://get-blog.oss-cn-shanghai.aliyuncs.com/"+filepath);
        if (!inFile.exists()) {
            PrintWriter writer = null;
            try {
                response.setContentType("text/html;charset=UTF-8");
                writer = response.getWriter();
                writer.write("<!doctype html><title>404 Not Found</title><h1 style=\"text-align: center\">404 Not Found</h1><hr/><p style=\"text-align: center\">Easy File Server</p>");
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        // 获取文件类型
        String contentType = null;
        try {
            // Path path = Paths.get(inFile.getName());
            // contentType = Files.probeContentType(path);
            contentType = new Tika().detect(inFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (contentType != null) {
            response.setContentType(contentType);
        } else {
            response.setContentType("application/force-download");
            String newName;
            try {
                newName = URLEncoder.encode(inFile.getName(), "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                newName = inFile.getName();
            }
            response.setHeader("Content-Disposition", "attachment;fileName=" + newName);
        }
        // 输出文件流
        OutputStream os = null;
        FileInputStream is = null;
        try {
            is = new FileInputStream(inFile);
            os = response.getOutputStream();
            byte[] bytes = new byte[1024];
            int len;
            while ((len = is.read(bytes)) != -1) {
                os.write(bytes, 0, len);
            }
            os.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * 获取全部文件
     */
    @RequestMapping("/file/list")
    public Map listfile(String dir, String accept, String exts) {
    	 System.out.println(dir);
    	 if (fileDir == null) {
             fileDir = "/";
         }
         if (!fileDir.endsWith("/")) {
             fileDir += "/";
         }
         Map<String, Object> rs = new HashMap<>();
         if (dir == null || "/".equals(dir)) {
             dir = "";
         } else if (dir.startsWith("/")) {
             dir = dir.substring(1);
             dir=dir+"/";
         }
         
		User loginuser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	    String username=loginuser.getUsername();
         
    	List<Map> dataList = new AliOssUtils().getOssPresentFileList("senlear/user/"+username+"/"+dir);
    	rs.put("code", 200);
        rs.put("msg", "查询成功");
        rs.put("data", dataList);
        return rs;
    }

    /**
     * 获取用户分享的文件
     */
    @RequestMapping("/{username}/file/list/{articleid}")
    public Map listsharefile(String dir, String accept, String exts,@PathVariable("username") String username,@PathVariable("articleid") Long articleid) {
        if (fileDir == null) {
            fileDir = "/";
        }
        if (!fileDir.endsWith("/")) {
            fileDir += "/";
        }
        Map<String, Object> rs = new HashMap<>();
        if (dir == null || "/".equals(dir)) {
            dir = "";
        } else if (dir.startsWith("/")) {
            dir = dir.substring(1);
            dir=dir+"/";
        }
        Article article=articleService.getArticleById(articleid);
        String resourceurl=article.getResourceurl();
        
        String str[]=resourceurl.split("=")[1].split("/");
        resourceurl="";
        for(int i=6;i<str.length;i++) {
        	resourceurl=resourceurl+str[i]+"/";
        }
        resourceurl = resourceurl.substring(0,resourceurl.length() - 1);
        System.out.println("111111111");
        System.out.println(resourceurl+dir);
        List<Map> dataList = new AliOssUtils().getOssPresentFileList("senlear/user/"+username+"/"+resourceurl+dir);
        rs.put("code", 200);
        rs.put("msg", "查询成功");
        rs.put("data", dataList);
        return rs;
    }

    /**
     * 删除
     */
    @RequestMapping("/file/del")
    public Map del(String file) {
        if (fileDir == null) {
            fileDir = "/";
        }
        if (!fileDir.endsWith("/")) {
            fileDir += "/";
        }
        if (file != null && !file.isEmpty()) {
            
           
           String filepath=file.replace("://get-blog.oss-cn-shanghai.aliyuncs.com/", "");
           System.out.println(filepath);
           return new AliOssUtils().FileRemove(filepath);
        }
        return getRS(500, "删除失败");
    }

    

    // 封装返回结果
    private Map getRS(int code, String msg, String url) {
        Map<String, Object> map = new HashMap<>();
        map.put("code", code);
        map.put("msg", msg);
        if (url != null) {
            map.put("url", url);
        }
        return map;
    }

    // 封装返回结果
    private Map getRS(int code, String msg) {
        return getRS(code, msg, null);
    }
}
