package cn.ovzv.idioms.navigation.me.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import cn.ovzv.idioms.R;
import cn.ovzv.idioms.help.SideslipListView;
import cn.ovzv.idioms.navigation.me.Me_card;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link message_fragment1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class message_fragment1 extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

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

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public message_fragment1() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment message_fragment1.
     */
    // TODO: Rename and change types and number of parameters
    public static message_fragment1 newInstance(String param1, String param2) {
        message_fragment1 fragment = new message_fragment1();
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
        View view = inflater.inflate(R.layout.fragment_me_message_fragment1, container, false);

        mSideslipListView = (SideslipListView) view.findViewById(R.id.sideslipListView);
        mSideslipListView.setAdapter(new CustomAdapter());//设置适配器


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
                convertView = View.inflate(getContext(), R.layout.fragment_me_message_style, null);
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