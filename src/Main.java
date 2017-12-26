import msms.report.ExportReport;

import java.io.File;

public class Main {

    public static void main(String[] _args) {
        try {
//            Map<String, String> map = new HashMap<>();
//            map.put("myFirstParam", "msms");
//            ExportReport.exportBoth(new File("msbsTrialWithParam.rpt"), map);
            if (_args.length > 0) {
                File file = new File(_args[0]);
                if (file.exists()) {
                    ExportReport.exportBoth(file, null);
                    System.out.println("Complete..");
                } else throw new Exception("Report file not exist");
            } else throw new Exception("No argument specified. Please specify report file as first argument.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
