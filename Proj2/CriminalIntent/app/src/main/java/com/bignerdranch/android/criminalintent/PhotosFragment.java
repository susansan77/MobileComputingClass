package com.bignerdranch.android.criminalintent;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.ArrayList;

/**
 * Created by zishanqin on 2/13/16.
 */
public class PhotosFragment extends Fragment {
    private View rootView;
    private String flattenPhotos;
    private ArrayList<Bitmap> views;
    private GridView gridview;
    private CrimeFragment father;
    private ArrayList<String> photos;
//    public PhotosFragment(){
//
//    }
//    public PhotosFragment(ArrayList<ImageView> vs){
//        this.views=vs;
//    }
//    public void onCreate(Bundle savedInstanceState){
//        super.onCreate(savedInstanceState);
//
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(this.views==null){
            views=new ArrayList<Bitmap>();
        }
        rootView = inflater.inflate(R.layout.photogallery, container, false);

        this.gridview=(GridView) rootView.findViewById(R.id.gridView);

        ImageAdapter adapter=new ImageAdapter(rootView.getContext(),views);
        gridview.setAdapter(adapter);
        return rootView;
    }
    public String getFlattenPhotos() {
        return flattenPhotos;
    }

    public void setFlattenPhotos() {
//        this.flattenPhotos = flattenPhotos;
            StringBuilder builder=new StringBuilder();
            for(String s: this.photos){
                builder.append(s+",");
            }
            if(builder.length()>0){
                builder.deleteCharAt(builder.length()-1);
            }
            this.flattenPhotos=builder.toString();
    }
    public ArrayList<String> getPhotos() {
        return photos;
    }

    public void setPhotos(ArrayList<Bitmap> photos) {
        this.views = photos;
        ImageAdapter adapter=new ImageAdapter(gridview.getContext(),views);
        gridview.setAdapter(adapter);
        //gridview.no
        gridview.invalidateViews();
    }

    public void deleteAll() {

    }
    public ArrayList<Bitmap> getBitMaps(){
        return this.views;
    }
}