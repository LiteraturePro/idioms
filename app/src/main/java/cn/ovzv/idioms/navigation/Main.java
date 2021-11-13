package cn.ovzv.idioms.navigation;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import cn.ovzv.idioms.R;
import cn.ovzv.idioms.navigation.main.Main_couplet;
import cn.ovzv.idioms.navigation.main.Main_fun;
import cn.ovzv.idioms.navigation.main.Main_game1;
import cn.ovzv.idioms.navigation.main.Main_game2;
import cn.ovzv.idioms.navigation.main.Main_game3;
import cn.ovzv.idioms.navigation.main.Main_study;
import cn.ovzv.idioms.navigation.main.Main_studyset;
import cn.ovzv.idioms.navigation.main.Main_words;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Main#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Main extends Fragment {

    private TextView Studyset,Couplet,Fun;
    private Button Words,Study,Game1,Game2,Game3;




    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Main() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Main.
     */
    // TODO: Rename and change types and number of parameters
    public static Main newInstance(String param1, String param2) {
        Main fragment = new Main();
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
        return inflater.inflate(R.layout.fragment_main, container, false);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);

        // 学习设置
        Studyset = (TextView) getActivity().findViewById(R.id.studyset);
        Studyset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Main_studyset.class);
                startActivity(intent);

            }
        });
        // 查看词表
        Words = (Button) getActivity().findViewById(R.id.words);
        Words.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),Main_words.class);
                startActivity(intent);
            }
        });
        // 开始学习
        Study = (Button) getActivity().findViewById(R.id.study);
        Study.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Main_study.class);
                startActivity(intent);
            }
        });
        // 成语接龙
        Game1 = (Button) getActivity().findViewById(R.id.game1);
        Game1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Main_game1.class);
                startActivity(intent);
            }
        });
        // 看文字猜成语
        Game2 = (Button) getActivity().findViewById(R.id.game2);
        Game2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(getActivity(), Main_game2.class);
                startActivity(intent);
            }
        });
        // 看图猜成语
        Game3 = (Button) getActivity().findViewById(R.id.game3);
        Game3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Main_game3.class);
                startActivity(intent);
            }
        });

        // 成语对联
        Couplet = (TextView)getActivity().findViewById(R.id.couplet);
        Couplet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Main_couplet.class);
                startActivity(intent);
            }
        });

        // 有趣的成语
        Fun = (TextView) getActivity().findViewById(R.id.fun);
        Fun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Main_fun.class);
                startActivity(intent);
            }
        });



    }
}