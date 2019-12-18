package com.example.socialnextwork.activitys;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.socialnextwork.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class ShowDown_Image extends AppCompatActivity {

    ImageView show, save;
    ProgressDialog pd;
    String img_Url;
    FirebaseStorage firebaseStorage;
    StorageReference storage, ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_down__image);

        Intent i = getIntent();
        img_Url = i.getStringExtra("Image_Url");
        final String nameUrl = img_Url.substring(91, 132);

        show = findViewById(R.id.img_show);
        save = findViewById(R.id.img_down);
        Glide.with(this).load(img_Url).placeholder(R.drawable.yasuo).into(show);
        pd = new ProgressDialog(this);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                download(nameUrl, ".jpg" );
            }
        });
    }

    private void  download(final String fileName, final String fileExtension){


        storage = firebaseStorage.getInstance().getReference();
        ref = storage.child("PhotoChat/").child(fileName);

        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                downloadFile(ShowDown_Image.this, fileName + "", fileExtension + "", DIRECTORY_DOWNLOADS, uri.toString());
            }
        });
    }

    private void downloadFile(Context context, String fileName, String fileExtension, String des, String url) {
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context, des, fileName + fileExtension);

        downloadManager.enqueue(request);
    }
}
