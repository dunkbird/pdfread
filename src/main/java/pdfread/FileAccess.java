package pdfread;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FileAccess {

    private final static byte commonCsvHead[] = { (byte) 0xEF, (byte) 0xBB,
            (byte) 0xBF };

    public static boolean Move(File srcFile, String destPath) {
        // Destination directory
        File dir = new File(destPath);
        // Move file to new directory
        boolean success = srcFile.renameTo(new File(dir, srcFile.getName()));
        return success;
    }

    public static boolean Move(String srcFile, String destPath) {
        // File (or directory) to be moved
        File file = new File(srcFile);

        // Destination directory
        File dir = new File(destPath);
        // Move file to new directory
        boolean success = file.renameTo(dir);

        return success;
    }

    public static void Copy(String oldPath, String newPath) {
        InputStream inStream = null;
        FileOutputStream fs = null;
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) {
                inStream = new FileInputStream(oldPath);
                fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                int length;
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread;
                    System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
                fs.flush();
                fs.close();
            }
        } catch (Exception e) {
            if (fs != null) {
                try {
                    fs.close();
                } catch (Exception e1) {
                }
            }
            if (inStream != null) {
                try {
                    inStream.close();
                } catch (Exception e1) {
                }
            }
        }
    }

    public static void Copy(File oldfile, String newPath) {
        InputStream inStream = null;
        FileOutputStream fs = null;
        try {
            int bytesum = 0;
            int byteread = 0;
            // File oldfile = new File(oldPath);
            if (oldfile.exists()) {
                inStream = new FileInputStream(oldfile);
                fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread;
                    System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
                fs.flush();
                fs.close();
            }
        } catch (Exception e) {
            if (fs != null) {
                try {
                    fs.close();
                } catch (Exception e1) {
                }
            }
            if (inStream != null) {
                try {
                    inStream.close();
                } catch (Exception e1) {
                }
            }
        }
    }

    public static boolean createFolder(String path) {
        try {
            if (!(new File(path).isDirectory())) {
                new File(path).mkdir();
            }
            return true;
        } catch (SecurityException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isDirectory(String path) {
        return new File(path).isDirectory();
    }

    public static ArrayList<PdfMode> readCSV(String file) {
        ArrayList<PdfMode> list = new ArrayList<PdfMode>();
        BufferedReader reader = null;
        FileReader fr = null;
        try {
            fr = new FileReader(file);
            reader = new BufferedReader(fr);// 换成你的文件名
            reader.readLine();// 第一行信息，为标题信息，不用,如果需要，注释掉
            String line = null;
            while ((line = reader.readLine()) != null) {
                String[] item = line.split(",");// CSV格式文件为逗号分隔符文件，这里根据逗号切分
                if (item.length == 3) {
                    PdfMode pdfMode = new PdfMode();
                    pdfMode.setCompanyCode(item[0]);
                    pdfMode.setCompanyName(item[1]);
                    pdfMode.setMailAddress(item[2]);
                    list.add(pdfMode);
                }
            }
            reader.close();
            fr.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                }
            }
            if (fr != null) {
                try {
                    fr.close();
                } catch (Exception e) {

                }
            }
        }
        return list;
    }

    public static ArrayList<PdfMode> readCSVForSendMail(String file) {
        ArrayList<PdfMode> list = new ArrayList<PdfMode>();
        BufferedReader reader = null;
        FileReader fr = null;
        try {
            fr = new FileReader(file);
            reader = new BufferedReader(fr);// 换成你的文件名
            reader.readLine();// 第一行信息，为标题信息，不用,如果需要，注释掉
            String line = null;
            while ((line = reader.readLine()) != null) {
                String[] item = line.split(",");// CSV格式文件为逗号分隔符文件，这里根据逗号切分
                if (item.length == 6 || item.length == 5) {
                    PdfMode pdfMode = new PdfMode();
                    pdfMode.setOrderNo(item[0]);
                    pdfMode.setCompanyCode(item[1]);
                    pdfMode.setCompanyName(item[2]);
                    pdfMode.setMailAddress(item[3]);
                    pdfMode.setPdfFile(item[4]);
                    if (item.length == 6) {
                        pdfMode.setSendMailFlg(item[5]);
                    }
                    list.add(pdfMode);
                }
            }
            reader.close();
            fr.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                }
            }
            if (fr != null) {
                try {
                    fr.close();
                } catch (Exception e) {

                }
            }
        }
        return list;
    }

    public static String readMailTpl(String file) {
        ArrayList<PdfMode> list = new ArrayList<PdfMode>();
        BufferedReader reader = null;
        FileReader fr = null;
        StringBuffer buffer = new StringBuffer();
        ;
        try {
            fr = new FileReader(file);
            reader = new BufferedReader(fr);// 换成你的文件名
            reader.readLine();// 第一行信息，为标题信息，不用,如果需要，注释掉
            String line = null;
            while ((line = reader.readLine()) != null) {
                buffer.append("<span>").append(line).append("</span><br>");
            }
            reader.close();
            fr.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                }
            }
            if (fr != null) {
                try {
                    fr.close();
                } catch (Exception e) {

                }
            }
        }
        return buffer.toString();
    }

    public static void writeCSVByPdfModes(String file, ArrayList<PdfMode> lists) {
        ArrayList<PdfMode> list = new ArrayList<PdfMode>();
        BufferedWriter bw = null;
        FileWriter fw = null;
        try {
            File csv = new File(file); // CSV数据文件
            fw = new FileWriter(csv, false);
            bw = new BufferedWriter(fw); // 附加
            // 添加新的数据行

            bw.write(new String(commonCsvHead, "UTF-8"));
            bw.write("order_no,company_code,company_name,mail_address,pdf_file,sendmail_flag");
            bw.newLine();
            for (PdfMode mode : lists) {
                StringBuffer buffer = new StringBuffer();
                buffer.append(converStr(mode.getOrderNo())).append(",");
                buffer.append(converStr(mode.getCompanyCode())).append(",");
                buffer.append(converStr(mode.getCompanyName())).append(",");
                buffer.append(converStr(mode.getMailAddress())).append(",");
                buffer.append(converStr(mode.getPdfFile())).append(",");
                buffer.append(converStr(mode.getSendMailFlg()));
                bw.write(converCode(buffer.toString(), "UTF-8", "GBK"));
                bw.newLine();
            }
            bw.close();
            fw.close();
        } catch (Exception e) {
            // File对象的创建过程中的异常捕获
            e.printStackTrace();
        } finally {
            if (bw != null) {
                try {
                    bw.close();
                } catch (Exception e) {
                }
            }
            if (fw != null) {
                try {
                    fw.close();
                } catch (Exception e) {
                }
            }
        }
    }

    public static String converCode(String str, String u1, String u2) {
        try {
            str = new String(str.getBytes(u1));
            System.out.println(str);
            str = new String(str.getBytes(), u1);
            System.out.println(str);
            // str = new String(str.getBytes(u2));
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return str;
    }

    public static String converStr(String str) {
        if (str == null) {
            return "";
        } else {
            return str;
        }
    }

    public static void main(String[] str) {
        // Move("F:\\pdf\\a.pdf","F:\\pdf\\20150625\\b.pdf");
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd_HHmmss");// 设置日期格式
        System.out.println(df.format(new Date()));// new Date()为获取当前系统时间
    }
}