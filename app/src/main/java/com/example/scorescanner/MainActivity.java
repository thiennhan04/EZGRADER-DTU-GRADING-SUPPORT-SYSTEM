package com.example.scorescanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    EditText edtusername, edtpassword;
    Button signbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edtusername = findViewById(R.id.edtusername);
        edtpassword = findViewById(R.id.edtpassword);
        signbtn = findViewById(R.id.signbtn);
        signbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = edtusername.getText().toString();
                String pass = edtpassword.getText().toString();
                if(username.equals("admin") && pass.equals("123")){
                    Intent home = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(home);
                }


            }
        });
    }

}