package cn.ovzv.idioms.navigation.me;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import cn.ovzv.idioms.R;
import cn.ovzv.idioms.help.SideslipListView;

public class Me_card extends AppCompatActivity {
    private TextView mTextView;
    private ImageView mImageView;

    private SideslipListView mSideslipListView;
    /**
     * 初始化数据
     */
    private ArrayList<String> mDataList = new ArrayList<String>() {
        {
            for (int i = 0; i < 5; i++) {
                add("ListView item  " + i);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_me_card);


        initView();


        mSideslipListView = (SideslipListView) findViewById(R.id.sideslipListView);
        mSideslipListView.setAdapter(new CustomAdapter());//设置适配器
//        //设置item点击事件
//        mSideslipListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (mSideslipListView.isAllowItemClick()) {
//                    Log.i(TAG, mDataList.get(position) + "被点击了");
//                    Toast.makeText(Me_card.this, mDataList.get(position) + "被点击了",
//                            Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//        //设置item长按事件
//        mSideslipListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                if (mSideslipListView.isAllowItemClick()) {
//                    Log.i(TAG, mDataList.get(position) + "被长按了");
//                    Toast.makeText(Me_card.this, mDataList.get(position) + "被长按了",
//                            Toast.LENGTH_SHORT).show();
//                    return true;//返回true表示本次事件被消耗了，若返回
//                }
//                return false;
//            }
//        });




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
            return mDataList.size();
        }

        @Override
        public Object getItem(int position) {
            return mDataList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (null == convertView) {
                convertView = View.inflate(Me_card.this, R.layout.fragment_me_card_style, null);
                viewHolder = new ViewHolder();
//                viewHolder.textView = (TextView) convertView.findViewById(R.id.textView);
//                viewHolder.txtv_delete = (TextView) convertView.findViewById(R.id.txtv_delete);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
//            viewHolder.textView.setText(mDataList.get(position));

            return convertView;
        }
    }

    class ViewHolder {
        public TextView textView;
        public TextView txtv_delete;
    }

}