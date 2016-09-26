package com.yny.download;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Yang on 2016/4/14.
 */
public class FileUtil {

    private static final String TAG = FileUtil.class.getSimpleName();

    public static String getFILESPATH(Context context) {
        return context.getFilesDir().getPath() + "//";
    }

    /**
     * 判断SDCard是否存在？是否可以进行读写
     */
    public static boolean SDCardState() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取SDCard文件路径
     *
     * @return SD卡路径
     */
    public static String SDCardPath() {
        String path = "";
        try {
            if (SDCardState()) {//如果SDCard存在并且可以读写
                path = Environment.getExternalStorageDirectory().getPath();
            }
        } catch (Exception e) {
            Log.i(TAG, "SD Card:" + e.getMessage());
            e.printStackTrace();
        }
        return path;
    }

    /**
     * 在SD卡上创建目录
     *
     * @param dirName 要创建的目录名
     * @return 创建得到的目录
     */
    public static File createSDDir(String dirName) {
        File dir = new File(Environment.getExternalStorageDirectory().getPath() + "/" + dirName);
        dir.mkdir();
        return dir;
    }

    /**
     * 删除SD卡上的目录
     *
     * @param dirName
     */
    public static boolean delSDDir(String dirName) {
        File dir = new File(Environment.getExternalStorageDirectory().getPath() + "/" + dirName);
        return delDir(dir);
    }

    /**
     * 在SD卡上创建文件
     *
     * @throws IOException
     */
    public static File createSDFile(String fileName) throws IOException {
        File file = new File(Environment.getExternalStorageDirectory().getPath() + "/" + fileName);
        file.createNewFile();
        return file;
    }

    /**
     * 删除SD卡上的文件。只删除文件，不删除文件夹
     *
     * @param fileName
     */
    public static boolean delSDFile(String fileName) {
        File file = new File(Environment.getExternalStorageDirectory().getPath() + "/" + fileName);
        if (file == null || !file.exists() || file.isDirectory())
            return false;
        file.delete();
        return true;
    }

    /**
     * 修改SD卡上的文件或目录名
     *
     * @param oldfileName
     * @param newFileName
     * @return
     */
    public static boolean renameSDFile(String oldfileName, String newFileName) {
        File oleFile = new File(Environment.getExternalStorageDirectory().getPath() + "/" + oldfileName);
        File newFile = new File(Environment.getExternalStorageDirectory().getPath() + "/" + newFileName);
        return oleFile.renameTo(newFile);
    }

    /**
     * 拷贝SD卡上的单个文件
     *
     * @param srcFileName
     * @param destFileName
     * @return
     * @throws IOException
     */
    public static boolean copySDFileTo(String srcFileName, String destFileName)
            throws IOException {
        File srcFile = new File(Environment.getExternalStorageDirectory().getPath() + "/" + srcFileName);
        File destFile = new File(Environment.getExternalStorageDirectory().getPath() + "/" + destFileName);
        return copyFileTo(srcFile, destFile);
    }

    /**
     * 拷贝SD卡上指定目录的所有文件
     *
     * @param srcDirName
     * @param destDirName
     * @return
     * @throws IOException
     */
    public static boolean copySDFilesTo(String srcDirName, String destDirName)
            throws IOException {
        File srcDir = new File(Environment.getExternalStorageDirectory().getPath() + "/" + srcDirName);
        File destDir = new File(Environment.getExternalStorageDirectory().getPath() + "/" + destDirName);
        return copyFilesTo(srcDir, destDir);
    }

    /**
     * 移动SD卡上的单个文件
     *
     * @param srcFileName
     * @param destFileName
     * @return
     * @throws IOException
     */
    public static boolean moveSDFileTo(String srcFileName, String destFileName)
            throws IOException {
        File srcFile = new File(Environment.getExternalStorageDirectory().getPath() + "/" + srcFileName);
        File destFile = new File(Environment.getExternalStorageDirectory().getPath() + "/" + destFileName);
        return moveFileTo(srcFile, destFile);
    }

