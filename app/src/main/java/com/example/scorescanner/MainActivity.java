package com.example.scorescanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {
    EditText edtusername, edtpassword;
    Button signbtn;
    String DB_PATH_SUFFIX = "/databases/";
    SQLiteDatabase database=null;
    String DATABASE_NAME="ssdb2.db";
    String username = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edtusername = findViewById(R.id.edtusername);
        edtpassword = findViewById(R.id.edtpassword);
        signbtn = findViewById(R.id.signbtn);

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

        signbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = edtusername.getText().toString();
                String pass = edtpassword.getText().toString();
                processCopy();
//                 if(username.equals("tnhan")){
//                     Intent home = new Intent(MainActivity.this, HomeActivity.class);
//                     home.putExtra("username", username);
//                     startActivity(home);
//                 }
                database = openOrCreateDatabase("ssdb2.db", MODE_PRIVATE, null);
//                Toast.makeText(MainActivity.this, getDatabasePath()+"", Toast.LENGTH_SHORT).show();
                String sql = "select * from kithi where username = '" + username + "'";
                Cursor c = database.rawQuery("select * from user where username = '"
                        + username + "' and password = '" + pass + "'", null);
                c.moveToFirst();
                String data ="";
                if(c.getCount() != 0){
                    //đăng nhập thành công
                    Intent home = new Intent(MainActivity.this, HomeActivity.class);
                    home.putExtra("username", username);
                    startActivity(home);
                }else{
                    Toast.makeText(MainActivity.this, "Tài khoản không tồn tại", Toast.LENGTH_SHORT).show();
                }
                c.close();
            }
        });

    }
    private void processCopy() {
        File dbFile = getDatabasePath(DATABASE_NAME);
//        dbFile.delete();
        if (!dbFile.exists())
        {
            try{
                CopyDataBaseFromAsset();
//                Toast.makeText(this, "Copying sucess from Assets folder", Toast.LENGTH_LONG).show();
            }
            catch (Exception e){
                Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
            }
        }

    }

    private String getDatabasePath() {
        return getApplicationInfo().dataDir + DB_PATH_SUFFIX+ DATABASE_NAME;
    }
    public void CopyDataBaseFromAsset() {

        try {
            InputStream myInput;
            myInput = getAssets().open(DATABASE_NAME);

            String outFileName = getDatabasePath();

            File f = new File(getApplicationInfo().dataDir + DB_PATH_SUFFIX);
//            if (!f.exists())
            f.mkdir();

            OutputStream myOutput = new FileOutputStream(outFileName);

            int size = myInput.available();
            byte[] buffer = new byte[size];
            myInput.read(buffer);
            myOutput.write(buffer);

            myOutput.flush();
            myOutput.close();
            myInput.close();
        } catch (IOException e) {

            e.printStackTrace();
        }
    }


}