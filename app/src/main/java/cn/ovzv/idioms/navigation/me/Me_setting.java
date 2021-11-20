package cn.ovzv.idioms.navigation.me;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codbking.view.ItemView;

import cn.leancloud.LCUser;
import cn.leancloud.types.LCNull;
import cn.ovzv.idioms.Login;
import cn.ovzv.idioms.R;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class Me_setting extends AppCompatActivity {

    private TextView mTextView;
    private ImageView mImageView;
    private ItemView itemView1,itemView2,itemView3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_me_setting);
        initView();
        itemView1 = (ItemView) findViewById(R.id.zhanghao);
        itemView2 = (ItemView) findViewById(R.id.requestPassword);
        itemView3 = (ItemView) findViewById(R.id.logout);
        LCUser currentUser = LCUser.getCurrentUser();
        if (currentUser != null) {
            itemView1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                }
            });
            itemView2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LCUser.requestPasswordResetInBackground(currentUser.getEmail()).subscribe(new Observer<LCNull>() {
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
                            finish();
                        }
                    });
                }
            });
            itemView3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 用户登出
                    LCUser.logOut();
                }
            });
        } else {
            // 显示注册或登录页面
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
        }

    }
    /**
     * 设置view
     * */
    public void initView(){
        mTextView = (TextView) findViewById(R.id.title);
        mTextView.setText("系统设置");
        mImageView = (ImageView)findViewById(R.id.back);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });
    }
    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}