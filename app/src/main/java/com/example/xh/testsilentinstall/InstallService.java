package com.example.xh.testsilentinstall;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by xh on 2017/12/13.
 */

public class InstallService extends Service {

    private static final String TAG = InstallService.class.getSimpleName();

    /**
     * 安装 apk
     */
    public void installApk() {
        String apkFilePath = Environment.getExternalStorageDirectory() + "/apk/app.apk";
        try {
            Log.e(TAG, "开始安装：" + apkFilePath);
            ToastUtils.getInstance(this).showText("开始安装");
            new ApplicationManager(this).installPackage(apkFilePath,
                    new ApplicationManager.OnInstalledPackaged() {

                        @Override
                        public void packageInstalled(String packageName, int returnCode) {

                            if (returnCode == ApplicationManager.INSTALL_SUCCEEDED) {
                                Log.e(TAG, "Install succeeded");
                                ToastUtils.getInstance(InstallService.this).showText("安装成功");
                            } else {
                                String errorMsg = ApplicationManager.getInstallFailedMsg(returnCode);
                                Log.e(TAG, "Install failed: " + errorMsg);
                                ToastUtils.getInstance(InstallService.this).showText("安装失败");
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        installApk();
        return super.onStartCommand(intent, flags, startId);
    }

}
