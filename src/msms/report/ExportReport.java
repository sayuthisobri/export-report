package msms.report;

import com.crystaldecisions.reports.sdk.ReportClientDocument;
import com.crystaldecisions.sdk.occa.report.exportoptions.ReportExportFormat;
import com.crystaldecisions.sdk.occa.report.lib.ReportSDKException;

import java.io.*;
import java.util.Map;

/**
 * @author msms
 */
@SuppressWarnings("WeakerAccess")
public class ExportReport {
    private static boolean export(String src, String to, ReportExportFormat format, Map<String, String> params) {
        try {
            ReportClientDocument clientDoc = new ReportClientDocument();
            System.out.println(String.format("Attempt to convert report to %s format", format.toString()));
            clientDoc.open(src, format.value());
            if (params != null) {
                params.forEach((k, v) -> {
                    try {
                        clientDoc.getDataDefController().getParameterFieldController().setCurrentValue("", k, v);
                    } catch (ReportSDKException e) {
                        e.printStackTrace();
                    }
                });
            }
            //Writing into PDF file
            ByteArrayInputStream is = (ByteArrayInputStream) clientDoc.getPrintOutputController().export(format);
            int size = is.available();
            byte[] barray = new byte[size];
            FileOutputStream fos = new FileOutputStream(new File(to));
            ByteArrayOutputStream baos = new ByteArrayOutputStream(size);
            int bytes = is.read(barray, 0, size);
            baos.write(barray, 0, bytes);
            baos.writeTo(fos);
            clientDoc.close();
            is.close();
            baos.close();
            fos.close();
            System.out.println(String.format("Save file to : %s", to));
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean exportPdf(String from, String to, Map<String, String> params) {
        return export(from, to, ReportExportFormat.PDF, params);
    }

    public static boolean exportExcel(String from, String to, Map<String, String> params) {
        return export(from, to, ReportExportFormat.MSExcel, params);
    }

    public static int exportBoth(File from, Map<String, String> params) throws IOException {
        int status = -1;
        String fileName = from.getCanonicalPath().replaceFirst("[.][^.]+$", "");
        status += exportPdf(from.getCanonicalPath(), fileName.concat(".pdf"), params) ? 1 : 0;
        status += exportExcel(from.getCanonicalPath(), fileName.concat(".xls"), params) ? 1 : 0;
        return status;
    }
}
