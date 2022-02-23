package cn.ovzv.idioms.navigation;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.dogecloud.support.DogeInclude;
import com.dogecloud.support.DogeInfoManager;
import com.dogecloud.support.VideoInfo;

import java.util.LinkedHashMap;

import cn.ovzv.idioms.R;
import cn.ovzv.idioms.navigation.course.course_video;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Course#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Course extends Fragment {
    public Button listview_bt;
    EditText UserId_et;
    EditText vCode_et;
    TextView info_tv;
    ImageView thumb_iv;


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


        listview_bt = (Button) view.findViewById(R.id.listview_btn);
        listview_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), course_video.class);
                startActivity(intent);
            }
        });


        UserId_et = view.findViewById(R.id.UserId_et);
        vCode_et = view.findViewById(R.id.vCode_et);
        info_tv = view.findViewById(R.id.info_tv);
        thumb_iv = view.findViewById(R.id.thumb_iv);
        view.findViewById(R.id.info_get_btn).setOnClickListener(views -> {
            DogeInfoManager.addDogeInfoRequest(Integer.parseInt(UserId_et.getText().toString()),vCode_et.getText().toString(), DogeInclude.INFO_GET_BASEINFO,InfoListener);
        });

        return view;
    }
    private DogeInfoManager.infoListener InfoListener = new DogeInfoManager.infoListener() {
        String Info = "";
        @Override
        public void onInfoUpdate(int type,String vcode,DogeInfoManager.InfoData infoData) {
            switch (type){
                case DogeInclude.INFO_GET_BASEINFO:
                    DogeInfoManager.addDogeInfoRequest(Integer.parseInt(UserId_et.getText().toString()),vCode_et.getText().toString(), DogeInclude.INFO_GET_THUMB,InfoListener);
                    Info = "";
                    Info += "\tVideoName:" + infoData.getVideoName() + "\n";
                    Info += "\tDuration:"+ infoData.getVideoDuration() + "\n";
                    Info += "\tVtypes:\n";
                    Info += "\tDefalutVtypes:"+infoData.getDefaultVtype()+"\n";
                    LinkedHashMap<Integer, VideoInfo> videos = infoData.getVideos();
                    for (Integer key : videos.keySet()) {
                        VideoInfo vi = videos.get(key);
                        Info+="\t\tvtype:"+vi.vtype+"  ---->>\n";
                        Info+="\t\t\tName:"+vi.name+"\n";
                        Info+="\t\t\tFormat:"+vi.format+"\n";
                        Info+="\t\t\tSize:"+vi.size+"\n";
                        Info+="\t\t\tisLocal:"+vi.isLocal+"\n";
                        //Info+="\t\tUrl:"+vi.url+"\n";
                    }
                    info_tv.setText(Info);
                    break;
                case DogeInclude.INFO_GET_THUMB:
                    thumb_iv.setImageBitmap(infoData.getThumb());
                    break;
            }
        }
    };

}