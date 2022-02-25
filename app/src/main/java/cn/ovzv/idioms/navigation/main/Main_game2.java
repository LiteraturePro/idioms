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
import io.reactivex.Observer;

import cn.leancloud.LCCloud;
import cn.ovzv.idioms.R;
import io.reactivex.disposables.Disposable;

public class Main_game2 extends AppCompatActivity implements View.OnTouchListener{
    private ImageView mImageView;
    private ViewFlipper viewFlipper;
    private float startX; //手指按下时的x坐标
    private float endX; //手指抬起时的x坐标
    private float moveX = 100f; //判断是否切换页面的标准值
    private GestureDetector gestureDetector; //创建手势监听器

    private EditText game2_text;
    private Button game2_button;
    private TextView game2_daan;
    private JSONArray DataJSONArray;
    private String daan;
    private ConfirmDialog confirmDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main_game2);

        // 初始化弹窗对象

        confirmDialog = new ConfirmDialog(this);

        game2_button =(Button) findViewById(R.id.game2_button);
        game2_text =(EditText) findViewById(R.id.game2_text);

        game2_daan =(TextView) findViewById(R.id.game2_daan);

        initView();
        viewFlipper = (ViewFlipper) findViewById(R.id.viewFlipper);
        viewFlipper.addView(createViewWithXml(), viewFlipper.getChildCount());
        viewFlipper.setOnTouchListener(this);
        gestureDetector = new GestureDetector(this, new MyGestureListener());

        game2_daan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                game2_daan.setText(daan);
                showToast("已查看答案，3秒后自动跳转下一个");
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        viewFlipper.addView(createViewWithXml(), viewFlipper.getChildCount());
                        viewFlipper.setInAnimation(Main_game2.this, R.anim.right_in);
                        viewFlipper.setOutAnimation(Main_game2.this, R.anim.left_out);
                        viewFlipper.showNext();
                        game2_daan.setText("点击查看答案");
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
                break;
        }
        return true;
    }
    /**
     * 自定义手势监听类
     */
    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (e2.getX() - e1.getX() > moveX) {
                viewFlipper.setInAnimation(Main_game2.this, R.anim.left_in);
                viewFlipper.setOutAnimation(Main_game2.this, R.anim.right_out);
                viewFlipper.showPrevious();
            } else if (e2.getX() - e1.getX() < moveX) {
                if (viewFlipper.getChildCount() > 1) {
                    viewFlipper.removeViewAt(0);
                }
                viewFlipper.addView(createViewWithXml(), viewFlipper.getChildCount());
                viewFlipper.setInAnimation(Main_game2.this, R.anim.right_in);
                viewFlipper.setOutAnimation(Main_game2.this, R.anim.left_out);
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


        View view = LayoutInflater.from(this).inflate(R.layout.fragment_main_game2_item, null);//也可以从XML中加载布局



        view.setLayoutParams(lp);//设置布局参数

        // 构建传递给服务端的参数字典
        Map<String, Object> dicParameters = new HashMap<>();
        dicParameters.put("test", "test");

        // 调用指定名称的云函数 averageStars，并且传递参数（默认不使用缓存）
        LCCloud.callFunctionInBackground("DB_Get_riddle", dicParameters).subscribe(new Observer<Object>() {
            @Override
            public void onSubscribe(Disposable disposable) {

            }

            @Override
            public void onNext(Object object) {
                // succeed.
                JSONObject json = (JSONObject) JSONObject.toJSON(object);
                DataJSONArray = json.getJSONArray("data");
                System.out.println(DataJSONArray);
                daan = DataJSONArray.get(0).toString();
                TextView textView = (TextView)view.findViewById(R.id.game2_texts);
                System.out.println(DataJSONArray.get(1).toString());
                textView.setText(DataJSONArray.get(1).toString());
            }

            @Override
            public void onError(Throwable throwable) {
                // failed.
            }

            @Override
            public void onComplete() {
                game2_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (TextUtils.isEmpty(game2_text.getText().toString().trim()) ) {
                            showToast("答案不能为空");
                            return;
                        }else{
                            if(game2_text.getText().toString().equals(daan)){

                                confirmDialog.setLogoImg(R.drawable.true_1).setMsg("恭喜你！回答正确，点击确认进入下一题");
                                confirmDialog.setClickListener(new ConfirmDialog.OnBtnClickListener() {
                                    @Override
                                    public void ok() {

                                        Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                viewFlipper.addView(createViewWithXml(), viewFlipper.getChildCount());
                                                viewFlipper.setInAnimation(Main_game2.this, R.anim.right_in);
                                                viewFlipper.setOutAnimation(Main_game2.this, R.anim.left_out);
                                                viewFlipper.showNext();
                                                game2_text.setText("");
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
                                                viewFlipper.setInAnimation(Main_game2.this, R.anim.right_in);
                                                viewFlipper.setOutAnimation(Main_game2.this, R.anim.left_out);
                                                viewFlipper.showNext();
                                                game2_text.setText("");
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
                                        game2_text.setText("");
                                    }

                                    @Override
                                    public void cancel() {
                                        game2_text.setText("");
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

}