package cn.ovzv.idioms.navigation.me;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import cn.leancloud.LCUser;
import cn.ovzv.idioms.Login;
import cn.ovzv.idioms.R;
import cn.ovzv.idioms.adapter.FragmentOrderListAdapter;
import cn.ovzv.idioms.navigation.me.fragment.message_fragment1;
import cn.ovzv.idioms.navigation.me.fragment.message_fragment2;
import cn.ovzv.idioms.navigation.me.fragment.subscribe_fragment1;
import cn.ovzv.idioms.navigation.me.fragment.subscribe_fragment2;
import cn.ovzv.idioms.navigation.me.fragment.subscribe_fragment3;

public class Me_subscribe extends AppCompatActivity {

    private TextView mTextView;
    private ImageView mImageView;
    private TabLayout tableLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_me_subscribe);

        initView();
        tableLayout = findViewById(R.id.order_tab);
        viewPager = findViewById(R.id.order_viewpager);
        LCUser currentUser = LCUser.getCurrentUser();
        if (currentUser != null) {
            List<Fragment> fragments = new ArrayList<>();
            fragments.add(new subscribe_fragment1());
            fragments.add(new subscribe_fragment2());
            fragments.add(new subscribe_fragment3());
            FragmentPagerAdapter adapter = new FragmentOrderListAdapter(getSupportFragmentManager(),fragments, new String[]{"全部", "待付款","已付款"});
            viewPager.setAdapter(adapter);
            tableLayout.setupWithViewPager(viewPager);
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
        mTextView.setText("我的订阅");


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