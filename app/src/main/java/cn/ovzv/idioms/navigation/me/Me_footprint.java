package cn.ovzv.idioms.navigation.me;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cn.leancloud.LCUser;
import cn.ovzv.idioms.Login;
import cn.ovzv.idioms.R;
public class Me_footprint extends AppCompatActivity {

    private TextView mTextView;
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_me_footprint);

        initView();
        LCUser currentUser = LCUser.getCurrentUser();
        if (currentUser != null) {
            // 业务逻辑

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
        mTextView.setText("学习足迹");


        mImageView = (ImageView)findViewById(R.id.back);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });
    }
}