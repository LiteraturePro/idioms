package cn.ovzv.idioms.navigation.course;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.dogecloud.support.DogeManager;

import java.util.ArrayList;
import java.util.List;

import cn.ovzv.idioms.R;
import cn.ovzv.idioms.adapter.DogeLisAdapter;
import cn.ovzv.idioms.help.DogeVideo;
import cn.ovzv.idioms.help.ScrollDogePlayer;
import cn.ovzv.idioms.help.ScrollHelper;

public class course_video extends AppCompatActivity {
    private ListView LsView;
    private List<DogeVideo> DogeVideoList = new ArrayList<>();
    private int firstVisibleItem, lastVisibleItem, visibleCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_course_video);

        TestVideo();
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

    private void TestVideo() {
        DogeVideoList.add(new DogeVideo(1,"baa45d1691de80e5"));
        DogeVideoList.add(new DogeVideo(1,"0cd2be1e5895ae2a"));
        DogeVideoList.add(new DogeVideo(1,"ab0bffe5ea1c1cd2"));
        DogeVideoList.add(new DogeVideo(1,"49c9e10ae2f05aaf"));
        DogeVideoList.add(new DogeVideo(1,"80054e07a2556e52"));
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