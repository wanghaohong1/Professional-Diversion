package com.glxy.pro.service.impl;

import com.glxy.pro.entity.User;
import com.glxy.pro.service.IEmailService;
import com.glxy.pro.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements IEmailService {
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private IUserService userService;

    /**
     * 发送简单文本邮件
     */
    public void sendSimpleMail(String receiveEmail, String subject, String content) {
        User admin = userService.getById("admin");
        String fromEmail = admin.getEmail();
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(receiveEmail);
        message.setSubject(subject);
        message.setText(content);
        mailSender.send(message);
    }

    /**
     * 发送Html格式的邮件
     */
    public void sendHtmlMail(String receiveEmail, String subject, String emailContent) throws MessagingException {
        User admin = userService.getById("admin");
        String fromEmail = admin.getEmail();
        init(receiveEmail, subject, emailContent, mailSender, fromEmail);
    }


    public static void init(String receiveEmail, String subject, String emailContent, JavaMailSender mailSender, String fromEmail) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(fromEmail);
        helper.setTo(receiveEmail);
        helper.setSubject(subject);
        helper.setText(emailContent, true);
        mailSender.send(message);
    }
}
