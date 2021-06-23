package com.hkzr.wlwd.ui.utils;


import android.content.Context;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Locale;

/**
 * @Description:文件处理工具类
 * @Date:2014-11-21 下午5:03:27
 */
public class FileUtil {

    public static final long B = 1;
    public static final long KB = B * 1024;
    public static final long MB = KB * 1024;
    public static final long GB = MB * 1024;
    private static final int BUFFER = 8192;

    /**
     * 格式化文件大小(带单位)
     *
     * @param size
     * @return
     */
    public static String formatFileSize(long size) {
        StringBuilder sb = new StringBuilder();
        String unit = "";
        double tmpSize = 0;
        if (size < KB) {
            sb.append(size).append("B");
            return sb.toString();
        } else if (size < MB) {
            tmpSize = getSize(size, KB);
            unit = "KB";
        } else if (size < GB) {
            tmpSize = getSize(size, MB);
            unit = "MB";
        } else {
            tmpSize = getSize(size, GB);
            unit = "GB";
        }
        return sb.append(twodot(tmpSize)).append(unit).toString();
    }
    /**
     * @param context
     *            删除缓存
     */
    public static void clearAllCache(Context context) {
        deleteFile(context.getCacheDir());
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            deleteFile(context.getExternalCacheDir());
        }
    }
    public static double getSize(long size, long unit) {
        return (double) size / (double) unit;
    }

    /**
     * 保留两位小数
     *
     * @return
     */
    public static String twodot(double num) {
        return String.format(Locale.CHINA, "%.2f", num);
    }

    /**
     * 读取文件
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static String readTextFile(File file) throws IOException {
        String text = null;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            text = readTextInputStream(fis);
        } finally {
            if (fis != null) {
                fis.close();
            }
        }
        return text;
    }

    /**
     * 从流中读取文件
     *
     * @param is 输入流
     * @return
     * @throws IOException
     */
    public static String readTextInputStream(InputStream is) throws IOException {
        StringBuffer sb = new StringBuffer();
        String line;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\r\n");
            }
        } finally {
            if (br != null) {
                br.close();
            }
        }
        return sb.toString();
    }

    /**
     * 将文本内容写入文件
     *
     * @param file 要写入的文件
     * @param str  要写入的字符串内容
     * @throws IOException
     */
    public static void writeTextFile(File file, String str) throws IOException {
        DataOutputStream dos = null;
        try {
            dos = new DataOutputStream(new FileOutputStream(file));
            dos.write(str.getBytes());
        } finally {
            if (dos != null) {
                dos.close();
            }
        }
    }


    /**
     * 获取一个文件夹大小
     *
     * @param f 目标文件夹
     * @return long size
     * @throws Exception
     */
    public static long getFileSize(File f) {
        long size = 0;
        File flist[] = f.listFiles();
        for (int i = 0; i < flist.length; i++) {
            if (flist[i].isDirectory()) {
                size = size + getFileSize(flist[i]);
            } else {
                size = size + flist[i].length();
            }
        }
        return size;
    }

    /**
     * 删除文件
     *
     * @param file 目标文件
     */
    public static void deleteFile(File file) {
        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                File files[] = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    deleteFile(files[i]);
                }
            }
            file.delete();
        }
    }

    /**
     * 　　*
     * 　　* @param myContext
     * 　　* @param ASSETS_NAME 要复制的文件名
     * 　　* @param savePath 要保存的路径
     * 　　* @param saveName 复制后的文件名
     * 　　* testCopy(Context context)是一个测试例子。
     *
     */
    public static void copy(Context myContext, String ASSETS_NAME,
                            String savePath, String saveName) {
        String filename = savePath + "/" + saveName;
        File dir = new File(savePath);
        Log.e(dir.getAbsolutePath());
        Log.e(dir.getPath());
        try {
            Log.e(dir.getCanonicalPath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 如果目录不中存在，创建这个目录
        if (!dir.exists())
            dir.mkdir();
        try {
            if (!(new File(filename)).exists()) {
                InputStream is = myContext.getResources().getAssets()
                        .open(ASSETS_NAME);
                FileOutputStream fos = new FileOutputStream(filename);
                byte[] buffer = new byte[7168];
                int count = 0;
                while ((count = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                }
                fos.close();
                is.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
