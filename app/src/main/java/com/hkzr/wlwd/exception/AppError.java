package com.hkzr.wlwd.exception;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Date;
import android.os.Build;
import com.hkzr.wlwd.ui.utils.AppManager;
import com.hkzr.wlwd.ui.utils.Constant;
import com.hkzr.wlwd.ui.utils.DateUtils;
import com.hkzr.wlwd.ui.utils.FileUtils;

public class AppError implements UncaughtExceptionHandler {

    protected boolean isSendEmail = false;

    protected UncaughtExceptionHandler mDefaultHandler;

    public void initUncaught() {
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            AppManager.getAppManager().appExit(
                    AppManager.getAppManager().currentActivity());
        }
    }

    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        if (isSendEmail) {
            sendErrorInfoMail(ex);
        }
        saveErrorLog(ex);
        return true;
    }

    public void sendErrorInfoMail(Throwable ex) {
        StringBuffer sb = new StringBuffer();
        sb.append("--------------------" + (new Date().toLocaleString())
                + "---------------------\n");
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        sb.append(result);
        // send handler
    }

    public void saveErrorLog(Throwable ex) {
        StringBuffer sb = new StringBuffer();
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        sb.append(result);
        System.err.println(sb.toString());
        String errorlog = "error_Log" + DateUtils.getCurTime() + ".txt";
        String logFilePath = "";
        FileWriter fw = null;
        PrintWriter pw = null;
        try {

            logFilePath = FileUtils.getApkFile(Constant.FILE_NAME_ERROR) + errorlog;

            if (logFilePath == "") {
                return;
            }
            File logFile = new File(logFilePath);
            if (!logFile.exists()) {
                logFile.createNewFile();
            }

            fw = new FileWriter(logFile, true);
            pw = new PrintWriter(fw);
            pw.println("--------------------" + (new Date().toLocaleString())
                    + "---------------------\n");
            pw.println("-----------current phone model:" + Build.MODEL);
            pw.println("-----------current phone sdk_version:" + Build.VERSION.SDK_INT);
            pw.write(sb.toString());
            pw.close();
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (pw != null) {
                pw.close();
            }
            if (fw != null) {
                try {
                    fw.close();
                } catch (IOException e) {
                }
            }
        }
    }

}
