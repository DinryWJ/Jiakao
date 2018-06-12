package com.example.a82712.jiakao;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class Question extends Activity {
    //成绩结果数组
    static int[][] score = new int[100][100];
    //题号
    static int count = -1;

    //题目列表
    List<QuestionHolder> list;

    SQLiteDatabase db;
    Bundle bundle;
    String currentname;
    int currentid;
    //QuestionHolder holder;
    Button back;
    Button over;
    TextView countdown;
    Button fontsize;
    Button next;
    TextView qtitle;
    ImageView imageView;
    Handler handler;
    Handler handler2;
    RadioGroup radiogroup;
    RadioButton item1;
    RadioButton item4;
    RadioButton item3;
    RadioButton item2;
    TextView correct;
    TextView wrong;
    TextView sum;

    int correctNum = 0;
    int wrongNum = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question);
        ActivityController.addActivity(this);
        bundle = this.getIntent().getExtras();
        currentname = bundle.getString("currentName");
        currentid = bundle.getInt("currentId");
        db = SQLiteDatabase.openOrCreateDatabase(
                this.getFilesDir().toString()
                        + "/Jiakao.db3", null);

        back = findViewById(R.id.back);
        countdown = findViewById(R.id.countdown);
        fontsize = findViewById(R.id.fontsize);
        next = findViewById(R.id.next);
        over = findViewById(R.id.over);
        qtitle = findViewById(R.id.qtitle);
        imageView = findViewById(R.id.imageView);
        radiogroup = findViewById(R.id.radiogroup);
        item1 = findViewById(R.id.item1);
        item2 = findViewById(R.id.item2);
        item3 = findViewById(R.id.item3);
        item4 = findViewById(R.id.item4);
        correct = findViewById(R.id.correct);
        wrong = findViewById(R.id.wrong);
        sum = findViewById(R.id.sum);

        //获取题目
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Bundle data = msg.getData();
                String str = data.getString("strValue");
                parseJsonWithGson(str);
                count++;
                setQuestion(count);
            }
        };
        //对有图片的题目添加图片
        handler2 = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Bundle data = msg.getData();
                Bitmap val;
                if(data.getBoolean("isValid")) {
                    val = data.getParcelable("bitmap");
                    imageView.setImageBitmap(val);
                }else {
                    imageView.setImageDrawable(null);
                }

            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                Object obj = JuheDemo.getRequest1();
                Message msg = new Message();
                Bundle data = new Bundle();
                data.putString("strValue",String.valueOf(obj));
                msg.setData(data);
                handler.sendMessage(msg);
            }
        }).start();

        //下一题
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(count>=100){
                    Toast.makeText(Question.this,"已经是最后一题了！",Toast.LENGTH_SHORT);
                }else{
                    int currentAnswer = radiogroup.indexOfChild(findViewById(radiogroup.getCheckedRadioButtonId()))+1;
                    if(currentAnswer == list.get(count).getAnswer()){
                        correctNum++;
                        correct.setText(correctNum+"");
                    }else{
                        wrongNum++;
                        wrong.setText(wrongNum+"");
                    }
                    sum.setText(correctNum+wrongNum+1+"/100");

                    //保存成绩结果
                    score[0][count] = currentAnswer;
                    score[1][count] = list.get(count).getAnswer();

                    //下一题
                    count++;
                    setQuestion(count);
                }
            }
        });

        over.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //保存成绩
                saveScore(currentid,correctNum);

                Intent intent = new Intent(Question.this,Conclude.class);
                Bundle bundle = new Bundle();
                bundle.putInt("result",correctNum);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private void saveScore(int uid, int usc) {
        db.execSQL("insert into t_score(userid,score) values(?,?)"
                , new Object[] {uid,usc});
    }

    private void setQuestion(final int count) {
        if(count>=100){
            Toast.makeText(Question.this,"已经是最后一题了！",Toast.LENGTH_SHORT);
        }else{
            //holder = list.get(count);
            qtitle.setText(list.get(count).getQuestion());
            //图片处理
            String url = list.get(count).getUrl();
            if(url == null || url.equals("")){
                Log.v("Msg","本题图片为空");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message msg = new Message();
                        Bundle data = new Bundle();
                        data.putBoolean("isValid",false);
                        msg.setData(data);
                        handler2.sendMessage(msg);
                    }
                }).start();
            } else{
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Bitmap bitmap = BitmapUtil.getInstance().returnBitMap(list.get(count).getUrl());
                        Message msg = new Message();
                        Bundle data = new Bundle();
                        data.putParcelable("bitmap", bitmap);
                        data.putBoolean("isValid",true);
                        msg.setData(data);
                        handler2.sendMessage(msg);
                    }
                }).start();
            }
            item1.setText(list.get(count).getItem1());
            item2.setText(list.get(count).getItem2());
            item3.setText(list.get(count).getItem3());
            item4.setText(list.get(count).getItem4());

            //细节处理
            if(item1.getText().toString()==null|| item1.getText().toString().equals("")){
                item1.setText("正确");
                item2.setText("错误");
            }
            if(item3.getText().toString()==null|| item3.getText().toString().equals("")){
                item3.setVisibility(View.GONE);
            }else{
                item3.setVisibility(View.VISIBLE);
            }
            if(item4.getText().toString()==null|| item4.getText().toString().equals("")){
                item4.setVisibility(View.GONE);
            }else{
                item4.setVisibility(View.VISIBLE);
            }
        }

    }

    private void parseJsonWithGson(String jsonData){
        Gson gson = new Gson();
        list = gson.fromJson(jsonData,new TypeToken<List<QuestionHolder>>(){}.getType());
        //TODO print
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityController.removeActivity(this);
        if (db != null && db.isOpen())
        {
            db.close();
        }
    }
}
