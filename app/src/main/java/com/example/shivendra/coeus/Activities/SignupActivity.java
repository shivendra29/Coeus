package com.example.shivendra.coeus.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.shivendra.coeus.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class SignupActivity extends AppCompatActivity {

    private EditText firstname;
    private EditText lasttname;
    private EditText email;
    private EditText password;
    private Button signup;
    private DatabaseReference mDataBaseReference;
    private FirebaseDatabase mDataBase;
    private FirebaseAuth mAuth;
    private StorageReference mFirebaseStorage;
    private ProgressDialog mProgressDialog;
    private ImageButton profilepic;
    private Uri resultUri = null;
    private final static int GALLERY_CODE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getSupportActionBar().hide();

        setContentView(R.layout.activity_signup);

        mDataBase = FirebaseDatabase.getInstance();
        mDataBaseReference = mDataBase.getReference().child("Users");
        mAuth = FirebaseAuth.getInstance();

        mFirebaseStorage = FirebaseStorage.getInstance().getReference().child("Blog_ProfilePics");

        mProgressDialog = new ProgressDialog(this);

        firstname = findViewById(R.id.firstnameAct);
        lasttname = findViewById(R.id.lastnameAtc);
        email = findViewById(R.id.emailAtc);
        password = findViewById(R.id.passwordAtc);
        signup = findViewById(R.id.signupAtc);
        profilepic =findViewById(R.id.profilepic);

        profilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,GALLERY_CODE);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createnewaccount();
            }
        });
    }

    private void createnewaccount() {

        final String name = firstname.getText().toString().trim();
        final String lname = lasttname.getText().toString().trim();
        String em = email.getText().toString().trim();
        String pwd = password.getText().toString().trim();

        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(lname) && !TextUtils.isEmpty(em) && !TextUtils.isEmpty(pwd)){

            mProgressDialog.setMessage("Creating Account ");
            mProgressDialog.show();

            mAuth.createUserWithEmailAndPassword(em,pwd).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    if(authResult != null){

                        StorageReference imagePath = mFirebaseStorage.child("Blog_ProfilePics")
                                .child(resultUri.getLastPathSegment());

                        imagePath.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                String uid = mAuth.getCurrentUser().getUid();
                                DatabaseReference currentDB = mDataBaseReference.child(uid);

                                currentDB.child("firstname").setValue(name);
                                currentDB.child("lastname").setValue(lname);
                                currentDB.child("image").setValue(resultUri.toString());

                                mProgressDialog.dismiss();

                                Intent intent = new Intent(SignupActivity.this,PostactivityList.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);

                            }
                        });

//                        String uid = mAuth.getCurrentUser().getUid();
//                        DatabaseReference currentDB = mDataBaseReference.child(uid);
//
//                        currentDB.child("firstname").setValue(name);
//                        currentDB.child("lastname").setValue(lname);
//                        currentDB.child("image").setValue("none");
//
//                        mProgressDialog.dismiss();
//
//                        Intent intent = new Intent(SignupActivity.this,PostactivityList.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        startActivity(intent);

                    }
                }
            });
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(SignupActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_CODE && resultCode == RESULT_OK){

            Uri mImageuri = data.getData();

            CropImage.activity(mImageuri).setAspectRatio(1,1)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                resultUri = result.getUri();
                profilepic.setImageURI(resultUri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}
