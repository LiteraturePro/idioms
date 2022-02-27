package cn.ovzv.idioms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.leancloud.LCUser;
import cn.leancloud.types.LCNull;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class Login extends AppCompatActivity {
    private ImageButton Button_login;
    private EditText Email,Passwd;
    private TextView Button_register,login_forget;
    //由于Android边编译边生成的原理，将匹配字符串放入全局，作为静态变量可以提高效率
    public static Pattern p =
            Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button_register = (TextView) findViewById(R.id.login_register);
        Button_login =(ImageButton) findViewById(R.id.imageButton);
        Email = (EditText)findViewById(R.id.login_user);
        Passwd =(EditText)findViewById(R.id.login_pass);
        login_forget = (TextView) findViewById(R.id.login_forget);

        login_forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(Email.getText().toString().trim())  ) {
                    showToast("邮箱不能为空");
                    return;
                }
                if(isEmail(Email.getText().toString().trim())){
                    LCUser.requestPasswordResetInBackground(Email.getText().toString().trim()).subscribe(new Observer<LCNull>() {
                        public void onSubscribe(Disposable disposable) {}

                        @Override
                        public void onNext(@NonNull LCNull lcNull) {
                            // 调用成功
                            showToast("操作成功，请查看邮箱中的密码重置邮件！");
                        }
                        public void onError(Throwable throwable) {
                            // 调用出错
                        }
                        public void onComplete() {


                        }
                    });

                }else{
                    showToast("邮箱格式不正确！");
                }


            }
        });



        Button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(Email.getText().toString().trim()) || TextUtils.isEmpty(Passwd.getText().toString().trim()) ) {
                    showToast("账号或密码不能为空");
                    return;
                }
                if(isEmail(Email.getText().toString().trim())){
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
                            System.out.println("登录成功");
                            LCUser currentUser = LCUser.getCurrentUser();
                            System.out.println(currentUser);
                            /**跳转到主页面*/
                            Intent intent = new Intent(Login.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                }else{
                    showToast("邮箱格式不正确！");
                }
            }
        });
        Button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Register.class);
                startActivity(intent);
            }
        });


    }
    //验证函数优化版
    public static boolean isEmail(String email){
        if (null==email || "".equals(email)) return false;
        Matcher m = p.matcher(email);
        return m.matches();
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}