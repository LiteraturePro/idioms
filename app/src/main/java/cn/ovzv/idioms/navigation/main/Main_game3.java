package cn.ovzv.idioms.navigation.main;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import com.hb.dialog.dialog.ConfirmDialog;
import cn.ovzv.idioms.R;

public class Main_game3 extends AppCompatActivity implements View.OnTouchListener{
    private ImageView mImageView;
    private ViewFlipper viewFlipper;
    //要添加的页面布局ID
    private int viewIds[] = {R.layout.fragment_main_couplet_item};
    private float startX; //手指按下时的x坐标
    private float endX; //手指抬起时的x坐标
    private float moveX = 100f; //判断是否切换页面的标准值
    private GestureDetector gestureDetector; //创建手势监听器

    private EditText game3_text;
    private Button game3_button;
    private TextView game3_daan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main_game3);

        // 初始化弹窗对象

        ConfirmDialog confirmDialog = new ConfirmDialog(this);

        game3_button =(Button) findViewById(R.id.game3_button);
        game3_text =(EditText) findViewById(R.id.game3_text);
        game3_daan = (TextView)findViewById(R.id.game3_daan);
        initView();
        viewFlipper = (ViewFlipper) findViewById(R.id.viewFlipper);
        viewFlipper.setOnTouchListener(this);
        gestureDetector = new GestureDetector(this, new MyGestureListener());

        game3_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(game3_text.getText().toString().trim()) ) {
                    showToast("答案不能为空");
                    return;
                }else{
                    if(game3_text.getText().toString().equals("我是答案")){

                        confirmDialog.setLogoImg(R.drawable.true_1).setMsg("恭喜你！回答正确，点击确认进入下一题");
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
                    else{
                        confirmDialog.setLogoImg(R.drawable.false_1).setMsg("答案不正确哦！再试试吧");
                        confirmDialog.setClickListener(new ConfirmDialog.OnBtnClickListener() {
                            @Override
                            public void ok() {
                            }

                            @Override
                            public void cancel() {
                            }
                        });
                        confirmDialog.show();
                        return;
                    }
                }
            }
        });
        game3_daan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                game3_daan.setText("我是答案");
                showToast("5秒后自动跳转下一个");
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
                //手指抬起时获取结束点坐标
                endX = event.getX();
                //比较startX和endX，判断手指的滑动方向
                if (endX - startX > moveX) { //手指从左向右滑动
                    viewFlipper.setInAnimation(this, R.anim.left_in);
                    viewFlipper.setOutAnimation(this, R.anim.right_out);
                    viewFlipper.showPrevious();
                } else if (startX - endX > moveX) { //手指向右向左滑动
                    viewFlipper.setInAnimation(this, R.anim.right_in);
                    viewFlipper.setOutAnimation(this, R.anim.left_out);
                    viewFlipper.showNext();
                }
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
                viewFlipper.setInAnimation(Main_game3.this, R.anim.left_in);
                viewFlipper.setOutAnimation(Main_game3.this, R.anim.right_out);
                viewFlipper.showPrevious();
            } else if (e2.getX() - e1.getX() < moveX) {
                viewFlipper.setInAnimation(Main_game3.this, R.anim.right_in);
                viewFlipper.setOutAnimation(Main_game3.this, R.anim.left_out);
                viewFlipper.showNext();
            }
            return true;
        }
    }
    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

}