package cn.ovzv.idioms.help;
import android.graphics.Bitmap;

public class Msg {

    private int id ;
    private Bitmap imgResId;
    private String title;
    private  int count;

    public Msg(){

    }

    public Msg(int id, Bitmap imgResId, String title,int count) {
        this.id = id;
        this.imgResId = imgResId;
        this.title = title;
        this.count = count;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Bitmap getImgResId() {
        return imgResId;
    }

    public void setImgResId(Bitmap imgResId) {
        this.imgResId = imgResId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
