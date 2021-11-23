package cn.ovzv.idioms.navigation.me;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.provider.MediaStore;
import android.widget.TextView;
import com.hb.dialog.myDialog.ActionSheetDialog;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import cn.leancloud.LCFile;
import cn.leancloud.LCObject;
import cn.leancloud.LCUser;
import cn.ovzv.idioms.R;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class Me_mine extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "";
    private TextView mTextView;
    private ImageView mImageView,Image;
    private Uri ImageUri;
    public static final int TAKE_PHOTO = 101;
    public static final int TAKE_CAMARA = 100;
    private static String path = "/sdcard/myHead/";// sd路径
    LCUser currentUser = LCUser.getCurrentUser();
    //需要的权限数组 读/写/相机
    private static String[] PERMISSIONS_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_me_mine);

        mTextView = (TextView) findViewById(R.id.title);
        mTextView.setText("我的资料");
        mImageView = (ImageView)findViewById(R.id.back);
        mImageView.setOnClickListener(this);
        Image = (ImageView) findViewById(R.id.image);
        Image.setOnClickListener(this);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        //跳转相机动态权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.image:
                choose();
                break;
            default:
                break;
        }

    }


    private void choose() {
        ActionSheetDialog dialog = new ActionSheetDialog(this).builder().setTitle("请选择")
                .addSheetItem("相册", null, new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        toPicture();

                    }
                }).addSheetItem("拍照", null, new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        //检查是否已经获得相机的权限
                        if (verifyPermissions((Activity) getApplicationContext(), PERMISSIONS_STORAGE[2]) == 0) {
                            Log.i(TAG, "提示是否要授权");
                            ActivityCompat.requestPermissions((Activity) getApplicationContext(), PERMISSIONS_STORAGE, 3);
                        } else {
                            //已经有权限
                            toCamera();  //打开相机
                        }
                    }
                });
        dialog.show();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == this.RESULT_OK) {
                    try {
                        //将拍摄的照片显示出来
                        //Bitmap bitmap = BitmapFactory.decodeStream(this.getContentResolver().openInputStream(ImageUri));
                        InputStream inputStream = this.getContentResolver().openInputStream(ImageUri);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        byte buff[] = new byte[1024];
                        int len = 0;
                        while ((len = inputStream.read(buff)) != -1) {
                            baos.write(buff, 0, len);
                        }
                        baos.flush();
                        LCFile file = new LCFile(currentUser.getUsername()+"-UserLogo.png", baos.toByteArray());
                        file.saveInBackground().subscribe(new Observer<LCFile>() {
                            public void onSubscribe(Disposable disposable) {}
                            public void onNext(LCFile file) {
                                System.out.println("文件保存完成。URL：" + file.getUrl());
                                LCObject todo = LCObject.createWithoutData("UserLogo", "582570f38ac247004f39c24b");
                                todo.put("LogoUrl", file.getUrl());
                                todo.saveInBackground().subscribe(new Observer<LCObject>() {
                                    public void onSubscribe(Disposable disposable) {}
                                    public void onNext(LCObject savedTodo) {
                                        System.out.println("保存成功");
                                    }
                                    public void onError(Throwable throwable) {
                                        System.out.println("保存失败！");
                                    }
                                    public void onComplete() {}
                                });;
                            }
                            public void onError(Throwable throwable) {
                                // 保存失败，可能是文件无法被读取，或者上传过程中出现问题
                            }
                            public void onComplete() {}
                        });
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                break;
            case TAKE_CAMARA:
                if (resultCode == this.RESULT_OK) {
                    try {
                        //将相册的照片显示出来
                        Uri uri_photo = data.getData();
                        //Bitmap bitmap = BitmapFactory.decodeStream(this.getContentResolver().openInputStream(uri_photo));
                        InputStream inputStream = this.getContentResolver().openInputStream(uri_photo);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        byte buff[] = new byte[1024];
                        int len = 0;
                        while ((len = inputStream.read(buff)) != -1) {
                            baos.write(buff, 0, len);
                        }
                        baos.flush();
                        LCFile file = new LCFile(currentUser.getUsername()+"-UserLogo.png", baos.toByteArray());
                        file.saveInBackground().subscribe(new Observer<LCFile>() {
                            public void onSubscribe(Disposable disposable) {}
                            public void onNext(LCFile file) {
                                System.out.println("文件保存完成。URL：" + file.getUrl());
                                LCObject todo = LCObject.createWithoutData("UserLogo", "582570f38ac247004f39c24b");
                                todo.put("LogoUrl", file.getUrl());
                                todo.saveInBackground().subscribe(new Observer<LCObject>() {
                                    public void onSubscribe(Disposable disposable) {}
                                    public void onNext(LCObject savedTodo) {
                                        System.out.println("保存成功");
                                    }
                                    public void onError(Throwable throwable) {
                                        System.out.println("保存失败！");
                                    }
                                    public void onComplete() {}
                                });;
                            }
                            public void onError(Throwable throwable) {
                                // 保存失败，可能是文件无法被读取，或者上传过程中出现问题
                            }
                            public void onComplete() {}
                        });
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            default:
                break;
        }
    }
    /**
     * 检查是否有对应权限
     *
     * @param activity   上下文
     * @param permission 要检查的权限
     * @return 结果标识
     */
    public int verifyPermissions(Activity activity, java.lang.String permission) {
        int Permission = ActivityCompat.checkSelfPermission(activity, permission);
        if (Permission == PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "已经同意权限");
            return 1;
        } else {
            Log.i(TAG, "没有同意权限");
            return 0;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "用户授权");
            toCamera();
        } else {
            Log.i(TAG, "用户未授权");
        }
    }

    //跳转相册
    private void toPicture() {
        Intent intent = new Intent(Intent.ACTION_PICK);  //跳转到 ACTION_IMAGE_CAPTURE
        intent.setType("image/*");
        startActivityForResult(intent, TAKE_CAMARA);
        Log.i(TAG, "跳转相册成功");
    }

    //跳转相机
    private void toCamera() {
        //创建File对象，用于存储拍照后的图片
        File outputImage = new File(this.getExternalCacheDir(), System.currentTimeMillis() + ".jpg");
        if (outputImage.exists()) {
            outputImage.delete();
        } else {
            try {
                outputImage.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //判断SDK版本高低，ImageUri方法不同
        if (Build.VERSION.SDK_INT >= 24) {
            ImageUri = FileProvider.getUriForFile(this, "cn.ovzv.idioms.fileprovider", outputImage);
        } else {
            ImageUri = Uri.fromFile(outputImage);
        }

        //启动相机程序
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, ImageUri);
        startActivityForResult(intent, TAKE_PHOTO);
    }
}