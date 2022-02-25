package cn.ovzv.idioms.navigation.main;

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
import cn.ovzv.idioms.navigation.main.fragment.fun_fragment;


public class Main_fun extends AppCompatActivity {
    private TextView mTextView;
    private ImageView mImageView;

    private TabLayout tableLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main_fun);

        initView();

        tableLayout = findViewById(R.id.order_tab);
        viewPager = findViewById(R.id.order_viewpager);
        String[] words = new String[]{"成语之最", "量词成语","动物类","5字成语","6字成语","7字成语","8字成语"};
        String[] word = new String[]{"most", "num_word","animal_word","5_word","6_word","7_word","8_word"};

        List<Fragment> fragments = new ArrayList<>();
        for(int i = 0;i <7;i++){
            fragments.add(fun_fragment.newInstance(word[i]));
        }
        FragmentPagerAdapter adapter = new FragmentOrderListAdapter(getSupportFragmentManager(),fragments,words );
        viewPager.setAdapter(adapter);
        tableLayout.setupWithViewPager(viewPager);
    }
    /**
     * 设置view
     * */
    public void initView(){
        mTextView = (TextView) findViewById(R.id.title);
        mTextView.setText("有趣的成语");


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