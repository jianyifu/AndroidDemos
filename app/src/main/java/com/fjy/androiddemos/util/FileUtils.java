package com.fjy.androiddemos.util;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.progress.ProgressMonitor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class FileUtils {

    public static void CopyAssets(Context context, String oldPath, String newPath) {
        try {
            String fileNames[] = context.getAssets().list(oldPath);// 获取assets目录下的所有文件及目录名
            if (fileNames.length > 0) {// 如果是目录
                File file = new File(newPath);
                file.mkdirs();// 如果文件夹不存在，则递归
                for (String fileName : fileNames) {
                    CopyAssets(context, oldPath + "/" + fileName, newPath + "/" + fileName);
                }
            } else {// 如果是文件
                InputStream is = context.getAssets().open(oldPath);
                FileOutputStream fos = new FileOutputStream(new File(newPath));
                byte[] buffer = new byte[1024];
                int byteCount = 0;
                while ((byteCount = is.read(buffer)) != -1) {// 循环从输入流读取
                    // buffer字节
                    fos.write(buffer, 0, byteCount);// 将读取的输入流写入到输出流
                }
                fos.flush();// 刷新缓冲区
                is.close();
                fos.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void unzip(final File srcFile, String dest, String passwd, boolean isInNewThread, final boolean isDeleteZipFile,final Handler handler) throws ZipException {
        final ZipFile zipFile = new ZipFile(srcFile);  // 首先创建ZipFile指向磁盘上的.zip文件
//        zFile.setFileNameCharset("GBK");       // 设置文件名编码，在GBK系统中需要设置
        if (!zipFile.isValidZipFile()) {   // 验证.zip文件是否合法，包括文件是否存在、是否为zip文件、是否被损坏等
            throw new ZipException("压缩文件不合法,可能被损坏.");
        }
        File destDir = new File(dest);     // 解压目录
        if (destDir.isDirectory() && !destDir.exists()) {
            destDir.mkdir();
        }
        if (zipFile.isEncrypted()) {
            zipFile.setPassword(passwd.toCharArray());  // 设置密码
        }
        final ProgressMonitor progressMonitor = zipFile.getProgressMonitor();

        Thread progressThread = new Thread(new Runnable() {

            @Override
            public void run() {
                Bundle bundle = null;
                Message msg = null;
                try {
                    int percentDone = 0;
                    // long workCompleted=0;
                    // handler.sendEmptyMessage(ProgressMonitor.RESULT_SUCCESS)
                    if (handler == null) {
                        return;
                    }
                    handler.sendEmptyMessage(CompressStatus.START);
                    while (true) {
                        Thread.sleep(100);

                        percentDone = progressMonitor.getPercentDone();
                        bundle = new Bundle();
                        bundle.putInt(CompressKeys.PERCENT, percentDone);
                        msg = new Message();
                        msg.what = CompressStatus.HANDLING;
                        msg.setData(bundle);
                        handler.sendMessage(msg);
                        if (percentDone >= 100) {
                            break;
                        }
                    }
                    handler.sendEmptyMessage(CompressStatus.COMPLETED);
                } catch (InterruptedException e) {
                    bundle = new Bundle();
                    bundle.putString(CompressKeys.ERROR, e.getMessage());
                    msg = new Message();
                    msg.what = CompressStatus.ERROR;
                    msg.setData(bundle);
                    handler.sendMessage(msg);
                    e.printStackTrace();
                }
                finally
                {
                    if(isDeleteZipFile)
                    {
                        try {
                            zipFile.removeFile(srcFile.getAbsolutePath());//zipFile.delete();
                        }catch (ZipException e){
                            e.printStackTrace();
                        }

                    }
                }
            }
        });

        progressThread.start();
        zipFile.setRunInThread(isInNewThread);
        zipFile.extractAll(dest);      // 将文件抽出到解压目录(解压)
    }
}