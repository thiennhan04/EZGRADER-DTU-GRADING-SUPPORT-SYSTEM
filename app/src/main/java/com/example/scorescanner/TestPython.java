package com.example.scorescanner;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;

public class TestPython extends AppCompatActivity {
    TextView textView3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_python);
        textView3 = findViewById(R.id.textView3);
        if (! Python.isStarted()) {
            Python.start(new AndroidPlatform(this));
        }
        Python py = Python.getInstance();
        PyObject pyob = py.getModule("mypy");
        PyObject obj = pyob.callAttr("main");
        textView3.setText(obj.toString());
    }
}