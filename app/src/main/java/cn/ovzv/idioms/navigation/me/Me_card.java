package cn.ovzv.idioms.navigation.me;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.util.HashMap;
import java.util.Map;
import cn.leancloud.LCCloud;
import cn.ovzv.idioms.R;
import cn.ovzv.idioms.help.SideslipListView;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class Me_card extends AppCompatActivity {
    private TextView mTextView;
    private ImageView mImageView;

    private SideslipListView mSideslipListView;
    /**
     * 初始化数据
     */
    private JSONArray DataJSONArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_me_card);
        initView();

        /** 设置请求参数*/
        Map<String, Object> dicParameters = new HashMap<>();
        dicParameters.put("UserID", "system" );
        /** 调用云函数*/
        LCCloud.callFunctionInBackground("Card_Get", dicParameters).subscribe(new Observer<Object>() {
            @Override
            public void onSubscribe(Disposable disposable) {

            }
            @Override
            public void onNext(Object object) {
                JSONObject json = (JSONObject) JSONObject.toJSON(object);
                DataJSONArray = json.getJSONArray("data");
            }
            @Override
            public void onError(Throwable throwable) {
                // failed.
                Log.d("error",throwable.toString());
            }
            @Override
            public void onComplete() {
                mSideslipListView = (SideslipListView) findViewById(R.id.sideslipListView);
                mSideslipListView.setAdapter(new CustomAdapter());//设置适配器
            }
        });
    }
    /**
     * 设置view
     * */
    public void initView(){
        mTextView = (TextView) findViewById(R.id.title);
        mTextView.setText("我的卡券");
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
     * 自定义ListView适配器
     */
    class CustomAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return DataJSONArray.size();
        }

        @Override
        public Object getItem(int position) {
            return DataJSONArray.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            JSONObject OneDate = DataJSONArray.getJSONObject(position);
            ViewHolder viewHolder;
            if (null == convertView) {
                convertView = View.inflate(Me_card.this, R.layout.fragment_me_card_style, null);
                viewHolder = new ViewHolder();
                viewHolder.Used = (TextView) convertView.findViewById(R.id.used);
                viewHolder.Quota = (TextView) convertView.findViewById(R.id.quota);
                viewHolder.Time = (TextView) convertView.findViewById(R.id.time);
                viewHolder.Course = (TextView) convertView.findViewById(R.id.course);
                viewHolder.Limit = (TextView) convertView.findViewById(R.id.limit);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.Used.setText(OneDate.getString(""));

            viewHolder.Limit.setText(OneDate.getString("Limit"));
            viewHolder.Course.setText(OneDate.getString("Course"));
            viewHolder.Quota.setText(OneDate.getString("Quota"));
            viewHolder.Time.setText(OneDate.getString("Time"));
            return convertView;
        }
    }

    class ViewHolder {
        public TextView Used,Quota,Time,Course,Limit;
    }

}