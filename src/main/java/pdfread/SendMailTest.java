package pdfread;

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
public class SendMailTest {

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
    
    private static String user = "";
    private static String password = "abcd@1234";

    // �ռ���
    private static String to = "zhen.wang2012@hotmail.com";

    public static void loadProps(String file) throws Exception {
        Properties properties = new Properties();
        InputStream resourceAsStream = null;
        try {
            resourceAsStream = Object.class.getResourceAsStream(file);
            properties.load(resourceAsStream);
            HOST = properties.getProperty("mail.smtp.host");
            PORT = properties.getProperty("mail.smtp.port");
            from = properties.getProperty("from");
            cc = properties.getProperty("cc");
            user = properties.getProperty("username");
            password = properties.getProperty("password");

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (resourceAsStream != null) {
                try {
                    resourceAsStream.close();
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

    public static void main(String[] args) throws Exception {
        // �����ı��ʼ�
        sendTextEmail();

        // ���ͼ򵥵�html�ʼ�
        // sendHtmlEmail();

        sendHtmlWithInnerImageEmail();

        System.out.println(Thread.currentThread().getContextClassLoader()
                .getResource("firefoxlogo.png").getPath());
        System.out.println(ClassLoader.getSystemResource("���ѧ��C����.txt"));
        System.out.println(Thread.currentThread().getContextClassLoader()
                .getResource("���ѧ��C����.txt").getPath());
        Properties iframeproperties = new Properties();
        ClassLoader classloader = Thread.currentThread()
                .getContextClassLoader();
        System.out.println(classloader);
        classloader = iframeproperties.getClass().getClassLoader();
        System.out.println(classloader);
        if (true) {
            System.exit(0);
        }
        // ���ʹ���ǶͼƬ��HTML�ʼ�
        // sendHtmlWithInnerImageEmail();

        // ���ͻ������ʼ�
        sendMultipleEmail_back();

        // �����Ѿ����ɵ�eml�ʼ�
        // sendMailForEml();
    }

    /**
     * ���ͼ򵥵��ı��ʼ�
     */
    public static void sendTextEmail() throws Exception {
        // ����Sessionʵ������
        Properties props = getProps();
        Session session = Session.getDefaultInstance(props);

        // ����MimeMessageʵ������
        MimeMessage message = new MimeMessage(session);
        // ���÷�����
        message.setFrom(new InternetAddress(from));
        // �����ʼ�����
        message.setSubject("ʹ��javamail���ͼ��ı��ʼ�");
        // �����ռ���
        message.setRecipient(RecipientType.TO, new InternetAddress(to));
        // ���÷���ʱ��
        message.setSentDate(new Date());
        // ���ô��ı�����Ϊ�ʼ�����
        message.setText("ʹ��POP3Э�鷢���ı��ʼ�����!!!");
        // ���沢�������յ��ʼ�����
        message.saveChanges();

        // ���Transportʵ������
        Transport transport = session.getTransport();
        // ������
        transport.connect("93091620", "abcd@1234");
        // ��message���󴫵ݸ�transport���󣬽��ʼ����ͳ�ȥ
        transport.sendMessage(message, message.getAllRecipients());
        // �ر�����
        transport.close();
    }

    /**
     * ���ͼ򵥵�html�ʼ�
     */
    public static void sendHtmlEmail() throws Exception {
        Properties props = getProps();
        // ����Sessionʵ������
        Session session = Session.getInstance(props, new MyAuthenticator());
        session.setDebug(true);

        // ����MimeMessageʵ������
        MimeMessage message = new MimeMessage(session);
        // �����ʼ�����
        message.setSubject("html�ʼ�����");
        // ���÷�����
        message.setFrom(new InternetAddress(from));
        // ���÷���ʱ��
        message.setSentDate(new Date());
        // �����ռ���
        message.setRecipients(RecipientType.TO, InternetAddress.parse(to));
        // ����html����Ϊ�ʼ����ģ�ָ��MIME����Ϊtext/html���ͣ���ָ���ַ�����Ϊgbk
        message.setContent("<span style='color:red;'>html�ʼ�����...</span>",
                "text/html;charset=gbk");

        // ���沢�������յ��ʼ�����
        message.saveChanges();

        // �����ʼ�
        Transport.send(message);
    }

    /**
     * ���ʹ���ǶͼƬ��HTML�ʼ�
     */
    public static void sendHtmlWithInnerImageEmail() throws Exception {
        Properties props = getProps();
        // ����Sessionʵ������
        Session session = Session.getDefaultInstance(props,
                new MyAuthenticator());

        // �����ʼ�����
        MimeMessage message = new MimeMessage(session);
        // �ʼ�����,��ָ�������ʽ
        message.setSubject("����ǶͼƬ��HTML�ʼ�", "utf-8");
        // ������
        message.setFrom(new InternetAddress(from));
        // �ռ���
        message.setRecipients(RecipientType.TO, InternetAddress.parse(to));
        // ����
        message.setRecipient(RecipientType.CC, new InternetAddress(
                "zhen.wang2012@hotmail.com"));
        // ���� (�������ʼ��ռ�����������ʾ����)
        message.setRecipient(RecipientType.BCC, new InternetAddress(
                "93091620@qq.com"));
        // ����ʱ��
        message.setSentDate(new Date());

        // ����һ��MIME������Ϊ��related����MimeMultipart����
        MimeMultipart mp = new MimeMultipart("related");
        // ����һ����ʾ���ĵ�MimeBodyPart���󣬲��������뵽ǰ�洴����MimeMultipart������
        MimeBodyPart htmlPart = new MimeBodyPart();
        mp.addBodyPart(htmlPart);
        // ����һ����ʾͼƬ��Դ��MimeBodyPart���󣬽��������뵽ǰ�洴����MimeMultipart������
        MimeBodyPart imagePart = new MimeBodyPart();
        mp.addBodyPart(imagePart);

        // ��MimeMultipart��������Ϊ�����ʼ�������
        message.setContent(mp);

        // ������ǶͼƬ�ʼ���
        System.out.println(Thread.currentThread().getContextClassLoader()
                .getResource("firefoxlogo.png"));
        DataSource ds = new FileDataSource(new File(Thread.currentThread()
                .getContextClassLoader().getResource("firefoxlogo.png")
                .getPath()));
        DataHandler dh = new DataHandler(ds);
        imagePart.setDataHandler(dh);
        imagePart.setContentID("firefoxlogo.png"); // �������ݱ��,���������ʼ�������

        // ����һ��MIME������Ϊ"alternative"��MimeMultipart���󣬲���Ϊǰ�洴����htmlPart������ʼ�����
        MimeMultipart htmlMultipart = new MimeMultipart("alternative");
        // ����һ����ʾhtml���ĵ�MimeBodyPart����
        MimeBodyPart htmlBodypart = new MimeBodyPart();
        // ����cid=androidlogo.gif�������ʼ��ڲ���ͼƬ����imagePart.setContentID("androidlogo.gif");�����������ͼƬ
        htmlBodypart
                .setContent(
                        "<span style='color:red;'>���Ǵ���ǶͼƬ��HTML�ʼ�Ŷ������<img src=\"cid:firefoxlogo.png\" /></span>",
                        "text/html;charset=utf-8");
        htmlMultipart.addBodyPart(htmlBodypart);
        htmlPart.setContent(htmlMultipart);

        // ���沢�������յ��ʼ�����
        message.saveChanges();

        // �����ʼ�
        Transport.send(message);
    }

    /**
     * ���ʹ���ǶͼƬ�����������ռ���(��ʾ��������)���ʼ����ȼ����Ķ���ִ��������HTML�ʼ�
     */
    public static void sendMultipleEmail(MailMode mailMode) throws Exception {
        Properties props = getProps();

        String charset = "utf-8"; // ָ�����ı����ʽ
        // ����Sessionʵ������
        Session session = Session.getInstance(props, new MyAuthenticator());

        // ����MimeMessageʵ������
        MimeMessage message = new MimeMessage(session);
        // ��������
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");// �������ڸ�ʽ
        message.setSubject("DHL���ص�(" + df.format(new Date()) +")");
        // ���÷�����
        message.setFrom(new InternetAddress(from, "������-�غ������ֹ�˾",
                charset));
        // �����ռ���
        message.setRecipients(RecipientType.TO, new Address[] {
        // ����1�������ַ������2���������ڿͻ����ռ�ֻ��ʾ������������ʾ�ʼ���ַ��������3�����������ַ�������
                new InternetAddress(mailMode.getTo(), "", charset), });
        // ���ó���
        message.setRecipient(RecipientType.CC,
                new InternetAddress(cc, "", charset));
        // ��������
        // message.setRecipient(RecipientType.BCC, new
        // InternetAddress("93091620@qq.com", "����_QQ", charset));
        // ���÷���ʱ��
        message.setSentDate(new Date());
        // ���ûظ���(�ռ��˻ظ����ʼ�ʱ,Ĭ���ռ���)
         message.setReplyTo(InternetAddress.parse("\"" +
         MimeUtility.encodeText("") + "\" <" + cc + ">"));
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
    }

    /**
     * ���ʹ���ǶͼƬ�����������ռ���(��ʾ��������)���ʼ����ȼ����Ķ���ִ��������HTML�ʼ�
     */
    public static void sendMultipleEmail_back() throws Exception {
        Properties props = getProps();
        String charset = "utf-8"; // ָ�����ı����ʽ
        // ����Sessionʵ������
        Session session = Session.getInstance(props, new MyAuthenticator());

        // ����MimeMessageʵ������
        MimeMessage message = new MimeMessage(session);
        // ��������
        message.setSubject("ʹ��JavaMail���ͻ��������͵��ʼ�����");
        // ���÷�����
        message.setFrom(new InternetAddress(from, "���˲�������", charset));
        // �����ռ���
        message.setRecipients(RecipientType.TO, new Address[] {
                // ����1�������ַ������2���������ڿͻ����ռ�ֻ��ʾ������������ʾ�ʼ���ַ��������3�����������ַ�������
                new InternetAddress("zhen.wang2012@hotmail.com", "����_sohu",
                        charset),
                new InternetAddress("zhen.wang2012@hotmail.com", "����_163",
                        charset),
                new InternetAddress("93091620@qq.com", "����_qq", charset) });
        // ���ó���
        message.setRecipient(RecipientType.CC, new InternetAddress(
                "zhen.wang2012@hotmail.com", "����_gmail", charset));
        // ��������
        message.setRecipient(RecipientType.BCC, new InternetAddress(
                "93091620@qq.com", "����_QQ", charset));
        // ���÷���ʱ��
        message.setSentDate(new Date());
        // ���ûظ���(�ռ��˻ظ����ʼ�ʱ,Ĭ���ռ���)
        message.setReplyTo(InternetAddress.parse("\""
                + MimeUtility.encodeText("����") + "\" <93091620@qq.com>"));
        // �������ȼ�(1:���� 3:��ͨ 5:��)
        message.setHeader("X-Priority", "1");
        // Ҫ���Ķ���ִ(�ռ����Ķ��ʼ�ʱ����ʾ�ظ�������,�����ʼ����յ�,�����Ķ�)
        message.setHeader("Disposition-Notification-To", from);

        // ����һ��MIME������Ϊ"mixed"��MimeMultipart���󣬱�ʾ����һ����������͵��ʼ�
        MimeMultipart mailContent = new MimeMultipart("mixed");
        message.setContent(mailContent);

        // ����
        MimeBodyPart attach1 = new MimeBodyPart();
        MimeBodyPart attach2 = new MimeBodyPart();
        // ����
        MimeBodyPart mailBody = new MimeBodyPart();

        // ��������������ӵ��ʼ�����
        mailContent.addBodyPart(attach1);
        mailContent.addBodyPart(attach2);
        mailContent.addBodyPart(mailBody);

        // ����1(����jaf��ܶ�ȡ����Դ�����ʼ���)
        DataSource ds1 = new FileDataSource(new File(Thread.currentThread()
                .getContextClassLoader().getResource("Earth.bmp").getPath()));
        DataHandler dh1 = new DataHandler(ds1);
        attach1.setFileName(MimeUtility.encodeText("Earth.bmp"));
        attach1.setDataHandler(dh1);

        // ����2
        DataSource ds2 = new FileDataSource(new File(Thread.currentThread()
                .getContextClassLoader().getResource("aa.txt").getPath()));
        DataHandler dh2 = new DataHandler(ds2);
        attach2.setDataHandler(dh2);
        attach2.setFileName(MimeUtility.encodeText("���ѧ��C����.txt"));

        // �ʼ�����(��ǶͼƬ+html�ı�)
        MimeMultipart body = new MimeMultipart("related"); // �ʼ�����Ҳ��һ�������,��Ҫָ����Ϲ�ϵ
        mailBody.setContent(body);

        // �ʼ�������html��ͼƬ����
        MimeBodyPart imgPart = new MimeBodyPart();
        MimeBodyPart htmlPart = new MimeBodyPart();
        body.addBodyPart(imgPart);
        body.addBodyPart(htmlPart);

        // ����ͼƬ
        DataSource ds3 = new FileDataSource(Thread.currentThread()
                .getContextClassLoader().getResource("firefoxlogo.png")
                .getPath());
        DataHandler dh3 = new DataHandler(ds3);
        imgPart.setDataHandler(dh3);
        imgPart.setContentID("firefoxlogo.png");

        // html�ʼ�����
        MimeMultipart htmlMultipart = new MimeMultipart("alternative");
        htmlPart.setContent(htmlMultipart);
        MimeBodyPart htmlContent = new MimeBodyPart();
        htmlContent.setContent("<span style='color:red'>�������Լ���java mail���͵��ʼ�Ŷ��"
                + "<img src='cid:firefoxlogo.png' /></span>",
                "text/html;charset=utf-8");
        htmlMultipart.addBodyPart(htmlContent);

        // �����ʼ������޸�
        message.saveChanges();

        /*
         * File eml = buildEmlFile(message); sendMailForEml(eml);
         */

        // �����ʼ�
        Transport.send(message);
    }

    /**
     * ���ʼ���������eml�ļ�
     * 
     * @param message
     *            �ʼ�����
     */
    public static File buildEmlFile(Message message) throws MessagingException,
            FileNotFoundException, IOException {
        File file = new File("c:\\"
                + MimeUtility.decodeText(message.getSubject()) + ".eml");
        message.writeTo(new FileOutputStream(file));
        return file;
    }

    /**
     * ���ͱ����Ѿ����ɺõ�email�ļ�
     */
    public static void sendMailForEml(File eml) throws Exception {
        Properties props = getProps();
        // ����ʼ��Ự
        Session session = Session.getInstance(props, new MyAuthenticator());
        // ����ʼ�����,������ǰ���ɵ�eml�ļ�
        InputStream is = new FileInputStream(eml);
        MimeMessage message = new MimeMessage(session, is);
        // �����ʼ�
        Transport.send(message);
    }

    /**
     * ���ʼ��������ύ��֤��Ϣ
     */
    static class MyAuthenticator extends Authenticator {

        private String username = "93091620"; // 59946781
        private String password = "abcd@1234";

        public MyAuthenticator() {
            super();
        }

        public MyAuthenticator(String username, String password) {
            super();
            this.username = username;
            this.password = password;
        }

        @Override
        protected PasswordAuthentication getPasswordAuthentication() {

            return new PasswordAuthentication(username, password);
        }
    }
}