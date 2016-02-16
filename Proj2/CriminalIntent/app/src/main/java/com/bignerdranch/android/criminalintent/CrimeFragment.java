package com.bignerdranch.android.criminalintent;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class CrimeFragment extends Fragment {

    private static final String ARG_CRIME_ID = "crime_id";
    private static final String DIALOG_DATE = "DialogDate";

    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_CONTACT = 1;
    private static final int REQUEST_PHOTO= 2;

    private Crime mCrime;
    private File mPhotoFile;
    private int roundRobin=0;
    /**
     * Crime Photos
     */
    private ArrayList<File> mphotos;
    private ArrayList<ImageView> mphtosImages;
    /**
     *  PhotoWindowFragment
     */
    PhotoWindowFragment photoFragment;
    private EditText mTitleField;
    private Button mDateButton;
    private CheckBox mSolvedCheckbox;
    private Button mReportButton;
    private Button mSuspectButton;
    private ImageButton mPhotoButton;
    private ImageView mPhotoView;
    private ImageView mPhotoTaken;

    public static CrimeFragment newInstance(UUID crimeId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID, crimeId);

        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID crimeId = (UUID) getArguments().getSerializable(ARG_CRIME_ID);
        mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);
//        mPhotoFile = CrimeLab.get(getActivity()).getPhotoFile(mCrime);
        mPhotoFile= CrimeLab.get(getActivity()).getPhotoFile(mCrime);

//        if(savedInstanceState==null){
//            photoFragment = new PhotoWindowFragment();
//            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
//            transaction.replace(R.id.photos_fragment, photoFragment).commit();
//        }else{

        //}

        mphotos=CrimeLab.get(getActivity()).getPhotoFiles(mCrime);
        if(mphotos!=null) {
            //mPhotoFile = mphotos.get(0);
        }else{
            mphotos=new ArrayList<File>();

        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mCrime.setPhotoFilesname(mphotos);
        CrimeLab.get(getActivity())
                .updateCrime(mCrime);
    }
