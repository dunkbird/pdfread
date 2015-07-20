package pdfread;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 * swing����ʵ��
 * 
 * @author HZ20232
 * 
 */
public class DHLMain {
    public static void main(String args[]) throws Exception {
        NewFrame frame1 = new NewFrame();
        frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // һ��Ҫ���ùر�
        frame1.setVisible(true);
    }
}

class NewFrame extends JFrame {
    private static String CSV_FILE_ORDER_INFO = "order info.csv";
    private static String CSV_FILE_COMPANY_MAIL_CONFIG = "company mail config.csv";
    private static String TPL_FILE_EMAIL_CONTENT = "email_template.txt";
    private static String PROP_FILE_EMAIL_CONFIG = "email.properties";

    private JLabel labelPath;
    private JLabel labelVersion;
    private JLabel labelMessage;
    private JButton buttonConvert;
    private JButton buttonSendMail;
    private JTextField textPath;
    private boolean buttonEvent;

    private Runnable run = null; // ����������߳�
    private int countProcess = 0;
    private int okMail = 0;
    private int pdfReadSuccessCount = 0; //Ҫ�����pdf�Ĵ�����ܼ���
    private int mailSize = 0;
    
    public NewFrame() {
        super();
        this.setSize(600, 500);
        this.getContentPane().setLayout(null); // ���ò��ֿ�����
        this.add(this.getTextField(), null); // ����ı���
        this.add(this.getConvertButton(), null); // ��Ӱ�ť
        this.add(this.getSendMailButton(), null); // ��Ӱ�ť
        this.add(this.getLabel(), null); // ��ӱ�ǩ
        this.add(this.getVersionLabel(), null); // ��ӱ�ǩ
        this.add(this.getMessageLabel(), null); // ��ӱ�ǩ
        this.setTitle("DHL Crystal Report Notice Tool"); // ���ô��ڱ���
        buttonEvent = true;
        run = new Runnable() { // ʵ��������������߳�
            public void run() {
                System.out.println("**************************" + countProcess);
                if (countProcess == 0) {
                    labelMessage.setText("Processing start!");
                } else {
                    labelMessage.setText("Processing ... " + countProcess);
                }
            }
        };
    }

    /**
     * ���ñ�ǩ
     * 
     * @return ���úõı�ǩ
     */
    private JLabel getLabel() {
        if (labelPath == null) {
            labelPath = new JLabel();
            labelPath.setBounds(34, 49, 53, 18);
            labelPath.setText("  Path:");
            labelPath.setToolTipText("JLabel");
        }
        return labelPath;
    }

    /**
     * ���ñ�ǩ
     * 
     * @return ���úõı�ǩ
     */
    private JLabel getMessageLabel() {
        if (labelMessage == null) {
            labelMessage = new JLabel();
            labelMessage.setBounds(96, 200, 400, 40);
            labelMessage.setText("");
            labelMessage.setBackground(Color.RED);
            Font f = new Font("����", Font.BOLD, 18);
            labelMessage.setFont(f);
            labelMessage.setToolTipText("Message");
        }
        return labelMessage;
    }

    /**
     * ���ñ�ǩ
     * 
     * @return ���úõı�ǩ
     */
    private JLabel getVersionLabel() {
        if (labelVersion == null) {
            labelVersion = new JLabel();
            labelVersion.setBounds(100, 300, 100, 18);
            labelVersion.setText("  Ver: 1.03");

        }
        return labelVersion;
    }

    /**
     * ���ð�ť
     * 
     * @return ���úõİ�ť
     */
    private JButton getConvertButton() {
        if (buttonConvert == null) {
            buttonConvert = new JButton();
            buttonConvert.setBounds(103, 110, 100, 27);
            buttonConvert.setText("Convert");
            buttonConvert.setToolTipText("OK");
            buttonConvert.addActionListener(new ConvertButton()); // ��Ӽ������࣬����Ҫ����Ӧ���ɼ�������ķ���ʵ��
        }
        return buttonConvert;
    }

