package pdfread;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import com.sun.mail.util.MailSSLSocketFactory;

/**
 * 使用SMTP协议发送电子邮件
 */
public class SendMail {

    // 邮件发送协议
    private static String PROTOCOL = "smtp";

    // SMTP邮件服务器
    private static String HOST = "smtp.qq.com";

    // SMTP邮件服务器默认端口
    private static String PORT = "465";

    // 是否要求身份认证
    private static String IS_AUTH = "true";

    // 是否启用调试模式（启用调试模式可打印客户端与服务器交互过程时一问一答的响应消息）
    private static String IS_ENABLED_DEBUG_MOD = "true";

    // 发件人
    private static String from = "93091620@qq.com";
    private static String cc = "93091620@qq.com";
    
    private static String username = "";
    private static String password = "abcd@1234";

    public static void loadProps(String filePath) throws Exception {
        Properties properties = new Properties();
        InputStream in = null;
        FileInputStream fis = null;
        try {
            System.out.println(filePath);
            fis = new FileInputStream(filePath);
            in = new BufferedInputStream (fis);
            properties.load(in);
            HOST = properties.getProperty("mail.smtp.host");
            PORT = properties.getProperty("mail.smtp.port");
            from = properties.getProperty("from");
            cc = properties.getProperty("cc");
            username = properties.getProperty("username");
            password = properties.getProperty("password");
            in.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    public static Properties getProps() throws Exception {

        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", PROTOCOL);
        props.setProperty("mail.smtp.host", HOST);
        props.setProperty("mail.smtp.port", PORT);
        props.setProperty("mail.smtp.auth", IS_AUTH);
        props.setProperty("mail.debug", IS_ENABLED_DEBUG_MOD);
        props.setProperty("mail.smtp.auth", "true");// 必须 普通客户端
        props.put("mail.debug", "true");
        props.put("mail.debug.auth", "true");
        //
        MailSSLSocketFactory sf = null;
        try {
            sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
        } catch (GeneralSecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw e;
        }
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.ssl.socketFactory", sf);
        return props;
    }

    /**
     * 发送带内嵌图片、附件、多收件人(显示邮箱姓名)、邮件优先级、阅读回执的完整的HTML邮件
     */
    public static void sendMultipleEmail(MailMode mailMode) throws Exception {
        System.out.println("send mail start!!!!!!!!!!!!!!!!!!");
        Properties props = getProps();

        String charset = "utf-8"; // 指定中文编码格式
        // 创建Session实例对象
        Session session = Session.getInstance(props, new MyAuthenticator());

        // 创建MimeMessage实例对象
        MimeMessage message = new MimeMessage(session);
        // 设置主题

        message.setSubject(mailMode.getTitle());
        // 设置发送人
        message.setFrom(new InternetAddress(from, "中外运-敦豪辽宁分公司",
                charset));
        // 设置收件人
        message.setRecipients(RecipientType.TO, new Address[] {
        // 参数1：邮箱地址，参数2：姓名（在客户端收件只显示姓名，而不显示邮件地址），参数3：姓名中文字符串编码
                new InternetAddress(mailMode.getTo(), "", charset) });
        // 设置抄送
        String[] ccMail = cc.split(",");
        Address[] addressCc = new Address[ccMail.length];
        for (int i=0; i<ccMail.length; i++){
            addressCc[i] = new InternetAddress(ccMail[i], "", charset);
        }

        message.setRecipients(RecipientType.CC, addressCc);
        // 设置密送
        // message.setRecipient(RecipientType.BCC, new
        // InternetAddress("93091620@qq.com", "赵六_QQ", charset));
        // 设置发送时间
        message.setSentDate(new Date());
        // 设置回复人(收件人回复此邮件时,默认收件人)
         message.setReplyTo(InternetAddress.parse("\"" +
         MimeUtility.encodeText("") + "\" <" + ccMail[0] + ">"));
        // 设置优先级(1:紧急 3:普通 5:低)
        message.setHeader("X-Priority", "3");
        // 要求阅读回执(收件人阅读邮件时会提示回复发件人,表明邮件已收到,并已阅读)
        // message.setHeader("Disposition-Notification-To", from);

        // 创建一个MIME子类型为"mixed"的MimeMultipart对象，表示这是一封混合组合类型的邮件
        MimeMultipart mailContent = new MimeMultipart("mixed");
        message.setContent(mailContent);

        for (String attachFile : mailMode.getAttachFiles()) {
            // 附件
            MimeBodyPart attach1 = new MimeBodyPart();

            // 将附件和内容添加到邮件当中
            mailContent.addBodyPart(attach1);
            // 附件1(利用jaf框架读取数据源生成邮件体)
            File attachFileObj = new File(attachFile);
            DataSource ds1 = new FileDataSource(attachFileObj);
            DataHandler dh1 = new DataHandler(ds1);
            int index = attachFileObj.getName().indexOf("_");
            String attachFileName = attachFileObj.getName().substring(0, index)
                    + ".pdf";
            attach1.setFileName(MimeUtility.encodeText(attachFileName));
            attach1.setDataHandler(dh1);
        }
        // 内容
        MimeBodyPart mailBody = new MimeBodyPart();

        mailContent.addBodyPart(mailBody);

        // 邮件正文(内嵌图片+html文本)
        MimeMultipart body = new MimeMultipart("related"); // 邮件正文也是一个组合体,需要指明组合关系
        mailBody.setContent(body);

        // 邮件正文由html和图片构成
        MimeBodyPart imgPart = new MimeBodyPart();
        MimeBodyPart htmlPart = new MimeBodyPart();
        body.addBodyPart(imgPart);
        body.addBodyPart(htmlPart);

        // 正文图片
        DataSource ds3 = new FileDataSource(mailMode.getLogoFile());
        DataHandler dh3 = new DataHandler(ds3);
        imgPart.setDataHandler(dh3);
        String logoFileName = new File(mailMode.getLogoFile()).getName();
        imgPart.setContentID(logoFileName);

        // html邮件内容
        MimeMultipart htmlMultipart = new MimeMultipart("alternative");
        htmlPart.setContent(htmlMultipart);
        MimeBodyPart htmlContent = new MimeBodyPart();
        htmlContent.setContent(mailMode.getContent()
                + "<span style='color:black'></br>"
                + "<img src='cid:" + logoFileName + "' /></span>",
                "text/html;charset=utf-8");
        htmlMultipart.addBodyPart(htmlContent);

        // 保存邮件内容修改
        message.saveChanges();

        /*
         * File eml = buildEmlFile(message); sendMailForEml(eml);
         */

        // 发送邮件
        Transport.send(message);
        
        System.out.println("send mail ok!!!!!!!!!!!!!!!!!!");
    }

    /**
     * 向邮件服务器提交认证信息
     */
    static class MyAuthenticator extends Authenticator {
        public MyAuthenticator() {
            super();
        }
        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(username, password);
        }
    }
}