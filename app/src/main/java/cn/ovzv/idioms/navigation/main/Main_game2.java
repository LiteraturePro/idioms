package cn.ovzv.idioms.navigation.main;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cn.ovzv.idioms.R;
public class Main_game2 extends AppCompatActivity {
    private ImageView mImageView;
    private TextView Idiom;

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
    }
}