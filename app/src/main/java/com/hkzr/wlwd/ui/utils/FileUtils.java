package com.hkzr.wlwd.ui.utils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import android.os.Environment;
import android.text.TextUtils;

public class FileUtils {
	public static String getApkFile(String path) {
		String savePath = null;
		String storageState = Environment.getExternalStorageState();
		if (storageState.equals(Environment.MEDIA_MOUNTED)) {
			savePath = Environment.getExternalStorageDirectory()
					.getAbsolutePath() + path;
			File file = new File(savePath);
			if (!file.exists()) {
				file.mkdirs();
			}
		}
		if (TextUtils.isEmpty(savePath)) {
			return null;
		}
		return savePath;
	}

	public static final boolean saveBytesToFile(byte[] bytes, File file) {
		if (bytes == null) {
			return false;
		}

		ByteArrayInputStream bais = null;
		BufferedOutputStream bos = null;
		try {
			file.getParentFile().mkdirs();
			file.createNewFile();

			bais = new ByteArrayInputStream(bytes);
			bos = new BufferedOutputStream(new FileOutputStream(file));

			int size;
			byte[] temp = new byte[1024];
			while ((size = bais.read(temp, 0, temp.length)) != -1) {
				bos.write(temp, 0, size);
			}

			bos.flush();

			return true;

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				bos = null;
			}
			if (bais != null) {
				try {
					bais.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				bais = null;
			}
		}
		return false;
	}

	public static void clearCache() {
		String filePath = getApkFile(Constant.FILE_NAME_IMG);
		File f = new File(filePath);
		if (f.isDirectory()) {
			File[] fs = f.listFiles();
			for (File i : fs) {
				i.delete();
			}
		}
	}

}
