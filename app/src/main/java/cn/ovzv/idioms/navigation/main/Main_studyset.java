package cn.ovzv.idioms.navigation.main;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Switch;
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
    private SharedPreferences sp;
    private CheckBox chk1,chk2;
    private Switch switch1,switch2,switch3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main_studyset);
        initView();
        numLimitation1 = (TextView) findViewById(R.id.num_limitation1);
        numLimitation2 = (TextView) findViewById(R.id.num_limitation2);

        switch1 = (Switch) findViewById(R.id.switch1);
        switch2 = (Switch) findViewById(R.id.switch2);
        switch3 = (Switch) findViewById(R.id.switch3);

        chk1 = (CheckBox) findViewById(R.id.chk1);
        chk2 = (CheckBox) findViewById(R.id.chk2);


        //获取Sp对象
        //参数一 文件名   参数二  模式（固定写法）
        sp = getSharedPreferences("words", MODE_PRIVATE);

        chk1.setChecked(sp.getBoolean("study_1",false));
        chk2.setChecked(sp.getBoolean("study_2",false));

        chk1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(chk1.isChecked()){
                    SharedPreferences.Editor edit = sp.edit();
                    //写入数据
                    edit.putBoolean("study_1",true);
                    //提交
                    edit.commit();
                }
                else {
                    SharedPreferences.Editor edit = sp.edit();
                    //写入数据
                    edit.putBoolean("study_1",false);
                    //提交
                    edit.commit();
                }

            }
        });

        chk2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(chk2.isChecked()){
                    SharedPreferences.Editor edit = sp.edit();
                    //写入数据
                    edit.putBoolean("study_2",true);
                    //提交
                    edit.commit();
                }
                else {
                    SharedPreferences.Editor edit = sp.edit();
                    //写入数据
                    edit.putBoolean("study_2",false);
                    //提交
                    edit.commit();
                }

            }
        });



        switch1.setChecked(sp.getBoolean("auto_1",false));
        switch2.setChecked(sp.getBoolean("auto_2",false));
        switch3.setChecked(sp.getBoolean("auto_3",true));


        switch1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(switch1.isChecked()){
                    System.out.println(true);
                    SharedPreferences.Editor edit = sp.edit();
                    //写入数据
                    edit.putBoolean("auto_1",true);
                    //提交
                    edit.commit();
                }
                else {
                    System.out.println(true);
                    SharedPreferences.Editor edit = sp.edit();
                    //写入数据
                    edit.putBoolean("auto_1",false);
                    //提交
                    edit.commit();
                }

            }
        });
        switch2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(switch2.isChecked()){
                    System.out.println(true);
                    SharedPreferences.Editor edit = sp.edit();
                    //写入数据
                    edit.putBoolean("auto_2",true);
                    //提交
                    edit.commit();
                }
                else {
                    System.out.println(true);
                    SharedPreferences.Editor edit = sp.edit();
                    //写入数据
                    edit.putBoolean("auto_2",false);
                    //提交
                    edit.commit();
                }

            }
        });
        switch3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(switch3.isChecked()){
                    System.out.println(true);
                    SharedPreferences.Editor edit = sp.edit();
                    //写入数据
                    edit.putBoolean("auto_3",true);
                    //提交
                    edit.commit();
                }
                else {
                    System.out.println(true);
                    SharedPreferences.Editor edit = sp.edit();
                    //写入数据
                    edit.putBoolean("auto_3",false);
                    //提交
                    edit.commit();
                }

            }
        });




        //点击按钮吐司一下内容
        numLimitation1.setText("每日新学 "+String.valueOf(sp.getInt("new_words", 20))+" 个");
        numLimitation2.setText("每日复习 "+String.valueOf(sp.getInt("new_words", 20))+" 个");

        numLimitation1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(numLimitation1, numberlist, 20,1);
            }
        });

    }

    //数字选择器
    private void initData() {
        numberlist.clear();
        for (int i = 0; i <= 100; i++) {
            numberlist.add(String.format("%d", i));
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
                                numLimitation1.setText("每日新词 "+selectText + " 个");
                                numLimitation2.setText("每日复习 "+selectText + " 个");
                                //参数一 文件名   参数二  模式（固定写法）
                                SharedPreferences sp = getSharedPreferences("words",MODE_PRIVATE);
                                //编辑者
                                SharedPreferences.Editor edit = sp.edit();
                                //写入数据
                                edit.putInt("new_words",Integer.parseInt(selectText));
                                //提交
                                edit.commit();
                                Log.d("每日新词",selectText);
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