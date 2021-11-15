package cn.ovzv.idioms;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import com.next.easynavigation.view.EasyNavigationBar;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;

import java.util.ArrayList;
import java.util.List;

import cn.ovzv.idioms.navigation.Course;
import cn.ovzv.idioms.navigation.Main;
import cn.ovzv.idioms.navigation.Me;
import cn.ovzv.idioms.navigation.Study;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EasyNavigationBar navigationBar;
    private List<Fragment> fragmentList = new ArrayList<>();
    private Handler handler;

    private String[] tabText = {"首页","课程","学习","我的"};
    private int[] normalIcon = {R.drawable.fragment_main_1,R.drawable.fragment_course_1,R.drawable.fragment_study_1,R.drawable.fragment_me_1};
    private int[] selectIcon = {R.drawable.fragment_main_2,R.drawable.fragment_course_2,R.drawable.fragment_study_2,R.drawable.fragment_me_2};


    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化SDK appkey在官方注册应用即可获取
        UMConfigure.init(this, "6191b9f9e014255fcb786568", "Umeng", UMConfigure.DEVICE_TYPE_PHONE, null);
        // 选用AUTO页面采集模式，如果是在AUTO页面采集模式下，则需要注意，所有Activity中都不能调用MobclickAgent.onResume和onPause方法
        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO);




        navigationBar = (EasyNavigationBar) findViewById(R.id.easy_bars);
        //创建handler
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 0x11) {

                    // 导航栏设置
                    navigationBar.defaultSetting()  //恢复默认配置、可用于重绘导航栏
                            .titleItems(tabText)      //  Tab文字集合  只传文字则只显示文字
                            .normalIconItems(normalIcon)   //  Tab未选中图标集合
                            .selectIconItems(selectIcon)   //  Tab选中图标集合
                            .fragmentList(fragmentList)       //  fragment集合
                            .fragmentManager(getSupportFragmentManager())
                            .iconSize(25)     //Tab图标大小
                            .tabTextSize(10)   //Tab文字大小
                            .tabTextTop(2)     //Tab文字距Tab图标的距离
                            .normalTextColor(Color.parseColor("#e0e0e0"))   //Tab未选中时字体颜色
                            .selectTextColor(Color.parseColor("#000000"))   //Tab选中时字体颜色
                            .scaleType(ImageView.ScaleType.CENTER_INSIDE)  //同 ImageView的ScaleType
                            .navigationBackground(Color.parseColor("#F5F5F5"))   //导航栏背景色
                            .setOnTabClickListener(new EasyNavigationBar.OnTabClickListener() {
                                @Override
                                public boolean onTabSelectEvent(View view, int position) {
                                    return false;
                                }

                                @Override
                                public boolean onTabReSelectEvent(View view, int position) {
                                    return false;
                                }
                            })
                            .smoothScroll(false)  //点击Tab  Viewpager切换是否有动画
                            .canScroll(false)    //Viewpager能否左右滑动
                            .mode(EasyNavigationBar.NavigationMode.MODE_NORMAL)   //默认MODE_NORMAL 普通模式  //MODE_ADD 带加号模式
                            .centerLayoutHeight(100)   //包含加号的布局高度 背景透明  所以加号看起来突出一块
                            .navigationHeight(50)  //导航栏高度
                            .lineHeight(10)         //分割线高度  默认1px
                            .lineColor(Color.parseColor("#F5F5F5"))
                            .centerLayoutRule(EasyNavigationBar.RULE_BOTTOM) //RULE_CENTER 加号居中addLayoutHeight调节位置 EasyNavigationBar.RULE_BOTTOM 加号在导航栏靠下
                            .build();
                }
            }
        };

        new Thread(new Runnable() {
            @Override
            public void run() {
                //FIXME 这里直接更新ui是不行的
                Message message = Message.obtain();
                //还有其他更新ui方式,runOnUiThread()等
                try {
                    fragmentList.add(new Main());
                    fragmentList.add(new Course());
                    fragmentList.add(new Study());
                    fragmentList.add(new Me());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                message.obj = fragmentList;
                message.what = 0x11;
                handler.sendMessage(message);
            }
        }).start();
    }

    @Override
    protected void onResume() {

        super.onResume();
    }

    @Override
    public void onClick(View v) {

    }
}