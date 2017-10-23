package com.caro.smartmodule.helpers.ExceptionHelpers;

import android.os.Build;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class JavaEmail {


    /**
     * send email
     * testEmail = "2419625609@qq.com";
     * password = "carolily520"; // 密码为且需要邮箱独立密码，并需要开通smtp功能
     * @param fromerDesc
     * @param loginEmail
     * @param loginPasswd
     * @param recerverEmail
     * @param sendContent
     */
    public static void sendEmail(String fromerDesc, String loginEmail, String loginPasswd, String recerverEmail, String sendContent) {
        Properties properties = new Properties();
        properties.setProperty("mail.transport.protocol", "smtp");// Send mail	// protocol
        properties.setProperty("mail.smtp.auth", "true");// Need to verify
        properties.setProperty("mail.debug", "true");//Background debug mode
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.host", "smtp.qq.com");
        properties.put("mail.smtp.port", "220");
        MyAuthenticator smyauth = new MyAuthenticator(loginEmail, loginPasswd);
        // setup process output messages sent
        Session session = Session.getInstance(properties, smyauth);
        session.setDebug(true);// debug mode
        // Email message
        Message messgae = new MimeMessage(session);
        try {
            messgae.setFrom(new InternetAddress(loginPasswd));// set sender
            messgae.setText(sendContent);// set the messgae content


            messgae.setSubject(fromerDesc);// set the message subject
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        try {
            // Send e-mail
            Transport tran = session.getTransport();
            // tran.connect("smtp.sohu.com", 25, "xxx@sohu.com", "xxxx");//sohu-mail
            // server to connect to
            // tran.connect("smtp.sina.com", 25, "xxx@sina.cn",
            // "xxxxxxx");//Sina-mail server to connect to
            // tran.connect("smtp.qq.com", 25, "xxx@qq.com", "xxxx");//qq-mail
            // server to connect to
            tran.connect("smtp.qq.com", 25, loginEmail, loginPasswd);
            tran.sendMessage(messgae, new Address[]{new InternetAddress(recerverEmail)});// Set// mail// recipient (email)
            tran.close();
        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }

    public static void sendEmailBUGForDevloper(String formApp, Throwable e, String email, String emailPassword) throws AddressException, MessagingException {
        Properties properties = new Properties();
        properties.setProperty("mail.transport.protocol", "smtp");// Send mail	// protocol
        properties.setProperty("mail.smtp.auth", "true");// Need to verify
        properties.setProperty("mail.debug", "true");//Background debug mode
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.host", "smtp.qq.com");
        properties.put("mail.smtp.port", "220");
        MyAuthenticator smyauth = new MyAuthenticator(email, emailPassword);
        // setup process output messages sent
        Session session = Session.getInstance(properties, smyauth);
        session.setDebug(true);// debug mode
        // Email message
        Message messgae = new MimeMessage(session);
        messgae.setFrom(new InternetAddress(email));// set sender
        messgae.setText(getErrorInfoFromException(e));// set the messgae content
        System.out.println(getErrorInfoFromException(e));
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");//可以方便地修改日期格式
        String hehe = dateFormat.format(now);
        messgae.setSubject("author:caro-->recv" + formApp + "   异常反馈,时间:" + hehe);// set the message subject
        // Send e-mail
        Transport tran = session.getTransport();

        // tran.connect("smtp.sohu.com", 25, "xxx@sohu.com", "xxxx");//sohu-mail
        // server to connect to
        // tran.connect("smtp.sina.com", 25, "xxx@sina.cn",
        // "xxxxxxx");//Sina-mail server to connect to
        // tran.connect("smtp.qq.com", 25, "xxx@qq.com", "xxxx");//qq-mail
        // server to connect to

        tran.connect("smtp.qq.com", 25, email, emailPassword);
        tran.sendMessage(messgae, new Address[]{new InternetAddress("1025807062@qq.com")});// Set// mail// recipient (email)  test:2376323219@qq.com  452262448@qq.com  1025807062@qq.com
        tran.close();
    }

    public static String getErrorInfoFromException(Throwable e) {
        try {
            StringBuilder sb = new StringBuilder();
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            // return "Exception from:" + getDeviceMsg()+"\r\n" + sw.toString()
            // + "\r\n";
            return (sb.append("Exception from:").append(getDeviceMsg())
                    .append("\r\n").append(sw.toString()).append("\r\n"))
                    .toString();
        } catch (Exception e2) {
            e2.printStackTrace();
            return "bad getErrorInfoFromException";
        }
    }

    /**
     * Get phone version information
     *
     * @return String
     */
    public static String getDeviceMsg() {

        StringBuilder sb = new StringBuilder();
        Field[] fields = Build.class.getFields();

        for (Field field : fields) {
            field.setAccessible(true); // Reflection violence
            String key = field.getName();// eg:BOARD BOOTLOADER unknown
            String value;
            try {
                value = field.get(null).toString();// eg:MSM8610 unknown Coolpad
                sb.append(key).append("=").append(value).append("\n");
            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return sb.toString();

    }

    public static class MyAuthenticator extends Authenticator {
        public String strUser;
        public String strPwd;

        public MyAuthenticator(String user, String password) {
            this.strUser = user;
            this.strPwd = password;
        }

        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(strUser, strPwd);
        }
    }

}
