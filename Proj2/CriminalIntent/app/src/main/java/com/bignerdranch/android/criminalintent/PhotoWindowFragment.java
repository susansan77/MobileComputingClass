package com.bignerdranch.android.criminalintent;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;

/**
 * Created by zishanqin on 2/13/16.
 */
public class PhotoWindowFragment extends Fragment {
    private CrimeFragment father;
    private Button deleteAllButton;
    private PhotosFragment photosFragment;

    public void setFather(CrimeFragment father){
            this.father=father;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        if(savedInstanceState==null){
//            photosFragment = new PhotosFragment();
//            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
//            transaction.replace(R.id.gridView, photosFragment).commit();
//        }else{
//
//        }
        //photosFragment=(PhotosFragment) getFragmentManager().findFragmentById(R.id.gridView);
        View view = inflater.inflate(R.layout.gallery_window, container, false);
        deleteAllButton=(Button)view.findViewById(R.id.button);
        photosFragment=(PhotosFragment) getChildFragmentManager().findFragmentById(R.id.fragment);
        deleteAllButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // delete All photos
                deleteAllPhotos();

            }
        });

        return view;
    }
    public void deleteAllPhotos(){
        father.deleteAllPics();
        photosFragment.setPhotos(new ArrayList<Bitmap>());

    }
    public void setPhotos(ArrayList<Bitmap> listOfthePhotos){
            //photosFragment.setPhotos(listOfthePhotos);
        photosFragment.setPhotos(listOfthePhotos);
    }
    public ArrayList<Bitmap> getBitMaps(){
        return this.photosFragment.getBitMaps();
    }
}