    /**
     * 移动SD卡上的指定目录的所有文件
     *
     * @param srcDirName
     * @param destDirName
     * @return
     * @throws IOException
     */
    public static boolean moveSDFilesTo(String srcDirName, String destDirName)
            throws IOException {
        File srcDir = new File(Environment.getExternalStorageDirectory().getPath() + "/" + srcDirName);
        File destDir = new File(Environment.getExternalStorageDirectory().getPath() + "/" + destDirName);
        return moveFilesTo(srcDir, destDir);
    }

    /**
     * 清空指定文件夹下所有文件和文件夹
     *
     * @param directory
     */
    public static void delFilesFromData(File directory) {
        if (directory != null && directory.exists() && directory.isDirectory()) {
            for (File item : directory.listFiles()) {
                item.delete();
            }
        }
    }

    /**
     * 判断目录是否存在，不存在则创建目录
     *
     * @param filePath
     * @param isCreate 是否创建，true/false
     * @return
     */
    public static boolean isExistFile(String filePath, boolean isCreate) {
        File destDir = new File(filePath);

        if (!destDir.exists()) {
            if (isCreate) {
                return destDir.mkdirs();
            }
            return false;
        }
        return true;
    }

    /**
     * 文件大小
     *
     * @param file
     * @return
     * @throws Exception
     */
    public static int getFileSize(File file) {
        int s = 0;
        if (file.exists()) {
            try {
                FileInputStream is = new FileInputStream(file);
                s = is.available();
                is.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return s;
    }

    /**
     * 获得指定文件的byte数组
     */
    public static byte[] getBytes(File file){
        byte[] buffer = null;
        try {
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }

    /**
     * 根据byte数组，生成文件
     */
    public static void getFile(byte[] bfile, String filePath, String fileName) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            File dir = new File(filePath);
            if(!dir.exists()&&dir.isDirectory()){//判断文件目录是否存在
                dir.mkdirs();
            }
            file = new File(filePath+"\\"+fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bfile);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    /**
     * 建立私有文件
     *
     * @param context
     * @param fileName
     * @return
     * @throws IOException
     */
    public static File creatDataFile(Context context, String fileName) throws IOException {
        File file = new File(context.getFilesDir().getPath() + "/" + fileName);
        file.createNewFile();
        return file;
    }

    /**
     * 建立私有目录
     *
     * @param dirName
     * @return
     */
    public static File creatDataDir(Context context, String dirName) {
        File dir = new File(context.getFilesDir().getPath() + "/" + dirName);
        dir.mkdir();
        return dir;
    }

    /**
     * 删除私有文件
     *
     * @param fileName
     * @return
     */
    public static boolean delDataFile(Context context, String fileName) {
        File file = new File(context.getFilesDir().getPath() + "/" + fileName);
        return delFile(file);
    }

    /**
     * 删除私有目录
     *
     * @param dirName
     * @return
     */
    public static boolean delDataDir(Context context, String dirName) {
        File file = new File(context.getFilesDir().getPath() + "/" + dirName);
        return delDir(file);
    }

    /**
     * 更改私有文件名
     *
     * @param oldName
     * @param newName
     * @return
     */
    public static boolean renameDataFile(Context context, String oldName, String newName) {
        File oldFile = new File(context.getFilesDir().getPath() + "/" + oldName);
        File newFile = new File(context.getFilesDir().getPath() + "/" + newName);
        return oldFile.renameTo(newFile);
    }

    /**
     * 在私有目录下进行文件复制
     *
     * @param srcFileName  包含路径及文件名
     * @param destFileName
     * @return
     * @throws IOException
     */
    public static boolean copyDataFileTo(Context context, String srcFileName, String destFileName)
            throws IOException {
        File srcFile = new File(context.getFilesDir().getPath() + "/" + srcFileName);
        File destFile = new File(context.getFilesDir().getPath() + "/" + destFileName);
        return copyFileTo(srcFile, destFile);
    }

    /**
     * 复制私有目录里指定目录的所有文件
     *
     * @param srcDirName
     * @param destDirName
     * @return
     * @throws IOException
     */
    public static boolean copyDataFilesTo(Context context, String srcDirName, String destDirName)
            throws IOException {
        File srcDir = new File(context.getFilesDir().getPath() + "/" + srcDirName);
        File destDir = new File(context.getFilesDir().getPath() + "/" + destDirName);
        return copyFilesTo(srcDir, destDir);
    }

    /**
     * 移动私有目录下的单个文件
     *
     * @param srcFileName
     * @param destFileName
     * @return
     * @throws IOException
     */
    public static boolean moveDataFileTo(Context context, String srcFileName, String destFileName)
            throws IOException {
        File srcFile = new File(context.getFilesDir().getPath() + "/" + srcFileName);
        File destFile = new File(context.getFilesDir().getPath() + "/" + destFileName);
        return moveFileTo(srcFile, destFile);
    }

    /**
     * 移动私有目录下的指定目录下的所有文件
     *
     * @param srcDirName
     * @param destDirName
     * @return
     * @throws IOException
     */
    public static boolean moveDataFilesTo(Context context, String srcDirName, String destDirName)
            throws IOException {
        File srcDir = new File(context.getFilesDir().getPath() + "/" + srcDirName);
        File destDir = new File(context.getFilesDir().getPath() + "/" + destDirName);
        return moveFilesTo(srcDir, destDir);
    }

    /**
     * 将一个输入流中的内容写入到SD卡上生成文件
     *
     * @param path        文件目录
     * @param fileName    文件名
     * @param inputStream 字节输入流
     * @return 得到的文件
     */
    public static File writeToSDCard(String path, String fileName,
                                     InputStream inputStream) {
        File file = null;
        OutputStream output = null;
        try {
            createSDDir(path);
            file = createSDFile(path + fileName);
            output = new FileOutputStream(file);
            byte buffer[] = new byte[4 * 1024];
            while ((inputStream.read(buffer)) != -1) {
                output.write(buffer);
            }
            output.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                output.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    /**
     * 拷贝资源文件夹raw下的文件到目标路径。并更改文件内容
     *
     * @param context 上下文对象
     * @param id      资源文件id
     * @return 是否成功拷贝文件
     * @throws IOException
     */
    public static boolean CopyRawFile(Context context, int id, String filePath) throws IOException {

        File file = new File(filePath);

        //目录不存在，创建
        if (!isExistFile(filePath, false)) {
            file.getParentFile().mkdirs();
        }

        try {
            file.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }

        FileOutputStream os = null;
        InputStream is = null;

        try {
            os = new FileOutputStream(filePath);
            is = context.getResources().openRawResource(id);
            long len = is.available();
            byte[] buffer = new byte[1024 * 4];
            int count = 0;
            int readCount = 0;

            while ((count = is.read(buffer)) > 0) {
                os.write(buffer, 0, count);
                os.flush();
                readCount += count;
                if (readCount >= len)
                    break;
            }

        } catch (IOException e) {
            Log.i(TAG, "raw file IO:" + e.getMessage());
            return false;
        } finally {
            try {
                if (is != null)
                    is.close();
                if (os != null) {
                    os.flush();
                    os.close();
                }

            } catch (Exception e) {
                Log.i(TAG, "raw file close:" + e.getMessage());
                e.printStackTrace();
            }
        }
        return true;
    }

    /**
     * 拷贝资源文件夹assets下的文件到目标路径，并更改文件内容
     *
     * @param context  上下文对象
     * @param descPath 目标路径
     * @return 是否成功拷贝文件
     * @throws IOException
     */
    public static boolean CopyAssetsFile(Context context, String fileName, String descPath)
            throws IOException {
        String filepath = descPath + "/" + fileName;
        File file = new File(filepath);

        //目录不存在，创建
        if (!isExistFile(descPath, true)) {
            file.getParentFile().mkdirs();
        }

        try {
            //该方法在文件已存在时返回false
            file.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }

        FileOutputStream os = null;
        InputStream is = null;

        try {
            os = new FileOutputStream(filepath);
            is = context.getResources().getAssets().open(fileName);
            long len = is.available();
            byte[] buffer = new byte[1024 * 4];
            int count = 0;
            int readCount = 0;

            while ((count = is.read(buffer)) > 0) {
                os.write(buffer, 0, count);
                os.flush();
                readCount += count;
                if (readCount >= len)
                    break;
            }

        } catch (IOException e) {
            Log.i(TAG, "assets file IO:" + e.getMessage());
            return false;
        } finally {
            try {
                if (is != null)
                    is.close();
                if (os != null) {
                    os.flush();
                    os.close();
                }

            } catch (Exception e) {
                Log.i(TAG, "assets file close:" + e.getMessage());
                e.printStackTrace();
            }
        }
        return true;
    }

    //--------------------------

    /**
     * 删除一个文件
     *
     * @param file
     * @return
     */
    private static boolean delFile(File file) {
        if (file.isDirectory())
            return false;
        return file.delete();
    }

    /**
     * 删除一个目录（可以是非空目录）
     *
     * @param dir
     */
    private static boolean delDir(File dir) {
        if (dir == null || !dir.exists() || dir.isFile()) {
            return false;
        }
        for (File file : dir.listFiles()) {
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                delDir(file);// 递归
            }
        }
        dir.delete();
        return true;
    }

    /**
     * 拷贝目录下的所有文件到指定目录
     *
     * @param srcDir
     * @param destDir
     * @return
     * @throws IOException
     */
    private static boolean copyFilesTo(File srcDir, File destDir) throws IOException {
        if (!srcDir.isDirectory() || !destDir.isDirectory())
            return false;// 判断是否是目录
        if (!destDir.exists())
            return false;// 判断目标目录是否存在
        File[] srcFiles = srcDir.listFiles();
        for (int i = 0; i < srcFiles.length; i++) {
            if (srcFiles[i].isFile()) {
                // 获得目标文件
                File destFile = new File(destDir.getPath() + "//"
                        + srcFiles[i].getName());
                copyFileTo(srcFiles[i], destFile);
            } else if (srcFiles[i].isDirectory()) {
                File theDestDir = new File(destDir.getPath() + "//"
                        + srcFiles[i].getName());
                copyFilesTo(srcFiles[i], theDestDir);
            }
        }
        return true;
    }

    /**
     * 移动一个文件
     *
     * @param srcFile
     * @param destFile
     * @return
     * @throws IOException
     */
    private static boolean moveFileTo(File srcFile, File destFile) throws IOException {
        boolean iscopy = copyFileTo(srcFile, destFile);
        if (!iscopy)
            return false;
        delFile(srcFile);
        return true;
    }

    /**
     * 移动目录下的所有文件到指定目录
     *
     * @param srcDir
     * @param destDir
     * @return
     * @throws IOException
     */
    private static boolean moveFilesTo(File srcDir, File destDir) throws IOException {
        if (!srcDir.isDirectory() || !destDir.isDirectory()) {
            return false;
        }
        File[] srcDirFiles = srcDir.listFiles();
        for (int i = 0; i < srcDirFiles.length; i++) {
            if (srcDirFiles[i].isFile()) {
                File oneDestFile = new File(destDir.getPath() + "//"
                        + srcDirFiles[i].getName());
                moveFileTo(srcDirFiles[i], oneDestFile);
                delFile(srcDirFiles[i]);
            } else if (srcDirFiles[i].isDirectory()) {
                File oneDestFile = new File(destDir.getPath() + "//"
                        + srcDirFiles[i].getName());
                moveFilesTo(srcDirFiles[i], oneDestFile);
                delDir(srcDirFiles[i]);
            }

        }
        return true;
    }

    /**
     * 拷贝一个文件,srcFile源文件，destFile目标文件
     *
     * @param srcFile
     * @param destFile
     * @throws IOException
     */
    private static boolean copyFileTo(File srcFile, File destFile) throws IOException {
        if (srcFile.isDirectory() || destFile.isDirectory())
            return false;// 判断是否是文件
        FileInputStream fis = new FileInputStream(srcFile);
        FileOutputStream fos = new FileOutputStream(destFile);
        int readLen = 0;
        byte[] buf = new byte[1024];
        while ((readLen = fis.read(buf)) != -1) {
            fos.write(buf, 0, readLen);
        }
        fos.flush();
        fos.close();
        fis.close();
        return true;
    }
}
