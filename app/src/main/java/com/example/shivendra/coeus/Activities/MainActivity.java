package com.example.shivendra.coeus.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.shivendra.coeus.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

   private FirebaseAuth mAuth;
   private FirebaseAuth.AuthStateListener mAuthListner;
   private FirebaseUser mUser;
   private Button loginButton;
   private Button signuputton;
   private EditText email;
   private EditText password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getSupportActionBar().hide();

        setContentView(R.layout.activity_main);

        loginButton = findViewById(R.id.login_button);
        signuputton = findViewById(R.id.signup_button);
        email = findViewById(R.id.email_id);
        password = findViewById(R.id.password_id);

        mAuth = FirebaseAuth.getInstance();
        mAuthListner= new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                mUser = firebaseAuth.getCurrentUser();
                if(mUser != null){
                    Toast.makeText(MainActivity.this,"Signed In",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(MainActivity.this,PostactivityList.class));
                    finish();
                }else {
                    //Toast.makeText(MainActivity.this,"Not signed In",Toast.LENGTH_LONG).show();
                }

            }
        };

        signuputton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,SignupActivity.class));
                finish();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(email.getText().toString()) && !TextUtils.isEmpty(password.getText().toString())){

                    String emailstring = email.getText().toString();
                    String pwd = password.getText().toString();

                    login(emailstring, pwd);

                }else {

                    Toast.makeText(MainActivity.this,"Field can't be empty ",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.action_signout){
            mAuth.signOut();

        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);

    }

    private void login(String emailstring, String pwd) {

        mAuth.signInWithEmailAndPassword(emailstring,pwd).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    //Signed In
                    Toast.makeText(MainActivity.this,"Signed In",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(MainActivity.this,PostactivityList.class));
                    finish();

                }else {
                    Toast.makeText(MainActivity.this,"Error Logging In",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListner);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mAuthListner!=null){
            mAuth.removeAuthStateListener(mAuthListner);
        }
    }
}
