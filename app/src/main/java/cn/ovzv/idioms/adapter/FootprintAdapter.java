package cn.ovzv.idioms.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.ovzv.idioms.help.Footprint;
import cn.ovzv.idioms.R;
public class FootprintAdapter extends ArrayAdapter<Footprint> {

    private int resourceId;

    public FootprintAdapter(Context context, int textViewResourceId, List<Footprint> objects){
        super(context,textViewResourceId,objects);
        resourceId = textViewResourceId;

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Footprint footprint = getItem(position); //获取当前项的Footprint实例

        View view= LayoutInflater.from(getContext()).inflate(resourceId,parent,false);


        ImageView footprintImage = (ImageView) view.findViewById(R.id.daka_image);

        TextView footprinttime = (TextView) view.findViewById(R.id.time);

        TextView footprintusetime =  (TextView) view.findViewById(R.id.use_time);

        TextView footprintext = (TextView) view.findViewById(R.id.text);

        footprintImage.setImageResource(footprint.getDaka());

        footprinttime.setText(footprint.getTime());

        footprintusetime.setText(footprint.getUse_time());

        footprintext.setText(footprint.getText());

        return view;
    }
}
