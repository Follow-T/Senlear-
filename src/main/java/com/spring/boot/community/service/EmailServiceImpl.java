package com.spring.boot.community.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.spring.boot.community.email.image.ImageResource;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;

@Service
public class EmailServiceImpl implements EmailService{
	
    public static final String DEFAULT_ENCODING = "UTF-8";

    private Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

   
    @Value("${spring.mail.username}")
    private String userName;

   
    @Autowired
    private TemplateEngine templateEngine;

   
    @Autowired
    private JavaMailSender mailSender;

    @Override
	public void sendSimpleTextMailActual(String subject, String content, String[] toWho, String[] ccPeoples,
			String[] bccPeoples, String[] attachments) {
		
    	System.out.println(111111);
        if(subject == null||toWho == null||toWho.length == 0||content == null){

            logger.error("Error",subject);

            throw new RuntimeException("Error");
        }

        logger.info(">...",subject,toWho,ccPeoples,bccPeoples,attachments);

        
        if(attachments != null&&attachments.length > 0){

            try{
              
                MimeMessage mimeMessage = mailSender.createMimeMessage();

                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,true,DEFAULT_ENCODING);

              
                boolean continueProcess = handleBasicInfo(helper,subject,content,toWho,ccPeoples,bccPeoples,false);

               
                if(!continueProcess){

                    logger.error(">{}",subject);

                    return;
                }

                
                handleAttachment(helper,subject,attachments);

               
                mailSender.send(mimeMessage);

                logger.info("{}",subject);

            }catch(MessagingException e){
                e.printStackTrace();

                logger.error("{}",subject);
            }
        }else{

           
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

          
            handleBasicInfo(simpleMailMessage,subject,content,toWho,ccPeoples,bccPeoples);

   
            mailSender.send(simpleMailMessage);

            logger.info(">{}",subject,toWho,ccPeoples,bccPeoples,attachments);
        }
	}
    
    @Override
    public void handleAttachment(MimeMessageHelper mimeMessageHelper,String subject,String[] attachmentFilePaths){

  
        if(attachmentFilePaths != null&&attachmentFilePaths.length > 0){

            FileSystemResource resource;

            String fileName;

       
            for(String attachmentFilePath : attachmentFilePaths){

       
                resource = new FileSystemResource(new File(attachmentFilePath));

             
                if(!resource.exists()){

                    logger.warn("",subject,attachmentFilePath);

         
                    continue;
                }

  
                fileName = resource.getFilename();

                try{

     
                    mimeMessageHelper.addAttachment(fileName,resource);

                }catch(MessagingException e){

                    e.printStackTrace();

                    logger.error("->{}",subject,attachmentFilePath,e.getMessage());
                }
            }
        }
    }

	@Override
	public void handleBasicInfo(SimpleMailMessage simpleMailMessage, String subject, String content, String[] toWho,
			String[] ccPeoples, String[] bccPeoples) {


        simpleMailMessage.setFrom(userName);

        simpleMailMessage.setSubject(subject);

        simpleMailMessage.setText(content);

        simpleMailMessage.setTo(toWho);

        simpleMailMessage.setCc(ccPeoples);

        simpleMailMessage.setBcc(bccPeoples);

	}

	

	@Override
	public boolean handleBasicInfo(MimeMessageHelper mimeMessageHelper, String subject, String content, String[] toWho,
			String[] ccPeoples, String[] bccPeoples, boolean isHtml) {
		// TODO Auto-generated method stub
		try{
            mimeMessageHelper.setFrom(userName);
    
            mimeMessageHelper.setSubject(subject);
       
            mimeMessageHelper.setText(content,isHtml);
 
            mimeMessageHelper.setTo(toWho);

           

            if(ccPeoples != null)
               
                mimeMessageHelper.setCc(ccPeoples);

            if(bccPeoples != null)
                
                mimeMessageHelper.setBcc(bccPeoples);

            return true;
        }catch(MessagingException e){
            e.printStackTrace();

            logger.error(">{}",subject);
        }

		return false;
	}

	@Override
	public void sendHtmlTemplateMailActual(String subject, String[] toWho, String[] ccPeoples, String[] bccPeoples,
			String[] attachments, String templateName, Context context,
			Set<ImageResource> imageResourceSet) {


        if(subject == null||toWho == null||toWho.length == 0||templateName == null){

            logger.error("",subject);

            throw new RuntimeException("");
        }

   
        logger.info("",subject,toWho,ccPeoples,bccPeoples,attachments,templateName,context,imageResourceSet);

        try{

     
            if(context == null){

                context = new Context();
                logger.info("",subject);
            }

        
            String content = templateEngine.process(templateName,context);

            MimeMessage mimeMessage = mailSender.createMimeMessage();

          
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,true,DEFAULT_ENCODING);

  
            content = handleInLineImageResourceContent(helper,subject,content,imageResourceSet);

            logger.info("->{}",content);

        
            boolean continueProcess = handleBasicInfo(helper,subject,content,toWho,ccPeoples,bccPeoples,true);

            if(!continueProcess){

                logger.error("{}",subject);

                return;
            }

      
            handleInLineImageResource(helper,subject,imageResourceSet);

   
            handleAttachment(helper,subject,attachments);

  
            mailSender.send(mimeMessage);

            logger.info("->{}",subject);

        }catch(MessagingException e){

            e.printStackTrace();

            logger.error("->{}",subject);
        }
	}

	@Override
	public String handleInLineImageResourceContent(MimeMessageHelper mimeMessageHelper, String subject,
			String originContent, Set<ImageResource> imageResourceSet) {
	

        if(imageResourceSet != null&&imageResourceSet.size() > 0){

      
            String rscId;
     
            String resourcePath = null;
      
            String placeHolder;
    
            FileSystemResource resource;

            for(ImageResource imageResource : imageResourceSet){

                rscId = imageResource.getId();
                placeHolder = imageResource.getPlaceholder();
                resourcePath = imageResource.getImageFilePath();

                resource = new FileSystemResource(new File(resourcePath));

   
                if(!resource.exists()){

                    logger.warn("",subject,resourcePath);

                    continue;
                }

                originContent = originContent.replace("\"" + ImageResource.PLACEHOLDERPREFIX + placeHolder + "\"","\'cid:" + rscId + "\'");
            }
        }
        return originContent;
	}

	@Override
	public void handleInLineImageResource(MimeMessageHelper mimeMessageHelper, String subject,
			Set<ImageResource> imageResourceSet) {

		if(imageResourceSet != null&&imageResourceSet.size() > 0){

            FileSystemResource resource;

            for(ImageResource imageResource : imageResourceSet){

                resource = new FileSystemResource(new File(imageResource.getImageFilePath()));

                if(!resource.exists()){

                    logger.warn("",subject,imageResource);

                    continue;
                }

                try{

                 
                    mimeMessageHelper.addInline(imageResource.getId(),resource);

                }catch(MessagingException e){
                    e.printStackTrace();

                    logger.error("",subject,imageResource);
                }
            }
        }
	}

	@Override
	public boolean assertNotNull(Object... args) {

		return Arrays.stream(args).noneMatch(Objects::isNull);
	}

}
