package cn.ovzv.idioms.navigation.me;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.leancloud.LCCloud;
import cn.leancloud.LCUser;
import cn.ovzv.idioms.Login;
import cn.ovzv.idioms.R;
import cn.ovzv.idioms.adapter.FootprintAdapter;
import cn.ovzv.idioms.api.LeancloudApi;
import cn.ovzv.idioms.help.Footprint;
import cn.ovzv.idioms.navigation.main.Main_game1;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class Me_footprint extends AppCompatActivity {

    private TextView mTextView;
    private ImageView mImageView;
    private List<Footprint> FootprintList = new ArrayList<>();
    private ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_me_footprint);

        listView = (ListView) findViewById(R.id.listview);


        LCUser currentUser = LCUser.getCurrentUser();
        if (currentUser != null) {
            // 业务逻辑

            // 构建传递给服务端的参数字典
            Map<String, Object> dicParameters = new HashMap<>();
            dicParameters.put("UserID", currentUser.getObjectId());

            // 调用指定名称的云函数 averageStars，并且传递参数（默认不使用缓存）
            LCCloud.callFunctionInBackground("Daka_Data_Get", dicParameters).subscribe(new Observer<Object>() {
                @Override
                public void onSubscribe(Disposable disposable) {

                }

                @Override
                public void onNext(Object object) {
                    // succeed.
                    JSONObject json = (JSONObject) JSONObject.toJSON(object);

                    initView();
                    System.out.println(FootprintList);
                    JSONArray jsonArray = json.getJSONArray("data");
                    for(int i = jsonArray.size()-1; i >= 0;i--){
                        if(jsonArray.getJSONObject(i).getBoolean("Do"))
                        {
                            Footprint data = new Footprint(jsonArray.getJSONObject(i).getString("Time"),"用时: "+jsonArray.getJSONObject(i).getString("Use_time"),"已打卡",R.drawable.daka_do);
                            FootprintList.add(data);

                        }
                        else{
                            Footprint data = new Footprint(jsonArray.getJSONObject(i).getString("Time"),"用时: "+jsonArray.getJSONObject(i).getString("Use_time"),"未打卡",R.drawable.daka_no);
                            FootprintList.add(data);

                        }

                    }

                    FootprintAdapter arrayAdapter = new FootprintAdapter(getApplicationContext(),R.layout.footprint_item,FootprintList);
                    listView.setAdapter(arrayAdapter);

                }

                @Override
                public void onError(Throwable throwable) {
                    // failed.
                }

                @Override
                public void onComplete() {

                }

            });


        } else {
            // 显示注册或登录页面
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
        }



    }
    /**
     * 设置view
     * */
    public void initView(){

        mTextView = (TextView) findViewById(R.id.title);
        mTextView.setText("学习足迹");


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