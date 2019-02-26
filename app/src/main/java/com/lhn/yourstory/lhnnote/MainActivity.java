package com.lhn.yourstory.lhnnote;


import android.content.Intent;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Button;

import com.lhn.yourstory.lhnnote.adapter.HeadRecyclerAdapter;
import com.lhn.yourstory.lhnnote.bean.MyNote;
import com.lhn.yourstory.lhnnote.db.MyDBDao;
import com.lhn.yourstory.lhnnote.util.Util;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnUser;
    private Button btnStyle;
    private RecyclerView recycler;
    private FloatingActionButton floatButton;
    private MyDBDao mDao;
    private List<MyNote> noteList = new ArrayList<>();
    private static HeadRecyclerAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initRecycler();
        Util.setWindowsColor(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //刷新recycler
        refreshList();
    }

    private void initView() {
        btnUser=findViewById(R.id.btn_head_toUser);
        btnStyle=findViewById(R.id.btn_head_style);
        recycler=findViewById(R.id.rv_head);
        floatButton=findViewById(R.id.fab_addNote);
        mDao=new MyDBDao(this);
        btnUser.setOnClickListener(this);
        floatButton.setOnClickListener(this);
        btnStyle.setOnClickListener(this);
    }


    private void initRecycler() {
        //适配器,创建Dao类，使用其中的方法
        noteList = mDao.queryAll();
        adapter = new HeadRecyclerAdapter(noteList,this);
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(
                2,StaggeredGridLayoutManager.VERTICAL);
        recycler.setLayoutManager(manager);
        //添加每行的分割线
//        recycler.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recycler.setAdapter(adapter);
    }



    /**
     * 点击事件
     * @param view 主页面
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_head_toUser:
                Intent intentToUser = new Intent(this, UserActivity.class);
                startActivity(intentToUser);
                break;

            case R.id.btn_head_style:
                //根据当前的style来更换列表模式
                if (recycler.getLayoutManager() instanceof StaggeredGridLayoutManager){
                    //若是瀑布流模式，则更换到列表模式
                    LinearLayoutManager manager=new LinearLayoutManager(this);
                    recycler.setLayoutManager(manager);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        btnStyle.setBackground(getDrawable(R.drawable.liststyle_list));
                    }
                }else {
                    StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(
                            2,StaggeredGridLayoutManager.VERTICAL);
                    recycler.setLayoutManager(manager);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        btnStyle.setBackground(getDrawable(R.drawable.liststyle_patch));
                    }
                }
                break;
            case R.id.fab_addNote:
                Intent intentNewNote = new Intent(this, NewNoteActivity.class);
                startActivity(intentNewNote);
                break;
        }
    }


    /**
     * 刷新list列表
     */
    private static void refreshList(){
        MyDBDao mDao=new MyDBDao(MyApplication.getContext());
        List<MyNote> noteList = mDao.queryAll();
        adapter.updateList(noteList);
        adapter.notifyDataSetChanged();
    }
}
