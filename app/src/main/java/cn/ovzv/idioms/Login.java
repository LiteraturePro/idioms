package cn.ovzv.idioms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cn.leancloud.LCUser;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class Login extends AppCompatActivity {
    private Button Button_register,Button_login;
    private EditText Email,Passwd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button_register = (Button) findViewById(R.id.register);
        Button_login =(Button) findViewById(R.id.login);
        Email = (EditText)findViewById(R.id.email);
        Passwd =(EditText)findViewById(R.id.passwd);



        Button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(Email.getText().toString().trim()) || TextUtils.isEmpty(Passwd.getText().toString().trim()) ) {
                    showToast("账号或密码不能为空");
                    return;
                }
                LCUser.loginByEmail(Email.getText().toString().trim(), Passwd.getText().toString().trim()).subscribe(new Observer<LCUser>() {
                    public void onSubscribe(Disposable disposable) {}
                    public void onNext(LCUser user) {
                        // 登录成功
                        System.out.println("登录成功");

                    }
                    public void onError(Throwable throwable) {
                        // 登录失败（可能是密码错误）
                    }
                    public void onComplete() {
                        System.out.println("登录成功");
                        LCUser currentUser = LCUser.getCurrentUser();
                        System.out.println(currentUser);
                        finish();
                    }
                });
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
    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}