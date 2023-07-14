package chats.com.chats.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import chats.com.chats.R;
import chats.com.chats.ModelClass.Users;
import de.hdodenhof.circleimageview.CircleImageView;

public class RegistrationActivity extends AppCompatActivity {

    TextView txt_signIn;
    CircleImageView profile_image;
    EditText reg_name,reg_status,reg_email,reg_password,reg_cPassword;
    Button btn_signUp;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    Uri imageUri;
    String imageURI;

    ProgressDialog progressDialog;


    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);


        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Creating Account...");
        progressDialog.setCancelable(false);


        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        storage=FirebaseStorage.getInstance();

        txt_signIn=findViewById(R.id.txt_signIn);
        profile_image=findViewById(R.id.profile_image);
        reg_name=findViewById(R.id.reg_name);
        reg_status=findViewById(R.id.reg_status);
        reg_email=findViewById(R.id.reg_email);
        reg_password=findViewById(R.id.reg_pass);
        reg_cPassword=findViewById(R.id.reg_cPass);
        btn_signUp=findViewById(R.id.btn_signUp);

        btn_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.show();

                String name = reg_name.getText().toString();
                String email = reg_email.getText().toString();
                String password = reg_password.getText().toString();
                String Cpassword = reg_cPassword.getText().toString();
                String status=reg_status.getText().toString();


                if(TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(Cpassword)
                        || TextUtils.isEmpty(status)){

                    progressDialog.dismiss();

                    Toast.makeText(RegistrationActivity.this, " Please Enter Valid Data ", Toast.LENGTH_SHORT).show();
                }
                else  if(!email.matches(emailPattern)){

                    progressDialog.dismiss();
                    reg_email.setError("Please Enter Valid Email");
                    Toast.makeText(RegistrationActivity.this, " Please Enter Valid Email ", Toast.LENGTH_SHORT).show();
                }
                else  if(! password.equals(Cpassword) ){

                    progressDialog.dismiss();
                    Toast.makeText(RegistrationActivity.this, " Password does not Match", Toast.LENGTH_SHORT).show();
                }
                else if(  password.length() <6){

                    progressDialog.dismiss();
                    Toast.makeText(RegistrationActivity.this, "Enter Long Password", Toast.LENGTH_SHORT).show();
                }
                else {

                    auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull  Task<AuthResult> task) {

                            if(task.isSuccessful()) {


                                Toast.makeText(RegistrationActivity.this, "User Account Created. ", Toast.LENGTH_SHORT).show();

                                DatabaseReference reference=database.getReference().child("user").child(auth.getUid());
                                StorageReference storageReference=storage.getReference().child("upload").child(auth.getUid());

                                if(imageUri !=null){
                                    storageReference.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull  Task<UploadTask.TaskSnapshot> task) {
                                            if(task.isSuccessful()){
                                                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                       imageURI= uri.toString();
                                                       Users users=new Users(auth.getUid(),name,email,imageURI,status);

                                                       reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                           @Override
                                                           public void onComplete(@NonNull  Task<Void> task) {

                                                               if(task.isSuccessful()){

                                                                   progressDialog.dismiss();
                                                                   startActivity(new Intent(RegistrationActivity.this,HomeActivity.class));
                                                               }
                                                               else {
                                                                   Toast.makeText(RegistrationActivity.this, "Oops! Something went wrong.", Toast.LENGTH_SHORT).show();
                                                               }

                                                           }
                                                       });
                                                    }
                                                });
                                            }
                                        }
                                    });
                                }
                                else {
                                    //String status="Hey! there I'm using chats";

                                    imageURI="https://firebasestorage.googleapis.com/v0/b/chats-c3bdb.appspot.com/o/profile_image.png?alt=media&token=b38f338b-85af-46ba-b77c-ba157b3128af";
                                    Users users=new Users(auth.getUid(),name,email,imageURI,status);

                                    reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull  Task<Void> task) {

                                            if(task.isSuccessful()){
                                                startActivity(new Intent(RegistrationActivity.this,HomeActivity.class));
                                            }
                                            else {
                                                Toast.makeText(RegistrationActivity.this, "Oops! Something went wrong.", Toast.LENGTH_SHORT).show();
                                            }

                                        }
                                    });
                                }

                            }else{

                                progressDialog.dismiss();
                                Toast.makeText(RegistrationActivity.this, "Oops! Something went wrong.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }




            }
        });


        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 10);
            }
        });


        txt_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegistrationActivity.this,LoginActivity.class));
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode ==10){
            if(data!= null){
                imageUri = data.getData();
                profile_image.setImageURI(imageUri);
            }
        }
    }
}