package com.example.a82712.jiakao;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class register extends Activity {
    EditText rusername;
    EditText rpasswd;
    EditText rpasswd2;
    Button btn;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        rusername = findViewById(R.id.rusername);
        rpasswd = findViewById(R.id.rpasswd);
        rpasswd2 = findViewById(R.id.rpasswd2);
        btn = findViewById(R.id.btn);
        // 创建或打开数据库（此处需要使用绝对路径）
        db = SQLiteDatabase.openOrCreateDatabase(
                this.getFilesDir().toString()
                        + "/Jiakao.db3", null);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rpasswd.getText().toString().equals(rpasswd2.getText().toString())){
                    String name = rusername.getText().toString();
                    String pwd = rpasswd.getText().toString();
                    try {
                        insertData(db, name, pwd);
                    }
                    catch (SQLiteException se) {
                        // 执行DDL创建数据表
                        db.execSQL("create table t_user(id integer"
                                + " primary key autoincrement,"
                                + " username varchar(50),"
                                + " password varchar(255))");
                        db.execSQL("create table t_score(id integer"
                                + " primary key autoincrement,"
                                + " userid integer,"
                                + " score integer," +
                                "date varchar(50))");
                        // 执行insert语句插入数据
                        insertData(db, name, pwd);

                    }
                    Toast.makeText(register.this,"注册成功",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(register.this,"两次密码不一致",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void insertData(SQLiteDatabase db
            , String name, String pwd) {
        // 执行插入语句
        Cursor cursor = db.rawQuery("select * from t_user where username = ? "
                , new String[] {name});
        if(cursor.getCount()==0){
            db.execSQL("insert into t_user values(null , ? , ?)"
                    , new String[] {name, pwd });
        }else{
            Toast.makeText(register.this,"该用户名已被注册",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        // 退出程序时关闭SQLiteDatabase
        if (db != null && db.isOpen())
        {
            db.close();
        }
    }
}
