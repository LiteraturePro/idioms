package cn.ovzv.idioms.navigation.main;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import cn.ovzv.idioms.R;
public class Main_game2 extends AppCompatActivity implements View.OnTouchListener{
    private ImageView mImageView;
    private TextView Idiom;
    private ViewFlipper viewFlipper;
    //要添加的页面布局ID
    private int viewIds[] = {R.layout.fragment_main_couplet_item};
    private float startX; //手指按下时的x坐标
    private float endX; //手指抬起时的x坐标
    private float moveX = 100f; //判断是否切换页面的标准值
    private GestureDetector gestureDetector; //创建手势监听器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main_game2);
        initView();
    }
    /**
     * 设置view
     * */
    public void initView(){
        mImageView = (ImageView)findViewById(R.id.back);
        Idiom = (TextView)findViewById(R.id.idiom);
        Idiom.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });
        viewFlipper = (ViewFlipper) findViewById(R.id.viewFlipper);
        viewFlipper.setOnTouchListener(this);
        gestureDetector = new GestureDetector(this, new MyGestureListener());
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
                viewFlipper.setInAnimation(Main_game2.this, R.anim.right_in);
                viewFlipper.setOutAnimation(Main_game2.this, R.anim.left_out);
                viewFlipper.showNext();
            }
            return true;
        }
    }
}