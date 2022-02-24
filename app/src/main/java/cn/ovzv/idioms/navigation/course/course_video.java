package cn.ovzv.idioms.navigation.course;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dogecloud.support.DogeManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.leancloud.LCCloud;
import cn.ovzv.idioms.R;
import cn.ovzv.idioms.adapter.DogeLisAdapter;
import cn.ovzv.idioms.adapter.MsgAdapter;
import cn.ovzv.idioms.help.DogeVideo;
import cn.ovzv.idioms.help.MsgLab;
import cn.ovzv.idioms.help.ScrollDogePlayer;
import cn.ovzv.idioms.help.ScrollHelper;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class course_video extends AppCompatActivity {
    private ListView LsView;
    private List<DogeVideo> DogeVideoList = new ArrayList<>();
    private int firstVisibleItem, lastVisibleItem, visibleCount;
    private JSONArray DataJSONArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_course_video);

        /**
         * 获取数据
         */
        Intent intent = getIntent();
        Map<String, Object> dicParameters = new HashMap<>();
        dicParameters.put("videocid", intent.getStringExtra("cid"));
        /** 调用云函数*/
        LCCloud.callFunctionInBackground("Get_video_list", dicParameters).subscribe(new Observer<Object>() {
            @Override
            public void onSubscribe(Disposable disposable) {

            }
            @Override
            public void onNext(Object object) {
                JSONObject json = (JSONObject) JSONObject.toJSON(object);
                DataJSONArray = json.getJSONArray("data");
                TestVideo(DataJSONArray);




            }
            @Override
            public void onError(Throwable throwable) {
                // failed.
                Log.d("error",throwable.toString());
            }
            @Override
            public void onComplete() {

                LsView = findViewById(R.id.LsView);
                DogeLisAdapter adapter = new DogeLisAdapter(course_video.this,R.layout.dogeplayer_item, DogeVideoList);
                LsView.setOnScrollListener(new AbsListView.OnScrollListener() {
                    boolean Inited = false;
                    @Override
                    public void onScrollStateChanged(AbsListView listview, int scrollState) {
                        switch (scrollState) {
                            case SCROLL_STATE_IDLE: //滚动停止
                                ScrollHelper.onScrolled(course_video.this, listview,firstVisibleItem, lastVisibleItem);
                                autoPlayVideo(listview);
                                break;
                        }
                    }

                    @Override
                    public void onScroll(AbsListView listview, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                        course_video.this.firstVisibleItem = firstVisibleItem;
                        lastVisibleItem = visibleItemCount + firstVisibleItem;
                        visibleCount = visibleItemCount;
                        if(!Inited && visibleCount!=0){ScrollHelper.onScrolled(course_video.this, listview,firstVisibleItem, lastVisibleItem);Inited = true;}
                    }
                });
                LsView.setRecyclerListener(new AbsListView.RecyclerListener() {
                    @Override
                    public void onMovedToScrapHeap(View view) {
                        ScrollDogePlayer dgplayer  = (ScrollDogePlayer) view.findViewById(R.id.list_dgplayer);
                        dgplayer.setNeedReset();
                    }
                });
                LsView.setAdapter(adapter);

            }
        });



    }

    private void autoPlayVideo(AbsListView view) {
        boolean found = false;
        for (int i = 0; i < visibleCount; i++) {
            if (view != null && view.getChildAt(i) != null && view.getChildAt(i).findViewById(R.id.list_dgplayer) != null) {
                ScrollDogePlayer dgplayer  = (ScrollDogePlayer) view.getChildAt(i).findViewById(R.id.list_dgplayer);
                Rect rect = new Rect();
                //获取当前view 的 位置
                dgplayer.getLocalVisibleRect(rect);
                int videoheight = dgplayer.getHeight();
                if(!found) {
                    if (rect.top == 0 && rect.bottom == videoheight) {
                        dgplayer.scrollAutoStart(true);
                        found = true;
                    }
                } else dgplayer.scrollAutoStart(false);
            }
        }
    }

    private void TestVideo(JSONArray jsonArray) {
        for(int i=0;i<jsonArray.size();i++) {
            System.out.println(jsonArray.get(i));
            DogeVideoList.add(new DogeVideo(1670,jsonArray.get(i).toString()));
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        DogeManager.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        DogeManager.onResume(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DogeManager.onDestroy(this);
        ScrollHelper.onListDestory(this,LsView);
    }
}