package com.lhn.yourstory.lhnnote;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.lhn.yourstory.lhnnote.bean.MyNote;
import com.lhn.yourstory.lhnnote.db.MyDBDao;
import com.lhn.yourstory.lhnnote.util.NetUtil;
import com.lhn.yourstory.lhnnote.util.Util;
import com.lhn.yourstory.lhnnote.view.CircleImageView;
import com.google.gson.Gson;

import java.util.List;

public class UserActivity extends AppCompatActivity implements View.OnClickListener {


    private CircleImageView ivHead;
    private TextView tvName;
    private Button btnCancel;
    private Button btnTong;
    private View viewSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        initView();
        Util.setWindowsColor(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        String name = Util.getLoginInfo();
        tvName.setText(name);
        if (name.equals("未登录")) {
            ivHead.setImageResource(R.mipmap.icon);
            btnTong.setEnabled(false);
        } else {
            //在此更换头像
//            Picasso picasso = Picasso.with(getContext());
//            picasso.load(Constant.URL_ICON).into(ivHead);
            btnTong.setEnabled(true);
        }
    }

    private void initView() {
        ivHead = findViewById(R.id.civ_user_head);
        tvName = findViewById(R.id.tv_user_name);
        btnTong = findViewById(R.id.btn_user_tong);
        btnCancel=findViewById(R.id.btn_cancel);
        viewSetting = findViewById(R.id.view_setting);
        ivHead.setOnClickListener(this);
        tvName.setOnClickListener(this);
        btnTong.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        viewSetting.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_cancel:
                finish();
                break;
            case R.id.view_setting:
                Intent intentMenu = new Intent(this, MenuActivity.class);
                startActivity(intentMenu);
                break;
            case R.id.civ_user_head:
                if (Util.getLoginInfo().equals("未登录")){
                    Intent intentLogin=new Intent(this,LoginActivity.class);
                    startActivity(intentLogin);
                }else {
                    //我的信息界面
                }
                break;
            case R.id.tv_user_name:

                break;
            case R.id.btn_user_tong:
                if (NetUtil.isNetworkAvailable(this)) {
                    upload();
                } else {
                    Toast.makeText(this, "当前网络不可用", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    /**
     * 上传笔记数据
     */
    private void upload() {
        //生成json数据
        MyDBDao dao = new MyDBDao(this);
        Gson gson = new Gson();
        List<MyNote> list = dao.queryAll();
        Log.d("我的1", list.toString());
        //获取登录id
        String user_id = Util.getLoginInfo();
        String jsonStr;
        if (list.size() != 0) {
            //给每条笔记MyNote中的user_id添加值,因为数据库中不存user_id
            for (int i = 0; i < list.size(); i++) {
                list.get(i).setUser_id(user_id);
                Log.d("我的1", list.get(i).getUser_id());
            }
            jsonStr = gson.toJson(list);
            Log.d("我的1", jsonStr);
            //上传json数据
            NetUtil.uploadData(this, jsonStr);
        } else {
            NetUtil.downloadData(this);
        }
    }
}
