package cn.ovzv.idioms.navigation.main;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hb.dialog.dialog.ConfirmDialog;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechEvent;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;
import cn.leancloud.LCCloud;
import cn.leancloud.LCUser;
import cn.ovzv.idioms.R;
import cn.ovzv.idioms.help.TtsSettings;
import cn.ovzv.idioms.tts;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class Main_study extends AppCompatActivity implements View.OnTouchListener {

    private ImageView mImageView,yuyin;
    private static String TAG = tts.class.getSimpleName();
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
    private Typeface tf;
    private String daan;
    private Boolean aBoolean;
    private ConfirmDialog confirmDialog;
    // 语音合成对象
    private SpeechSynthesizer mTts;
    // 默认发音人
    private String voicer = "aisxping";
    private String[] mCloudVoicersEntries;
    private String[] mCloudVoicersValue;
    private String texts = "";
    // 缓冲进度
    private int mPercentForBuffering = 0;
    // 播放进度
    private int mPercentForPlaying = 0;

    // 云端/本地单选按钮
    private RadioGroup mRadioGroup;
    // 引擎类型
    private String mEngineType = SpeechConstant.TYPE_CLOUD;

    private Toast mToast;
    private SharedPreferences mSharedPreferences;
    private File pcmFile;

    private TextView pinyin,shiyi,chuchu,liju,shiyi1,chuchu1,liju1;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main_study);
        new_word =(TextView)findViewById(R.id.new_words);
        fuxi_word = (TextView) findViewById(R.id.fuxi_word);

        // 初始化弹窗对象

        confirmDialog = new ConfirmDialog(this);

        //参数一 文件名   参数二  模式（固定写法）
        sp = getSharedPreferences("words", Context.MODE_PRIVATE);

        //点击按钮吐司一下内容
        new_word.setText(String.valueOf(sp.getInt("new_words", 20)) +" 个");
        fuxi_word.setText(String.valueOf(sp.getInt("new_words", 20)) +" 个");

        //获取保存的状态
        if(savedInstanceState!=null){
            seconds = savedInstanceState.getInt("seconds");
            running = savedInstanceState.getBoolean("running");
            wasRunning = savedInstanceState.getBoolean("wasRunning");
        }

        LCUser currentUser = LCUser.getCurrentUser();

        running = true;
        runTime();
        initView();

        // 构建传递给服务端的参数字典
        Map<String, Object> dicParameters = new HashMap<>();
        dicParameters.put("UserID", currentUser.getObjectId());
        dicParameters.put("tag", 0);

        // 调用指定名称的云函数 averageStars，并且传递参数（默认不使用缓存）
        LCCloud.callFunctionInBackground("Study_Status_Get", dicParameters).subscribe(new Observer<Object>() {
            @Override
            public void onSubscribe(Disposable disposable) {

            }

            @Override
            public void onNext(Object object) {
                // succeed.
                JSONObject json = (JSONObject) JSONObject.toJSON(object);
                aBoolean = json.getBoolean("status");

                if(aBoolean){

                    confirmDialog.setLogoImg(R.drawable.true_1).setMsg("今日任务已经完成，明日再来吧！");
                    confirmDialog.setClickListener(new ConfirmDialog.OnBtnClickListener() {
                        @Override
                        public void ok() {
                            finish();
                        }

                        @Override
                        public void cancel() {
                            finish();
                        }
                    });
                    confirmDialog.show();
                }



            }

            @Override
            public void onError(Throwable throwable) {
                // failed.
            }

            @Override
            public void onComplete() {
            }

        });

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
        tf = Typeface.createFromAsset(mgr, "fonts/kaiti_GB2312.ttf");

        // 构建传递给服务端的参数字典
        LCUser currentUser = LCUser.getCurrentUser();
        Map<String, Object> dicParameters = new HashMap<>();
        dicParameters.put("tag", 2);
        dicParameters.put("count", sp.getInt("new_words", 20));
        dicParameters.put("UserID", currentUser.getObjectId());

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
                // 设置成语
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
                // 设置拼音
                pinyin = view.findViewById(R.id.pinyin);
                pinyin.setText("【********】");
                pinyin.setTypeface(tf, Typeface.BOLD);
                daan = DataJSONArray.getJSONObject(tag).getString("pinyin");

                pinyin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pinyin.setText("【"+daan+"】");
                        pinyin.setTypeface(tf, Typeface.BOLD);

                    }
                });

                // 设置释义
                shiyi = view.findViewById(R.id.shiyi);
                shiyi.setText("\t\t"+DataJSONArray.getJSONObject(tag).getString("explanation"));
                shiyi.setTypeface(tf, Typeface.BOLD);

                shiyi1 = view.findViewById(R.id.shiyi1);
                shiyi1.setTypeface(tf);

                // 设置出处
                chuchu = view.findViewById(R.id.chuchu);
                chuchu.setText("\t\t"+DataJSONArray.getJSONObject(tag).getString("derivation"));
                chuchu.setTypeface(tf, Typeface.BOLD);

                chuchu1 = view.findViewById(R.id.chuchu1);
                chuchu1.setTypeface(tf);

                // 设置列举
                liju = view.findViewById(R.id.liju);
                liju.setText("\t\t"+DataJSONArray.getJSONObject(tag).getString("example"));
                liju.setTypeface(tf, Typeface.BOLD);

                liju1 = view.findViewById(R.id.liju1);
                liju1.setTypeface(tf);

                // 初始化合成对象
                mTts = SpeechSynthesizer.createSynthesizer(getApplicationContext(), mTtsInitListener);
                mSharedPreferences = getSharedPreferences(TtsSettings.PREFER_NAME, MODE_PRIVATE);
                pcmFile = new File(getExternalCacheDir().getAbsolutePath(), "tts_pcmFile.pcm");
                pcmFile.delete();
                // 设置参数
                setParam();
                // 合成并播放

                // 成语自动播放检测
                if(!aBoolean) {


                    if (sp.getBoolean("auto_1", false)) {
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mTts.startSpeaking(DataJSONArray.getJSONObject(tag).getString("word"), mTtsListener);
                                // 例句自动播放检测
                                if (sp.getBoolean("auto_2", false)) {
                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            mTts.startSpeaking(DataJSONArray.getJSONObject(tag).getString("example"), mTtsListener);

                                        }
                                    }, 2000);//1秒后执行Runnable中的run方法
                                }

                            }
                        }, 1000);//1秒后执行Runnable中的run方法

                    } else {
                        // 例句自动播放检测
                        if (sp.getBoolean("auto_2", false)) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mTts.startSpeaking(DataJSONArray.getJSONObject(tag).getString("example"), mTtsListener);

                                }
                            }, 2000);//1秒后执行Runnable中的run方法
                        }

                    }
                }

                // 点击播放成语
                yuyin = view.findViewById(R.id.yuyin);
                yuyin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mTts.startSpeaking(DataJSONArray.getJSONObject(tag).getString("word") , mTtsListener);
                    }
                });
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
                if(tag > 0){
                    if (viewFlipper.getChildCount() > 1) {
                        viewFlipper.removeViewAt(0);
                    }
                    mTts.stopSpeaking();
                    viewFlipper.addView(createViewWithXml(), viewFlipper.getChildCount());
                    viewFlipper.setInAnimation(Main_study.this, R.anim.left_in);
                    viewFlipper.setOutAnimation(Main_study.this, R.anim.right_out);
                    viewFlipper.showPrevious();
                    tag = tag - 1;
                }
                else{
                        confirmDialog.setLogoImg(R.drawable.false_1).setMsg("当前为第一个成语");
                        confirmDialog.setClickListener(new ConfirmDialog.OnBtnClickListener() {
                            @Override
                            public void ok() {

                            }
                            @Override
                            public void cancel() {

                            }
                        });
                        confirmDialog.show();
                        //回答正确，跳转下一题
                }

            } else if (e2.getX() - e1.getX() < moveX) {

                if(tag < sp.getInt("new_words", 20)*2-1){
                    tag = tag + 1;
                    mTts.stopSpeaking();
                    if (viewFlipper.getChildCount() > 1) {
                        viewFlipper.removeViewAt(0);
                    }
                    if(tag <= sp.getInt("new_words", 20)){
                        fuxi_word.setText(String.valueOf(sp.getInt("new_words", 20) - tag)+" 个");
                    }else{
                        new_word.setText(String.valueOf(tag - sp.getInt("new_words", 20))+" 个");
                    }

                    viewFlipper.addView(createViewWithXml(), viewFlipper.getChildCount());
                    viewFlipper.setInAnimation(Main_study.this, R.anim.right_in);
                    viewFlipper.setOutAnimation(Main_study.this, R.anim.left_out);

                    viewFlipper.showNext();
                }else{
                    confirmDialog.setLogoImg(R.drawable.true_1).setMsg("恭喜完成今日任务！");
                    confirmDialog.setClickListener(new ConfirmDialog.OnBtnClickListener() {
                        @Override
                        public void ok() {
                            // 构建传递给服务端的参数字典
                            LCUser currentUser = LCUser.getCurrentUser();
                            Map<String, Object> dicParameters = new HashMap<>();
                            dicParameters.put("UserID", currentUser.getObjectId());
                            dicParameters.put("Use_time", time);
                            dicParameters.put("tag", 1);

                            // 调用指定名称的云函数 averageStars，并且传递参数（默认不使用缓存）
                            LCCloud.callFunctionInBackground("Study_Status_Get", dicParameters).subscribe(new Observer<Object>() {
                                @Override
                                public void onSubscribe(Disposable disposable) {

                                }

                                @Override
                                public void onNext(Object object) {


                                }

                                @Override
                                public void onError(Throwable throwable) {
                                    // failed.
                                }

                                @Override
                                public void onComplete() {
                                    finish();
                                }

                            });



                        }
                        @Override
                        public void cancel() {


                            // 构建传递给服务端的参数字典
                            LCUser currentUser = LCUser.getCurrentUser();
                            Map<String, Object> dicParameters = new HashMap<>();
                            dicParameters.put("UserID", currentUser.getObjectId());
                            dicParameters.put("Use_time", time);
                            dicParameters.put("tag", 1);

                            // 调用指定名称的云函数 averageStars，并且传递参数（默认不使用缓存）
                            LCCloud.callFunctionInBackground("Study_Status_Get", dicParameters).subscribe(new Observer<Object>() {
                                @Override
                                public void onSubscribe(Disposable disposable) {

                                }

                                @Override
                                public void onNext(Object object) {


                                }

                                @Override
                                public void onError(Throwable throwable) {
                                    // failed.
                                }

                                @Override
                                public void onComplete() {

                                }

                            });


                        }
                    });
                    confirmDialog.show();
                    //回答正确，跳转下一题
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
     * 初始化监听。
     */
    private InitListener mTtsInitListener = new InitListener() {
        @Override
        public void onInit(int code) {
            Log.d(TAG, "InitListener init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                showTip("初始化失败,错误码：" + code + ",请点击网址https://www.xfyun.cn/document/error-code查询解决方案");
            } else {
                // 初始化成功，之后可以调用startSpeaking方法
                // 注：有的开发者在onCreate方法中创建完合成对象之后马上就调用startSpeaking进行合成，
                // 正确的做法是将onCreate中的startSpeaking调用移至这里
            }
        }
    };

    /**
     * 合成回调监听。
     */
    private SynthesizerListener mTtsListener = new SynthesizerListener() {

        @Override
        public void onSpeakBegin() {
            showTip("开始播放");
        }

        @Override
        public void onSpeakPaused() {
            //showTip("暂停播放");
        }

        @Override
        public void onSpeakResumed() {
            //showTip("继续播放");
        }

        @Override
        public void onBufferProgress(int percent, int beginPos, int endPos,
                                     String info) {
            // 合成进度
            Log.e("MscSpeechLog_", "percent =" + percent);
            mPercentForBuffering = percent;
            //showTip(String.format(getString(R.string.tts_toast_format),
            //mPercentForBuffering, mPercentForPlaying));
        }

        @Override
        public void onSpeakProgress(int percent, int beginPos, int endPos) {
            // 播放进度
            Log.e("MscSpeechLog_", "percent =" + percent);
            mPercentForPlaying = percent;
            //showTip(String.format(getString(R.string.tts_toast_format),
            //mPercentForBuffering, mPercentForPlaying));

            SpannableStringBuilder style = new SpannableStringBuilder(texts);
            Log.e(TAG, "beginPos = " + beginPos + "  endPos = " + endPos);
            style.setSpan(new BackgroundColorSpan(Color.RED), beginPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            ((EditText) findViewById(R.id.tts_text)).setText(style);
        }

        @Override
        public void onCompleted(SpeechError error) {
            //showTip("播放完成");
            if (error != null) {
                showTip(error.getPlainDescription(true));
                return;
            }
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            //	 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            //	 若使用本地能力，会话id为null
            if (SpeechEvent.EVENT_SESSION_ID == eventType) {
                String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
                Log.d(TAG, "session id =" + sid);
            }
            // 当设置 SpeechConstant.TTS_DATA_NOTIFY 为1时，抛出buf数据
            if (SpeechEvent.EVENT_TTS_BUFFER == eventType) {
                byte[] buf = obj.getByteArray(SpeechEvent.KEY_EVENT_TTS_BUFFER);
                Log.e(TAG, "EVENT_TTS_BUFFER = " + buf.length);
                // 保存文件
                appendFile(pcmFile, buf);
            }

        }
    };

    private void showTip(final String str) {
        runOnUiThread(() -> {
            if (mToast != null) {
                mToast.cancel();
            }
            mToast = Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT);
            mToast.show();
        });
    }

    /**
     * 参数设置
     *
     * @return
     */
    private void setParam() {
        // 清空参数
        mTts.setParameter(SpeechConstant.PARAMS, null);
        // 根据合成引擎设置相应参数
        if (mEngineType.equals(SpeechConstant.TYPE_CLOUD)) {
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
            // 支持实时音频返回，仅在 synthesizeToUri 条件下支持
            mTts.setParameter(SpeechConstant.TTS_DATA_NOTIFY, "1");
            //	mTts.setParameter(SpeechConstant.TTS_BUFFER_TIME,"1");

            // 设置在线合成发音人
            mTts.setParameter(SpeechConstant.VOICE_NAME, voicer);
            //设置合成语速
            mTts.setParameter(SpeechConstant.SPEED, mSharedPreferences.getString("speed_preference", "60"));
            //设置合成音调
            mTts.setParameter(SpeechConstant.PITCH, mSharedPreferences.getString("pitch_preference", "50"));
            //设置合成音量
            mTts.setParameter(SpeechConstant.VOLUME, mSharedPreferences.getString("volume_preference", "80"));
        } else {
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
            mTts.setParameter(SpeechConstant.VOICE_NAME, "");

        }

        //设置播放器音频流类型
        mTts.setParameter(SpeechConstant.STREAM_TYPE, mSharedPreferences.getString("stream_preference", "3"));
        // 设置播放合成音频打断音乐播放，默认为true
        mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "false");

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        mTts.setParameter(SpeechConstant.AUDIO_FORMAT, "pcm");
        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH,
                getExternalFilesDir("msc").getAbsolutePath() + "/tts.pcm");
    }

    @Override
    public void onDestroy() {
        if (null != mTts) {
            mTts.stopSpeaking();
            // 退出时释放连接
            mTts.destroy();
        }
        super.onDestroy();
    }

    /**
     * 给file追加数据
     */
    private void appendFile(File file, byte[] buffer) {
        try {
            if (!file.exists()) {
                boolean b = file.createNewFile();
            }
            RandomAccessFile randomFile = new RandomAccessFile(file, "rw");
            randomFile.seek(file.length());
            randomFile.write(buffer);
            randomFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
                             time = String.format("%02d:%02d:%02d", hour, minute, seconds % 60);
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