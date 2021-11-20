package cn.ovzv.idioms.navigation;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.king.app.dialog.AppDialog;
import com.king.app.dialog.AppDialogConfig;
import com.king.app.updater.AppUpdater;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

import cn.leancloud.LCCloud;
import cn.leancloud.LCInstallation;
import cn.leancloud.LCObject;
import cn.ovzv.idioms.R;
import cn.ovzv.idioms.help.GetHttpBitmap;
import cn.ovzv.idioms.navigation.main.Main_couplet;
import cn.ovzv.idioms.navigation.main.Main_fun;
import cn.ovzv.idioms.navigation.main.Main_game1;
import cn.ovzv.idioms.navigation.main.Main_game2;
import cn.ovzv.idioms.navigation.main.Main_game3;
import cn.ovzv.idioms.navigation.main.Main_news;
import cn.ovzv.idioms.navigation.main.Main_study;
import cn.ovzv.idioms.navigation.main.Main_studyset;
import cn.ovzv.idioms.navigation.main.Main_words;
import cn.ovzv.idioms.tts;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Main#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Main extends Fragment {

    private TextView Studyset,Couplet,Fun,News,Text,Time;
    private Button Words,Study,Game1,Game2,Game3;
    private ImageView Image;
    private AppUpdater mAppUpdater;


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

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        // 获取设备信息保存云端
        LCInstallation.getCurrentInstallation().saveInBackground().subscribe(new Observer<LCObject>() {
            @Override
            public void onSubscribe(Disposable d) {
            }
            @Override
            public void onNext(LCObject avObject) {
                // 关联 installationId 到用户表等操作。
                String installationId = LCInstallation.getCurrentInstallation().getInstallationId();
                System.out.println("保存成功：" + installationId );
            }
            @Override
            public void onError(Throwable e) {
                System.out.println("保存失败，错误信息：" + e.getMessage());
            }
            @Override
            public void onComplete() {
            }
        });

        // 获取版本更新信息
        Map<String, Object> Version = new HashMap<String, Object>();
        Version.put("Version", "1.0");

        // 调用指定名称的云函数 averageStars，并且传递参数（默认不使用缓存）
        LCCloud.callFunctionInBackground("Version_Get", Version).subscribe(new Observer<Object>() {
            @Override
            public void onSubscribe(Disposable disposable) {

            }

            @Override
            public void onNext(Object object) {
                // succeed.
                JSONObject Version_json = (JSONObject) JSONObject.toJSON(object);

                JSONArray Version_DataJSONArray = Version_json.getJSONArray("data");

                if(Version_DataJSONArray.getJSONObject(0).getString("New_Version").equals(Version_DataJSONArray.getJSONObject(0).getString("Old_Version"))){

                }else{
                    AppDialogConfig config = new AppDialogConfig(getContext(),R.layout.fragment_main_version_dialog);
                    config.setConfirm("升级")
                            .setHideCancel(true)
                            .setTitle("简单自定义弹框升级")
                            .setContent("1、新增某某功能、\n2、修改某某问题、\n3、优化某某BUG、")
                            .setOnClickConfirm(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mAppUpdater = new AppUpdater.Builder()
                                            .setUrl(Version_DataJSONArray.getJSONObject(0).getString("Apk_Url"))
                                            .build(getContext());
                                    mAppUpdater.start();
                                    AppDialog.INSTANCE.dismissDialog();
                                }
                            });
                    AppDialog.INSTANCE.showDialog(config);
                }
            }
            @Override
            public void onError(Throwable throwable) {
                // failed.
            }
            @Override
            public void onComplete() {

            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        News = (TextView) view.findViewById(R.id.news);
        Time = (TextView) view.findViewById(R.id.new_time);
        Image = (ImageView) view.findViewById(R.id.new_img);
        Text = (TextView)view.findViewById(R.id.new_text);


        // 构建传递给服务端的参数字典
        Map<String, Object> NewsData = new HashMap<String, Object>();
        // dicParameters.put("movie", "夏洛特烦恼");

        // 调用指定名称的云函数 averageStars，并且传递参数（默认不使用缓存）
        LCCloud.callFunctionInBackground("News_Get", NewsData).subscribe(new Observer<Object>() {
            @Override
            public void onSubscribe(Disposable disposable) {

            }

            @Override
            public void onNext(Object object) {
                // succeed.
                System.out.println(object);
                JSONObject json = (JSONObject) JSONObject.toJSON(object);

                JSONArray DataJSONArray = json.getJSONArray("data");

                News.setText(DataJSONArray.getJSONObject(0).getString("Title"));
                Text.setText(DataJSONArray.getJSONObject(0).getString("Text"));
                Time.setText(DataJSONArray.getJSONObject(0).getString("Time"));
                Image.setImageBitmap(GetHttpBitmap.getHttpBitmap(DataJSONArray.getJSONObject(0).getString("Image")));

            }
            @Override
            public void onError(Throwable throwable) {
                // failed.
            }

            @Override
            public void onComplete() {

                // 新闻详情
                News.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), Main_news.class);
                        startActivity(intent);
                    }
                });

            }
        });

        return view;
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
                Intent intent = new Intent(getActivity(), Main_words.class);
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