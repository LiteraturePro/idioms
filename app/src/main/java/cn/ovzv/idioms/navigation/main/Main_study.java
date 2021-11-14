package cn.ovzv.idioms.navigation.main;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.ovzv.idioms.R;
public class Main_study extends AppCompatActivity {

    private ImageView mImageView;
    private LinearLayout mLinearLayout;//对应于主布局中用来添加子布局的View
    private View mGridView,mGridView2,mGridView3,mGridView4;// 子Layout要以view的形式加入到主Layout

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main_study);

        // 楷体
        AssetManager mgr = getAssets();
        Typeface tf = Typeface.createFromAsset(mgr, "fonts/kaiti_GB2312.ttf");
        mLinearLayout = (LinearLayout)findViewById(R.id.study_words);

        for(int i = 0; i < 4; i++){
            mGridView = View.inflate(this,R.layout.fragment_main_study_words,null);
            TextView txt = mGridView.findViewById(R.id.words_text);
            txt.setText("高");
            txt.setTextSize(40);
            txt.setTypeface(tf);
            mLinearLayout.addView(mGridView,140,140);
        }
        // 1
//        mGridView = View.inflate(this,R.layout.fragment_main_study_words,null);
//        TextView txt = mGridView.findViewById(R.id.words_text);
//        txt.setText("高");
//        txt.setTypeface(tf);
//
//        // 2
//        mGridView2 = View.inflate(this,R.layout.fragment_main_study_words,null);
//        TextView txt2 = mGridView2.findViewById(R.id.words_text);
//        txt2.setText("不");
//        txt2.setTypeface(tf);
//
//        // 3
//        mGridView3 = View.inflate(this,R.layout.fragment_main_study_words,null);
//        TextView txt3 = mGridView3.findViewById(R.id.words_text);
//        txt3.setText("可");
//        txt3.setTypeface(tf);
//
//        // 4
//        mGridView4 = View.inflate(this,R.layout.fragment_main_study_words,null);
//        TextView txt4 = mGridView4.findViewById(R.id.words_text);
//        txt4.setText("攀");
//        txt4.setTypeface(tf);

//
//
//
//
//        mLinearLayout.addView(mGridView,140,140);
//        mLinearLayout.addView(mGridView2,140,140);
//        mLinearLayout.addView(mGridView3,140,140);
//        mLinearLayout.addView(mGridView4,140,140);

        initView();
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
}