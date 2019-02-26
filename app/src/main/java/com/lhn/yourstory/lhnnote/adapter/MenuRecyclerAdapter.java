package com.lhn.yourstory.lhnnote.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lhn.yourstory.lhnnote.AboutUsActivity;
import com.lhn.yourstory.lhnnote.MenuActivity;
import com.lhn.yourstory.lhnnote.R;
import com.lhn.yourstory.lhnnote.constant.Constant;
import com.lhn.yourstory.lhnnote.util.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2018/10/10.
 * 菜单页面的适配器
 */

public class MenuRecyclerAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<String> menuList = new ArrayList<>();

    public MenuRecyclerAdapter(Context context) {
        this.context = context;
        //初始化menuList
        menuList.add("关于");
        menuList.add("退出登录");
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        View view;
        TextView tvTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_menu_title);
            view = itemView;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_menu, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder myHolder = (ViewHolder) holder;
        if (position == 0) {
            myHolder.tvTitle.setText(menuList.get(0));
            myHolder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, AboutUsActivity.class);
                    context.startActivity(intent);
                }
            });
        } else if (position == 1) {
            myHolder.tvTitle.setText(menuList.get(1));
            myHolder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //显示是否真的确认退出
                    AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                    dialog.setTitle("退出前记得先同步笔记哦，是否退出?");
                    dialog.setCancelable(true);
                    dialog.setPositiveButton("退出", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //清除登录账号信息
                            Util.setSharedPreferences(Constant.LOGIN_OUT);
                            //清除所有note
                            Util.deleteAllNote();
                            ((MenuActivity) context).finish();
                        }
                    });
                    dialog.setNegativeButton("再想想", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    dialog.show();
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }
}
