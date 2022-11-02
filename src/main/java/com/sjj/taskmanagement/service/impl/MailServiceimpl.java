package com.sjj.taskmanagement.service.impl;


import com.sjj.taskmanagement.service.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

@Service
public class MailServiceimpl implements MailService {
    @Autowired
    private JavaMailSender mailSender;
    private static final Logger logger = LoggerFactory.getLogger(MailServiceimpl.class);
    public final String senderMail="1781088254@qq.com";
    @Override
    public void sendSimpleMail(String receiverMail, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(senderMail); //设置发送者邮箱
        message.setTo(receiverMail); //设置接受者邮箱
        message.setSubject(subject); //设置主题
        message.setText(content);    //设置发送内容
        mailSender.send(message);
        logger.info("简单邮件已经发送");
    }


    @Override
    public void sendHtmlMail(String receiverMail, String subject, String content) {

    }

    @Override
    public void sendAttachmentsMail(String receiverMail, String subject, String content, String filePath) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(senderMail);
            helper.setTo(receiverMail);
            helper.setSubject(subject);
            helper.setText(content, true);

            FileSystemResource file = new FileSystemResource(new File(filePath));
            String filename = file.getFile().getName();
            helper.addAttachment(filename, file);

            mailSender.send(message);
            logger.info("带附件的邮件已经发送。");

        } catch (MessagingException e) {
            e.printStackTrace();
            logger.error("发送带附件的邮件时发生异常！");
        }

    }

}
