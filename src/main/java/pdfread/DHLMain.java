package pdfread;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * swing基础实例
 * 
 * @author HZ20232
 * 
 */
public class DHLMain {
    public static void main(String args[]) throws Exception {
        NewFrame frame1 = new NewFrame();
        frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// 一定要设置关闭

        frame1.setVisible(true);
    }
}

class NewFrame extends JFrame {
    private JLabel labelPath;
    private JLabel labelVersion;
    private JLabel labelMessage;
    private JButton buttonConvert;
    private JButton buttonSendMail;
    private JTextField textPath;
    private boolean buttonEvent;

    public NewFrame() {
        super();
        this.setSize(600, 500);
        this.getContentPane().setLayout(null);// 设置布局控制器
        this.add(this.getTextField(), null);// 添加文本框
        this.add(this.getConvertButton(), null);// 添加按钮
        this.add(this.getSendMailButton(), null);// 添加按钮
        this.add(this.getLabel(), null);// 添加标签
        this.add(this.getVersionLabel(), null);// 添加标签
        this.add(this.getMessageLabel(), null);// 添加标签
        this.setTitle("DHL Crystal Report Notice Tool");// 设置窗口标题
        buttonEvent = true;
    }

    /**
     * 设置标签
     * 
     * @return 设置好的标签
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
     * 设置标签
     * 
     * @return 设置好的标签
     */
    private JLabel getMessageLabel() {
        if (labelMessage == null) {
            labelMessage = new JLabel();
            labelMessage.setBounds(96, 200, 400, 18);
            labelMessage.setText("");
            labelMessage.setBackground(Color.RED);
            Font f = new Font("Times New Roman", Font.BOLD, 18);
            labelMessage.setFont(f);
            labelMessage.setToolTipText("Message");
        }
        return labelMessage;
    }

    /**
     * 设置标签
     * 
     * @return 设置好的标签
     */
    private JLabel getVersionLabel() {
        if (labelVersion == null) {
            labelVersion = new JLabel();
            labelVersion.setBounds(100, 300, 100, 18);
            labelVersion.setText("  Ver: 1.02");
        }
        return labelVersion;
    }

    /**
     * 设置按钮
     * 
     * @return 设置好的按钮
     */
    private JButton getConvertButton() {
        if (buttonConvert == null) {
            buttonConvert = new JButton();
            buttonConvert.setBounds(103, 110, 100, 27);
            buttonConvert.setText("Convert");
            buttonConvert.setToolTipText("OK");
            buttonConvert.addActionListener(new ConvertButton());// 添加监听器类，其主要的响应都由监听器类的方法实现
        }
        return buttonConvert;
    }

