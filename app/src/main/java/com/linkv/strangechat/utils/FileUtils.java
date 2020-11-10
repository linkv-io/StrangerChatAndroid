package com.linkv.strangechat.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Xiaohong on 2020/11/8.
 * desc:
 */
public class FileUtils {

    /**
     * 拷贝文件
     * @param in
     * @param target
     * @throws IOException
     */
    public static void copyToFile(InputStream in, File target) throws IOException {
        if (target == null) {
            throw new IOException();
        }
        createNewFile(target);
        FileOutputStream fos = new FileOutputStream(target);
        try {
            copy(in, fos);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            try {
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    /**
     * 创建文件
     * @param file
     * @return
     */
    public static boolean createNewFile(File file) {
        boolean complete = false;
        if (!file.exists() || file.isDirectory()) {
            File parent = file.getParentFile();
            boolean dirExist = parent.exists() && parent.isDirectory();
            if (!dirExist) {
                dirExist = parent.mkdirs();
            }
            if (dirExist) {
                try {
                    complete = file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return complete;
    }

    /**
     * will not close stream
     *
     * @param in
     * @param fos
     * @throws IOException
     */
    public static void copy(InputStream in, FileOutputStream fos) throws IOException {
        byte buffer[] = new byte[1024];
        int len = 0;
        while ((len = in.read(buffer)) != -1) {
            fos.write(buffer, 0, len);
        }
        fos.flush();
    }

}
