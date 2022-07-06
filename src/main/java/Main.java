import javax.swing.*;
import java.io.*;

public class Main {

    private static boolean isCrash = false;

    public static void main(String[] args) throws Exception {


        new Thread(() -> {
            // 1s 检查一次是否还在homeActivity
            while (!checkCrash()) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            isCrash = true;
            // 显示对话框
            showDialog();
            System.out.println("crash!!!");
        }).start();

        while (!isCrash) {
            swipe();
            System.out.println("swipe");
            Thread.sleep(1500);
        }
    }

    /**
     * 上滑
     */
    private static void swipe() {
        String cmd = "adb shell input swipe 560 1745 560 880";
        runCmd(cmd);
    }

    /**
     * 检查当前页面是否在HomeActivity
     */
    private static boolean checkCrash() {
        String cmd = "adb shell dumpsys activity top | grep ACTIVITY";
        String result = runCmd(cmd);
        System.out.println("check");
        System.out.println(result);
        if (result.contains("HomeActivity")) {
            return false;
        }
        return true;
    }

    private static void showDialog() {
        JOptionPane.showConfirmDialog(null,
                "快导出日志！","Crash!",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE);
    }


    private static String runCmd(String cmd) {
        try {
            Process process = Runtime.getRuntime().exec(cmd);
            InputStream inputStream = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(inputStream);
            BufferedReader br = new BufferedReader(isr);

            StringBuilder result = new StringBuilder();
            String temp;
            while ((temp = br.readLine()) != null) {
                result.append(temp);
            }
            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
