package com.lhn.yourstory.lhnnote;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lhn.yourstory.lhnnote.constant.Constant;
import com.lhn.yourstory.lhnnote.db.MyDBDao;
import com.lhn.yourstory.lhnnote.util.Util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class UpdateActivity extends AppCompatActivity {

    private Button btnRefresh;
    private Button btnHold;
    private EditText etTitle;
    private EditText etContent;

    private int id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        initView();
        Util.setWindowsColor(this);
        //保存键事件
        btnHold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title=etTitle.getText().toString();
                String content=etContent.getText().toString();
                String time;
                if (!title.equals("")){
                    MyDBDao dbDao=new MyDBDao(UpdateActivity.this);
                    //获取当前系统事件
                    Date date = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat(Constant.TIME_FORMAT);
                    time = sdf.format(date);
                    if (dbDao.update(id,title,time,content)==1){
                        Toast.makeText(UpdateActivity.this,"修改成功",
                                Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }
        });
        //退出键事件
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(UpdateActivity.this);
                dialog.setTitle("确认不保存退出？");
                dialog.setCancelable(true);
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                dialog.show();
            }
        });



    }

    private void initView() {
        //获取传来的数据
        Intent intent=getIntent();
        String title=intent.getStringExtra("title");
        String content=intent.getStringExtra("content");
        id=intent.getIntExtra("id",-1);
        btnRefresh=findViewById(R.id.btn_refresh);
        btnHold = findViewById(R.id.btn_hold);
        etTitle = findViewById(R.id.et_title);
        etContent = findViewById(R.id.et_content);
        etTitle.setText(title);
        etContent.setText(content);

    }

}
