package cn.ovzv.idioms;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.dogecloud.support.DogeManager;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.next.easynavigation.view.EasyNavigationBar;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.commonsdk.utils.UMUtils;
import com.umeng.message.PushAgent;
import com.umeng.message.api.UPushRegisterCallback;
import java.util.ArrayList;
import java.util.List;
import cn.leancloud.LCLogger;
import cn.leancloud.LeanCloud;
import cn.ovzv.idioms.api.LeancloudApi;
import cn.ovzv.idioms.help.PushHelper;
import cn.ovzv.idioms.navigation.Course;
import cn.ovzv.idioms.navigation.Main;
import cn.ovzv.idioms.navigation.Me;
import cn.ovzv.idioms.navigation.Study;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EasyNavigationBar navigationBar;
    private List<Fragment> fragmentList = new ArrayList<>();
    private Handler handler;
    private LeancloudApi leancloudApi;

    private String[] tabText = {"首页","课程","我的"};
    private int[] normalIcon = {R.drawable.fragment_main_1,R.drawable.fragment_course_1,R.drawable.fragment_me_1};
    private int[] selectIcon = {R.drawable.fragment_main_2,R.drawable.fragment_course_2,R.drawable.fragment_me_2};


    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sp = getSharedPreferences("one",MODE_PRIVATE);


        Boolean aBoolean = sp.getBoolean("one", true);

        if (aBoolean == false){
            writeSp();
        }


        DogeManager.DogeInit(this);    //整个 app 只需要运行一次，最好直接放在 Application 中


        //
        // 将“12345678”替换成您申请的APPID，申请地址：http://www.xfyun.cn
        // 请勿在“=”与appid之间添加任何空字符或者转义符
        SpeechUtility.createUtility(getApplicationContext(), SpeechConstant.APPID +"=8b071f83");

        // 调试模式，在 LeanCloud.initialize() 之前调用
        LeanCloud.setLogLevel(LCLogger.Level.DEBUG);
        // 在 AVOSCloud.initialize 之前调用
        // 可选的节点有：
        //    REGION.NorthChina - 华北节点，默认节点
        //    REGION.EastChina  - 华东节点
        //    REGION.NorthAmerica - 北美节点
//        LeanCloud.setRegion(LeanCloud.REGION.EastChina);
//
//        // 初始化 Leancloud SDK
//        LeanCloud.initialize(this, leancloudApi.APP_ID,leancloudApi.APP_KEY, leancloudApi.REST_API);

        // 初始化SDK appkey在官方注册应用即可获取
        UMConfigure.init(this, "6191b9f9e014255fcb786568", "Umeng", UMConfigure.DEVICE_TYPE_PHONE, null);
        // 选用AUTO页面采集模式，如果是在AUTO页面采集模式下，则需要注意，所有Activity中都不能调用MobclickAgent.onResume和onPause方法
        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO);

        initUmengSDK();

        if (hasAgreedAgreement()) {
            PushAgent.getInstance(this).onAppStart();
            String deviceToken = PushAgent.getInstance(this).getRegistrationId();
//            TextView token = findViewById(R.id.tv_device_token);
//            token.setText(deviceToken);
            Log.d("deviceToken=", deviceToken);
        } else {
            showAgreementDialog();
        }



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

    private boolean hasAgreedAgreement() {
        return PushPreferences.getInstance(this).hasAgreePrivacyAgreement();
    }
    private void showAgreementDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("邮箱验证说明");
        builder.setMessage("注册成功后,您将收到一封邮箱验证的邮件,请您尽快完成验证,避免影响使用.");
        builder.setPositiveButton(R.string.agreement_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //用户点击隐私协议同意按钮后，初始化PushSDK
                PushPreferences.getInstance(getApplicationContext()).setAgreePrivacyAgreement(true);
                PushHelper.init(getApplicationContext());
                PushAgent.getInstance(getApplicationContext()).register(new UPushRegisterCallback() {
                    @Override
                    public void onSuccess(final String deviceToken) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                TextView token = findViewById(R.id.tv_device_token);
//                                token.setText(deviceToken);
                                Log.d("deviceToken=", deviceToken);
                            }
                        });
                    }

                    @Override
                    public void onFailure(String code, String msg) {
                        Log.d("MainActivity", "code:" + code + " msg:" + msg);
                    }
                });
            }
        });
        builder.setNegativeButton(R.string.agreement_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        builder.create().show();
    }

    /**
     * 初始化友盟SDK
     */
    private void initUmengSDK() {
        //日志开关
        UMConfigure.setLogEnabled(true);
        //预初始化
        PushHelper.preInit(this);
        //是否同意隐私政策
        boolean agreed = PushPreferences.getInstance(this).hasAgreePrivacyAgreement();
        if (!agreed) {
            return;
        }
        boolean isMainProcess = UMUtils.isMainProgress(this);
        if (isMainProcess) {
            //启动优化：建议在子线程中执行初始化
            new Thread(new Runnable() {
                @Override
                public void run() {
                    PushHelper.init(getApplicationContext());
                }
            }).start();
        } else {
            //若不是主进程（":channel"结尾的进程），直接初始化sdk，不可在子线程中执行
            PushHelper.init(getApplicationContext());
        }
    }

    public void writeSp() {
        //获取Sp对象
        //参数一 文件名   参数二  模式（固定写法）
        SharedPreferences sp = getSharedPreferences("words",MODE_PRIVATE);
        //编辑者
        SharedPreferences.Editor edit = sp.edit();
        //写入数据
        edit.putInt("new_words",20);
        edit.putBoolean("study_1",false);
        edit.putBoolean("study_2",false);
        edit.putBoolean("auto_1",false);
        edit.putBoolean("auto_2",false);
        edit.putBoolean("auto_3",true);
        //提交
        edit.commit();

    }

    @Override
    protected void onResume() {

        super.onResume();
    }

    @Override
    public void onClick(View v) {

    }
}