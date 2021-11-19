package cn.ovzv.idioms.navigation.me;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import cn.ovzv.idioms.R;
import cn.ovzv.idioms.adapter.FragmentOrderListAdapter;
import cn.ovzv.idioms.navigation.me.fragment.message_fragment1;
import cn.ovzv.idioms.navigation.me.fragment.message_fragment2;

public class Me_message extends AppCompatActivity {

    private TextView mTextView;
    private ImageView mImageView;
    private TabLayout tableLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_me_message);
        initView();

        tableLayout = findViewById(R.id.order_tab);
        viewPager = findViewById(R.id.order_viewpager);

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new message_fragment1());
        fragments.add(new message_fragment2());
        FragmentPagerAdapter adapter = new FragmentOrderListAdapter(getSupportFragmentManager(),fragments, new String[]{"私信", "活动"});
        viewPager.setAdapter(adapter);
        tableLayout.setupWithViewPager(viewPager);


    }
    /**
     * 设置view
     * */
    public void initView(){
        mTextView = (TextView) findViewById(R.id.title);
        mTextView.setText("消息中心");
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