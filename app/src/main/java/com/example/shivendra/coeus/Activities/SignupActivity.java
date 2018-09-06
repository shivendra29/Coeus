package com.example.shivendra.coeus.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.shivendra.coeus.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {

    private EditText firstname;
    private EditText lasttname;
    private EditText email;
    private EditText password;
    private Button signup;
    private DatabaseReference mDataBaseReference;
    private FirebaseDatabase mDataBase;
    private FirebaseAuth mAuth;
    private ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mDataBase = FirebaseDatabase.getInstance();
        mDataBaseReference = mDataBase.getReference().child("Users");
        mAuth = FirebaseAuth.getInstance();

        mProgressDialog = new ProgressDialog(this);

        firstname = findViewById(R.id.firstnameAct);
        lasttname = findViewById(R.id.lastnameAtc);
        email = findViewById(R.id.emailAtc);
        password = findViewById(R.id.passwordAtc);
        signup = findViewById(R.id.signupAtc);

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

                        String uid = mAuth.getCurrentUser().getUid();
                        DatabaseReference currentDB = mDataBaseReference.child(uid);

                        currentDB.child("firstname").setValue(name);
                        currentDB.child("lastname").setValue(lname);
                        currentDB.child("image").setValue("none");

                        mProgressDialog.dismiss();

                        Intent intent = new Intent(SignupActivity.this,PostactivityList.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                    }
                }
            });
        }

    }
}
