package cn.ovzv.idioms.navigation;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import cn.leancloud.LCUser;
import cn.ovzv.idioms.Login;
import cn.ovzv.idioms.R;
import cn.ovzv.idioms.help.CircleImageView;
import cn.ovzv.idioms.help.GetHttpBitmap;
import cn.ovzv.idioms.navigation.me.Me_about;
import cn.ovzv.idioms.navigation.me.Me_card;
import cn.ovzv.idioms.navigation.me.Me_collection;
import cn.ovzv.idioms.navigation.me.Me_feedback;
import cn.ovzv.idioms.navigation.me.Me_footprint;
import cn.ovzv.idioms.navigation.me.Me_message;
import cn.ovzv.idioms.navigation.me.Me_mine;
import cn.ovzv.idioms.navigation.me.Me_setting;
import cn.ovzv.idioms.navigation.me.Me_subscribe;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Me#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Me extends Fragment {

    private ImageView Me,mImageView_1,mImageView_2,mImageView_3,mImageView_4;
    private ImageView ICO,mImageView_5,mImageView_6,mImageView_7,mImageView_8;
    private TextView Name, Phone;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Me() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Me.
     */
    // TODO: Rename and change types and number of parameters
    public static Me newInstance(String param1, String param2) {
        Me fragment = new Me();
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
        View view = inflater.inflate(R.layout.fragment_me, container, false);

        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        Me = (ImageView) getActivity().findViewById(R.id.me);
        mImageView_1 = (ImageView)getActivity().findViewById(R.id.circleImageView1);
        mImageView_2 = (ImageView)getActivity().findViewById(R.id.circleImageView2);
        mImageView_3 = (ImageView)getActivity().findViewById(R.id.circleImageView3);
        mImageView_4 = (ImageView)getActivity().findViewById(R.id.circleImageView4);
        mImageView_5 = (ImageView)getActivity().findViewById(R.id.circleImageView5);
        mImageView_6 = (ImageView)getActivity().findViewById(R.id.circleImageView6);
        mImageView_7 = (ImageView)getActivity().findViewById(R.id.circleImageView7);
        mImageView_8 = (ImageView)getActivity().findViewById(R.id.circleImageView8);


        Name = (TextView) getActivity().findViewById(R.id.name);
        Phone = (TextView) getActivity().findViewById(R.id.phone);
        ICO = (CircleImageView) getActivity().findViewById(R.id.touxiang);

        /**
         * 判定用户登录状态
         */
        LCUser currentUser = LCUser.getCurrentUser();
        if (currentUser != null) {
            // 跳到首页
            if (currentUser.getMobilePhoneNumber() == null){
                Phone.setText("暂未设置号码");
            }else{
                Phone.setText(currentUser.getMobilePhoneNumber());
            }
            Name.setText(currentUser.getUsername());
            currentUser.getObjectId();






        } else {
            // 显示注册或登录页面
//            Intent intent = new Intent(getActivity(), Login.class);
//            startActivity(intent);

        }




        Name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Login.class);
                startActivity(intent);
            }
        });

        Me.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Me_mine.class);
                startActivity(intent);
            }
        });
        mImageView_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), Me_message.class);
                startActivity(intent);
            }
        });
        mImageView_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), Me_subscribe.class);
                startActivity(intent);
            }
        });
        mImageView_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), Me_collection.class);
                startActivity(intent);
            }
        });
        mImageView_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), Me_footprint.class);
                startActivity(intent);
            }
        });

        mImageView_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), Me_about.class);
                startActivity(intent);
            }
        });
        mImageView_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), Me_card.class);
                startActivity(intent);
            }
        });
        mImageView_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), Me_feedback.class);
                startActivity(intent);
            }
        });
        mImageView_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), Me_setting.class);
                startActivity(intent);
            }
        });
    }
}