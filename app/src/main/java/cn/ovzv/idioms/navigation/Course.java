package cn.ovzv.idioms.navigation;


import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import cn.leancloud.LCCloud;
import cn.leancloud.LCUser;
import cn.ovzv.idioms.Login;
import cn.ovzv.idioms.R;
import cn.ovzv.idioms.adapter.MsgAdapter;
import cn.ovzv.idioms.help.Msg;
import cn.ovzv.idioms.help.MsgLab;
import cn.ovzv.idioms.navigation.course.course_video;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Course#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Course extends Fragment {
    public Button listview_bt;
    /**
     * 初始化数据
     */
    private JSONArray DataJSONArray;
    private GridView mLvMsgList;
    private List<Msg> mDatas = new ArrayList<>();
    private MsgAdapter mAdapter;
    private Typeface tf;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Course() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Course.
     */
    // TODO: Rename and change types and number of parameters
    public static Course newInstance(String param1, String param2) {
        Course fragment = new Course();
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
        View view = inflater.inflate(R.layout.fragment_course, container, false);

        TextView textView10 = (TextView)view.findViewById(R.id.textView10);

        AssetManager mgr = getActivity().getAssets();
        tf = Typeface.createFromAsset(mgr, "fonts/kaiti_GB2312.ttf");

        textView10.setTypeface(tf, Typeface.BOLD);



        // 检测用户是否登录
        LCUser currentUser = LCUser.getCurrentUser();
        if (currentUser != null) {
            /** 设置请求参数*/
            Map<String, Object> dicParameters = new HashMap<>();
            dicParameters.put("UserID", currentUser.getObjectId());
            /** 调用云函数*/
            LCCloud.callFunctionInBackground("Get_all_video_list", dicParameters).subscribe(new Observer<Object>() {
                @Override
                public void onSubscribe(Disposable disposable) {

                }
                @Override
                public void onNext(Object object) {
                    JSONObject json = (JSONObject) JSONObject.toJSON(object);
                    DataJSONArray = json.getJSONArray("data");
                    Log.d("DataJSONArray",DataJSONArray.toJSONString());
                    mLvMsgList = view.findViewById(R.id.id_lv_msgList);


                    // 设置纵向数据
                    //mDatas.addAll(MsgLab.generateMockList(C_Name,C_Pic1));
                    mDatas.addAll(MsgLab.generateMockList(DataJSONArray));

                    mAdapter = new MsgAdapter(getActivity(), mDatas);
                    mLvMsgList.setAdapter(mAdapter);
                    // 纵向的gridview的item的点击事件
                    mLvMsgList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Log.e("onItemClick", mDatas.get(position).getTitle());
                            //可以把要传递的参数放到一个bundle里传递过去，bumdle可以看做一个特殊的map。
                            Bundle bundle = new Bundle() ;


                            Intent intent = new Intent(getActivity(), course_video.class);
                            JSONObject Data = DataJSONArray.getJSONObject(position);
                            bundle.putString("cid",Data.getString("videocid"));
                            intent.putExtras(bundle) ;

                            startActivity(intent);
                        }
                    });

                }
                @Override
                public void onError(Throwable throwable) {
                    // failed.
                    Log.d("error",throwable.toString());
                }
                @Override
                public void onComplete() {

                }
            });
        } else {
            // 显示注册或登录页面
            Intent intent = new Intent(getActivity(), Login.class);
            startActivity(intent);
        }

        return view;
    }

}