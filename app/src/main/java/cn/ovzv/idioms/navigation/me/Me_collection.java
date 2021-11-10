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
import cn.ovzv.idioms.navigation.me.fragment.collection_fragment1;
import cn.ovzv.idioms.navigation.me.fragment.collection_fragment2;
import cn.ovzv.idioms.navigation.me.fragment.collection_fragment3;
import cn.ovzv.idioms.navigation.me.fragment.message_fragment1;
import cn.ovzv.idioms.navigation.me.fragment.message_fragment2;

public class Me_collection extends AppCompatActivity {

    private TextView mTextView;
    private ImageView mImageView;
    private TabLayout tableLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_me_collection);


        initView();

        tableLayout = findViewById(R.id.order_tab);
        viewPager = findViewById(R.id.order_viewpager);

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new collection_fragment1());
        fragments.add(new collection_fragment2());
        fragments.add(new collection_fragment3());
        FragmentPagerAdapter adapter = new FragmentOrderListAdapter(getSupportFragmentManager(),fragments, new String[]{"故事", "成语","文章"});
        viewPager.setAdapter(adapter);
        tableLayout.setupWithViewPager(viewPager);
    }
    /**
     * 设置view
     * */
    public void initView(){
        mTextView = (TextView) findViewById(R.id.title);
        mTextView.setText("我的收藏");


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