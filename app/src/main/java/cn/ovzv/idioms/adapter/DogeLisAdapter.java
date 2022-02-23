package cn.ovzv.idioms.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import cn.ovzv.idioms.R;

import java.util.List;

import cn.ovzv.idioms.help.DogeVideo;
import cn.ovzv.idioms.help.ScrollDogePlayer;

public class DogeLisAdapter extends ArrayAdapter<DogeVideo> {

    private int resourceId;
    public DogeLisAdapter(Context context, int textViewResourceId, List<DogeVideo> objects)
    {
        super(context, textViewResourceId, objects);
        this.resourceId = textViewResourceId;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        DogeVideo dv = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        ScrollDogePlayer dgplayer = view.findViewById(R.id.list_dgplayer);
        dgplayer.setVideoInfo(dv);
        dgplayer.initScrollPlayer(parent,position);
        return view;
    }
}
