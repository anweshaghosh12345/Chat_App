package chats.com.chats.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import chats.com.chats.R;

public class LoginActivity extends AppCompatActivity {

    TextView txt_signUp;
    EditText login_email, login_Password;
    Button sign_In_btn;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    FirebaseAuth auth;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Opening Account...");
        progressDialog.setCancelable(false);

        auth=FirebaseAuth.getInstance();

        txt_signUp=findViewById(R.id.txt_signUp);
        sign_In_btn=(Button)findViewById(R.id.sign_In_btn);
        login_email=(EditText)findViewById(R.id.login_email);
        login_Password=(EditText)findViewById(R.id.login_Password);

        sign_In_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();

                String email=login_email.getText().toString();
                String password=login_Password.getText().toString();

                if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this,"Enter Valid Data",Toast.LENGTH_SHORT).show();
                }
                else if(! email.matches(emailPattern)){

                    progressDialog.dismiss();
                    login_email.setError("Invalid Email");
                    Toast.makeText(LoginActivity.this,"Invalid Email",Toast.LENGTH_SHORT).show();
                }
                else  if(password.length()<6){

                    progressDialog.dismiss();
                    login_Password.setError("Invalid Password");
                    Toast.makeText(LoginActivity.this,"Please enter valid password",Toast.LENGTH_SHORT).show();
                }
                else {
                    auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                progressDialog.dismiss();
                                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                            }else {
                                Toast.makeText(LoginActivity.this,"Error in login",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }


            }
        });

        txt_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegistrationActivity.class));
            }
        });
    }
}