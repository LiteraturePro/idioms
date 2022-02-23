package cn.ovzv.idioms.help;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import cn.ovzv.idioms.R;
import cn.ovzv.idioms.navigation.course.video_api;

import com.dogecloud.support.DogeInclude;
import com.dogecloud.support.DmpStd;
import com.dogecloud.support.DogeMediaPlayer;
import com.dogecloud.support.MediaPlayerListener;

import java.util.List;


public class ScrollDogePlayer extends FrameLayout implements MediaPlayerListener, ScrollPlayer {
    DogeMediaPlayer mMediaPlayer;
    FrameLayout mController;
    View controllerView;
    ImageView iv_thumb;
    ImageView iv_play;
    TextView tv_title;
    DogeVideo videoInfo;
    public ScrollDogePlayer(@NonNull Context context) {
        super(context);
    }

    public ScrollDogePlayer(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollDogePlayer(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ScrollDogePlayer setVideoInfo(DogeVideo dv){
        videoInfo = dv;
        return this;
    }

    private DogeMediaPlayer getMediaPlayer(){
        if(mMediaPlayer == null) {
            mMediaPlayer = new DmpStd(getContext());
            mMediaPlayer.setDrawLayout(this);
            mMediaPlayer.setController(mController);
            mMediaPlayer.addOnVideoViewStateChangeListener(this);
        }
        return mMediaPlayer;
    }

    public void initControllerView()
    {
        mController = new FrameLayout(getContext());
        controllerView = LayoutInflater.from(mController.getContext()).inflate(R.layout.dgplayer_layout_min_controller, mController);
        iv_thumb = controllerView.findViewById(R.id.thumb);
        iv_play = controllerView.findViewById(R.id.play);
        tv_title = controllerView.findViewById(R.id.title);
        iv_thumb.setScaleType(ImageView.ScaleType.FIT_CENTER);
        iv_thumb.setVisibility(VISIBLE);
        iv_play.setVisibility(VISIBLE);
        tv_title.setVisibility(VISIBLE);
        iv_thumb.setOnClickListener(view ->{
            ScrollHelper.UIresetAll(getContext(),ScrollObject);
            if(mMediaPlayer!=null) {
                Intent intent = new Intent(ScrollDogePlayer.this.getContext(), video_api.class);
                intent.putExtra("userId",Integer.parseInt((String)mMediaPlayer.getConfig(DogeInclude.CONFIG_USERID)));
                intent.putExtra("vcode",(String)mMediaPlayer.getConfig(DogeInclude.CONFIG_VCODE));
                intent.putExtra("autoPlay",true);
                ScrollDogePlayer.this.getContext().startActivity(intent);
            }
        });
        mController.setOnClickListener(view -> {
            ScrollHelper.UIresetAll(getContext(),ScrollObject);
            if(mMediaPlayer != null) {
                Intent intent = new Intent(ScrollDogePlayer.this.getContext(), video_api.class);
                intent.putExtra("userId",Integer.parseInt((String)mMediaPlayer.getConfig(DogeInclude.CONFIG_USERID)));
                intent.putExtra("vcode",(String)mMediaPlayer.getConfig(DogeInclude.CONFIG_VCODE));
                intent.putExtra("autoPlay",true);
                ScrollDogePlayer.this.getContext().startActivity(intent);
            }
        });
        iv_play.setOnClickListener(view -> {
            if(mMediaPlayer!=null) mMediaPlayer.start();
        });
        isScrollInited = false;
    }

    public void refreshThumb(Bitmap Thumb)
    {
        if(Thumb!=null) {
            iv_thumb.setImageBitmap(Thumb);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        float height = width * 9 / 16;
        heightMeasureSpec = MeasureSpec.makeMeasureSpec((int) height, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public ScrollDogePlayer setAutoPlay(boolean autoPlay){
        this.autoPlay = autoPlay;
        return this;
    }

    private int ScrollPosition = -1;
    private Object ScrollObject;
    private boolean autoPlay = false;
    private boolean isScrollInited = false;
    private int[] ViewSize = new int[6];
    private boolean startAfterInited = false;

    public void initMediaPlayer(){
        if(!isScrollInited) {
            removeCallbacks(runScrollInit);
            postDelayed(runScrollInit,400);
        }
    }

    private Runnable runScrollInit = new Runnable() {
        @Override
        public void run() {
            DogeMediaPlayer mMediaPlayer = ScrollDogePlayer.this.getMediaPlayer();
            mMediaPlayer.setSources(videoInfo.userId,videoInfo.vcode)
                    .setConfig(DogeInclude.CONFIG_VTYPE,"96");
            mMediaPlayer.Init();
            isScrollInited = true;
        }
    };

    public void InitScrollHelper() {
        ScrollHelper.addScrollHelper(getContext(),ScrollObject,this,ScrollPosition);
    }

    public void scrollAutoStart(boolean start){
        if(!start){
            startAfterInited = false;
            return;
        }
        if(mController!=null) mController.requestFocus();
        if(autoPlay){
            if(mMediaPlayer!=null){
                mMediaPlayer.start();
                mMediaPlayer.setConfig(DogeInclude.CONFIG_AUTOPLAY,"1");
            }
            startAfterInited = true;
        }
    }

    public void UIreset()
    {
        if(mMediaPlayer!=null) {
            mMediaPlayer.pause();
            mMediaPlayer.seekTo(0);
            iv_thumb.setVisibility(VISIBLE);
            iv_play.setVisibility(VISIBLE);
        }
    }

    public void scrollView(){
        onPlayerSizeChanged(ViewSize);
    }
    public void initScrollPlayer(Object Obj, int position) {
        this.ScrollObject = Obj;
        this.ScrollPosition = position;
        InitScrollHelper();
        initControllerView();
    }

    private void reset() {
        if(mMediaPlayer!=null) mMediaPlayer.reset();
        ScrollHelper.removeScrollHelper(getContext(),ScrollObject,this);
    }

    boolean needReset = false;

    @Override
    public void setNeedReset() {
        needReset = true;
    }
    @Override
    public void doNeedReset() {
        if(needReset){
            reset();needReset = false;
        }
    }

    @Override
    public void pause() {
        if(mMediaPlayer!=null) mMediaPlayer.pause();
    }

    @Override
    public void onError(int code) {

    }

    @Override
    public void onCompletion() {

    }

    @Override
    public void onBuffering() {

    }

    @Override
    public void onBuffered() {

    }

    @Override
    public void onStart() {
        iv_thumb.setVisibility(GONE);
        iv_play.setVisibility(GONE);
    }

    @Override
    public void onPause() {
        iv_thumb.setVisibility(VISIBLE);
        iv_play.setVisibility(VISIBLE);
    }

    @Override
    public void onPreparing() {

    }

    @Override
    public void onPrepared() {
        controllerView.setClickable(true);
        controllerView.setFocusable(true);
        if(startAfterInited) {
            mMediaPlayer.start();
            startAfterInited = false;
        }
    }


    @Override
    public void onPlayerSizeChanged(int[] ViewSize) {
        this.ViewSize = ViewSize.clone();
    }

    @Override
    public void onPlayercfg(String cfg) {

    }

    @Override
    public void onVideoInfo(int index, String info) {
        tv_title.setText(mMediaPlayer.getVideoName());
    }

    @Override
    public void onThumb(Bitmap thumb) {
        refreshThumb(thumb);
    }

    @Override
    public void onStoryboard(List<Bitmap> storyboard, int Interval, int Duration) {

    }

    @Override
    public void onFullScreen(boolean isFullScreen) {

    }

    @Override
    public void onScreenShot(Bitmap screenShot) {

    }

    @Override
    public void onPlayerInfo(int Type, String Info) {

    }

    @Override
    public void onSetConfig(int Key, boolean Value) {

    }

    @Override
    public void onSetConfig(int Key, String Value) {

    }

}

