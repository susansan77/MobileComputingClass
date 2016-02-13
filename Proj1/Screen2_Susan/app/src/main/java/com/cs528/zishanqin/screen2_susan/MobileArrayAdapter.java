package com.cs528.zishanqin.screen2_susan;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
/**
 * Created by zishanqin on 2/9/16.
 */
public class MobileArrayAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final String[] values;

    public MobileArrayAdapter(Context context, String[] values) {
        super(context, R.layout.activity_main, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (position == 0) {
            return getFirstRowView(inflater,parent);
        }
        int count=position+1;
        if(count%2==0){
            return getSelfieRowView(inflater,parent);
        }
        else{
            return getFriendRowView(inflater,parent);
        }
//        View rowView = inflater.inflate(R.layout.activity_main, parent, false);
//        TextView textView = (TextView) rowView.findViewById(R.id.label);
//        ImageView imageView = (ImageView) rowView.findViewById(R.id.logo);
//        String s = values[position];
//        if (s.equals("selfie")) {
//            imageView.setImageResource(R.drawable.cony);
//        } else {
//            imageView.setImageResource(R.drawable.brown);
//        }
//
//        return rowView;
    }

    public View getFirstRowView(LayoutInflater inflater,ViewGroup parent) {
        LinearLayout layout = new LinearLayout(context);
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT, 1.0f);

        //params.weight = 1.0f;
        param.gravity = Gravity.CENTER;
        layout.setLayoutParams(param);


        ImageView image=new ImageView(context);


        //image.setLayoutParams();
        //image.setScaleType(ImageView.ScaleType.CENTER);
        image.setImageDrawable(image.getResources().getDrawable(R.drawable.ic_launcher));
        image.setLayoutParams(param);
        layout.addView(image);

        return layout;
    }

    public View getSelfieRowView(LayoutInflater inflater,ViewGroup parent) {
        View rowView = inflater.inflate(R.layout.selfie, parent, false);
        return rowView;
    }

    public View getFriendRowView(LayoutInflater inflater,ViewGroup parent) {
        View rowView = inflater.inflate(R.layout.friend, parent, false);
        return rowView;
    }
}
