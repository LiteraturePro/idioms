package cn.ovzv.idioms.navigation.main.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.BackgroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.leancloud.LCCloud;
import cn.leancloud.LCUser;
import cn.ovzv.idioms.R;
import cn.ovzv.idioms.help.SideslipListView;
import cn.ovzv.idioms.help.TtsSettings;
import cn.ovzv.idioms.tts;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link words_fragment1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class words_fragment1 extends Fragment {

    private SideslipListView mSideslipListView;

    private ArrayList<String> mDataList_word,mDataList_pinyin,mDataList_text;
    private JSONArray DataJSONArray;
    private static String TAG = tts.class.getSimpleName();
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

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public words_fragment1() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment words_fragment1.
     */
    // TODO: Rename and change types and number of parameters
    public static words_fragment1 newInstance(String param1, String param2) {
        words_fragment1 fragment = new words_fragment1();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main_words_fragment1, container, false);

        //参数一 文件名   参数二  模式（固定写法）
        SharedPreferences sp = getActivity().getSharedPreferences("words", Context.MODE_PRIVATE);
        LCUser currentUser = LCUser.getCurrentUser();

        // 构建传递给服务端的参数字典
        Map<String, Object> dicParameters = new HashMap<String, Object>();
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



                mDataList_word = new ArrayList<String>() {
                    {
                        for (int i = 0; i < DataJSONArray.size(); i++) {
                            add(DataJSONArray.getJSONObject(i).getString("word"));
                        }
                    }
                };
                mDataList_pinyin = new ArrayList<String>() {
                    {
                        for (int i = 0; i < DataJSONArray.size(); i++) {
                            add(DataJSONArray.getJSONObject(i).getString("pinyin"));
                        }
                    }
                };
                mDataList_text = new ArrayList<String>() {
                    {
                        for (int i = 0; i < DataJSONArray.size(); i++) {
                            add(DataJSONArray.getJSONObject(i).getString("explanation"));
                        }
                    }
                };
            }

            @Override
            public void onError(Throwable throwable) {
                // failed.
            }

            @Override
            public void onComplete() {

                mSideslipListView = (SideslipListView) view.findViewById(R.id.sideslipListView);
                mSideslipListView.setAdapter(new CustomAdapter());//设置适配器

                // 初始化合成对象
                mTts = SpeechSynthesizer.createSynthesizer(getActivity().getApplicationContext(), mTtsInitListener);
                mSharedPreferences = getActivity().getSharedPreferences(TtsSettings.PREFER_NAME, getActivity().MODE_PRIVATE);
                //设置item点击事件
                mSideslipListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (mSideslipListView.isAllowItemClick()) {
                            Log.d(TAG, mDataList_word.get(position) + "被点击了");

                            pcmFile = new File(getActivity().getExternalCacheDir().getAbsolutePath(), "tts_pcmFile.pcm");
                            pcmFile.delete();
                            //texts = ((EditText) getActivity().findViewById(R.id.tts_text)).getText().toString();
                            // 设置参数
                            setParam();
                            // 合成并播放
                            int code = mTts.startSpeaking(mDataList_word.get(position) , mTtsListener);
//			/**
//			 * 只保存音频不进行播放接口,调用此接口请注释startSpeaking接口
//			 * text:要合成的文本，uri:需要保存的音频全路径，listener:回调接口
//			*/
//                String path = getExternalFilesDir("msc").getAbsolutePath() + "/tts.pcm";
//                //  synthesizeToUri 只保存音频不进行播放
//                int code = mTts.synthesizeToUri(texts, path, mTtsListener);

//                            if (code != ErrorCode.SUCCESS) {
//                                showTip("语音合成失败,错误码: " + code + ",请点击网址https://www.xfyun.cn/document/error-code查询解决方案");
//                            }


                        }
                    }
                });


            }
        });

        return view;
    }


    /**
     * 自定义ListView适配器
     */
    class CustomAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mDataList_word.size();
        }

        @Override
        public Object getItem(int position) {
            return mDataList_word.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (null == convertView) {
                convertView = View.inflate(getContext(), R.layout.fragment_main_words_style, null);
                viewHolder = new ViewHolder();
                viewHolder.word = (TextView) convertView.findViewById(R.id.words);
                viewHolder.pinyin = (TextView) convertView.findViewById(R.id.pingyin);
                viewHolder.texts = (TextView) convertView.findViewById(R.id.text);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            // 楷体
            AssetManager mgr = getActivity().getAssets();
            Typeface tf = Typeface.createFromAsset(mgr, "fonts/kaiti_GB2312.ttf");

            viewHolder.word.setText(mDataList_word.get(position));
            viewHolder.word.setTypeface(tf, Typeface.BOLD);

            viewHolder.pinyin.setText("【"+mDataList_pinyin.get(position)+"】");
            viewHolder.texts.setText("\t\t"+mDataList_text.get(position));

            return convertView;
        }
    }

    class ViewHolder {
        public TextView word;
        public TextView pinyin;
        public TextView texts;
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
            ((EditText) getActivity().findViewById(R.id.tts_text)).setText(style);
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
        getActivity().runOnUiThread(() -> {
            if (mToast != null) {
                mToast.cancel();
            }
            mToast = Toast.makeText(getActivity().getApplicationContext(), str, Toast.LENGTH_SHORT);
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
                getActivity().getExternalFilesDir("msc").getAbsolutePath() + "/tts.pcm");
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
}