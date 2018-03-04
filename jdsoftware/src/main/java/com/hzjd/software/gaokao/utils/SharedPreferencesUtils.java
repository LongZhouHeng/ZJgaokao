package com.hzjd.software.gaokao.utils;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

public class SharedPreferencesUtils {
    private static void paraCheck(SharedPreferences sp, String key) {
        if (sp == null) {
            throw new IllegalArgumentException();
        }
        if (TextUtils.isEmpty(key)) {
            throw new IllegalArgumentException();
        }
    }

    public static boolean putObject(SharedPreferences sp, String key, Object obj) {
        paraCheck(sp, key);

        if (obj == null) {
            Editor e = sp.edit();
            e.putString(key, "");
            return e.commit();
        } else {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = null;
            try {
                oos = new ObjectOutputStream(baos);
                oos.writeObject(obj);
            } catch (IOException e1) {
                e1.printStackTrace();
                return false;
            }
            String objectBase64 = new String(Base64.encode(baos.toByteArray(),
                    Base64.DEFAULT));
            Editor e = sp.edit();
            e.putString(key, objectBase64);
            return e.commit();
        }
    }

    public static Object getObject(SharedPreferences sp, String key,
                                   Object defaultValue) {
        paraCheck(sp, key);
        String objectBase64 = sp.getString(key, "");
        byte[] base64Bytes = Base64.decode(objectBase64.getBytes(),
                Base64.DEFAULT);
        ByteArrayInputStream bais = new ByteArrayInputStream(base64Bytes);
        ObjectInputStream ois;
        try {
            ois = new ObjectInputStream(bais);
            return ois.readObject();
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
            return defaultValue;
        } catch (IOException e) {
            e.printStackTrace();
            return defaultValue;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return defaultValue;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return defaultValue;
        }
    }
}
