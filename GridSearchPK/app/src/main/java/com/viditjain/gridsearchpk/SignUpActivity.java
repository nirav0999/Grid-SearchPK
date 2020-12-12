package com.viditjain.gridsearchpk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {

    Button signUpButton;
    FirebaseFirestore db;
    DocumentReference ref;
    EditText usernameEditText,emailEditText,passwordEditText,rStringEditText;
    Snackbar snackbar;
    ConstraintLayout constraintLayout;
    String[] dir1List={"First direction","Up","Down","Left","Right"};
    String[] dir2List={"Second direction","Up","Down","Left","Right"};
    SeekBar dir1Seekbar,dir2Seekbar;
    TextView dir1Steps,dir2Steps,logIn;
    Spinner dir1Spinner,dir2Spinner;
    ArrayAdapter<String> adapter1,adapter2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        FirebaseApp.initializeApp(this);
        signUpButton=findViewById(R.id.btn_signup);
        usernameEditText=findViewById(R.id.username);
        emailEditText=findViewById(R.id.email);
        passwordEditText=findViewById(R.id.password);
        rStringEditText=findViewById(R.id.r_string);
        constraintLayout=findViewById(R.id.constraint_layout);
        logIn=findViewById(R.id.log_in);
        dir1Seekbar=findViewById(R.id.dir_1_steps);
        dir2Seekbar=findViewById(R.id.dir_2_steps);
        dir1Steps=findViewById(R.id.dir_1_steps_label);
        dir2Steps=findViewById(R.id.dir_2_steps_label);
        dir1Seekbar.setProgress(0);
        dir2Seekbar.setProgress(0);
        dir1Seekbar.setMax(5);
        dir2Seekbar.setMax(5);
        dir1Spinner=findViewById(R.id.dir_1_drop_down);
        dir2Spinner=findViewById(R.id.dir_2_drop_down);
        dir1Steps.setText(dir1Seekbar.getProgress()+"");
        dir2Steps.setText(dir1Seekbar.getProgress()+"");
        adapter1=new ArrayAdapter<>(this,R.layout.spinner_item,dir1List);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dir1Spinner.setAdapter(adapter1);
        adapter2=new ArrayAdapter<>(this,R.layout.spinner_item,dir2List);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dir2Spinner.setAdapter(adapter2);
        db=FirebaseClient.getFirebaseDbInstance();
        ref=db.collection("users").document();

        logIn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent=new Intent(SignUpActivity.this,LogInActivity.class);
                startActivity(intent);
                finish();
            }
        });

        dir1Seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                dir1Steps.setText(progress+"");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {

            }
        });

        dir2Seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                dir2Steps.setText(progress+"");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String dir1=dir1Spinner.getSelectedItem().toString();
                final String dir2=dir2Spinner.getSelectedItem().toString();
                boolean err_found=false;
                if(usernameEditText.getText().toString().length()==0)
                {
                    usernameEditText.setError("Please provide a user name");
                    err_found=true;
                }
                if(emailEditText.getText().toString().length()==0)
                {
                    emailEditText.setError("Please provide an email address");
                    err_found=true;
                }
                if(!isValid(emailEditText.getText().toString()))
                {
                    emailEditText.setError("Please enter a valid email address");
                    err_found=true;
                }
                if(passwordEditText.getText().toString().length()==0)
                {
                    passwordEditText.setError("Please provide a password");
                    err_found=true;
                }
                if(rStringEditText.getText().toString().length()==0)
                {
                    rStringEditText.setError("Please provide a R-String");
                    err_found=true;
                }
                if(dir1.equals(dir1List[0]))
                {
                    TextView errorText= (TextView) dir1Spinner.getSelectedView();
                    errorText.setText("Please choose a direction");
                    errorText.setTextColor(Color.RED);
                    err_found=true;
                }
                if(dir2.equals(dir2List[0]))
                {
                    TextView errorText= (TextView) dir2Spinner.getSelectedView();
                    errorText.setText("Please choose a direction");
                    errorText.setTextColor(Color.RED);
                    err_found=true;
                }
                if(dir1Seekbar.getProgress()==0)
                {
                    Toast.makeText(SignUpActivity.this,"Please select a value other than 0 1st direction",Toast.LENGTH_LONG).show();
                    err_found=true;
                }
                if(dir2Seekbar.getProgress()==0)
                {
                    Toast.makeText(SignUpActivity.this,"Please select a value other than 0 2nd direction",Toast.LENGTH_LONG).show();
                    err_found=true;
                }

                if(!err_found)
                {
                    ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>()
                    {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot)
                        {
                            if(documentSnapshot.exists())
                            {
                                snackbar=Snackbar.make(constraintLayout,"User already exists",Snackbar.LENGTH_LONG);
                                snackbar.show();
                            }
                            else
                            {
                                HashMap<String,String> user=new HashMap<>();
                                user.put("user_name",usernameEditText.getText().toString());
                                user.put("email",emailEditText.getText().toString());
                                user.put("password",passwordEditText.getText().toString());
                                user.put("r_string",rStringEditText.getText().toString());
                                user.put("dir1",dir1Spinner.getSelectedItem().toString());
                                user.put("dir1_steps",dir1Seekbar.getProgress()+"");
                                user.put("dir2",dir2Spinner.getSelectedItem().toString());
                                user.put("dir2_steps",dir2Seekbar.getProgress()+"");

                                db.collection("users").add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>()
                                {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference)
                                    {
                                        Toast.makeText(SignUpActivity.this,"Sign Up successful",Toast.LENGTH_SHORT).show();
                                        Intent intent=new Intent(SignUpActivity.this,LogInActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }).addOnFailureListener(new OnFailureListener()
                                {
                                    @Override
                                    public void onFailure(@NonNull Exception e)
                                    {
                                        snackbar=Snackbar.make(constraintLayout,"There was an error",Snackbar.LENGTH_SHORT);
                                        snackbar.show();
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });
    }
    public boolean isValid(String email)
    {
        String regex="^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }
}
