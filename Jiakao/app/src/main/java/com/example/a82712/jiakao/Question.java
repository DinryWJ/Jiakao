package com.example.a82712.jiakao;

import android.app.Activity;
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
    Bundle bundle;
    String currentname;
    int currentid;
    //QuestionHolder holder;
    Button back;
    TextView countdown;
    Button fontsize;
    Button next;
    TextView qtitle;
    ImageView imageView;
    Handler handler;
    RadioGroup radiogroup;
    RadioButton item1;
    RadioButton item4;
    RadioButton item3;
    RadioButton item2;
    TextView correct;
    TextView wrong;
    TextView sum;
    List<QuestionHolder> list;
    int count = -1;
    int correctNum = 0;
    int wrongNum = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question);
        bundle = this.getIntent().getExtras();
        currentname = bundle.getString("currentName");
        currentid = bundle.getInt("currentId");
        back = findViewById(R.id.back);
        countdown = findViewById(R.id.countdown);
        fontsize = findViewById(R.id.fontsize);
        next = findViewById(R.id.next);
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
                int currentAnswer = radiogroup.indexOfChild(findViewById(radiogroup.getCheckedRadioButtonId()))+1;
                if(currentAnswer == list.get(count).getAnswer()){
                    correctNum++;
                    correct.setText(correctNum+"");
                }else{
                    wrongNum++;
                    wrong.setText(wrongNum+"");
                }
                sum.setText(correctNum+wrongNum+1+"/100");

                count++;
                setQuestion(count);
            }
        });
    }

    private void setQuestion(int count) {
        if(count>=100){
            Toast.makeText(Question.this,"已经是最后一题了！",Toast.LENGTH_SHORT);
        }else{
            //holder = list.get(count);
            qtitle.setText(list.get(count).getQuestion());
            //TODO setImage(holder.getUrl());
            item1.setText(list.get(count).getItem1());
            item2.setText(list.get(count).getItem2());
            item3.setText(list.get(count).getItem3());
            item4.setText(list.get(count).getItem4());
        }

    }

    private void parseJsonWithGson(String jsonData){
        Gson gson = new Gson();
        list = gson.fromJson(jsonData,new TypeToken<List<QuestionHolder>>(){}.getType());
//        for (QuestionHolder holder : list){
//            System.out.print("MainActivity"+"id is"+holder.getId());
//            System.out.print("MainActivity"+"question is"+holder.getQuestion());
//            System.out.print("MainActivity"+"answer is"+holder.getAnswer());
//            System.out.print("MainActivity"+"item1 is"+holder.getItem1());
//            System.out.print("MainActivity"+"item2 is"+holder.getItem2());
//            System.out.print("MainActivity"+"item3 is"+holder.getItem3());
//            System.out.print("MainActivity"+"item4 is"+holder.getItem4());
//            System.out.print("MainActivity"+"explains is"+holder.getExplains());
//            System.out.print("MainActivity"+"url is"+holder.getUrl());
//            System.out.println();
//        }
    }

}
