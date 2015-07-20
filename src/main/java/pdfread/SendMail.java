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
 * ʹ��SMTPЭ�鷢�͵����ʼ�
 */
public class SendMail {

    // �ʼ�����Э��
    private static String PROTOCOL = "smtp";

    // SMTP�ʼ�������
    private static String HOST = "smtp.qq.com";

    // SMTP�ʼ�������Ĭ�϶˿�
    private static String PORT = "465";

    // �Ƿ�Ҫ�������֤
    private static String IS_AUTH = "true";

    // �Ƿ����õ���ģʽ�����õ���ģʽ�ɴ�ӡ�ͻ������������������ʱһ��һ�����Ӧ��Ϣ��
    private static String IS_ENABLED_DEBUG_MOD = "true";

    // ������
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
        props.setProperty("mail.smtp.auth", "true");// ���� ��ͨ�ͻ���
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
     * ���ʹ���ǶͼƬ�����������ռ���(��ʾ��������)���ʼ����ȼ����Ķ���ִ��������HTML�ʼ�
     */
    public static void sendMultipleEmail(MailMode mailMode) throws Exception {
        System.out.println("send mail start!!!!!!!!!!!!!!!!!!");
        Properties props = getProps();

        String charset = "utf-8"; // ָ�����ı����ʽ
        // ����Sessionʵ������
        Session session = Session.getInstance(props, new MyAuthenticator());

        // ����MimeMessageʵ������
        MimeMessage message = new MimeMessage(session);
        // ��������

        message.setSubject(mailMode.getTitle());
        // ���÷�����
        message.setFrom(new InternetAddress(from, "������-�غ������ֹ�˾",
                charset));
        // �����ռ���
        message.setRecipients(RecipientType.TO, new Address[] {
        // ����1�������ַ������2���������ڿͻ����ռ�ֻ��ʾ������������ʾ�ʼ���ַ��������3�����������ַ�������
                new InternetAddress(mailMode.getTo(), "", charset) });
        // ���ó���
        String[] ccMail = cc.split(",");
        Address[] addressCc = new Address[ccMail.length];
        for (int i=0; i<ccMail.length; i++){
            addressCc[i] = new InternetAddress(ccMail[i], "", charset);
        }

        message.setRecipients(RecipientType.CC, addressCc);
        // ��������
        // message.setRecipient(RecipientType.BCC, new
        // InternetAddress("93091620@qq.com", "����_QQ", charset));
        // ���÷���ʱ��
        message.setSentDate(new Date());
        // ���ûظ���(�ռ��˻ظ����ʼ�ʱ,Ĭ���ռ���)
         message.setReplyTo(InternetAddress.parse("\"" +
         MimeUtility.encodeText("") + "\" <" + ccMail[0] + ">"));
        // �������ȼ�(1:���� 3:��ͨ 5:��)
        message.setHeader("X-Priority", "3");
        // Ҫ���Ķ���ִ(�ռ����Ķ��ʼ�ʱ����ʾ�ظ�������,�����ʼ����յ�,�����Ķ�)
        // message.setHeader("Disposition-Notification-To", from);

        // ����һ��MIME������Ϊ"mixed"��MimeMultipart���󣬱�ʾ����һ����������͵��ʼ�
        MimeMultipart mailContent = new MimeMultipart("mixed");
        message.setContent(mailContent);

        for (String attachFile : mailMode.getAttachFiles()) {
            // ����
            MimeBodyPart attach1 = new MimeBodyPart();

            // ��������������ӵ��ʼ�����
            mailContent.addBodyPart(attach1);
            // ����1(����jaf��ܶ�ȡ����Դ�����ʼ���)
            File attachFileObj = new File(attachFile);
            DataSource ds1 = new FileDataSource(attachFileObj);
            DataHandler dh1 = new DataHandler(ds1);
            int index = attachFileObj.getName().indexOf("_");
            String attachFileName = attachFileObj.getName().substring(0, index)
                    + ".pdf";
            attach1.setFileName(MimeUtility.encodeText(attachFileName));
            attach1.setDataHandler(dh1);
        }
        // ����
        MimeBodyPart mailBody = new MimeBodyPart();

        mailContent.addBodyPart(mailBody);

        // �ʼ�����(��ǶͼƬ+html�ı�)
        MimeMultipart body = new MimeMultipart("related"); // �ʼ�����Ҳ��һ�������,��Ҫָ����Ϲ�ϵ
        mailBody.setContent(body);

        // �ʼ�������html��ͼƬ����
        MimeBodyPart imgPart = new MimeBodyPart();
        MimeBodyPart htmlPart = new MimeBodyPart();
        body.addBodyPart(imgPart);
        body.addBodyPart(htmlPart);

        // ����ͼƬ
        DataSource ds3 = new FileDataSource(mailMode.getLogoFile());
        DataHandler dh3 = new DataHandler(ds3);
        imgPart.setDataHandler(dh3);
        String logoFileName = new File(mailMode.getLogoFile()).getName();
        imgPart.setContentID(logoFileName);

        // html�ʼ�����
        MimeMultipart htmlMultipart = new MimeMultipart("alternative");
        htmlPart.setContent(htmlMultipart);
        MimeBodyPart htmlContent = new MimeBodyPart();
        htmlContent.setContent(mailMode.getContent()
                + "<span style='color:black'></br>"
                + "<img src='cid:" + logoFileName + "' /></span>",
                "text/html;charset=utf-8");
        htmlMultipart.addBodyPart(htmlContent);

        // �����ʼ������޸�
        message.saveChanges();

        /*
         * File eml = buildEmlFile(message); sendMailForEml(eml);
         */

        // �����ʼ�
        Transport.send(message);
        
        System.out.println("send mail ok!!!!!!!!!!!!!!!!!!");
    }

    /**
     * ���ʼ��������ύ��֤��Ϣ
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