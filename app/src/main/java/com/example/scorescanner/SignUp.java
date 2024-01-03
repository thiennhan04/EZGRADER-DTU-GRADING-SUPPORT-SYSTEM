package com.example.scorescanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignUp extends AppCompatActivity {
    EditText edtusername, edtpassword, confirm_pass;
    Button signinbtn, signupbtn;
    DataBase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        db = new DataBase(this);

        edtusername = findViewById(R.id.edtusername);
        edtpassword = findViewById(R.id.edtpassword);
        confirm_pass = findViewById(R.id.confirm_pass);
        signinbtn = findViewById(R.id.signinbtn);
        signupbtn = findViewById(R.id.signupbtn);

        edtusername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().contains(" ")) {
                    String filteredText = charSequence.toString().replace(" ", "");
                    edtusername.setText(filteredText);
                    edtusername.setSelection(filteredText.length());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        signinbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = edtusername.getText().toString();
                String pass = edtpassword.getText().toString();
                String confirm = confirm_pass.getText().toString();
                Cursor c = db.mydatabase.rawQuery("select username from user where username = '" + username + "'", null);
                if (c.getCount() > 0) {
                    Toast.makeText(SignUp.this, "Tên tài khoản đã tồn tại", Toast.LENGTH_SHORT).show();
                } else {
                    if (pass.equals(confirm)) {
                        ContentValues values = new ContentValues();
                        values.put("username", username);
                        values.put("password", pass);
                        long row = db.mydatabase.insert("user", null, values);
                        if (row == 1) {
                            Toast.makeText(SignUp.this, "Tạo tài khoản thành công!", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    } else {
                        Toast.makeText(SignUp.this, "Mật khẩu không khớp!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}