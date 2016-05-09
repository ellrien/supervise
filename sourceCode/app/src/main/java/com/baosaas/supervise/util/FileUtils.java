package com.baosaas.supervise.util;

import java.io.File;

/**
 * Created by Ellrien on 2015/12/11.
 */
public class FileUtils {
    /**
     * 删除某个文件夹下的所有文件(包括自己)
     */
    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            //递归删除目录中的子目录下
            for (int i=0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }
}
