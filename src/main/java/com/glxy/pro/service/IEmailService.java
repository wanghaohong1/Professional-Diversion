package com.glxy.pro.service;

import javax.mail.MessagingException;

public interface IEmailService {
    void sendHtmlMail(String email, String mailSubject, String emailContent) throws MessagingException;
}
