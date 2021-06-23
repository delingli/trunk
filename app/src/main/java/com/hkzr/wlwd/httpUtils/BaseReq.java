package com.hkzr.wlwd.httpUtils;

import android.text.TextUtils;

import com.hkzr.wlwd.ui.utils.DesUtil;
import com.hkzr.wlwd.ui.utils.GsonInstance;
import com.hkzr.wlwd.ui.utils.Log;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class BaseReq<T> implements Serializable {
    public boolean Success;
    public String ResultCode;
    public String Message;
    public String ReturnData;

    public T getValue(Class mClass) {
        T mValue = null;
        if (!TextUtils.isEmpty(ReturnData)) {
            final String mDesStr = DesUtil.decrypt(ReturnData);
            if (!TextUtils.isEmpty(mDesStr)) {
                Log.e("返回(明文) : " + mDesStr + "");
                if (mClass.isAssignableFrom(String.class)) {
                    // return (T)"";
                    return (T) mDesStr;
                } else {
                    mValue = (T) GsonInstance.gson().fromJson(mDesStr, mClass);
                }
            }
        }
        return mValue;
    }

    /**
     * @param mClass
     * @param Decrypt 是否解密 false界解密
     * @return
     */
    public T getValue(Class mClass, boolean Decrypt) {
        T mValue = null;
        if (!TextUtils.isEmpty(ReturnData)) {
            // final String mDesStr = DesUtil.decrypt(ReturnData);
            if (!TextUtils.isEmpty(ReturnData)) {
                Log.e("返回(明文) : " + ReturnData + "");
                if (mClass.isAssignableFrom(String.class)) {
                    // return (T)"";
                    return (T) ReturnData;
                } else {
                    mValue = (T) GsonInstance.gson().fromJson(ReturnData,
                            mClass);
                }
            }
        }
        return mValue;
    }

    private static ParameterizedType type(final Class raw, final Type... args) {
        return new ParameterizedType() {
            @Override
            public Type getRawType() {
                return raw;
            }

            @Override
            public Type getOwnerType() {
                return null;
            }

            @Override
            public Type[] getActualTypeArguments() {
                return args;
            }
        };
    }

}
