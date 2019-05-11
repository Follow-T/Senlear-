package com.spring.boot.community.controller;

import com.spring.boot.community.tools.AliOssUtils;
import com.spring.boot.community.tools.UeditConfig;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;
import java.io.IOException;

@Controller
public class IndexController {
    //自动注入
    private  String upLoadDir;
    @GetMapping("/upload")
    public String upload() {
        return "upload";
    }

    @GetMapping("/FileRomve")
    @ResponseBody
    public Boolean FileRomve(){
        return false;
    }
    @GetMapping("/FileIsExits")
    @ResponseBody
    public Boolean FileIsExits(){
        return false;
    }
    @GetMapping("/getOssFiles")
    @ResponseBody
    public String getOssFiles(){
        new AliOssUtils().getOssFileList("test");
        return "";
    }
    @GetMapping("/getConfig")
    @ResponseBody
    public String getConfig(){
        //返回UeditConfig 配置  @ResponseBody只返回string
        return UeditConfig.config;
    }
    @RequestMapping("/upload")
    @ResponseBody
    public String upload(@RequestParam(value = "upfile") MultipartFile file, ServletRequest request){

        if (file.isEmpty()){
            return "error";
        }
        try {
            //获得文件的字节流
//            HttpServletRequest req = (HttpServletRequest) request;
//            upLoadDir=req.getSession().getServletContext().getRealPath("/");
            byte[] bytes=file.getBytes();
            //获得path对象，也即是文件保存的路径对象
            String fileName = file.getOriginalFilename();
//            Path path= Paths.get(upLoadDir+"/"+file.getOriginalFilename());
//            System.out.println(path);
//            //调用静态方法完成将文件写入到目标路径
//            Files.write(path,bytes);
            //上传到oss
            String url = new AliOssUtils().upload(file,"senlear/user");
            //返回格式
            String Json = "{\"state\": \"SUCCESS\"," +
                    "\"url\": \"" + url+ "\"," +
                    "\"title\": \"" + fileName + "\"," +
                    "\"original\": \"" + fileName + "\"}";
            return Json;
        } catch (IOException e){
            System.out.println(e.toString());
        }
        return "error";
    }

}

