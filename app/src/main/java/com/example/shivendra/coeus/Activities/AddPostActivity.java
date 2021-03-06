package com.example.shivendra.coeus.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.shivendra.coeus.Model.Blog;
import com.example.shivendra.coeus.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.nio.channels.GatheringByteChannel;
import java.util.HashMap;
import java.util.Map;

public class AddPostActivity extends AppCompatActivity {


    private ImageButton mPostImage;
    private EditText mPostTitle;
    private EditText mPostDesc;
    private Button mSubmit;
    private DatabaseReference mPostDatabase;
    private FirebaseDatabase mDatabase;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;
    private StorageReference mStorage;
    private ProgressDialog mProgress;
    private Uri mImageUri;
    private static final int GALLERY_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        mProgress = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mStorage = FirebaseStorage.getInstance().getReference();

        mPostDatabase = FirebaseDatabase.getInstance().getReference().child("Blog");

        mPostImage = findViewById(R.id.Post_Image);
        mPostTitle = findViewById(R.id.Post_Title);
        mPostDesc = findViewById(R.id.Post_Description);
        mSubmit = findViewById(R.id.Post_Submit);


        mPostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GALLERY_CODE);
            }
        });

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Posting to database
                startposting();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode ==GALLERY_CODE && resultCode == RESULT_OK ){

            mImageUri = data.getData();
            mPostImage.setImageURI(mImageUri);
        }
    }

    private void startposting() {

        mProgress.setMessage("Posting ");
        mProgress.show();

        final String titleval = mPostTitle.getText().toString().trim();
        final String descval = mPostDesc.getText().toString().trim();

        if(!TextUtils.isEmpty(titleval) && !TextUtils.isEmpty(descval) && mImageUri!= null){
            //Time to upload!!

            final StorageReference filepath = mStorage.child("BlogImages").child(mImageUri.getLastPathSegment());

            filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    //Uri downloadurl = taskSnapshot.getUploadSessionUri();

                    Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!urlTask.isSuccessful());
                    Uri downloadUrl = urlTask.getResult();



                    DatabaseReference newPost = mPostDatabase.push();

                    Map<String,String> dataToSave = new HashMap<>();
                    dataToSave.put("title",titleval);
                    dataToSave.put("desc",descval);
                    dataToSave.put("image",downloadUrl.toString());
                    dataToSave.put("userid",mUser.getUid());
                    dataToSave.put("timestamp", String.valueOf(System.currentTimeMillis()));

                    newPost.setValue(dataToSave);

                    mProgress.dismiss();

                    startActivity(new Intent(AddPostActivity.this,PostactivityList.class));
                    finish();

                }
            });


    }
}

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(AddPostActivity.this,PostactivityList.class);
        startActivity(intent);
        finish();
    }
}
