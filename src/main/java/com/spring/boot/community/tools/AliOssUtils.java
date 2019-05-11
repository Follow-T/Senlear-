package com.spring.boot.community.tools;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.ListObjectsRequest;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.ObjectListing;
import com.fasterxml.jackson.core.sym.Name;

import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * # OSS 配置
 * oss.endpoint=http://oss-cn-shanghai.aliyuncs.com
 * oss.accessKeyId=LTAICHWL4ouRcNfi
 * oss.accessKeySecret=wGZVwRzhNR59uAJYCieZHPKI3VVtiA
 * oss.bucketName=get-blog
 * oss.expireTime=3600
 */
public class AliOssUtils {
    //oss配置
    private static String endpoint="http://oss-cn-shanghai.aliyuncs.com";

    private static String accessKeyId="LTAICHWL4ouRcNfi";

    private static String accessKeySecret="wGZVwRzhNR59uAJYCieZHPKI3VVtiA";

    private static String bucketName="get-blog";

    private static Long expireTime=300L;
    private OSSClient ossClient;
    public AliOssUtils(){
       this.ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
    }
    public  String upload(MultipartFile file,String path){
        String url="";
        //文件不为空
        if(null == file){
            url="";
        }else{
            //不为空时创建
            try{
                //上传到OSS
                String objectName =path;
                ossClient.putObject(bucketName, objectName, new ByteArrayInputStream(file.getBytes()));
                url = "https://"+bucketName+".oss-cn-shanghai.aliyuncs.com/"+objectName;
            }catch (OSSException oe){
                System.out.println(oe.getMessage());
            }catch (ClientException ce){
                System.out.println(ce.getMessage());
            } catch (IOException e) {
                System.out.println(e.getMessage());
            } finally {
                //关闭
                ossClient.shutdown();
            }
        }
        return url;
    }
    public  void getOssFileList(String KeyPrefix){
        // 列举文件。 如果不设置KeyPrefix，则列举存储空间下所有的文件。KeyPrefix，则列举包含指定前缀的文件。
        ObjectListing objectListing = ossClient.listObjects(bucketName,KeyPrefix);
        List<OSSObjectSummary> sums = objectListing.getObjectSummaries();
        for (OSSObjectSummary s : sums){
        	System.out.println(s.getBucketName());
        	System.out.println(s.getETag());
        	System.out.println(s.getStorageClass());
        	System.out.println(s.getOwner());
            System.out.println("\t" + s.getKey());
        }
        // 关闭OSSClient。
        ossClient.shutdown();
    }
    
