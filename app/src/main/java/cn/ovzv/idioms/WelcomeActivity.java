package cn.ovzv.idioms;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;

import cn.leancloud.LCLogger;
import cn.leancloud.LCUser;
import cn.leancloud.LeanCloud;
import cn.ovzv.idioms.api.LeancloudApi;

public class WelcomeActivity extends Activity {
    private LeancloudApi leancloudApi;
    @Override
    /*** 创建欢迎页面
     * @version 1.0
     */
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        // 使用PostDelayed方法，两秒后调用此Runnable对象
        // handler.postDelayed(runnable, 2000);
        // 实际上也就实现了一个2s的一个定时器

        // 调试模式，在 LeanCloud.initialize() 之前调用
        LeanCloud.setLogLevel(LCLogger.Level.DEBUG);
        // 在 AVOSCloud.initialize 之前调用
        // 可选的节点有：
        //    REGION.NorthChina - 华北节点，默认节点
        //    REGION.EastChina  - 华东节点
        //    REGION.NorthAmerica - 北美节点
        LeanCloud.setRegion(LeanCloud.REGION.EastChina);

        // 初始化 Leancloud SDK
        LeanCloud.initialize(this, leancloudApi.APP_ID,leancloudApi.APP_KEY, leancloudApi.REST_API);



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /**跳转到主页面*/

                LCUser currentUser = LCUser.getCurrentUser();
                if (currentUser != null) {
                    Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Intent intent = new Intent(WelcomeActivity.this, Login.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 0);
    }
}
