package com.lhn.yourstory.lhnnote.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lhn.yourstory.lhnnote.R;
import com.lhn.yourstory.lhnnote.UpdateActivity;
import com.lhn.yourstory.lhnnote.bean.MyNote;
import com.lhn.yourstory.lhnnote.db.MyDBDao;
import com.lhn.yourstory.lhnnote.util.NetUtil;

import java.util.List;

/**
 * Created by lenovo on 2018/10/10.
 *
 */

public class HeadRecyclerAdapter extends RecyclerView.Adapter {


    private List<MyNote> noteList;
    private Context context;

    private HeadRecyclerAdapter adapter;

    public HeadRecyclerAdapter(List<MyNote> noteList, Context context) {
        this.noteList = noteList;
        this.context = context;
        adapter=this;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        View item;
        TextView title;
        TextView time;
        TextView content;
        public ViewHolder(View itemView) {
            super(itemView);
            this.title=itemView.findViewById(R.id.text_title);
            this.time=itemView.findViewById(R.id.text_time);
            this.content=itemView.findViewById(R.id.text_content);
            this.item=itemView.findViewById(R.id.item_note);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_head_note,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ViewHolder myHolder= (ViewHolder) holder;

        final String titleStr = noteList.get(position).getNote_title();
        final String contentStr=noteList.get(position).getNote_message();
        final int noteID = noteList.get(position).getNote_id();
        myHolder.title.setText(titleStr);
        myHolder.content.setText(contentStr);
        myHolder.time.setText(noteList.get(position).getNote_time());

        //设置点击事件
        myHolder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, UpdateActivity.class);
                intent.putExtra("title",titleStr);
                intent.putExtra("content",contentStr);
                intent.putExtra("id",noteID);
                context.startActivity(intent);
            }
        });


        //设置长按点击事件
        myHolder.item.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                dialog.setTitle("你确定要删除 " + titleStr + " 吗？");
                dialog.setCancelable(true);
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        MyDBDao myDBDao = new MyDBDao(context);

                        if (NetUtil.isNetworkAvailable(context)) {
                            //如果网络可用
                            //删除本地数据库数据
                            if (myDBDao.delete(noteID) == 0) {
                                Toast.makeText(context, "删除出错", Toast.LENGTH_SHORT).show();
                            } else {
                                noteList.remove(position);
                                adapter.notifyDataSetChanged();
                            }
                            //删除服务器端数据
                            NetUtil.deleteOnServer(noteID);

                        } else {
                            //如果网络不可用,只删除本地数据
                            if (myDBDao.delete(noteID) == 0) {
                                Toast.makeText(context, "删除出错", Toast.LENGTH_SHORT).show();
                            } else {
                                noteList.remove(position);
                                adapter.notifyDataSetChanged();
                            }
                        }

                    }
                });
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                dialog.show();
                return true;

            }
        });
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    public void updateList(List<MyNote> noteList) {
        this.noteList = noteList;
    }
}
