package com.bignerdranch.android.criminalintent;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by zishanqin on 2/13/16.
 */
public class ImageAdapter extends BaseAdapter {

    private Context mContext;
    ArrayList<Bitmap> itemList= new ArrayList<Bitmap>();

    public ImageAdapter(Context c, ArrayList<Bitmap> arrayImgs){
        mContext=c;
        this.itemList=arrayImgs;
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
//       if()
        LayoutInflater inflater=(LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Bitmap v=itemList.get(i);
        //new ImageView();

        View newpic=new View(mContext);
        newpic=inflater.inflate(R.layout.single_photo,null);
        ImageView imgV=(ImageView)newpic.findViewById(R.id.singlePhoto);
        //Bitmap fittedBitmap=Bitmap.createScaledBitmap(v, 80, 80, true);
        imgV.setImageBitmap(v);
        return newpic;
    }
}
