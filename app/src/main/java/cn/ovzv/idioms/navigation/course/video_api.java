package cn.ovzv.idioms.navigation.course;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import cn.ovzv.idioms.R;
import android.content.Intent;
import com.dogecloud.support.DogeInclude;
import com.dogecloud.support.DogeManager;
import com.dogecloud.support.DogeMediaPlayer;
import com.dogecloud.support.DogePlayer;

public class video_api extends AppCompatActivity {
    DogePlayer dgplayer;
    DogeMediaPlayer mMediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_course_video_api);
        Intent intent = getIntent();
        int userid = intent.getIntExtra("userId",0);
        String vcode = intent.getStringExtra("vcode");
        if(userid == 0) {userid = 1;vcode = "baa45d1691de80e5";}

        int vtype = intent.getIntExtra("vtype",0);
        boolean offlinePlay = intent.getBooleanExtra("offline",false);
        boolean autoPlay = intent.getBooleanExtra("autoPlay",false);


        dgplayer = findViewById(R.id.api_dgplayer);
        mMediaPlayer = dgplayer.getMediaPlayer();
        mMediaPlayer.setSources(userid,vcode);
        if(vtype != 0) mMediaPlayer.setConfig(DogeInclude.CONFIG_VTYPE,String.valueOf(vtype));
        if(offlinePlay) mMediaPlayer.setConfig(DogeInclude.CONFIG_OFFLINEPLAY,true);
        if(!autoPlay) mMediaPlayer.setConfig(DogeInclude.CONFIG_AUTOPLAY,false);
        mMediaPlayer.Init();
        findViewById(R.id.start_btn).setOnClickListener(view->mMediaPlayer.start());
        findViewById(R.id.pause_btn).setOnClickListener(view->mMediaPlayer.pause());
        findViewById(R.id.toggle_btn).setOnClickListener(view->mMediaPlayer.toggle());
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
    }

    @Override
    public void onBackPressed() {
        if(DogeManager.onBackPressed(this)) return; super.onBackPressed();
    }
}
