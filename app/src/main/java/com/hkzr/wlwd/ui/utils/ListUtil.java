package com.hkzr.wlwd.ui.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

/**
 * 创    建:  lt  2018/2/22--14:52
 * 作    用:  集合有关
 * 注意事项:
 */

public class ListUtil {
    /**
     * 深度拷贝list
     */
    public static <T> List<T> deepCopy(List<T> src) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteOut);
        out.writeObject(src);

        ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
        ObjectInputStream in = new ObjectInputStream(byteIn);
        @SuppressWarnings("unchecked")
        List<T> dest = (List<T>) in.readObject();
        return dest;
    }
    public static <T> boolean notEmpty(List<T> list) {
        return !isEmpty(list);
    }

    public static <T> boolean isEmpty(List<T> list) {
        if (list == null || list.size() == 0) {
            return true;
        }
        return false;
    }

    public static int getSize(List list) {
        if (list == null)
            return 0;
        return list.size();
    }

    public static <T> int getSize(T[] objs) {
        if (objs == null)
            return 0;
        return objs.length;
    }

    public static int getSize(int[] is) {
        if (is == null)
            return 0;
        return is.length;
    }
}
