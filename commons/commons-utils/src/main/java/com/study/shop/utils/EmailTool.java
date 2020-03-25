package com.study.shop.utils;

import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

/**
 * @author Tiger
 * @date 2020-03-15
 * @see com.study.shop.utils
 **/
public class EmailTool {

    private final static String host="smtp.qq.com";
    private final static String from="1357958736@qq.com";
    private final static String password="xyzjzvxvsullfehc";

    /**
     * 发送邮件
     * @param mail 目标邮箱
     * @param subject 邮件标题
     * @param text 邮件内容
     * @throws MessagingException m
     * @throws UnsupportedEncodingException u
     */
    public static void send(String mail,String subject,String text) throws MessagingException, UnsupportedEncodingException {
        JavaMailSenderImpl jms = new JavaMailSenderImpl();
        jms.setHost(host);
        jms.setUsername(from);
        jms.setPassword(password);
        jms.setDefaultEncoding("Utf-8");
        Properties p = new Properties();
        p.setProperty("mail.smtp.auth", "true");
        p.setProperty("mail.smtp.ssl.enable", "true");
        p.setProperty("mail.smtp.starttls.enable", "true");
        p.setProperty("mail.smtp.starttls.required", "true");
        p.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        p.setProperty("mail.smtp.socketFactory.port", "465");
        p.setProperty("mail.smtp.port", "465");
        jms.setJavaMailProperties(p);
        MimeMessage mimeMessage = jms.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setFrom(from,"二手交易平台");
        helper.setTo(mail);
        helper.setSubject(subject);
        helper.setText(text);
        jms.send(mimeMessage);
    }
}
