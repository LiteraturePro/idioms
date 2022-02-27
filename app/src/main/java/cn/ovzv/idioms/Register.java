package cn.ovzv.idioms;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.leancloud.LCCloud;
import cn.leancloud.LCUser;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class Register extends AppCompatActivity implements View.OnClickListener{

    private EditText Email,Passwd,UserName;
    private ImageButton Button_login;
    private TextView login_back,user_xieyi;
    public static Pattern p =
            Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        UserName = (EditText)findViewById(R.id.username);

        Email = (EditText)findViewById(R.id.login_user);
        Passwd =(EditText)findViewById(R.id.login_pass);

        login_back = (TextView) findViewById(R.id.login_back);
        user_xieyi = (TextView) findViewById(R.id.user_xieyi);
        user_xieyi.setOnClickListener(this);

        Button_login =(ImageButton) findViewById(R.id.imageButton);



        Button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(Email.getText().toString().trim()) || TextUtils.isEmpty(UserName.getText().toString().trim())  || TextUtils.isEmpty(Passwd.getText().toString().trim()) ) {
                    showToast("账号或密码不能为空");
                    return;
                }
                if(isEmail(Email.getText().toString().trim())){
                    Map<String, Object> dicParameters = new HashMap<String, Object>();
                    dicParameters.put("name", UserName.getText().toString().trim());
                    dicParameters.put("passwd", Passwd.getText().toString().trim());
                    dicParameters.put("email", Email.getText().toString().trim());

                    // 调用指定名称的云函数 averageStars，并且传递参数（默认不使用缓存）
                    LCCloud.callFunctionInBackground("Sign_up", dicParameters).subscribe(new Observer<Object>() {
                        @Override
                        public void onSubscribe(Disposable disposable) {

                        }

                        @Override
                        public void onNext(Object object) {

                            // succeed.

                        }

                        @Override
                        public void onError(Throwable throwable) {
                            // failed.
                        }

                        @Override
                        public void onComplete() {
                            LCUser.loginByEmail(Email.getText().toString().trim(), Passwd.getText().toString().trim()).subscribe(new Observer<LCUser>() {
                                public void onSubscribe(Disposable disposable) {}
                                public void onNext(LCUser user) {
                                    // 登录成功
                                    System.out.println("登录成功");

                                }
                                public void onError(Throwable throwable) {
                                    // 登录失败（可能是密码错误）
                                    showToast("登录失败，请检查账号密码！");
                                }
                                public void onComplete() {
                                    /**跳转到主页面*/
                                    Intent intent = new Intent(Register.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        }
                    });

                }else{
                    showToast("邮箱格式不正确！");
                }



            }
        });

        login_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);
                finish();
            }
        });


    }
    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_xieyi:
                String str = initAssets("yszc.txt");
                final View inflate = LayoutInflater.from(this).inflate(R.layout.dialog_xieyi_yinsi_style, null);
                TextView tv_title = (TextView) inflate.findViewById(R.id.tv_title);
                tv_title.setText("隐私政策");
                TextView tv_content = (TextView) inflate.findViewById(R.id.tv_content);
                tv_content.setText(str);
                final Dialog dialog = new AlertDialog
                        .Builder(this)
                        .setView(inflate)
                        .show();
                final WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
                params.width = 800;
                params.height = 1200;
                dialog.getWindow().setAttributes(params);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            default:
                break;
        }

    }
    //验证函数优化版
    public static boolean isEmail(String email){
        if (null==email || "".equals(email)) return false;
        Matcher m = p.matcher(email);
        return m.matches();
    }
    // 读取文件
    public String initAssets(String fileName) {
        String str = null;
        try {
            InputStream inputStream = getAssets().open(fileName);

            str = getString(inputStream);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return str;
    }
    public static String getString(InputStream inputStream) {
        InputStreamReader inputStreamReader = null;
        try {
            inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        BufferedReader reader = new BufferedReader(inputStreamReader);
        StringBuffer sb = new StringBuffer("");
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


}