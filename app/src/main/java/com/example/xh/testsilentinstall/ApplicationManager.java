package com.example.xh.testsilentinstall;

import android.content.Context;
import android.content.pm.IPackageInstallObserver;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.RemoteException;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by xh on 2017/6/2.
 */

public class ApplicationManager {

    public static final int INSTALL_REPLACE_EXISTING = 2;
    public static final int INSTALL_SUCCEEDED = 1;

    public static final int INSTALL_FAILED_ALREADY_EXISTS = -1;
    public static final int INSTALL_FAILED_INVALID_APK = -2;
    public static final int INSTALL_FAILED_INVALID_URI = -3;
    public static final int INSTALL_FAILED_INSUFFICIENT_STORAGE = -4;
    public static final int INSTALL_FAILED_DUPLICATE_PACKAGE = -5;
    public static final int INSTALL_FAILED_NO_SHARED_USER = -6;
    public static final int INSTALL_FAILED_UPDATE_INCOMPATIBLE = -7;
    public static final int INSTALL_FAILED_SHARED_USER_INCOMPATIBLE = -8;
    public static final int INSTALL_FAILED_MISSING_SHARED_LIBRARY = -9;
    public static final int INSTALL_FAILED_REPLACE_COULDNT_DELETE = -10;
    public static final int INSTALL_FAILED_DEXOPT = -11;
    public static final int INSTALL_FAILED_OLDER_SDK = -12;
    public static final int INSTALL_FAILED_CONFLICTING_PROVIDER = -13;
    public static final int INSTALL_FAILED_NEWER_SDK = -14;
    public static final int INSTALL_FAILED_TEST_ONLY = -15;
    public static final int INSTALL_FAILED_CPU_ABI_INCOMPATIBLE = -16;
    public static final int INSTALL_FAILED_MISSING_FEATURE = -17;
    public static final int INSTALL_FAILED_CONTAINER_ERROR = -18;
    public static final int INSTALL_FAILED_INVALID_INSTALL_LOCATION = -19;
    public static final int INSTALL_FAILED_MEDIA_UNAVAILABLE = -20;
    public static final int INSTALL_FAILED_VERIFICATION_TIMEOUT = -21;
    public static final int INSTALL_FAILED_VERIFICATION_FAILURE = -22;
    public static final int INSTALL_FAILED_PACKAGE_CHANGED = -23;
    public static final int INSTALL_FAILED_UID_CHANGED = -24;
    public static final int INSTALL_FAILED_VERSION_DOWNGRADE = -25;
    public static final int INSTALL_FAILED_PERMISSION_MODEL_DOWNGRADE = -26;

    /**
     * 安装失败错误消息
     */
    private static final String[] error_msg = {"INSTALL_FAILED_ALREADY_EXISTS", "INSTALL_FAILED_INVALID_APK", "INSTALL_FAILED_INVALID_URI", "INSTALL_FAILED_INSUFFICIENT_STORAGE", "INSTALL_FAILED_DUPLICATE_PACKAGE",
            "INSTALL_FAILED_NO_SHARED_USER", "INSTALL_FAILED_UPDATE_INCOMPATIBLE", "INSTALL_FAILED_SHARED_USER_INCOMPATIBLE", "INSTALL_FAILED_MISSING_SHARED_LIBRARY", "INSTALL_FAILED_REPLACE_COULDNT_DELETE",
            "INSTALL_FAILED_DEXOPT", "INSTALL_FAILED_OLDER_SDK", "INSTALL_FAILED_CONFLICTING_PROVIDER", "INSTALL_FAILED_NEWER_SDK", "INSTALL_FAILED_TEST_ONLY", "INSTALL_FAILED_CPU_ABI_INCOMPATIBLE",
            "INSTALL_FAILED_MISSING_FEATURE", "INSTALL_FAILED_CONTAINER_ERROR", "INSTALL_FAILED_INVALID_INSTALL_LOCATION", "INSTALL_FAILED_MEDIA_UNAVAILABLE", "INSTALL_FAILED_VERIFICATION_TIMEOUT",
            "INSTALL_FAILED_VERIFICATION_FAILURE", "INSTALL_FAILED_PACKAGE_CHANGED", "INSTALL_FAILED_UID_CHANGED", "INSTALL_FAILED_VERSION_DOWNGRADE", "INSTALL_FAILED_PERMISSION_MODEL_DOWNGRADE"};

    private PackageInstallObserver observer;
    private PackageManager pm;
    private Method method;

    private OnInstalledPackaged onInstalledPackaged;

    public interface OnInstalledPackaged {
        void packageInstalled(String packageName, int returnCode);
    }

    private class PackageInstallObserver extends IPackageInstallObserver.Stub {
        public void packageInstalled(String packageName, int returnCode) throws RemoteException {
            if (onInstalledPackaged != null) {
                onInstalledPackaged.packageInstalled(packageName, returnCode);
            }
        }
    }

    public ApplicationManager(Context context) throws SecurityException, NoSuchMethodException {

        observer = new PackageInstallObserver();
        pm = context.getPackageManager();


        Class<?>[] types = new Class[]{Uri.class, IPackageInstallObserver.class, int.class, String.class};
        method = pm.getClass().getMethod("installPackage", types);
    }

    public void setOnInstalledPackaged(OnInstalledPackaged onInstalledPackaged) {
        this.onInstalledPackaged = onInstalledPackaged;
    }

    public void installPackage(String apkFile, OnInstalledPackaged onInstalledPackaged) throws IllegalArgumentException, IllegalAccessException,
            InvocationTargetException {
        installPackage(new File(apkFile), onInstalledPackaged);
    }

    public void installPackage(File apkFile, OnInstalledPackaged onInstalledPackaged) throws IllegalArgumentException, IllegalAccessException,
            InvocationTargetException {
        if (!apkFile.exists())
            throw new IllegalArgumentException();
        Uri packageURI = Uri.fromFile(apkFile);
        installPackage(packageURI);
        setOnInstalledPackaged(onInstalledPackaged);
    }

    public void installPackage(Uri apkFile) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        //        pm.installPackage(apkFile, observer, INSTALL_REPLACE_EXISTING, null);

        method.invoke(pm, apkFile, observer, INSTALL_REPLACE_EXISTING, null);
    }

    /**
     * 根据错误码返回相应的错误消息
     * @param errorCode 错误码
     * @return 错误信息
     */
    public static String getInstallFailedMsg(int errorCode) {
        if (errorCode >= INSTALL_FAILED_PERMISSION_MODEL_DOWNGRADE && errorCode <= INSTALL_FAILED_ALREADY_EXISTS) {
            int errorMsgIndex = Math.abs(errorCode) - 1;
            return error_msg[errorMsgIndex];
        }
        return "" + errorCode;
    }

}
