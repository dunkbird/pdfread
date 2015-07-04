package pdfread;

import java.util.ArrayList;

public class MailMode {
    public final static String SENDMAIL_FLG_OK = "OK";

    String from = null;
    String to = null;
    String cc = null;
    ArrayList<String> attachFiles  = new ArrayList<String>();
    String logoFile = "";
    String logoFilePath = "";
    String content = "";
    String SendMailFlg = "";
    public String getContent() {
        return content;
    }
    public String getSendMailFlg() {
        return SendMailFlg;
    }
    public void setSendMailFlg(String sendMailFlg) {
        SendMailFlg = sendMailFlg;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getFrom() {
        return from;
    }
    public void setFrom(String from) {
        this.from = from;
    }
    public String getTo() {
        return to;
    }
    public void setTo(String to) {
        this.to = to;
    }
    public String getCc() {
        return cc;
    }
    public void setCc(String cc) {
        this.cc = cc;
    }
    public ArrayList<String> getAttachFiles() {
        return attachFiles;
    }
    public void setAttachFiles(String file) {
        this.attachFiles.add(file);
    }
    public String getLogoFile() {
        return logoFile;
    }
    public void setLogoFile(String logoFile) {
        this.logoFile = logoFile;
    }
    public String getLogoFilePath() {
        return logoFilePath;
    }
    public void setLogoFilePath(String logoFilePath) {
        this.logoFilePath = logoFilePath;
    }
    @Override
    public String toString() {
        return "MailMode [from=" + from + ", to=" + to + ", cc=" + cc
                + ", attachFiles=" + attachFiles + ", logoFile=" + logoFile
                + ", logoFilePath=" + logoFilePath + "]";
    }
 
}
