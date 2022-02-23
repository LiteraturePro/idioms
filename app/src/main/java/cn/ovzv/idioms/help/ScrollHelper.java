package cn.ovzv.idioms.help;

import android.content.Context;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class ScrollHelper {
    private static Context mApplication;
    private volatile static ScrollHelper mScrollHelper = null;
    private ScrollHelper(Context context) {
        ScrollPlayers = new HashMap<>();
    }
    private static ScrollHelper getInstance(Context context) {
        mApplication = context.getApplicationContext();
        if (mScrollHelper == null) {
            synchronized (ScrollHelper.class) {
                if (mScrollHelper == null) {
                    mScrollHelper = new ScrollHelper(mApplication);
                }
            }
        }
        return mScrollHelper;
    }
    private static Map<Object, Map<Integer, ScrollPlayer>> ScrollPlayers;

    public static void onListDestory(Context mContext,Object list){
        getInstance(mContext).ListDestory(list);}
    private void ListDestory(Object list){
        new Thread("scroll") {
            @Override
            public void run() {
                synchronized (ScrollPlayers)
                {
                    if(ScrollPlayers.get(list)!=null) ScrollPlayers.remove(list);
                }
            }
        }.start();
    }

    public static void UIresetAll(Context mContext, Object obj) {
        getInstance(mContext).UIresetAll(obj);}
    private void UIresetAll(Object obj){
        new Thread("scroll") {
            @Override
            public void run() {
                synchronized (ScrollPlayers)
                {
                    Map<Integer, ScrollPlayer> list = ScrollPlayers.get(obj);
                    if(list != null)
                        for (Iterator<Map.Entry<Integer, ScrollPlayer>> it = list.entrySet().iterator(); it.hasNext();){
                            Map.Entry<Integer, ScrollPlayer> item = it.next();
                            item.getValue().UIreset();
                        }
                }
            }
        }.start();
    }

    public static void removeScrollHelper(Context mContext, Object obj, ScrollPlayer dgPlayer) {
        getInstance(mContext).removeScrollHelper(obj,dgPlayer);}
    private void removeScrollHelper(Object obj, ScrollPlayer dgPlayer){
        new Thread("scroll") {
            @Override
            public void run() {
                synchronized (ScrollPlayers)
                {
                    Map<Integer, ScrollPlayer> list = ScrollPlayers.get(obj);
                    if(list!=null){
                        for (Iterator<Map.Entry<Integer, ScrollPlayer>> it = list.entrySet().iterator(); it.hasNext();){
                            Map.Entry<Integer, ScrollPlayer> item = it.next();
                            if(item.getValue() == dgPlayer) list.remove(item);
                        }
                        if(list.isEmpty()) ScrollPlayers.remove(list);
                    }
                }
            }
        }.start();
    }

    public static void addScrollHelper(Context mContext, Object obj, ScrollPlayer dgPlayer, int Position) {
        getInstance(mContext).AddScrollHelper(obj,dgPlayer,Position);}
    private void AddScrollHelper(Object obj, ScrollPlayer dgPlayer, int Position){
        new Thread("scroll") {
            @Override
            public void run() {
                synchronized (ScrollPlayers)
                {
                    if(!ScrollPlayers.containsKey(obj))ScrollPlayers.put(obj,new LinkedHashMap<>());
                    Map<Integer, ScrollPlayer> list = ScrollPlayers.get(obj);
                    list.put(Position,dgPlayer);
                }
            }
        }.start();
    }
    public static void onScrolled(Context mContext, Object obj, int firstVisibleItem, int lastVisibleItem){
        getInstance(mContext).OnScroll(obj,firstVisibleItem,lastVisibleItem);}
    private void OnScroll(Object obj,int firstVisibleItem,int lastVisibleItem){
        if(!ScrollPlayers.containsKey(obj)) return;
        new Thread("scroll") {
            @Override
            public void run() {
                synchronized (ScrollPlayers)
                {
                    Map<Integer, ScrollPlayer> list = ScrollPlayers.get(obj);
                    for (Iterator<Map.Entry<Integer, ScrollPlayer>> it = list.entrySet().iterator(); it.hasNext(); ) {
                        Map.Entry<Integer, ScrollPlayer> item = it.next();
                        item.getValue().doNeedReset();
                        item.getValue().initMediaPlayer();
                        if ((item.getKey() < firstVisibleItem || item.getKey() > lastVisibleItem)) {
                            item.getValue().pause();
                        } else {
                            item.getValue().scrollView();
                        }
                    }
                    if (list.isEmpty()) {
                        ScrollPlayers.remove(obj);
                    }
                }
            }
        }.start();
    }
}

