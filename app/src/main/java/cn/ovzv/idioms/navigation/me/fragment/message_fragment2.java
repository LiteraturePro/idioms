package cn.ovzv.idioms.navigation.me.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.leancloud.LCCloud;
import cn.ovzv.idioms.R;
import cn.ovzv.idioms.help.SideslipListView;
import cn.ovzv.idioms.navigation.me.Me_card;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link message_fragment2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class message_fragment2 extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private SideslipListView mSideslipListView;
    private JSONArray DataJSONArray;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public message_fragment2() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment message_fragment2.
     */
    // TODO: Rename and change types and number of parameters
    public static message_fragment2 newInstance(String param1, String param2) {
        message_fragment2 fragment = new message_fragment2();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_me_message_fragment2, container, false);
        /** 设置请求参数*/
        Map<String, Object> dicParameters = new HashMap<>();
        dicParameters.put("message", "activity" );
        /** 调用云函数*/
        LCCloud.callFunctionInBackground("Message_Get", dicParameters).subscribe(new Observer<Object>() {
            @Override
            public void onSubscribe(Disposable disposable) {
            }
            @Override
            public void onNext(Object object) {
                // succeed.
                System.out.println(object);
                System.out.println(object.toString());
                JSONObject json = (JSONObject) JSONObject.toJSON(object);
                System.out.println(json.getJSONArray("data")); //true
                DataJSONArray = json.getJSONArray("data");
            }
            @Override
            public void onError(Throwable throwable) {
                // failed.
                Log.d("error",throwable.toString());
            }
            @Override
            public void onComplete() {
                mSideslipListView = (SideslipListView) view.findViewById(R.id.sideslipListView);
                mSideslipListView.setAdapter(new CustomAdapter());//设置适配器
            }
        });
        return view;
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
                convertView = View.inflate(getContext(), R.layout.fragment_me_message_style, null);
                viewHolder = new ViewHolder();
                viewHolder.message = (TextView) convertView.findViewById(R.id.message);
                viewHolder.time = (TextView) convertView.findViewById(R.id.time);
                viewHolder.text = (TextView) convertView.findViewById(R.id.text);
                viewHolder.message_png = (ImageView) convertView.findViewById(R.id.message_png);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.message.setText("活动信息");
            viewHolder.message_png.setImageResource(R.drawable.fragment_me_message_activity);
            viewHolder.time.setText(OneDate.getString("Time"));
            viewHolder.text.setText(OneDate.getString("Text"));
            return convertView;
        }
    }

    class ViewHolder {
        public TextView message,time,text;
        public ImageView message_png;
    }
}