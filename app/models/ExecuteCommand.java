package models;

import play.Logger;
import play.Play;

import java.io.*;

import static java.lang.Runtime.getRuntime;

/**
 * Created by rmasal on 2015-07-03 .
 */

public class ExecuteCommand {


    static class StreamGobbler extends Thread {
        InputStream is;
        String type;

        StreamGobbler(InputStream is) {
            this.is = is;
        }

        public void run() {
            try {
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                String line;
                while ((line = br.readLine()) != null)
                  Logger.error(line);
            }
            catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }

    public static void runCMD() {
//        try {
//            Process process = getRuntime().exec("cmd /c start");
//            Process process = getRuntime().exec(Play.application().configuration().getString("config[1].cmd") + " " +
//                    Play.application().configuration().getString("config.param") + " " + Play.application().configuration().getObjectList("config.command"));
//            int exitCode = process.waitFor();
//        Config configFactory = ConfigFactory.load();
//
//        Config configItem = configFactory.getConfig("ikerg-server-side.mapping");
//        List connection = Play.application().configuration().getObjectList("ikerg-server-side.mapping.connect");
//        System.out.println("exit code: " + Play.application().configuration().getObjectList("ikerg-server-side.mapping.connect").get(0).get("url"));
//        String jsoneNode = configItem.root().render(ConfigRenderOptions.concise());
//        System.out.println(jsoneNode);
        fileRoots();
//        for (int i = 0; i < Play.application().configuration().getObjectList("ikerg-server-side.mapping.connect").size(); i++) {
//            String url = Play.application().configuration().getObjectList("ikerg-server-side.mapping.connect").get(i).get("url").toString();
//            String drive = Play.application().configuration().getObjectList("ikerg-server-side.mapping.connect").get(i).get("drive").toString() + ": ";
//            String user = Play.application().configuration().getObjectList("ikerg-server-side.mapping.connect").get(i).get("user").toString();
//            String password = Play.application().configuration().getObjectList("ikerg-server-side.mapping.connect").get(i).get("password").toString();
//            try {
//                System.out.println("net use " + drive + "\r\n" + "if %errorlevel% EQU 0 " + drive + "(set shareExists=1) else" + "( net use " + drive + url + " /" + user + " " + password + ")");
//                Process process = getRuntime().exec("net use + " + drive + "\r\n" + "if %errorlevel% EQU 0 " + drive + "(set shareExists=1) else" + "( net use " + drive + url + " /" + user + " " + password + ")");
//                StreamGobbler errorGobbler = new StreamGobbler(process.getErrorStream());
//
//                // any output?
//                StreamGobbler outputGobbler = new StreamGobbler(process.getInputStream());
//
//                // kick them off
//                errorGobbler.start();
//                outputGobbler.start();
//                int exitValue = process.waitFor();
//                System.out.println("Proces zwrócił wartość: " + exitValue);
////            } catch (IOException | InterruptedException e) {
////                e.getMessage();
////            }
//            }   catch (Throwable t)
//                {
//                    t.printStackTrace();
//                }
//        }

    }

    public static void fileRoots () {
        File[] roots = File.listRoots();
        System.out.println("Found " + roots.length + " roots " );
        for( int i = 0 ; i < roots.length ; i++ ){
            if (roots[i].toString().equals("Z:\\")) {
                System.out.println("Z exists ____");
            }
            System.out.println( roots[i].toString() + " exists= " + roots[i].exists() );
        }
        System.out.println("Done" );
    }


    public static boolean check(String drive) {
        try {
            Process process = getRuntime().exec("net use " + drive +
                    "if %errorlevel% EQU 0");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String line;
            String read = "";
            while ((line = reader.readLine()) != null) {
                read = read + line;
                process.waitFor();
                Logger.debug("Proces zwrócił wartość: " + process.exitValue());
            }
            Logger.error(read);
            return false;
        } catch (IOException | InterruptedException e) {
            e.getMessage();
        }
        return true;

    }

}


