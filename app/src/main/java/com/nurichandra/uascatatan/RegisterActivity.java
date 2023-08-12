package com.nurichandra.uascatatan;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

//  NIM     : 10120203
//  Nama    : Nuri Chandra Safitri
//  Kelas   : IF5

public class RegisterActivity extends AppCompatActivity {

    private EditText Nama, Email, Password, KonfirPwd;
    private Button btnRegister;
    private TextView btnLogin;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(RegisterActivity.this);
        progressDialog.setTitle("Loading");
        progressDialog.setTitle("Silahkan Tunggu!");
        progressDialog.setCancelable(false);

        Nama = findViewById(R.id.txtNama);
        Email = findViewById(R.id.txtEmailRg);
        Password = findViewById(R.id.txtPasswordRg);
        KonfirPwd = findViewById(R.id.txtKonfirPasswordRg);

        btnLogin = findViewById(R.id.btnLoginRg);
        btnRegister = findViewById(R.id.btnRegisterRg);

        btnLogin.setOnClickListener(view -> {
            finish();
        });

        btnRegister.setOnClickListener(view -> {
            if(Nama.getText().length()>0 && Email.getText().length()>0 && Password.getText().length()>0 && KonfirPwd.getText().length()>0){
                if(Password.getText().toString().equals(KonfirPwd.getText().toString())){
                    register(Nama.getText().toString(), Email.getText().toString(), Password.getText().toString());
                }else {
                    Toast.makeText(getApplicationContext(), "Konfirmasi password tidak sesuai!", Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(getApplicationContext(), "isi semua data!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void register(String nama, String email, String pwd){
        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful() && task.getResult()!=null){
                    FirebaseUser firebaseUser = task.getResult().getUser();
                    if(firebaseUser!=null){
                        UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                                .setDisplayName(nama)
                                .build();
                        firebaseUser.updateProfile(request).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                reload();
                            }
                        });
                    }else {
                        Toast.makeText(getApplicationContext(), "Register gagal", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getApplicationContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }
        });
    }

    private void reload(){
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            reload();
        }
    }
}