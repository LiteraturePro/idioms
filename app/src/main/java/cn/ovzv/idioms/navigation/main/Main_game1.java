package cn.ovzv.idioms.navigation.main;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hb.dialog.dialog.ConfirmDialog;
import java.util.HashMap;
import java.util.Map;
import cn.leancloud.LCCloud;
import cn.ovzv.idioms.R;
import cn.ovzv.idioms.help.GetHttpBitmap;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class Main_game1 extends AppCompatActivity implements View.OnTouchListener{
    private ImageView mImageView;

    private ViewFlipper viewFlipper;
    //要添加的页面布局ID
    private int viewIds[] = {R.layout.fragment_main_couplet_item};
    private float startX; //手指按下时的x坐标
    private float endX; //手指抬起时的x坐标
    private float moveX = 100f; //判断是否切换页面的标准值
    private GestureDetector gestureDetector; //创建手势监听器

    private EditText game1_text;
    private Button game1_button;
    private TextView game1_daan;
    private JSONArray DataJSONArray;
    private String daan;
    private ConfirmDialog confirmDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main_game1);

        initView();

        // 初始化弹窗对象

        confirmDialog = new ConfirmDialog(this);

        game1_button =(Button) findViewById(R.id.game1_button);
        game1_text =(EditText) findViewById(R.id.game1_text);
        game1_daan =(TextView) findViewById(R.id.game1_daan);

        viewFlipper = (ViewFlipper) findViewById(R.id.viewFlipper);
        viewFlipper.addView(createViewWithXml(), viewFlipper.getChildCount());
        viewFlipper.setOnTouchListener(this);
        gestureDetector = new GestureDetector(this, new MyGestureListener());
        game1_daan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                game1_daan.setText(daan);
                showToast("已查看答案，3秒后自动跳转下一个");
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        viewFlipper.addView(createViewWithXml(), viewFlipper.getChildCount());
                        viewFlipper.setInAnimation(Main_game1.this, R.anim.right_in);
                        viewFlipper.setOutAnimation(Main_game1.this, R.anim.left_out);
                        viewFlipper.showNext();
                        game1_daan.setText("点击查看答案");
                    }
                }, 3000);//3秒后执行Runnable中的run方法


            }
        });
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
     * 自定义手势监听类
     */
    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (e2.getX() - e1.getX() > moveX) {
                viewFlipper.setInAnimation(Main_game1.this, R.anim.left_in);
                viewFlipper.setOutAnimation(Main_game1.this, R.anim.right_out);
                viewFlipper.showPrevious();
            } else if (e2.getX() - e1.getX() < moveX) {
                viewFlipper.addView(createViewWithXml(), viewFlipper.getChildCount());
                viewFlipper.setInAnimation(Main_game1.this, R.anim.right_in);
                viewFlipper.setOutAnimation(Main_game1.this, R.anim.left_out);
                viewFlipper.showNext();
            }
            return true;
        }
    }
    private View createViewWithXml() {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        lp.setMarginEnd(40);
        lp.setMarginStart(40);
        View view = LayoutInflater.from(this).inflate(R.layout.fragment_main_game1_item, null);//也可以从XML中加载布局

        view.setLayoutParams(lp);//设置布局参数

        // 构建传递给服务端的参数字典
        Map<String, Object> dicParameters = new HashMap<>();
        dicParameters.put("test", "test");

        // 调用指定名称的云函数 averageStars，并且传递参数（默认不使用缓存）
        LCCloud.callFunctionInBackground("DB_Get_word_url", dicParameters).subscribe(new Observer<Object>() {
            @Override
            public void onSubscribe(Disposable disposable) {

            }

            @Override
            public void onNext(Object object) {
                // succeed.
                JSONObject json = (JSONObject) JSONObject.toJSON(object);
                DataJSONArray = json.getJSONArray("data");
                daan = DataJSONArray.get(0).toString();

                //设置文本
            }

            @Override
            public void onError(Throwable throwable) {
                // failed.
            }

            @Override
            public void onComplete() {
                game1_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (TextUtils.isEmpty(game1_text.getText().toString().trim()) ) {
                            showToast("答案不能为空");
                            return;
                        }else{
                            if(game1_text.getText().toString().equals(daan)){

                                confirmDialog.setLogoImg(R.drawable.true_1).setMsg("恭喜你！回答正确，点击确认进入下一题");
                                confirmDialog.setClickListener(new ConfirmDialog.OnBtnClickListener() {
                                    @Override
                                    public void ok() {

                                        Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                viewFlipper.addView(createViewWithXml(), viewFlipper.getChildCount());
                                                viewFlipper.setInAnimation(Main_game1.this, R.anim.right_in);
                                                viewFlipper.setOutAnimation(Main_game1.this, R.anim.left_out);
                                                viewFlipper.showNext();
                                                game1_text.setText("");
                                            }
                                        }, 1000);//3秒后执行Runnable中的run方法

                                    }
                                    @Override
                                    public void cancel() {
                                        Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                viewFlipper.addView(createViewWithXml(), viewFlipper.getChildCount());
                                                viewFlipper.setInAnimation(Main_game1.this, R.anim.right_in);
                                                viewFlipper.setOutAnimation(Main_game1.this, R.anim.left_out);
                                                viewFlipper.showNext();
                                                game1_text.setText("");
                                            }
                                        }, 1000);//3秒后执行Runnable中的run方法
                                    }
                                });
                                confirmDialog.show();
                                //回答正确，跳转下一题
                            }
                            else{
                                confirmDialog.setLogoImg(R.drawable.false_1).setMsg("答案不正确哦！再试试吧");
                                confirmDialog.setClickListener(new ConfirmDialog.OnBtnClickListener() {
                                    @Override
                                    public void ok() {
                                        game1_text.setText("");
                                    }

                                    @Override
                                    public void cancel() {
                                        game1_text.setText("");
                                    }
                                });
                                confirmDialog.show();
                                return;
                            }
                        }
                    }
                });
            }
        });
        return view;
    }
    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
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

        return true;
    }
}