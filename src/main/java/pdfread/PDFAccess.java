package pdfread;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.util.PDFTextStripper;
import com.sun.org.apache.xerces.internal.impl.xs.identity.Selector.Matcher;

public class PDFAccess {

    public static void main(String[] args) throws Exception {
        // TODO Auto-generated method stub
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        // Create a new font object selecting one of the PDF base fonts
        PDFont font = PDType1Font.HELVETICA_BOLD;

        // Start a new content stream which will "hold" the to be created
        // content
        PDPageContentStream contentStream = new PDPageContentStream(document,
                page);

        // Define a text content stream using the selected font, moving the
        // cursor and drawing the text "Hello World"
        contentStream.beginText();
        contentStream.setFont(font, 12);
        contentStream.moveTextPositionByAmount(100, 700);
        contentStream.drawString("Hello World");
        contentStream.endText();

        // Make sure that the content stream is closed:
        contentStream.close();

        // Save the results and ensure that the document is properly closed:
        document.save("Hello World.pdf");
        document.close();

    }

    public static PdfMode converPDF(String file, String path) {
        if (!path.endsWith("\\")){
            path = path + "\\";
        }
        PDDocument doc = new PDDocument();
        try {
            doc = PDDocument.load(file);
            PDFTextStripper stripper = new PDFTextStripper();
            // stripper.getXObjects();
            String str = stripper.getText(doc);
            // System.out.println(str);
            Pattern pattern1 = Pattern.compile("\r\n");
            String[] strs = pattern1.split(str);
            String pattern = "^[(]{1}[0-9a-zA-Z]{10}[)]{1}$";
            Pattern pat = Pattern.compile(pattern);
            for (int i = 0; i < strs.length; i++) {
                java.util.regex.Matcher mat = pat.matcher(strs[i]);
                boolean cb = mat.matches();
                if (cb && i == 28) {
                    System.out.println("istrue:" + cb);
                    System.out.println(strs[i] + "::::" + i);
                    String companyCode = strs[28];
                    companyCode = companyCode.replace("(", "").replace(")","");
                    String orderNo = strs[29];
                    String companyName = strs[26];
                    String[] orderNos = orderNo.split(" ");
                    if (orderNos.length !=2 ){
                        return null;
                    } else {
                      if (orderNos[0].equals(orderNos[1]) ){
                          orderNo = orderNos[0];
                      } else {
                          return null;
                      }
                    }
                    PdfMode pdfMode = new PdfMode();
                    pdfMode.setCompanyCode(companyCode);
                    pdfMode.setCompanyName(companyName);
                    pdfMode.setOrderNo(orderNo);
                    SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd_HHmmss");//设置日期格式
                    String pdfFile = path + orderNo + "_CRL_DLC_DLW_001_" + df.format(new Date()) + ".pdf";
                    pdfMode.setPdfFile(pdfFile);
                    FileAccess.Move(file, pdfFile);
                    return pdfMode;
                } else {
                    // System.out.println("isfalse:" + cb);
                }
            }
            try {
                doc.close();
            } catch (Exception e) {
            }
        } catch (Exception e) {
        } finally {
            if (doc != null) {
                try {
                    doc.close();
                } catch (Exception e) {
                }
            }
        }
        return null;
    }

}
