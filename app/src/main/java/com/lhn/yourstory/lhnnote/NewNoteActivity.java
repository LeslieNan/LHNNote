package com.lhn.yourstory.lhnnote;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lhn.yourstory.lhnnote.constant.Constant;
import com.lhn.yourstory.lhnnote.db.MyDBDao;
import com.lhn.yourstory.lhnnote.util.Util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NewNoteActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnCancel;
    private Button btnRefresh;
    private Button btnHold;
    private EditText etTitle;
    private EditText etContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);
        initView();
        Util.setWindowsColor(this);
    }

    private void initView() {
        btnCancel = findViewById(R.id.btn_hold);
        btnRefresh = findViewById(R.id.btn_refresh);
        btnHold = findViewById(R.id.btn_hold);
        etTitle = findViewById(R.id.et_title);
        etContent = findViewById(R.id.et_content);
        //绑定点击事件
        btnCancel.setOnClickListener(this);
        btnRefresh.setOnClickListener(this);
        btnHold.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_hold:
                saveNote();
                break;
            case R.id.btn_refresh:
                if (!etContent.getText().toString().equals("") || !etTitle.getText().toString().equals("")) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(this);
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
                }else {
                    finish();
                }
                break;
        }
    }

    //保存笔记
    private void saveNote() {
        if (etTitle.getText().toString().equals("")) {
            Toast.makeText(this, "标题不能为空", Toast.LENGTH_SHORT).show();
        } else {
            //获取当前系统时间
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat(Constant.TIME_FORMAT);
            String nowTime = sdf.format(date);
            String title = etTitle.getText().toString();
            String content = etContent.getText().toString();
            Log.d("我的", nowTime);

            //插入信息到到数据库
            MyDBDao dbDao = new MyDBDao(this);
            int i = dbDao.insert(title, nowTime, content);
            if (i == 1) {
                Toast.makeText(this, "新笔记保存成功", Toast.LENGTH_LONG).show();
                etTitle.setText("");
                etContent.setText("");
                finish();
            }
        }
    }
}
