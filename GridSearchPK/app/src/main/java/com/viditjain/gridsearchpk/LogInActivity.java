package com.viditjain.gridsearchpk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LogInActivity extends AppCompatActivity
{
    Button proceedLoginButton;
    TextView signUpTextView;
    EditText emailEditText,passwordEditText;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        proceedLoginButton=findViewById(R.id.btn_proceed_login);
        signUpTextView=findViewById(R.id.sign_up_textview);
        emailEditText=findViewById(R.id.email);
        passwordEditText=findViewById(R.id.password);
        db=FirebaseClient.getFirebaseDbInstance();
        signUpTextView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent=new Intent(LogInActivity.this,SignUpActivity.class);
                startActivity(intent);
            }
        });
        proceedLoginButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                boolean err_found=false;
                if(emailEditText.getText().toString().length()==0)
                {
                    emailEditText.setError("Enter your email address");
                    err_found=true;
                }
                if(passwordEditText.getText().toString().length()==0)
                {
                    passwordEditText.setError("Enter your password");
                    err_found=true;
                }

                if(!err_found)
                {
                    db.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task)
                        {
                            if(task.isSuccessful())
                            {
                                List<DocumentSnapshot> users=task.getResult().getDocuments();
                                for(int i=0;i<users.size();i++)
                                {
                                    String email=(String) users.get(i).get("email");
                                    String password=(String)users.get(i).get("password");
                                    if(email.equals(emailEditText.getText().toString()) && password.equals(passwordEditText.getText().toString()))
                                    {
                                        Intent intent=new Intent(LogInActivity.this,RStringActivity.class);
                                        intent.putExtra("dir1",users.get(i).get("dir1").toString());
                                        intent.putExtra("dir1_steps",users.get(i).get("dir1_steps").toString());
                                        intent.putExtra("dir2",users.get(i).get("dir2").toString());
                                        intent.putExtra("dir2_steps",users.get(i).get("dir2_steps").toString());
                                        intent.putExtra("r_string",users.get(i).get("r_string").toString());
                                        startActivity(intent);
                                        break;
                                    }
                                    else if(!email.equals(emailEditText.getText().toString()) && password.equals(passwordEditText.getText().toString()))
                                    {
                                        emailEditText.setError("Email address incorrect");
                                        break;
                                    }
                                    else if(email.equals(emailEditText.getText().toString()) && !password.equals(passwordEditText.getText().toString()))
                                    {
                                        passwordEditText.setError("Password incorrect");
                                        break;
                                    }
                                    else
                                    {
                                        Toast.makeText(LogInActivity.this,"User not found",Toast.LENGTH_SHORT).show();
                                        break;
                                    }
                                }
                            }
                        }
                    });
                }
            }
        });
    }
}
