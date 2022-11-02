package com.sjj.taskmanagement.service;

public interface MailService {
    /**
     * 发送简单的文字邮件
     *
     * @param receiverMail 接受者邮箱
     * @param subject      邮件主题
     * @param content      邮件内容
     */
    void sendSimpleMail(String receiverMail, String subject, String content);
    /**
     * 发送html文件的邮件
     *
     * @param receiverMail 接受者邮箱
     * @param subject      邮件主题
     * @param content      邮件内容
     */
    void sendHtmlMail(String receiverMail, String subject, String content);
    /**
     * 发送带有附件的邮件
     *
     * @param receiverMail 接受者邮箱
     * @param subject      邮件主题
     * @param content      邮件内容
     * @param filePath     附件文件地址
     */
    void sendAttachmentsMail(String receiverMail, String subject, String content, String filePath);
}

