package com.spring.boot.community.service;

import java.util.Set;

import javax.sound.midi.VoiceStatus;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;


import org.thymeleaf.context.Context;

import com.spring.boot.community.email.image.ImageResource;


public interface EmailService {
	void sendSimpleTextMailActual(String subject,String content,String[] toWho,String[] ccPeoples,String[] bccPeoples,String[] attachments);
	void handleAttachment(MimeMessageHelper mimeMessageHelper,String subject,String[] attachmentFilePaths);
	void handleBasicInfo(SimpleMailMessage simpleMailMessage,String subject,String content,String[] toWho,String[] ccPeoples,String[] bccPeoples);
	boolean handleBasicInfo(MimeMessageHelper mimeMessageHelper,String subject,String content,String[] toWho,String[] ccPeoples,String[] bccPeoples,boolean isHtml);
	
	
	void sendHtmlTemplateMailActual(String subject,String[] toWho,String[] ccPeoples,String[] bccPeoples,String[] attachments,String templateName,Context context,Set<ImageResource> imageResourceSet);
	
    String handleInLineImageResourceContent(MimeMessageHelper mimeMessageHelper,String subject,String originContent,Set<ImageResource> imageResourceSet);
    
    void handleInLineImageResource(MimeMessageHelper mimeMessageHelper,String subject,Set<ImageResource> imageResourceSet);

    
    boolean assertNotNull(Object... args);
	
}
