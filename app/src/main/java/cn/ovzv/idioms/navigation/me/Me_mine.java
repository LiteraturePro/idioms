package cn.ovzv.idioms.navigation.me;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import cn.ovzv.idioms.R;
public class Me_mine extends AppCompatActivity implements View.OnClickListener {
    private TextView mTextView;
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_me_mine);

        mTextView = (TextView) findViewById(R.id.title);
        mTextView.setText("我的资料");
        mImageView = (ImageView)findViewById(R.id.back);
        mImageView.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();break;
            default:
                break;
        }

    }
}