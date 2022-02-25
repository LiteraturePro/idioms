package cn.ovzv.idioms.navigation.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
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
import com.umeng.commonsdk.debug.D;

import java.util.HashMap;
import java.util.Map;
import cn.leancloud.LCCloud;
import cn.ovzv.idioms.R;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class Main_couplet extends AppCompatActivity implements View.OnTouchListener{
    private TextView mTextView;
    private ImageView mImageView;
    private ViewFlipper viewFlipper;
    private float startX; //手指按下时的x坐标
    private float endX; //手指抬起时的x坐标
    private float moveX = 100f; //判断是否切换页面的标准值
    private GestureDetector gestureDetector; //创建手势监听器
    private JSONArray DataJSONArray;

    private View mGridView;
    private LinearLayout mLinearLayout_right,mLinearLayout_left;//对应于主布局中用来添加子布局的View

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main_couplet);

        initView();
    }
    /**
     * 设置view
     * */
    public void initView(){
        mTextView = (TextView) findViewById(R.id.title);
        mTextView.setText("成语对联");
        mImageView = (ImageView)findViewById(R.id.back);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });
        viewFlipper = (ViewFlipper) findViewById(R.id.viewFlipper);
        viewFlipper.addView(createViewWithXml(), viewFlipper.getChildCount());
        viewFlipper.setOnTouchListener(this);
        gestureDetector = new GestureDetector(this, new MyGestureListener());

    }

    /**
     * 自定义手势监听类
     */
    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (e2.getX() - e1.getX() > moveX) {
                viewFlipper.setInAnimation(Main_couplet.this, R.anim.left_in);
                viewFlipper.setOutAnimation(Main_couplet.this, R.anim.right_out);
                viewFlipper.showPrevious();
            } else if (e2.getX() - e1.getX() < moveX) {
                viewFlipper.removeAllViews();
                viewFlipper.addView(createViewWithXml(), viewFlipper.getChildCount());
                viewFlipper.setInAnimation(Main_couplet.this, R.anim.right_in);
                viewFlipper.setOutAnimation(Main_couplet.this, R.anim.left_out);
                viewFlipper.showNext();

            }
            return true;
        }
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

//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                //手指按下时获取起始点坐标
//                startX = event.getX();
//                break;
//            case MotionEvent.ACTION_UP:
//                //手指抬起时获取结束点坐标
//                endX = event.getX();
//                //比较startX和endX，判断手指的滑动方向
//                if (endX - startX > moveX) { //手指从左向右滑动
//                    viewFlipper.setInAnimation(this, R.anim.left_in);
//                    viewFlipper.setOutAnimation(this, R.anim.right_out);
//                    viewFlipper.showPrevious();
//                } else if (startX - endX > moveX) { //手指向右向左滑动
//                    viewFlipper.setInAnimation(this, R.anim.right_in);
//                    viewFlipper.setOutAnimation(this, R.anim.left_out);
//                    viewFlipper.showNext();
//                }
//                break;
//        }
        return true;
    }
    private View createViewWithXml() {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        lp.setMarginEnd(40);
        lp.setMarginStart(40);
        View view = LayoutInflater.from(this).inflate(R.layout.fragment_main_couplet_item, null);//也可以从XML中加载布局

        view.setLayoutParams(lp);//设置布局参数

        // 构建传递给服务端的参数字典
        Map<String, Object> dicParameters = new HashMap<>();
        dicParameters.put("test", "test");

        // 调用指定名称的云函数 averageStars，并且传递参数（默认不使用缓存）
        LCCloud.callFunctionInBackground("DB_Get_couplet", dicParameters).subscribe(new Observer<Object>() {
            @Override
            public void onSubscribe(Disposable disposable) {

            }

            @Override
            public void onNext(Object object) {
                // succeed.
                JSONObject json = (JSONObject) JSONObject.toJSON(object);
                DataJSONArray = json.getJSONArray("data");

                // 楷体
                AssetManager mgr = getAssets();
                Typeface tf = Typeface.createFromAsset(mgr, "fonts/kaiti_GB2312.ttf");

                mLinearLayout_left = (LinearLayout) findViewById(R.id.study_words_left);
                mLinearLayout_right = (LinearLayout) findViewById(R.id.study_words_right);

                char left_str[] = DataJSONArray.get(0).toString().toCharArray();//利用toCharArray方法转换
                for (int j = 0; j < left_str.length; j++) {
                    System.out.println(left_str[j]);
                    mGridView = View.inflate(getApplicationContext(), R.layout.fragment_main_study_words, null);
                    TextView txt = mGridView.findViewById(R.id.words_text);
                    txt.setText(String.valueOf(left_str[j]));
                    txt.setTextSize(40);
                    txt.setTypeface(tf);
                    mLinearLayout_left.addView(mGridView, 140, 140);
                }
                char right_str[] = DataJSONArray.get(1).toString().toCharArray();//利用toCharArray方法转换
                for (int j = 0; j < right_str.length; j++) {
                    System.out.println(right_str[j]);
                    mGridView = View.inflate(getApplicationContext(), R.layout.fragment_main_study_words, null);
                    TextView txt = mGridView.findViewById(R.id.words_text);
                    txt.setText(String.valueOf(right_str[j]));
                    txt.setTextSize(40);
                    txt.setTypeface(tf);
                    mLinearLayout_right.addView(mGridView, 140, 140);
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
        return view;
    }
}