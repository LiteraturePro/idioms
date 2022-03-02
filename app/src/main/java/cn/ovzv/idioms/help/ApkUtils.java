package cn.ovzv.idioms.help;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.king.app.updater.constant.Constants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;

import androidx.core.content.FileProvider;


/**
 * @author Jenly <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
public final class ApkUtils {

    private ApkUtils(){
        throw new AssertionError();
    }

    /**
     * 通过url获取App的全名称
     * @param context
     * @return AppName.apk
     */
    public static String getAppFullName(Context context,String url,String defaultName){
        if(url.endsWith(".apk")){
            String apkName = url.substring(url.lastIndexOf("/") + 1);
            if(apkName.length() <= 64){
                return apkName;
            }
        }

        String filename = getAppName(context);

        if(TextUtils.isEmpty(filename)){
            filename = defaultName;
        }
        if(filename.endsWith(".apk")){
            return filename;
        }
        return String.format("%s.apk",filename);
    }

    /**
     * 获取包信息
     * @param context
     * @return
     * @throws PackageManager.NameNotFoundException
     */
    public static PackageInfo getPackageInfo(Context context) throws PackageManager.NameNotFoundException {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo = packageManager.getPackageInfo( context.getPackageName(), 0);
        return packageInfo;
    }

    /**
     * 通过APK路径获取包信息
     * @param context
     * @param archiveFilePath
     * @return
     */
    public static PackageInfo getPackageInfo(Context context, String archiveFilePath) throws Exception {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo = packageManager.getPackageArchiveInfo(archiveFilePath, PackageManager.GET_ACTIVITIES);
        return packageInfo;
    }


    /**
     * 获取App的名称
     */
    public static String getAppName(Context context) {
        try{
            int labelRes = getPackageInfo(context).applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取App的图标
     * @param context
     * @return
     */
    public static int getAppIcon(Context context){
        try{
            return getPackageInfo(context).applicationInfo.icon;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    /**
     * 安装apk
     * @param context
     * @param file
     */
    public static void installApk(Context context,File file,String authority){
        Intent intent = getInstallIntent(context,file,authority);
        context.startActivity(intent);
    }

    /**
     * 获取安装Intent
     * @param context
     * @param file
     * @param authority
     * @return
     */
    public static Intent getInstallIntent(Context context,File file,String authority){

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        Uri uriData;
        String type = "application/vnd.android.package-archive";
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            uriData = FileProvider.getUriForFile(context, authority, file);
        }else{
            uriData = Uri.fromFile(file);
        }
        intent.setDataAndType(uriData, type);
        return intent;
    }

    /**
     * APK是否存在
     * @param context
     * @param versionCode
     * @param file
     * @return
     * @throws Exception
     */
    public static boolean apkExists(Context context,int versionCode,File file) throws Exception{
        if(file!=null && file.exists()){
            String packageName = context.getPackageName();
            PackageInfo packageInfo = ApkUtils.getPackageInfo(context,file.getAbsolutePath());
            if(packageInfo != null){//比对versionCode
                Log.d(Constants.TAG,String.format("ApkVersionCode:%d",packageInfo.versionCode));
                if(versionCode == packageInfo.versionCode){
                    ApplicationInfo applicationInfo = packageInfo.applicationInfo;
                    if(applicationInfo != null && packageName.equals(applicationInfo.packageName)){//比对packageName
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 判断文件是否存在
     * @param context
     * @param path
     * @return
     */
    public static boolean isAndroidQFileExists(Context context,String path){
        return isAndroidQFileExists(context,new File(path));
    }

    /**
     * 判断文件是否存在
     * @param context
     * @param file
     * @return
     */
    public static boolean isAndroidQFileExists(Context context,File file){
        AssetFileDescriptor descriptor = null;
        ContentResolver contentResolver = context.getContentResolver();
        try {
            Uri uri = Uri.fromFile(file);
            descriptor = contentResolver.openAssetFileDescriptor(uri, "r");
            if (descriptor == null) {
                return false;
            } else {
                close(descriptor);
            }
            return true;
        } catch (FileNotFoundException e) {

        }finally {
            close(descriptor);
        }
        return false;
    }

    /**
     * 校验文件MD5
     * @param file
     * @param md5
     * @return
     */
    public static boolean checkFileMD5(File file,String md5){
        String fileMD5 = getFileMD5(file);
        Log.d(Constants.TAG,"FileMD5:"+ fileMD5);
        if(!TextUtils.isEmpty(md5)){
            return md5.equalsIgnoreCase(fileMD5);
        }

        return false;
    }

    /**
     * 获取文件MD5
     * @param file
     * @return
     */
    public static String getFileMD5(File file){
        try {
            FileInputStream fis = new FileInputStream(file);
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) != -1){
                messageDigest.update(buffer,0,length);
            }
            BigInteger bigInteger = new BigInteger(1,messageDigest.digest());
            return bigInteger.toString(16);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 关闭
     * @param descriptor
     */
    private static void close(AssetFileDescriptor descriptor){
        if(descriptor != null){
            try {
                descriptor.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
