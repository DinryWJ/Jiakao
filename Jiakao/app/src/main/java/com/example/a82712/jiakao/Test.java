package com.example.a82712.jiakao;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class Test extends Activity{
    RadioGroup radiogroup;
    RadioButton item1;
    RadioButton item4;
    RadioButton item3;
    RadioButton item2;
    Button button;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        radiogroup = findViewById(R.id.aradiogroup);
        item1 = findViewById(R.id.aitem1);
        item2 = findViewById(R.id.aitem2);
        item3 = findViewById(R.id.aitem3);
        item4 = findViewById(R.id.aitem4);
        button = findViewById(R.id.asdasd);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentAnswer = radiogroup.indexOfChild(findViewById(radiogroup.getCheckedRadioButtonId()))+1;
                Log.v("curr",currentAnswer+"");

            }
        });



    }



}