    /**
     * ��������ʵ��ActionListener�ӿڣ���Ҫʵ��actionPerformed����
     * 
     * @author HZ20232
     * 
     */
    private class ConvertButton implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            countProcess = 0;
            pdfReadSuccessCount = 0;
            new Thread(new Runnable() {
                public void run() {
                    try {
                        SwingUtilities.invokeAndWait(new Runnable() {
                            public void run() {
                                labelMessage.setText("1. Process Start");
                            }
                        });
                        String strPath = textPath.getText();
                        if (!FileAccess.isDirectory(strPath)) {
                            labelMessage
                                    .setText("Path is not exist, please check!");
                            return;
                        }
                        if (!strPath.endsWith("\\")) {
                            strPath = strPath + "\\" + "";
                        }
                        String strPdfPath = strPath + "pdf";
                        FileAccess.createFolder(strPdfPath);
                        FileAccess.createFolder(strPath + "tif");

                        long a = System.currentTimeMillis();
                        // LinkedList list = new LinkedList();
                        File dir = new File(textPath.getText());
                        File file[] = dir.listFiles();
                        if (file != null) {
                            ArrayList<PdfMode> pdfModeLists = new ArrayList<PdfMode>();
                            for (int i = 0; i < file.length; i++) {
                                if (file[i].isDirectory()) {
                                    // list.add(file[i]);
                                } else {
                                    String strTmp = file[i].getAbsolutePath();
                                    try {
                                        Thread.sleep(2000);
                                    } catch (InterruptedException e1) {
                                        // TODO Auto-generated catch block
                                        e1.printStackTrace();
                                    }
                                    if (strTmp.toLowerCase().endsWith(".pdf")) {
                                        PdfMode pdfMode = PDFAccess.converPDF(
                                                strTmp, strPdfPath);
                                        countProcess++;
                                        SwingUtilities
                                                .invokeAndWait(new Runnable() {
                                                    public void run() {
                                                        labelMessage
                                                                .setText("2. Processing..."
                                                                        + countProcess);
                                                    }
                                                });
                                        if (pdfMode != null) {
                                            pdfReadSuccessCount++;
                                            pdfModeLists.add(pdfMode);
                                            System.out.println(pdfMode);
                                        }
                                    }
                                }
                            }
                            String companyMailConfigFile = new File(strPath)
                                    .getParent()
                                    + "\\" + CSV_FILE_COMPANY_MAIL_CONFIG;
                            System.out.println(companyMailConfigFile);
                            ArrayList<PdfMode> pdfLists = FileAccess
                                    .readCSV(companyMailConfigFile);
                            HashMap<String, PdfMode> map = new HashMap<String, PdfMode>();
                            for (PdfMode item : pdfLists) {
                                map.put(item.getCompanyCode(), item);
                            }
                            ArrayList<PdfMode> pdfModeCSVLists = new ArrayList<PdfMode>();
                            for (PdfMode item : pdfModeLists) {
                                PdfMode pdfMode = map
                                        .get(item.getCompanyCode());
                                if (pdfMode != null) {
                                    item.setMailAddress(pdfMode
                                            .getMailAddress());
                                }
                                System.out.println(item);
                                pdfModeCSVLists.add(item);
                            }

                            FileAccess.writeCSVByPdfModes(strPath
                                    + CSV_FILE_ORDER_INFO, pdfModeCSVLists);
                        }

                        SwingUtilities.invokeAndWait(new Runnable() {
                            public void run() {
                                labelMessage.setText("3. Done."  
                                        + " Success: " + pdfReadSuccessCount + ", Failure: " + (countProcess - pdfReadSuccessCount) );
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

    }

    /**
     * ���ð�ť
     * 
     * @return ���úõİ�ť
     */
    private JButton getSendMailButton() {
        if (buttonSendMail == null) {
            buttonSendMail = new JButton();
            buttonSendMail.setBounds(301, 110, 100, 27);
            buttonSendMail.setText("Send Mail");
            buttonSendMail.setToolTipText("OK");
            buttonSendMail.addActionListener(new SendMailButton());// ��Ӽ������࣬����Ҫ����Ӧ���ɼ�������ķ���ʵ��
        }
        return buttonSendMail;
    }

    /**
     * ��������ʵ��ActionListener�ӿڣ���Ҫʵ��actionPerformed����
     * 
     * @author HZ20232
     * 
     */
    private class SendMailButton implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            okMail = 0;
            mailSize = 0;
            new Thread(new Runnable() {
                public void run() {
                    try {
                        SwingUtilities
                        .invokeAndWait(new Runnable() {
                            public void run() {
                                labelMessage
                                        .setText("1. Send Start!");
                            }
                        });
                        String strPath = textPath.getText();
                        if (!FileAccess.isDirectory(strPath)) {
                            labelMessage
                                    .setText("Path is not exist, please check!");
                            return;
                        }
                        if (!strPath.endsWith("\\")) {
                            strPath = strPath + "\\" + "";
                        }

                        ArrayList<PdfMode> pdfLists = FileAccess
                                .readCSVForSendMail(strPath
                                        + CSV_FILE_ORDER_INFO);

                        HashMap<String, MailMode> mailModeMap = new HashMap<String, MailMode>();
                        String strRootPath = new File(strPath).getParent();
                        String content = FileAccess.readMailTpl(strRootPath
                                + "\\" + TPL_FILE_EMAIL_CONTENT);
                        for (PdfMode pdfMode : pdfLists) {
                            if (!MailMode.SENDMAIL_FLG_OK.equals(pdfMode
                                    .getSendMailFlg())) {
                                if (!"".equals(pdfMode.getMailAddress())
                                        && !"null".equals(pdfMode
                                                .getMailAddress())
                                        && null != pdfMode.getMailAddress()) {
                                    String companyCode = pdfMode
                                            .getCompanyCode();
                                    MailMode mailMode = null;
                                    if (mailModeMap.containsKey(companyCode)) {
                                        mailMode = mailModeMap.get(companyCode);
                                    } else {
                                        mailMode = new MailMode();
                                    }
                                    mailMode.setTo(pdfMode.getMailAddress());
                                    Map<String,String> orderNos = mailMode.getOrderNos();
                                    if (!orderNos.containsKey(pdfMode.getOrderNo())){
                                        mailMode.setOrderNos(pdfMode.getOrderNo());
                                        mailMode.setAttachFiles(pdfMode
                                                .getPdfFile());
                                    }
                                    mailMode.setLogoFile(strRootPath
                                            + "\\logo.png");
                                    mailMode.setContent(content);
                                    SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");// �������ڸ�ʽ
                                    mailMode.setTitle("DHL���ص�(" + df.format(new Date()) +"-"+ companyCode + ")");
                                    mailModeMap.put(companyCode, mailMode);
                                }
                            }
                        }
                        try {
                            SendMail.loadProps(new File(strPath).getParent()
                                    + "\\" + PROP_FILE_EMAIL_CONFIG);
                        } catch (Exception e2) {
                            // TODO Auto-generated catch block
                            labelMessage.setText("�ʼ������ļ��������");
                            return;
                        }
                        for (String key : mailModeMap.keySet()) {
                            System.out.println("key= " + key + " and value= "
                                    + mailModeMap.get(key));
                            try {
                                MailMode mailMode = mailModeMap.get(key);

                                SendMail.sendMultipleEmail(mailMode);
                                mailMode
                                        .setSendMailFlg(MailMode.SENDMAIL_FLG_OK);
                                okMail++;
                                SwingUtilities
                                .invokeAndWait(new Runnable() {
                                    public void run() {
                                        labelMessage
                                                .setText("2. Sendding..."
                                                        + okMail);
                                    }
                                });
                                try {
                                    Thread.sleep(10000);
                                } catch (InterruptedException e1) {
                                    // TODO Auto-generated catch block
                                    e1.printStackTrace();
                                }
                            } catch (Exception e1) {
                                e1.printStackTrace();
                                break;
                            }
                        }

                        ArrayList<PdfMode> pdfListsForWrite = FileAccess
                                .readCSVForSendMail(strPath
                                        + CSV_FILE_ORDER_INFO);
                        for (PdfMode pdfMode : pdfListsForWrite) {
                            if (!"".equals(pdfMode.getMailAddress())
                                    && !"null".equals(pdfMode.getMailAddress())
                                    && null != pdfMode.getMailAddress()) {
                                String companyCode = pdfMode.getCompanyCode();
                                MailMode mailMode2 = (MailMode) mailModeMap
                                        .get(companyCode);
                                if (mailMode2 != null) {
                                    pdfMode.setSendMailFlg(mailMode2
                                            .getSendMailFlg());
                                }
                            }
                        }

                        FileAccess.writeCSVByPdfModes(strPath
                                + CSV_FILE_ORDER_INFO, pdfListsForWrite);
                        mailSize = mailModeMap.keySet().size();
                        SwingUtilities
                        .invokeAndWait(new Runnable() {
                            public void run() {
                                 labelMessage.setText("3. Done. Success: "
                                         + okMail + ", Failure: " + (mailSize - okMail));
                            }
                        });
                       
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    /**
     * �趨�ı���
     * 
     * @return
     */
    private JTextField getTextField() {
        if (textPath == null) {
            textPath = new JTextField();
            textPath.setText("F:\\pdf\\20150625");
            textPath.setBounds(96, 49, 400, 20);
            textPath.setToolTipText("Please input pdf path");
        }
        return textPath;
    }
}