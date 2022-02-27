package cn.ovzv.idioms.help;

public class Footprint {
    private String time;
    private String use_time;
    private String text;
    private int  daka;
    public Footprint (String time,String use_time, String text,int  daka){
        this.time = time;
        this.use_time = use_time;
        this.text = text;
        this.daka = daka;
    }
    public String getTime() {
        return time;
    }
    public String getUse_time() {
        return use_time;
    }

    public int  getDaka() {
        return daka;
    }

    public String getText() {
        return text;
    }
}
