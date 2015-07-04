package pdfread;

public class PdfMode {
    
    String orderNo = null;
    String companyCode = null;
    String companyName = null;
    String pdfFile = null;
    String mailAddress = null;
    String SendMailFlg = "";

    public String getSendMailFlg() {
        return SendMailFlg;
    }
    public void setSendMailFlg(String sendMailFlg) {
        SendMailFlg = sendMailFlg;
    }
    public String getOrderNo() {
        return orderNo;
    }
    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
    public String getCompanyCode() {
        return companyCode;
    }
    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }
    public String getCompanyName() {
        return companyName;
    }
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
    public String getPdfFile() {
        return pdfFile;
    }
    public void setPdfFile(String pdfFile) {
        this.pdfFile = pdfFile;
    }
    public String getMailAddress() {
        return mailAddress;
    }
    public void setMailAddress(String mailAddress) {
        this.mailAddress = mailAddress;
    }
    @Override
    public String toString() {
        return "PdfMode [SendMailFlg=" + SendMailFlg + ", companyCode="
                + companyCode + ", companyName=" + companyName
                + ", mailAddress=" + mailAddress + ", orderNo=" + orderNo
                + ", pdfFile=" + pdfFile + "]";
    }

}