    /**
     * 监听器类实现ActionListener接口，主要实现actionPerformed方法
     * 
     * @author HZ20232
     * 
     */
    private class ConvertButton implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            labelMessage.setText("Processing ...");
            String strPath = textPath.getText();
            if (!FileAccess.isDirectory(strPath)) {
                labelMessage.setText("Path is not exist, please check!");
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
                            PdfMode pdfMode = PDFAccess.converPDF(strTmp,
                                    strPdfPath);
                            pdfModeLists.add(pdfMode);
                            if (pdfMode != null) {
                                System.out.println(pdfMode);
                            }
                        }
                    }
                }
                String companyMailConfigFile = new File(strPath).getParent()
                        + "\\" + "company mail config.csv";
                System.out.println(companyMailConfigFile);
                ArrayList<PdfMode> pdfLists = FileAccess
                        .readCSV(companyMailConfigFile);
                HashMap<String, PdfMode> map = new HashMap<String, PdfMode>();
                for (PdfMode item : pdfLists) {
                    map.put(item.getCompanyCode(), item);
                }
                ArrayList<PdfMode> pdfModeCSVLists = new ArrayList<PdfMode>();
                for (PdfMode item : pdfModeLists) {
                    PdfMode pdfMode = map.get(item.getCompanyCode());
                    if (pdfMode != null) {
                        item.setMailAddress(pdfMode.getMailAddress());
                    }
                    System.out.println(item);
                    pdfModeCSVLists.add(item);
                }

                FileAccess.writeCSVByPdfModes(strPath + "order info.csv",
                        pdfModeCSVLists);

                // File tmp;
                // while (!list.isEmpty()) {
                // tmp = (File) list.removeFirst();
                // if (tmp.isDirectory()) {
                // file = tmp.listFiles();
                // if (file == null)
                // continue;
                // for (int i = 0; i < file.length; i++) {
                // if (file[i].isDirectory())
                // list.add(file[i]);
                // else
                // System.out.println(file[i]
                // .getAbsolutePath());
                // }
                // } else {
                // String strTmp = tmp.getAbsolutePath();
                // if (strTmp.toLowerCase().endsWith(".pdf")) {
                // PDFAccess.converPDF(strTmp);
                // }
                // }
                // }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            labelMessage.setText("Convert PDF Done!");
        }
    }

    /**
     * 设置按钮
     * 
     * @return 设置好的按钮
     */
    private JButton getSendMailButton() {
        if (buttonSendMail == null) {
            buttonSendMail = new JButton();
            buttonSendMail.setBounds(301, 110, 100, 27);
            buttonSendMail.setText("Send Mail");
            buttonSendMail.setToolTipText("OK");
            buttonSendMail.addActionListener(new SendMailButton());// 添加监听器类，其主要的响应都由监听器类的方法实现
        }
        return buttonSendMail;
    }

    /**
     * 监听器类实现ActionListener接口，主要实现actionPerformed方法
     * 
     * @author HZ20232
     * 
     */
    private class SendMailButton implements ActionListener {
        public void actionPerformed(ActionEvent e) {

            String strPath = textPath.getText();
            if (!FileAccess.isDirectory(strPath)) {
                labelMessage.setText("Path is not exist, please check!");
                return;
            }
            if (!strPath.endsWith("\\")) {
                strPath = strPath + "\\" + "";
            }

            ArrayList<PdfMode> pdfLists = FileAccess.readCSVForSendMail(strPath
                    + "order info.csv");
//            ArrayList<PdfMode> pdfListsForWrite = (ArrayList<PdfMode>) pdfLists.clone();
            HashMap<String, MailMode> mailModeMap = new HashMap<String, MailMode>();
            String strRootPath = new File(strPath).getParent();
            String content = FileAccess.readMailTpl(strRootPath
                    + "\\email_template.txt");
            for (PdfMode pdfMode : pdfLists) {
                if (!MailMode.SENDMAIL_FLG_OK.equals(pdfMode.getSendMailFlg())){
                    if (!"".equals(pdfMode.getMailAddress())
                            && !"null".equals(pdfMode.getMailAddress())
                            && null != pdfMode.getMailAddress()) {
                        String companyCode = pdfMode.getCompanyCode();
                        MailMode mailMode = null;
                        if (mailModeMap.containsKey(companyCode)) {
                            mailMode = mailModeMap.get(companyCode);
                        } else {
                            mailMode = new MailMode();
                        }
                        mailMode.setTo(pdfMode.getMailAddress());
                        mailMode.setAttachFiles(pdfMode.getPdfFile());
                        mailMode.setLogoFile(strRootPath + "\\logo.png");
                        mailMode.setContent(content);
                        mailModeMap.put(companyCode, mailMode);
                    }
                }
            }
            int okMail = 0;
            try {
                SendMail.loadProps( new File(strPath).getParent()
                        + "\\email.properties");
            } catch (Exception e2) {
                // TODO Auto-generated catch block
                labelMessage.setText("邮件配置文件读入出错！");
                return;
            }
            for (String key : mailModeMap.keySet()) {
                System.out.println("key= " + key + " and value= "
                        + mailModeMap.get(key));
                try {
                    MailMode mailMode  = mailModeMap.get(key);

                    SendMail.sendMultipleEmail(mailMode);
                    mailMode.setSendMailFlg(MailMode.SENDMAIL_FLG_OK);
                    okMail++;

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

            ArrayList<PdfMode> pdfListsForWrite = FileAccess.readCSVForSendMail(strPath
                    + "order info.csv");
            for (PdfMode pdfMode : pdfListsForWrite) {
                if (!"".equals(pdfMode.getMailAddress())
                        && !"null".equals(pdfMode.getMailAddress())
                        && null != pdfMode.getMailAddress()) {
                    String companyCode = pdfMode.getCompanyCode();
                    MailMode mailMode2 = (MailMode) mailModeMap.get(companyCode);
                    if (mailMode2 != null){
                        pdfMode.setSendMailFlg(mailMode2.getSendMailFlg());
                    }
                }
            }

            FileAccess.writeCSVByPdfModes(strPath + "order info.csv", pdfListsForWrite);
            int mailSize = mailModeMap.keySet().size();

            labelMessage.setText(FileAccess.converCode2("Main Send Done. Success (" + okMail + "). Failure (" + (mailSize-okMail) + ")" ,"UTF-8","GBK"));
        }
    }

    /**
     * 设定文本域
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