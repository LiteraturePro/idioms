package cn.ovzv.idioms.help;

public interface ScrollPlayer {

    public void initMediaPlayer();

    public void initScrollPlayer(Object Obj, int position);

    public void scrollAutoStart(boolean start);

    public void scrollView();

    public void UIreset();

    public void pause();

    public void setNeedReset();

    public void doNeedReset();
}
