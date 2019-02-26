package com.lhn.yourstory.lhnnote;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.lhn.yourstory.lhnnote.adapter.MenuRecyclerAdapter;
import com.lhn.yourstory.lhnnote.util.Util;

public class MenuActivity extends AppCompatActivity {


    private Toolbar toolbar;
    private Button btnCancel;
    private RecyclerView recycler;
    private int menuType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        initIntent();
        initView();
        initRecycler();
    }

    private void initIntent() {
        Intent intent=getIntent();
        menuType=intent.getIntExtra("menuType",0);
    }

    private void initView() {
        recycler=findViewById(R.id.rv_menu);
        btnCancel=findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //设置状态栏
        Util.setWindowsColor(this);
    }

    private void initRecycler() {
        if (menuType==0){
            MenuRecyclerAdapter adapter=new MenuRecyclerAdapter(this);
            LinearLayoutManager manager=new LinearLayoutManager(this);
            recycler.setLayoutManager(manager);
            //添加每行的分割线
            recycler.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
            recycler.setAdapter(adapter);
        }
    }
}
