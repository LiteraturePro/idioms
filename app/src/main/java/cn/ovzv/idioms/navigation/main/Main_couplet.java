package cn.ovzv.idioms.navigation.main;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cn.ovzv.idioms.R;
public class Main_couplet extends AppCompatActivity {
    private TextView mTextView;
    private ImageView mImageView;

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
    }
}