//    @Override
//    public void onResume(){
//        super.onResume();
//        this.loadPhotosView();
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crime, container, false);
        photoFragment=(PhotoWindowFragment) getChildFragmentManager().findFragmentById(R.id.photos_fragment);
        photoFragment.setFather(this);
        mTitleField = (EditText) v.findViewById(R.id.crime_title);
        mTitleField.setText(mCrime.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCrime.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mDateButton = (Button) v.findViewById(R.id.crime_date);
        updateDate();
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment
                        .newInstance(mCrime.getDate());
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                dialog.show(manager, DIALOG_DATE);
            }
        });

        mSolvedCheckbox = (CheckBox) v.findViewById(R.id.crime_solved);
        mSolvedCheckbox.setChecked(mCrime.isSolved());
        mSolvedCheckbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCrime.setSolved(isChecked);
            }
        });

        mReportButton = (Button)v.findViewById(R.id.crime_report);
        mReportButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT, getCrimeReport());
                i.putExtra(Intent.EXTRA_SUBJECT,
                        getString(R.string.crime_report_subject));
                i = Intent.createChooser(i, getString(R.string.send_report));

                startActivity(i);
            }
        });

        final Intent pickContact = new Intent(Intent.ACTION_PICK,
                ContactsContract.Contacts.CONTENT_URI);
        mSuspectButton = (Button)v.findViewById(R.id.crime_suspect);
        mSuspectButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivityForResult(pickContact, REQUEST_CONTACT);
            }
        });

        if (mCrime.getSuspect() != null) {
            mSuspectButton.setText(mCrime.getSuspect());
        }

        PackageManager packageManager = getActivity().getPackageManager();
        if (packageManager.resolveActivity(pickContact,
                PackageManager.MATCH_DEFAULT_ONLY) == null) {
            mSuspectButton.setEnabled(false);
        }

        mPhotoButton = (ImageButton) v.findViewById(R.id.crime_camera);
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        boolean canTakePhoto = mPhotoFile != null &&
                captureImage.resolveActivity(packageManager) != null;
        mPhotoButton.setEnabled(canTakePhoto);

        if (canTakePhoto) {
            Uri uri = Uri.fromFile(mPhotoFile);
            captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        }

        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(captureImage, REQUEST_PHOTO);
            }
        });

        mPhotoView = (ImageView) v.findViewById(R.id.crime_photo);
        //updatePhotoView();
        /**
         * update the photos for the crime
         */
        loadPhotosView();
        //updatePhotosView();
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_DATE) {
            Date date = (Date) data
                    .getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCrime.setDate(date);
            updateDate();
        } else if (requestCode == REQUEST_CONTACT && data != null) {
            Uri contactUri = data.getData();
            // Specify which fields you want your query to return
            // values for.
            String[] queryFields = new String[] {
                    ContactsContract.Contacts.DISPLAY_NAME,
            };
            // Perform your query - the contactUri is like a "where"
            // clause here
            ContentResolver resolver = getActivity().getContentResolver();
            Cursor c = resolver
                    .query(contactUri, queryFields, null, null, null);

            try {
                // Double-check that you actually got results
                if (c.getCount() == 0) {
                    return;
                }

                // Pull out the first column of the first row of data -
                // that is your suspect's name.
                c.moveToFirst();

                String suspect = c.getString(0);
                mCrime.setSuspect(suspect);
                mSuspectButton.setText(suspect);
            } finally {
                c.close();
            }
        } else if (requestCode == REQUEST_PHOTO) {
            //updatePhotoView();
            String id=UUID.randomUUID().toString();
            //mPhotoFile=
            //mPhotoFile=CrimeLab.get(getActivity()).getPhotoFile(id);
            updatePhotosView(id);

        }
    }



    private void updateDate() {
        mDateButton.setText(mCrime.getDate().toString());
    }

    private String getCrimeReport() {
        String solvedString = null;
        if (mCrime.isSolved()) {
            solvedString = getString(R.string.crime_report_solved);
        } else {
            solvedString = getString(R.string.crime_report_unsolved);
        }
        String dateFormat = "EEE, MMM dd";
        String dateString = DateFormat.format(dateFormat, mCrime.getDate()).toString();
        String suspect = mCrime.getSuspect();
        if (suspect == null) {
            suspect = getString(R.string.crime_report_no_suspect);
        } else {
            suspect = getString(R.string.crime_report_suspect, suspect);
        }
        String report = getString(R.string.crime_report,
                mCrime.getTitle(), dateString, solvedString, suspect);
        return report;
    }

    private void updatePhotoView() {
        if (mPhotoFile == null || !mPhotoFile.exists()) {
            mPhotoView.setImageDrawable(null);
        } else {
            Bitmap bitmap = PictureUtils.getScaledBitmap(
                    mPhotoFile.getPath(), getActivity());
            mPhotoView.setImageBitmap(bitmap);
        }
    }


    /**
     * load the view
     */
    private void loadPhotosView(){
        if (mphotos == null || mphotos.isEmpty()) {
            mPhotoView.setImageDrawable(null);
        } else {
            //mPhotoView=(ImageView)
            Bitmap bitmap = PictureUtils.getScaledBitmap(
                    mphotos.get(0).getPath(), getActivity());
            mPhotoView.setImageBitmap(bitmap);
            ArrayList<Bitmap> gallery=new ArrayList<Bitmap>();
            for(int i=1;i<mphotos.size();i++){
                //gallery.add(mphotos.get(i));
                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                Bitmap bm = BitmapFactory.decodeFile(mphotos.get(i).getAbsolutePath(), bmOptions);
                bitmap = Bitmap.createScaledBitmap(bm,240,240,true);
                gallery.add(bitmap);

            }
            photoFragment.setPhotos(gallery);

        }
    }
    /**
     * update the files based on the photos files
     */
    private void updatePhotosView(String photoID) {
        File p= CrimeLab.get(getActivity()).getPhotoFile(photoID);
        try {
            PictureUtils.copyPic(mPhotoFile, p);
        }catch(IOException e){
             ;
        }
        if(this.mphotos.size()>=4){
            roundRobin=roundRobin%4;
            this.mphotos.remove(roundRobin);
            this.mphotos.add(roundRobin,p);

        }

           else{ mphotos.add(p);}


            Bitmap bitmap = PictureUtils.getScaledBitmap(
                    mphotos.get(0).getPath(), getActivity());
            mPhotoView.setImageBitmap(bitmap);
               // mphotos.add(mPhotoFile);
                //this.photoFragment.
            ArrayList<Bitmap> gallery=new ArrayList<Bitmap>();
            for(int i=1;i<mphotos.size();i++){
                //gallery.add(mphotos.get(i));
                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                Bitmap bm = BitmapFactory.decodeFile(mphotos.get(i).getAbsolutePath(),bmOptions);
                bitmap = Bitmap.createScaledBitmap(bm,240,240,true);
//                Bitmap pic = PictureUtils.getScaledBitmap(
//                        mphotos.get(i).getPath(), getActivity());
                gallery.add(bitmap);

            }
            photoFragment.setPhotos(gallery);


        this.roundRobin++;

    }
    public void deleteAllPics(){
        this.mphotos.clear();
        loadPhotosView();
    }

}
