package com.example.activist.Login;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.activist.MainActivity;
import com.example.activist.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class SingIN extends AppCompatActivity {



    TextView Correo,Contraseña;
    FloatingActionButton BtnLogin;


    AlertDialog waitUploadData;
    SharedPreferences sharedPreferences;
    Cursor c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_i_n);
        Correo=findViewById(R.id.EditElectorKey);
        BtnLogin=findViewById(R.id.BtnLogin);


        BtnLogin.setOnClickListener(v -> {

            if(!Correo.getText().toString().equals(""))
            {

                sharedPreferences = getSharedPreferences("REGISTRO", MODE_PRIVATE);
                sharedPreferences.edit().putString("ElectorKey",Correo.getText().toString()).apply();

                sharedPreferences = getSharedPreferences("REGISTRO", MODE_PRIVATE);
                sharedPreferences.edit().putInt("Registrado",1).apply();

                if(sharedPreferences.getInt("Registrado", 0)==1)
                {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }

            }else
            {
                Toast.makeText(this,"Por favor introdusca su clave electoral",Toast.LENGTH_LONG).show();
            }


        });


    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        sharedPreferences = this.getSharedPreferences("REGISTRO", MODE_PRIVATE);
        if(sharedPreferences.getInt("Registrado", 0)==1)
        {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }




    }











}
