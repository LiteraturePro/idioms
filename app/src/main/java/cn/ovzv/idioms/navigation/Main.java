package cn.ovzv.idioms.navigation;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.king.app.dialog.AppDialog;
import com.king.app.dialog.AppDialogConfig;
import com.king.app.updater.AppUpdater;
import com.king.app.updater.UpdateConfig;
import com.king.app.updater.callback.UpdateCallback;
import com.king.app.updater.constant.Constants;
import com.king.app.updater.http.OkHttpManager;

import org.w3c.dom.Text;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import cn.leancloud.LCCloud;
import cn.leancloud.LCInstallation;
import cn.leancloud.LCObject;
import cn.leancloud.LCUser;
import cn.ovzv.idioms.MainActivity;
import cn.ovzv.idioms.R;
import cn.ovzv.idioms.help.ApkUtils;
import cn.ovzv.idioms.help.BSPatchUtil;
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

    private TextView Studyset,Couplet,Fun,News,Text,Time,News_src,word_do,word_no,word_study,word_fuxi,diyiword,dierword;
    private Button Words,Study,Game1,Game2,Game3;
    private ImageView Image;
    private AppUpdater mAppUpdater;
    private ProgressBar progressBar;
    private Toast toast;
    private final Object mLock = new Object();
    private Typeface tf;
    private LinearLayout mLinearLayout;//对应于主布局中用来添加子布局的View
    private View mGridView;// 子Layout要以view的形式加入到主Layout
    private static String TAG = MainActivity.class.getSimpleName();
    private TextView tvProgress;

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("bspatch");
    }


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


                if(Version_json.getString("Version").equals(packageName(getContext()))){

                }else{
                    AppDialogConfig config = new AppDialogConfig(getContext(),R.layout.fragment_main_version_dialog);
                    config.setConfirm("立即升级")
                            .setHideCancel(true)
                            .setTitle("版本更新啦!")
                            .setContent(Version_json.getString("Text"))
                            .setOnClickConfirm(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    UpdateConfig configs = new UpdateConfig();
                                    configs.setUrl(Version_json.getString("Apk_Url"));
                                    //configs.setUrl("https://lc-gluttony.s3.amazonaws.com/9WTW1sBG7ANz/nztLwGjPfFJhF9n268OHGr9MWNpYxaVi/app_patch.patch");
                                    configs.setInstallApk(false);
                                    configs.setFilename("new.patch");
                                    configs.addHeader("token","xxxxxx");
                                    mAppUpdater = new AppUpdater(getContext(),configs)
                                            .setHttpManager(OkHttpManager.getInstance())
                                            .setUpdateCallback(new UpdateCallback() {

                                                @Override
                                                public void onDownloading(boolean isDownloading) {
                                                    if(isDownloading){
                                                        showToast("已经在下载中,请勿重复下载。");
                                                    }else{
                                                        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_progress,null);
                                                        tvProgress = view.findViewById(R.id.tvProgress);
                                                        progressBar = view.findViewById(R.id.progressBar);
                                                        AppDialog.INSTANCE.showDialog(getContext(),view,false);
                                                    }
                                                }

                                                @Override
                                                public void onStart(String url) {
                                                    updateProgress(0,100);
                                                }

                                                @Override
                                                public void onProgress(long progress, long total, boolean isChange) {
                                                    if(isChange){
                                                        updateProgress(progress,total);
                                                    }
                                                }

                                                @Override
                                                public void onFinish(File file) {
                                                    AppDialog.INSTANCE.dismissDialog();
                                                    showToast("下载完成");
                                                    Install_Apk();

                                                }

                                                @Override
                                                public void onError(Exception e) {
                                                    AppDialog.INSTANCE.dismissDialog();
                                                    showToast("下载失败");
                                                }

                                                @Override
                                                public void onCancel() {
                                                    AppDialog.INSTANCE.dismissDialog();
                                                    showToast("取消下载");
                                                }
                                            });
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
        News_src = (TextView)view.findViewById(R.id.new_src);
        word_do = (TextView) view.findViewById(R.id.word_do);
        word_no = (TextView) view.findViewById(R.id.word_no);
        word_fuxi =(TextView) view.findViewById(R.id.word_fuxi);
        word_study = (TextView)view.findViewById(R.id.word_study);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar_healthy);
        diyiword = (TextView) view.findViewById(R.id.diyi_word);
        dierword = (TextView) view.findViewById(R.id.dierword);

        mLinearLayout = (LinearLayout) view.findViewById(R.id.study_words);

        AssetManager mgr = getActivity().getAssets();
        tf = Typeface.createFromAsset(mgr, "fonts/kaiti_GB2312.ttf");

        dierword.setTypeface(tf, Typeface.BOLD);
        diyiword.setTypeface(tf, Typeface.BOLD);

        char left_str[] = "成语文化".toCharArray();//利用toCharArray方法转换

        for (int j = 0; j < left_str.length; j++) {
            System.out.println(left_str[j]);
            mGridView = View.inflate(getContext(), R.layout.fragment_main_study_words, null);
            TextView txt = mGridView.findViewById(R.id.words_text);
            txt.setText(String.valueOf(left_str[j]));
            txt.setTextSize(40);
            txt.setTypeface(tf);
            mLinearLayout.addView(mGridView, 140, 140);
        }


        //获取Sp对象
        //参数一 文件名   参数二  模式（固定写法）
        SharedPreferences sp = getActivity().getSharedPreferences("words", Context.MODE_PRIVATE);

        //点击按钮吐司一下内容
        word_fuxi.setText(String.valueOf(sp.getInt("new_words", 20)));
        word_study.setText(String.valueOf(sp.getInt("new_words", 20)));

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
                News_src.setText(DataJSONArray.getJSONObject(0).getString("Src"));
                Image.setImageBitmap(GetHttpBitmap.getHttpBitmap(DataJSONArray.getJSONObject(0).getString("Image")));

                // 构建传递给服务端的参数字典
                LCUser currentUser = LCUser.getCurrentUser();
                Map<String, Object> dicParameters = new HashMap<String, Object>();
                dicParameters.put("UserID", currentUser.getObjectId());

                // 调用指定名称的云函数 averageStars，并且传递参数（默认不使用缓存）
                LCCloud.callFunctionInBackground("Get_word_do", dicParameters).subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {

                    }

                    @Override
                    public void onNext(Object object) {
                        // succeed.
                        System.out.println(object);
                        JSONObject json = (JSONObject) JSONObject.toJSON(object);
                        word_do.setText("已完成"+json.getString("do_bili")+"%");
                        word_no.setText(json.getString("no"));
                        progressBar.setProgress(Math.round(json.getIntValue("do_bili")));


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
    public static String packageName(Context context) {
        PackageManager manager = context.getPackageManager();
        String name = null;
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            name = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return name;
    }
    @Override
    public void onResume() {
        super.onResume();

        //参数一 文件名   参数二  模式（固定写法）
        SharedPreferences sp = getActivity().getSharedPreferences("words", Context.MODE_PRIVATE);

        //点击按钮吐司一下内容
        word_fuxi.setText(String.valueOf(sp.getInt("new_words", 20)));
        word_study.setText(String.valueOf(sp.getInt("new_words", 20)));


    }
    //

    public void showToast(String text){
        if(toast == null){
            synchronized (mLock){
                if(toast == null){
                    toast = Toast.makeText(getContext(),text,Toast.LENGTH_SHORT);
                }
            }
        }
        toast.setText(text);
        toast.show();
    }
    private void updateProgress(long progress, long total){
        if(tvProgress == null || progressBar == null){
            return;
        }
        if(progress > 0){
            int currProgress = (int)(progress * 1.0f / total * 100.0f);
            tvProgress.setText(getString(R.string.app_updater_progress_notification_content) + currProgress + "%");
            progressBar.setProgress(currProgress);
            Log.d(TAG,String.format("onProgress:%d/%d | %d%%",progress,total,currProgress));
        }else{
            tvProgress.setText(getString(R.string.app_updater_start_notification_content));
        }

    }

    /**
     * 获取缓存路径
     * @param context
     * @return
     */
    private String getExternalFilesDir(Context context) {
        File[] files = ContextCompat.getExternalFilesDirs(context, Constants.DEFAULT_DIR);
        if(files!=null && files.length > 0){
            return files[0].getAbsolutePath();
        }
        return context.getExternalFilesDir(Constants.DEFAULT_DIR).getAbsolutePath();

    }
    private boolean Install_Apk(){
        String OldApk = getActivity().getApplicationContext().getPackageResourcePath();
        String patchDir = getExternalFilesDir(getContext()) +"/new.patch";
        String NewApk = getExternalFilesDir(getContext()) +"/new.apk";
        Log.d(TAG,"开始合并");
        int ret = BSPatchUtil.bspatch(OldApk, NewApk,patchDir);
        Log.d(TAG,"开始完成");

        if (ret == 0) {
            File mFile = new File(getExternalFilesDir(getContext()),"new.apk");
            String authority = getContext().getPackageName() + Constants.DEFAULT_FILE_PROVIDER;

            ApkUtils.installApk(getContext(),mFile, authority);
            //File mFile = new File(path,filename);
//            if(mFile.exists()) {//文件是否存在)
//            }
            return true;
        } else {
            return false;
        }
    }
}