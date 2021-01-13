/**
 *
 */
package com.zrt.zenb.common;

import java.io.File;

/**
 * @author PGW
 *
 */
public abstract class FileCommon {

        /**
         *
         */
        protected FileCommon() {
        }

        /**
         *
         * @param srcFile
         * @param dstFile
         * @return
         */
        public static final boolean moveFile(String srcFile, String dstFile) {
                try {
                        File fromFile = new File(srcFile);
                        File toFile = new File(dstFile);

                        if (toFile.exists()) {
                                toFile.delete();
                        }
                        fromFile.renameTo(toFile);
                } catch (Exception e) {
                        e.printStackTrace();
                        return false;
                }

                return true;
        }

        /**
        *  移动目录下的所有文件，不含子目录
        *
        * @param srcFile
        * @param dstFile
        * @return
        */
       public static final boolean moveFiles(String from, String to) {
               try {
                       File fromDir = new File(from);
                       File toDir = new File(to);
                       if (!toDir.exists()) {
                               toDir.mkdirs();
                       }
                       File[] srcFiles = fromDir.listFiles();
                       if (srcFiles == null) {
                               return true;
                       }

                       for (int i = 0; i < srcFiles.length; i++) {
                               File dstFile = new File(toDir.getPath() + "\\" + srcFiles[i].getName());
                               if (dstFile.exists()) {
                                       dstFile.delete();
                               }
                               srcFiles[i].renameTo(dstFile);
                       }

               } catch (Exception e) {
                       e.printStackTrace();
                       return false;
               }

               return true;
       }

        /**
         *
         * @param from
         * @param to
         * @throws Exception
         */
        public static boolean moveDirFiles(String from, String to) throws Exception {
                try {
                        File dir = new File(from);
                        File moveDir = new File(to);
                        if (!moveDir.exists()) {
                                moveDir.mkdirs();
                        }
                        File[] files = dir.listFiles();
                        if (files == null) {
                                return true;
                        }
//                        IntStream.range(0, files.length).forEach(i -> {
//                                try {
//                                        if (files[i].isDirectory()) {
//                                                moveDirFiles(files[i].getPath(), to + "\\" + files[i].getName());
//                                                files[i].delete();
//                                        }
//                                        File moveFile = new File(moveDir.getPath() + "\\" + files[i].getName());
//                                        if (moveFile.exists()) {
//                                                moveFile.delete();
//                                        }
//                                        files[i].renameTo(moveFile);
//                                } catch (Exception e) {
//                                        e.printStackTrace();
//                                }
//                        });
                        for (int i = 0; i < files.length; i++) {
                                if (files[i].isDirectory()) {
                                        moveDirFiles(files[i].getPath(), to + "\\" + files[i].getName());
                                        files[i].delete();
                                }
                                File moveFile = new File(moveDir.getPath() + "\\" + files[i].getName());
                                if (moveFile.exists()) {
                                        moveFile.delete();
                                }
                                files[i].renameTo(moveFile);
                        }
                } catch (Exception e) {
                        e.printStackTrace();
                        return false;
                }
                return true;
        }
        
     
}
