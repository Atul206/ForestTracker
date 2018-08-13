package com.gramin.sakhala.gramintracker.service;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.gramin.sakhala.gramintracker.dto.PendingFileDto;
import com.gramin.sakhala.gramintracker.util.Prefs;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by atulsakhala on 12/08/18.
 */

public class UploadService extends IntentService {

    public static final String POD_UPLOAD_PENDING = "POD_UPLOAD_PENDING";
    public static final String POD_UPLOAD_SUCCESS = "POD_UPLOAD_SUCCESS";
    public static final String POD_UPLOAD_FAILURE = "POD_UPLOAD_FAILURE";
    private static final String TAG = UploadService.class.getSimpleName();
    private static int MAX_RETRY_COUNT = 1000;
    private Intent podUploadIntent;
    private IntentService intentService;

    private StorageReference mStorageReference;

    public UploadService(String name) {
        super(name);
    }

    public UploadService() {
        super(TAG);
        mStorageReference = FirebaseStorage.getInstance().getReference();
    }


    @Override
    protected void onHandleIntent(@Nullable final Intent intent) {
        Log.v(TAG, "Called for uploading pending POD's");
        intentService = this;


        final List<PendingFileDto> pendingFileDtoList = Prefs.getPendingPOD(this);
        List<PendingFileDto> updatePendingPOD = new ArrayList<>();
        if(pendingFileDtoList != null && pendingFileDtoList.size() > 0) {
            for(int i = 0; i < pendingFileDtoList.size(); i++){
                final Boolean[] isSuccess = {false};
                final PendingFileDto p = pendingFileDtoList.get(i);
                Uri file = Uri.fromFile(new File(pendingFileDtoList.get(i).getFilePath()));
                StorageReference riversRef = mStorageReference.child("kml/" + pendingFileDtoList.get(i).getFileName());

                riversRef.putFile(file)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // Get a URL to the uploaded content
                                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                                Log.d("DOWNLOAD URL", downloadUrl.toString());
                                isSuccess[0] = true;
                                List<PendingFileDto> pendingFileDtos = Prefs.getPendingPOD(intentService);
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    pendingFileDtos = pendingFileDtos.stream().filter(x -> !x.getFileName().equals(p.getFileName())).collect(Collectors.<PendingFileDto>toList());
                                }else{
                                    for(int  i = 0 ; i < pendingFileDtos.size(); i++) {
                                        if(pendingFileDtos.get(i).getFileName().equals(p.getFileName())){
                                            pendingFileDtoList.remove(i);
                                        }
                                    }
                                }
                                Prefs.addPendingPOD(intentService, pendingFileDtos);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle unsuccessful uploads
                                // ...

                            }
                        });
            }
        }else{
            Log.d("Upload service","NO kml pending");
        }


    }
}
