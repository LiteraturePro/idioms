package cn.ovzv.idioms.navigation.main;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import cn.ovzv.idioms.R;
import cn.ovzv.idioms.help.WheelView;

public class Main_studyset extends AppCompatActivity {
    private TextView mTextView;
    private ImageView mImageView;

    private String selectText = "";
    private ArrayList<String> numberlist = new ArrayList<>();
    private TextView numLimitation1,numLimitation2;
    private int TAG = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main_studyset);
        initView();
        numLimitation1 = findViewById(R.id.num_limitation1);
        numLimitation2 = findViewById(R.id.num_limitation2);

        numLimitation1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(numLimitation1, numberlist, 20,1);
            }
        });
        numLimitation2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(numLimitation2, numberlist, 20,2);
            }
        });

    }

    //数字选择器
    private void initData() {
        numberlist.clear();
        for (int i = 0; i <= 100; i++) {
            numberlist.add(String.format("%d个", i));
        }
    }
    /**
     * 设置view
     * */
    public void initView(){
        initData();
        mTextView = (TextView) findViewById(R.id.title);
        mTextView.setText("学习设置");


        mImageView = (ImageView)findViewById(R.id.back);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });
    }
    private void showDialog(TextView textView, ArrayList<String> list, int selected,int tag){
        showChoiceDialog(list, textView, selected,tag,
                new WheelView.OnWheelViewListener() {
                    @Override
                    public void onSelected(int selectedIndex, String item) {
                        selectText = item;
                    }
                });
    }

    private void showChoiceDialog(ArrayList<String> dataList,final TextView textView,int selected,int tag,
                                  WheelView.OnWheelViewListener listener){
        selectText = "";

        View outerView = LayoutInflater.from(this).inflate(R.layout.fragment_main_studyset_dialog_wheelview,null);
        final WheelView wheelView = outerView.findViewById(R.id.wheel_view);
        wheelView.setOffset(2);// 对话框中当前项上面和下面的项数
        wheelView.setItems(dataList);// 设置数据源
        wheelView.setSeletion(selected);// 默认选中第三项
        wheelView.setOnWheelViewListener(listener);

        // 显示对话框，点击确认后将所选项的值显示到Button上
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setView(outerView)
                .setPositiveButton("确认",
                        (dialogInterface, i) -> {
                            TAG = i;
                            if(tag == 1){
                                numLimitation1.setText("每日新词 "+selectText);
                                Log.d("每日新词",selectText);
                            }else{
                                numLimitation1.setText("每日新词 "+selectText);
                                Log.d("每日复习",selectText);
                            }
                            textView.setTextColor(this.getResources().getColor(R.color.txtnumber));
                        })
                .setNegativeButton("取消",null).create();
        alertDialog.show();
        int green = this.getResources().getColor(R.color.buyvip);
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(green);
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(green);

    }

}