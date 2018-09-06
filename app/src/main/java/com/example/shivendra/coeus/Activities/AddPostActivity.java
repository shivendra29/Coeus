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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.nio.channels.GatheringByteChannel;

public class AddPostActivity extends AppCompatActivity {


    private ImageButton mPostImage;
    private EditText mPostTitle;
    private EditText mPostDesc;
    private Button mSubmit;
    private DatabaseReference mPostDatabase;
    private FirebaseDatabase mDatabase;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;
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

        String titleval = mPostTitle.getText().toString().trim();
        String descval = mPostDesc.getText().toString().trim();

        if(!TextUtils.isEmpty(titleval) && !TextUtils.isEmpty(descval)){
            //Time to upload!!

//            Blog blog = new Blog("Title", "Description",
//                    "imageurl", "userid","timestamp");
//
//            mPostDatabase.setValue(blog).addOnSuccessListener(new OnSuccessListener<Void>() {
//                @Override
//                public void onSuccess(Void aVoid) {
//                    Toast.makeText(getApplicationContext(),"Posted",Toast.LENGTH_LONG).show();
//                    mProgress.dismiss();
//                }
//            });
        }

    }
}
