package cn.ovzv.idioms.navigation.main.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.leancloud.LCCloud;
import cn.ovzv.idioms.R;
import cn.ovzv.idioms.help.SideslipListView;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fun_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fun_fragment extends Fragment {


    private SideslipListView mSideslipListView;
    private JSONArray DataJSONArray;
    private ArrayList<String> mDataList ;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private String mParam1;


    public fun_fragment() {
        // Required empty public constructor

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment fun_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static fun_fragment newInstance(String param1) {
        fun_fragment fragment = new fun_fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            System.out.println(mParam1);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main_fun_fragment, container, false);

        // 构建传递给服务端的参数字典
        Map<String, Object> dicParameters = new HashMap<String, Object>();
        dicParameters.put("dbname", mParam1);

        // 调用指定名称的云函数 averageStars，并且传递参数（默认不使用缓存）
        LCCloud.callFunctionInBackground("DB_Get_fun", dicParameters).subscribe(new Observer<Object>() {
            @Override
            public void onSubscribe(Disposable disposable) {

            }

            @Override
            public void onNext(Object object) {
                // succeed.
                JSONObject json = (JSONObject) JSONObject.toJSON(object);
                DataJSONArray = json.getJSONArray("data");
                mDataList = new ArrayList<String>() {
                    {
                        for (int i = 0; i < 50; i++) {
                            add(DataJSONArray.get(i).toString());
                        }
                    }
                };
            }

            @Override
            public void onError(Throwable throwable) {
                // failed.
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
                convertView = View.inflate(getContext(), R.layout.fragment_main_fun_style, null);
                viewHolder = new ViewHolder();
                viewHolder.textView = (TextView) convertView.findViewById(R.id.text);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.textView.setText(mDataList.get(position));

            return convertView;
        }
    }

    class ViewHolder {
        public TextView textView;
    }
}