    public List<Map> getOssPresentFileList(String Prefix) {
    	
    	List<Map> dataList = new ArrayList<>();
    	// 构造ListObjectsRequest请求。
    	ListObjectsRequest listObjectsRequest = new ListObjectsRequest(bucketName);

    	// 设置正斜线（/）为文件夹的分隔符。
    	listObjectsRequest.setDelimiter("/");

    	// 列出fun目录下的所有文件和文件夹。
    	listObjectsRequest.setPrefix(Prefix);
    	
    	//System.out.println(Prefix);
    	ObjectListing listing = ossClient.listObjects(listObjectsRequest);

    	// 遍历所有文件。
    	System.out.println("Objects:");
    	// objectSummaries的列表中给出的是fun目录下的文件。
    	for (OSSObjectSummary objectSummary : listing.getObjectSummaries()) {
    		System.out.println(objectSummary.getKey());
    		Map<String, Object> m = new HashMap<>();
    		int len=objectSummary.getKey().split("/").length;
    		String filename=objectSummary.getKey().split("/")[len-1];
            m.put("name", filename);  // 文件名称
            m.put("updateTime", objectSummary.getLastModified());  // 修改时间
            m.put("isDir", false);  // 是否是目录
            
            Boolean hasurl=false;
            String suffix=filename.substring(filename.lastIndexOf(".") + 1);
            System.out.println(suffix);
            String type;
            // 获取文件图标
            if ("ppt".equalsIgnoreCase(suffix) || "pptx".equalsIgnoreCase(suffix)) {
                type = "ppt";
            } else if ("doc".equalsIgnoreCase(suffix) || "docx".equalsIgnoreCase(suffix)) {
                type = "doc";
            } else if ("xls".equalsIgnoreCase(suffix) || "xlsx".equalsIgnoreCase(suffix)) {
                type = "xls";
            } else if ("pdf".equalsIgnoreCase(suffix)) {
                type = "pdf";
            } else if ("html".equalsIgnoreCase(suffix) || "htm".equalsIgnoreCase(suffix)) {
                type = "htm";
            } else if ("txt".equalsIgnoreCase(suffix)) {
                type = "txt";
            } else if ("swf".equalsIgnoreCase(suffix)) {
                type = "flash";
            } else if ("zip".equalsIgnoreCase(suffix) || "rar".equalsIgnoreCase(suffix) || "7z".equalsIgnoreCase(suffix)) {
                type = "zip";
            } else if ("zip".equalsIgnoreCase(suffix) || "rar".equalsIgnoreCase(suffix) || "7z".equalsIgnoreCase(suffix)) {
                type = "zip";
            } else if ("mp3".equalsIgnoreCase(suffix)) {
                type = "mp3";
            } else if ("mp4".equalsIgnoreCase(suffix)) {
                type = "mp4";
            }/* else if (contentType != null && contentType.startsWith("image/")) {
                type = "file";
                m.put("hasSm", true);
                m.put("smUrl", m.get("url"));  // 缩略图地址
            }*/ else {
                type = "file";
                hasurl=false;
            }
            m.put("type", type);
            if("jpg".equalsIgnoreCase(suffix)||"jpeg".equalsIgnoreCase(suffix)||"png".equalsIgnoreCase(suffix)) {
            	hasurl=true;
            }
            m.put("hasurl", hasurl);
       
  
            m.put("url", "https://get-blog.oss-cn-shanghai.aliyuncs.com/"+objectSummary.getKey());
            dataList.add(m);
          
    	}

    	// 遍历所有commonPrefix。
    	System.out.println("\nCommonPrefixes:");
    	// commonPrefixs列表中给出的是fun目录下的所有子文件夹。fun/movie/001.avi和fun/movie/007.avi两个文件没有被列出来，因为它们属于fun文件夹下的movie目录。
    	for (String commonPrefix : listing.getCommonPrefixes()) {	
    		Map<String, Object> m = new HashMap<>();
    		int len=commonPrefix.split("/").length;
    		if("avatar".equals(commonPrefix.split("/")[len-1])) {
    			System.out.println("dui yong hu bu ke jian");
    		}else{
    			 m.put("name", commonPrefix.split("/")[len-1]);  // 文件名称
    	         m.put("updateTime", "111");  // 修改时间
    	         m.put("isDir", true);  // 是否是目录
    	         m.put("type", "dir");
    	         dataList.add(m);
    		}
           
    	    System.out.println(commonPrefix.split("/")[len-1]);
    	}
        // 把文件夹排在前面
        Collections.sort(dataList, new Comparator<Map>() {
            @Override
            public int compare(Map o1, Map o2) {
                Boolean l1 = (boolean) o1.get("isDir");
                Boolean l2 = (boolean) o2.get("isDir");
                return l2.compareTo(l1);
            }
        });
    	// 关闭OSSClient。
    	ossClient.shutdown();
    	
    	return dataList;
    }
    
    public  Map  FileRemove(String objectName){
    	Map<String, Object> m = new HashMap<>();
        // 删除文件。
        ossClient.deleteObject(this.bucketName, objectName);
        // 关闭OSSClient。
        m.put("code", "200");
        m.put("msg", "删除成功");
        ossClient.shutdown();    
        return m;
    }
}