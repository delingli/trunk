package com.hkzr.wlwd.ui.utils;

import com.hkzr.wlwd.model.ExternalContactEntity;

import java.util.Comparator;

/**
 * Created by admin on 2017/6/26.
 */

public class PinyinExterComparator implements Comparator<ExternalContactEntity> {

    public static PinyinComparator instance = null;

    public int compare(ExternalContactEntity o1, ExternalContactEntity o2) {
        if (o1.getSortLetters().equals("@")
                || o2.getSortLetters().equals("#")) {
            return -1;
        } else if (o1.getSortLetters().equals("#")
                || o2.getSortLetters().equals("@")) {
            return 1;
        } else {
            return o1.getSortLetters().compareTo(o2.getSortLetters());
        }
    }

    public static PinyinComparator getInstance() {
        if (instance == null) {
            instance = new PinyinComparator();
        }
        return instance;
    }
}
