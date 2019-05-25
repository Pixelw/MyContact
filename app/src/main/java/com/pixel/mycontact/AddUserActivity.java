package com.pixel.mycontact;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.pixel.mycontact.beans.People;
import com.pixel.mycontact.daos.PeopleDB;

import java.util.Calendar;

public class AddUserActivity extends AppCompatActivity {

    private EditText nameText;
    private EditText lastNameText;
    private EditText numText;
    private EditText num2Text;
    private EditText emailText;
    private TextView dateAdd;
    private EditText noteAdd;
    private int birthYear;
    private int birthMonth;
    private int birthDay;

    private int nowYear;
    private int nowMonth;
    private int nowDay;

    private PeopleDB peopleDB;
    private People people;

    private boolean isModify = false;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.adduser_toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = -39;//id 缺省值，不写入数据库
        //判断是否为修改模式，是修改模式的话id沿用数据库的id，方便数据库识别；
        if (isModify) {
            id = people.getId();
        }
        if (item.getItemId() == R.id.accept) {
            People savpeople = new People(
                    nameText.getText().toString(),
                    lastNameText.getText().toString(),
                    numText.getText().toString(),
                    num2Text.getText().toString(),
                    emailText.getText().toString(),
                    birthYear, birthMonth, birthDay,
                    noteAdd.getText().toString(),
                    id
            );
            Log.d("peopleToString", savpeople.toString());
            if (nameText.getText().toString().equals("")) {//判断至少填写一个电话一个姓名
                if (num2Text.getText().toString().equals("") || numText.getText().toString().equals(""))
                    Toast.makeText(AddUserActivity.this,
                            getString(R.string.missingname), Toast.LENGTH_SHORT).show();

            } else {
                if (isModify) {//修改模式，调用update方法

                    if (peopleDB.updateContact(savpeople) > 0) {
                        Toast.makeText(AddUserActivity.this, getString(R.string.contactsave),
                                Toast.LENGTH_SHORT).show();
                        finish();
                    }

                } else {//创建模式，调用insert方法；
                    if (peopleDB.insertContact(savpeople) > 0) {
                        Toast.makeText(AddUserActivity.this, getString(R.string.contactsave),
                                Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }

            }
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_user);
        Toolbar toolbar = findViewById(R.id.toolbarAdd);
        nameText = findViewById(R.id.nameAdd);
        lastNameText = findViewById(R.id.lastNameAdd);
        numText = findViewById(R.id.numAdd);
        num2Text = findViewById(R.id.num2Add);
        emailText = findViewById(R.id.emailAdd);
        dateAdd = findViewById(R.id.dateAdd);
        noteAdd = findViewById(R.id.noteAdd);

        //解析intent，如果有people对象则将对象的值填入文本框内，进入修改模式；
        Intent intent = getIntent();
        if (intent.hasExtra("people")) {
            people = (People) intent.getSerializableExtra("people");
            toolbar.setTitle(R.string.editcontact);
            toModify();
            isModify = true;
        } else {
            toolbar.setTitle(R.string.createcontact);
        }
        setSupportActionBar(toolbar);
        //返回键
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        peopleDB = new PeopleDB(AddUserActivity.this);
        //初始化日历，获取现在日期；
        Calendar calendar = Calendar.getInstance();
        nowYear = calendar.get(Calendar.YEAR);
        nowMonth = calendar.get(Calendar.MONTH);
        nowDay = calendar.get(Calendar.DAY_OF_MONTH);
        //创建选择日期对话框
        dateAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(AddUserActivity.this, onDateSetListener,
                        nowYear, nowMonth, nowDay).show();
            }
        });

    }

    @SuppressLint("SetTextI18n")
    //载入要修改的联系人
    private void toModify() {
        nameText.setText(people.getFirstName());
        lastNameText.setText(people.getLastName());
        numText.setText(people.getNumber1());
        num2Text.setText(people.getNumber2());
        emailText.setText(people.getEmail());
        noteAdd.setText(people.getNote());
        if (people.getBirthMonth() != 0) {
            nowDay = people.getBirthDay();
            nowMonth = people.getBirthMonth();
            nowYear = people.getBirthYear();
            dateAdd.setText(people.getBirthYear() + "/" + people.getBirthMonth() + "/" + people.getBirthDay());
        }
    }
    //选择日期操作，填入圆角矩形的TextView内
    private DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            birthYear = year;
            birthMonth = month + 1;
            birthDay = dayOfMonth;
            String dateText = birthYear + "/" + birthMonth + "/" + birthDay;
            if (birthYear >= nowYear && birthMonth >= nowMonth && birthDay > nowDay) {
                Toast.makeText(AddUserActivity.this, getString(R.string.futurebirth),
                        Toast.LENGTH_SHORT).show();
            }
            dateAdd.setText(dateText);
        }
    };
}
