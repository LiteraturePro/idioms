package cn.ovzv.idioms.navigation.me;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.ovzv.idioms.R;
import cn.ovzv.idioms.api.LeancloudApi;

public class Me_feedback extends AppCompatActivity implements View.OnClickListener {

    private TextView mTextView;
    private ImageView mImageView;
    private EditText mEditText;
    private Button mButton;
    private String Feedback_str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_me_feedback);
        mEditText = (EditText) findViewById(R.id.edittext);
        mButton = (Button) findViewById(R.id.put);
        mTextView = (TextView) findViewById(R.id.title);
        mTextView.setText("反馈建议");
        mImageView = (ImageView)findViewById(R.id.back);

        mEditText.setOnClickListener(this);
        mButton.setOnClickListener(this);
        mImageView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.put:
                Feedback_str = mEditText.getText().toString();
                if (TextUtils.isEmpty(Feedback_str)) {
                    showToast("反馈内容不能为空!");
                    return;
                }
                else{
                    // 意见反馈
                    try {
                        LeancloudApi.Feedback_save(Feedback_str);
                        showToast("您的反馈已收到!");
                    } catch (Exception e) {
                        e.printStackTrace();
                        showToast("反馈失败！"+e.toString());
                    }
                }
                finish();
                break;
            case R.id.back:
                finish();
                break;
            default:
                break;
        }
    }
    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}