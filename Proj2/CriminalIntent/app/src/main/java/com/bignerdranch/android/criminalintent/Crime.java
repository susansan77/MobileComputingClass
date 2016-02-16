package com.bignerdranch.android.criminalintent;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class Crime {

    private UUID mId;
    private String mTitle;
    private Date mDate;
    private boolean mSolved;
    private String mSuspect;
    private ArrayList<String> mPhotos;

    /**
     * Do not get photos by crime title name, instead,
     * using a String for serialize and deserialize the
     * photos
     */
    private String mphotosStr;

    public Crime() {
        this(UUID.randomUUID());
    }

    public Crime(UUID id) {
        mId = id;
        mDate = new Date();
    }
    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public boolean isSolved() {
        return mSolved;
    }

    public void setSolved(boolean solved) {
        mSolved = solved;
    }

    public String getSuspect() {
        return mSuspect;
    }

    public void setSuspect(String suspect) {
        mSuspect = suspect;
    }

    public String getPhotoFilename() {
        return "IMG_" + getId().toString() + ".jpg";
    }
    public static String getPhotoFilename(String customizedID) {
        return "IMG_" + customizedID + ".jpg";
    }
    /**
     *
     * @return  the list of the names
     */
    public ArrayList<String> getPhotoFilesname(){
        ArrayList<String> names=new ArrayList<String>();
        if(this.mphotosStr==null||this.mphotosStr.length()==0){
            return null;
        }
        String[] ns=this.mphotosStr.split(",");
        for(int i=0;i<ns.length;i++){
            names.add(ns[i]);
        }
        return names;
    }
    public void setPhotoFilesname(ArrayList<File> strs){
       //this.mPhotos=strs;
        ArrayList<String> names=new ArrayList<String>();
        for(File f:strs){
            String name=f.getName();
            names.add(name);
        }
        this.mPhotos=names;
    }
    public void setMphotosStr(String mphotosStr) {
        this.mphotosStr = mphotosStr;
    }

    public String getPhotoFilesNamesFlatte() {
        //return photoFilesNamesFlatte;
        if(this.mPhotos==null||this.mPhotos.isEmpty()){
            return null;
        }

        StringBuilder builder=new StringBuilder();
        for(String s: this.mPhotos){
            builder.append(s+",");
        }
        if(builder.length()>0){
            builder.deleteCharAt(builder.length()-1);
        }

        return builder.toString();
    }
}
