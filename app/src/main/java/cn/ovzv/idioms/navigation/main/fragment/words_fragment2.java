package cn.ovzv.idioms.navigation.main.fragment;

import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.iflytek.cloud.SpeechSynthesizer;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.leancloud.LCCloud;
import cn.ovzv.idioms.R;
import cn.ovzv.idioms.help.SideslipListView;
import cn.ovzv.idioms.help.TtsSettings;
import cn.ovzv.idioms.navigation.me.fragment.message_fragment1;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link words_fragment2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class words_fragment2 extends Fragment {

    private SideslipListView mSideslipListView;
    private ArrayList<String> mDataList_word,mDataList_pinyin,mDataList_text;
    private JSONArray DataJSONArray;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public words_fragment2() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment words_fragment2.
     */
    // TODO: Rename and change types and number of parameters
    public static words_fragment2 newInstance(String param1, String param2) {
        words_fragment2 fragment = new words_fragment2();
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
        View view = inflater.inflate(R.layout.fragment_main_words_fragment2, container, false);



        // 构建传递给服务端的参数字典
        Map<String, Object> dicParameters = new HashMap<String, Object>();
        dicParameters.put("tag", 1);
        dicParameters.put("UserID", "61936fa79ba582465b45d312");

        // 调用指定名称的云函数 averageStars，并且传递参数（默认不使用缓存）
        LCCloud.callFunctionInBackground("DB_Get_word", dicParameters).subscribe(new Observer<Object>() {
            @Override
            public void onSubscribe(Disposable disposable) {

            }

            @Override
            public void onNext(Object object) {
                // succeed.
                JSONObject json = (JSONObject) JSONObject.toJSON(object);


                DataJSONArray = json.getJSONArray("data");

                System.out.println(DataJSONArray.getJSONObject(0).toString());

                System.out.println(DataJSONArray.getJSONObject(0).getString("uuid"));


                mDataList_word = new ArrayList<String>() {
                    {
                        for (int i = 0; i < DataJSONArray.size(); i++) {
                            add(DataJSONArray.getJSONObject(i).getString("word"));
                        }
                    }
                };
                mDataList_pinyin = new ArrayList<String>() {
                    {
                        for (int i = 0; i < DataJSONArray.size(); i++) {
                            add(DataJSONArray.getJSONObject(i).getString("pinyin"));
                        }
                    }
                };
                mDataList_text = new ArrayList<String>() {
                    {
                        for (int i = 0; i < DataJSONArray.size(); i++) {
                            add(DataJSONArray.getJSONObject(i).getString("explanation"));
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
            return mDataList_word.size();
        }

        @Override
        public Object getItem(int position) {
            return mDataList_word.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (null == convertView) {
                convertView = View.inflate(getContext(), R.layout.fragment_main_words_style2, null);
                viewHolder = new ViewHolder();
                viewHolder.word = (TextView) convertView.findViewById(R.id.words);
                viewHolder.pinyin = (TextView) convertView.findViewById(R.id.pingyin);
                viewHolder.texts = (TextView) convertView.findViewById(R.id.text);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            // 楷体
            AssetManager mgr = getActivity().getAssets();
            Typeface tf = Typeface.createFromAsset(mgr, "fonts/kaiti_GB2312.ttf");

            viewHolder.word.setText(mDataList_word.get(position));
            viewHolder.word.setTypeface(tf, Typeface.BOLD);
            viewHolder.pinyin.setText("【"+mDataList_pinyin.get(position)+"】");
            viewHolder.texts.setText("\t\t"+mDataList_text.get(position));

            return convertView;
        }
    }

    class ViewHolder {
        public TextView word;
        public TextView pinyin;
        public TextView texts;
    }

}