package com.example.a82712.jiakao;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    SQLiteDatabase db;
    Button rbtn;
    EditText lusername;
    EditText lpasswd;
    Button lbtn;
    int currentId=-1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = SQLiteDatabase.openOrCreateDatabase(
                this.getFilesDir().toString()
                        + "/Jiakao.db3", null);
        lusername = findViewById(R.id.lusername);
        lpasswd = findViewById(R.id.lpasswd);
        lbtn = findViewById(R.id.lbtn);
        rbtn = findViewById(R.id.rBtn);
        lbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = lusername.getText().toString();
                String pwd = lpasswd.getText().toString();
                try{
                    Cursor cursor = db.rawQuery("select * from t_user where username = ? and password = ?"
                            , new String[] {name,pwd});
                    if(cursor.getCount()>0){
                        while(cursor.moveToNext()){
                            currentId= cursor.getInt(0);
                        }
                        Toast.makeText(MainActivity.this,"登录成功"+currentId,Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this,MainPower.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("currentId",currentId);
                        bundle.putString("currentName",name);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }else{
                        Toast.makeText(MainActivity.this,"用户名或密码错误",Toast.LENGTH_SHORT).show();
                    }
                }catch (SQLiteException se){
                    Toast.makeText(MainActivity.this,"用户名或密码错误",Toast.LENGTH_SHORT).show();
                }
            }
        });
        rbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,register.class);
                startActivity(intent);
            }
        });





        Button testbtn = findViewById(R.id.test);
        testbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,Test.class);
                startActivity(intent);
            }
        });
    }
}
