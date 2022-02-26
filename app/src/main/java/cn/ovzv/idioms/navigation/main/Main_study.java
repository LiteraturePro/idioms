package cn.ovzv.idioms.navigation.main;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.util.HashMap;
import java.util.Map;
import cn.leancloud.LCCloud;
import cn.ovzv.idioms.R;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class Main_study extends AppCompatActivity implements View.OnTouchListener {

    private ImageView mImageView;
    private LinearLayout mLinearLayout;//对应于主布局中用来添加子布局的View
    private View mGridView;// 子Layout要以view的形式加入到主Layout
    private TextView new_word,fuxi_word;
    private int seconds = 0;
    private int tag = 0;
    private String time;
    private boolean running = false; //计时状态
    private boolean wasRunning = false; //保存running的状态
    private ViewFlipper viewFlipper;
    private float startX; //手指按下时的x坐标
    private float endX; //手指抬起时的x坐标
    private float moveX = 100f; //判断是否切换页面的标准值
    private GestureDetector gestureDetector; //创建手势监听器
    private SharedPreferences sp ;
    private JSONArray DataJSONArray;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main_study);
        new_word =(TextView)findViewById(R.id.new_words);
        fuxi_word = (TextView) findViewById(R.id.fuxi_word);

        //参数一 文件名   参数二  模式（固定写法）
        sp = getSharedPreferences("words", Context.MODE_PRIVATE);

        //点击按钮吐司一下内容
        new_word.setText(String.valueOf(sp.getInt("new_words", 20)));
        fuxi_word.setText(String.valueOf(sp.getInt("new_words", 20)));

        //获取保存的状态
        if(savedInstanceState!=null){
            seconds = savedInstanceState.getInt("seconds");
            running = savedInstanceState.getBoolean("running");
            wasRunning = savedInstanceState.getBoolean("wasRunning");
        }

        running = true;
        runTime();
        initView();


        viewFlipper = (ViewFlipper) findViewById(R.id.viewFlipper);
        viewFlipper.addView(createViewWithXml(), viewFlipper.getChildCount());
        viewFlipper.setOnTouchListener(this);
        gestureDetector = new GestureDetector(this, new MyGestureListener());




    }

    private View createViewWithXml() {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        lp.setMarginEnd(40);
        lp.setMarginStart(40);
        View view = LayoutInflater.from(this).inflate(R.layout.fragment_main_study_item, null);//也可以从XML中加载布局

        view.setLayoutParams(lp);//设置布局参数


        mLinearLayout = (LinearLayout) view.findViewById(R.id.study_words);
        // 楷体
        AssetManager mgr = getAssets();
        Typeface tf = Typeface.createFromAsset(mgr, "fonts/kaiti_GB2312.ttf");

        // 构建传递给服务端的参数字典
        Map<String, Object> dicParameters = new HashMap<>();
        dicParameters.put("tag", 2);
        dicParameters.put("count", sp.getInt("new_words", 20));
        dicParameters.put("UserID", "61936fa79ba582465b45d312");

        // 调用指定名称的云函数 averageStars，并且传递参数（默认不使用缓存）
        LCCloud.callFunctionInBackground("DB_Get_word", dicParameters).subscribe(new Observer<Object>() {
            @Override
            public void onSubscribe(Disposable disposable) {

            }

            @Override
            public void onNext(Object object) {
                // succeed.
                JSONObject json = (JSONObject) JSONObject.toJSON(object);
                DataJSONArray = json.getJSONArray("data");
            }

            @Override
            public void onError(Throwable throwable) {
                // failed.
            }

            @Override
            public void onComplete() {
                char left_str[] = DataJSONArray.getJSONObject(tag).getString("word").toCharArray();//利用toCharArray方法转换
                for (int j = 0; j < left_str.length; j++) {
                    System.out.println(left_str[j]);
                    mGridView = View.inflate(getApplicationContext(), R.layout.fragment_main_study_words, null);
                    TextView txt = mGridView.findViewById(R.id.words_text);
                    txt.setText(String.valueOf(left_str[j]));
                    txt.setTextSize(40);
                    txt.setTypeface(tf);
                    mLinearLayout.addView(mGridView, 140, 140);
                }

            }
        });

        return view;
    }


    /**
     * 自定义手势监听类
     */
    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (e2.getX() - e1.getX() > moveX) {
                if (viewFlipper.getChildCount() > 1) {
                    viewFlipper.removeViewAt(0);
                }
                viewFlipper.setInAnimation(Main_study.this, R.anim.left_in);
                viewFlipper.setOutAnimation(Main_study.this, R.anim.right_out);
                viewFlipper.showPrevious();
                if(tag > 0){
                    tag = tag - 1;
                }

            } else if (e2.getX() - e1.getX() < moveX) {
                if (viewFlipper.getChildCount() > 1) {
                    viewFlipper.removeViewAt(0);
                }

                viewFlipper.addView(createViewWithXml(), viewFlipper.getChildCount());
                viewFlipper.setInAnimation(Main_study.this, R.anim.right_in);
                viewFlipper.setOutAnimation(Main_study.this, R.anim.left_out);
                viewFlipper.showNext();
                if(tag < sp.getInt("new_words", 20)*2-1){
                    tag = tag + 1;
                }

            }
            return true;
        }
    }


    /**
     * 设置view
     * */
    public void initView(){

        mImageView = (ImageView)findViewById(R.id.back);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });
    }

    /**
     * 触摸监听事件
     *
     * @param v
     * @param event
     * @return
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        gestureDetector.onTouchEvent(event);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //手指按下时获取起始点坐标
                startX = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }

    /**
     *保存状态
     */
    @Override
    public void onSaveInstanceState(Bundle saveInstanceState) {

        super.onSaveInstanceState(saveInstanceState);
        saveInstanceState.putInt("seconds",seconds);
        saveInstanceState.putBoolean("running",running);
        saveInstanceState.putBoolean("wasRunning",wasRunning);
    }
    /**
     * 响应button的onClick事件
     * 方法名和onClick的值一致
     */
    public void onClickStart(View button){
        running = true;
    }
    public void onClickStop(View button){
        running = false;
    }
    public void onClickReset(View button){
        running = false;
        seconds = 0;
    }

    /**
     * 注意 ui线程不能被堵塞，因此不能在ui线程中调用sleep方法
     * 只允许ui线程更新界面，不能在后台线程更新界面
     *
     * ** 使用ui线程的Handler定时更新 **
     * 将任务封装到 Runnable的run方法中 ，通过Handler的
     * post(立即提交任务)或postDelayed(实现定时调度)方法提交到ui线程
     */
    private void runTime() {
        final Handler handler = new Handler();
        handler.post(new Runnable() {
                         @Override
                         public void run() {
                             final TextView textView = findViewById(R.id.timeView);
                             int hour = seconds / 3600 % 24;
                             int minute = seconds % 3600 / 60;
                             String time = String.format("%02d:%02d:%02d", hour, minute, seconds % 60);
                             textView.setText(time);
                             if (running) seconds++;
                             handler.postDelayed(this, 1000);
                         }
                     }
        );
    }
    @Override
    protected void onStop() {
        super.onStop();
        wasRunning = running;
        running = false;
    }

    //重新进入app，开始计时
    @Override
    protected void onStart() {
        super.onStart();
        if(wasRunning) running = true;
    }

    //失去焦点(如分屏)，暂停计时
    @Override
    protected void onPause() {
        super.onPause();
        wasRunning = running;
        running = false;
    }

    //获得焦点,重新开始计时
    @Override
    protected void onResume() {
        super.onResume();
        if(wasRunning) running = true;
    }
}