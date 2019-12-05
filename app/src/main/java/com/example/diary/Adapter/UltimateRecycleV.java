package com.example.diary.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diary.EditActivity;
import com.example.diary.Model.Item;
import com.example.diary.Model.User;
import com.example.diary.R;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.ramotion.foldingcell.FoldingCell;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

class LoadingViewHolder extends RecyclerView.ViewHolder {
    public ProgressBar progressBar;


    public LoadingViewHolder(View itemView) {
        super(itemView);
        progressBar = itemView.findViewById(R.id.progressBar);
    }
}
class ItemViewHolder extends RecyclerView.ViewHolder {
    public TextView i1, i2,i3,i4,i5,i6;
    public FoldingCell FCell;

    public ItemViewHolder(View itemView) {
        super(itemView);
        i1 = itemView.findViewById(R.id.card_item1);
        i2 = itemView.findViewById(R.id.card_item2);
        i3 = itemView.findViewById(R.id.displayTime1);
        i5 = itemView.findViewById(R.id.card_item2_2);
        i6 = itemView.findViewById(R.id.displayTime);
        i4 = itemView.findViewById(R.id.dayUp);
        FCell= itemView.findViewById(R.id.folding_cell);
    }
}

public class UltimateRecycleV extends RecyclerView.Adapter {
    private final int VIEW_TYPE_ITEM = 0, VIEW_TYPE_LOADING = 1;
    //ILoadmore loadMore;
    boolean isLoading;
    Context context;
    ArrayList<Item> items;
    ArrayList<User> users;


    public  UltimateRecycleV(Context context, ArrayList<Item> list,ArrayList<User> users)
    {
        this.context=context;
        this.items=list;
        this.users=users;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(context)
                    .inflate(R.layout.items, parent, false);
            final RecyclerView.ViewHolder holder = new ItemViewHolder(view);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "" + items.get(holder.getPosition()).getTitle(), Toast.LENGTH_SHORT).show();
                }
            });

            return holder;
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(context)
                    .inflate(R.layout.item_loading, parent, false);

            return new LoadingViewHolder(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            Item item = items.get(position);
            final ItemViewHolder viewHolder = (ItemViewHolder) holder;

            final String key=items.get(position).getId();
            final String key1=items.get(position).getTitle();
            final String key2=items.get(position).getContent();
            String userName="";
            for (User u:users) {
                if(u.getId().equals(items.get(position).getuID()))
                    userName=u.getName();
            }

            viewHolder.i1.setText(items.get(position).getContent());
            viewHolder.i2.setText(String.valueOf(items.get(position).getTitle()));
            viewHolder.i5.setText(String.valueOf(items.get(position).getTitle()));
            String time=Daynum(items.get(position).getTime());

            //int d = Integer.parseInt(time.substring(0,1));
            Date today =new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy kk:mm");
            //String td = formatter.format(today).substring(0,1);
            Date diaryTime=null;
            try {
            diaryTime=formatter.parse(time);
            } catch (ParseException e) {
                Log.e("TEST", "Exception", e);
            }
            long diff = today.getTime() - diaryTime.getTime();

            viewHolder.i4.setText(diff/ 1000 / 60 / 60 / 24+" days ago! by "+userName);
            viewHolder.i3.setText(time.substring(11));
            viewHolder.i6.setText(time.substring(11));
            viewHolder.FCell.setBackgroundColor(Color.parseColor(items.get(position).getColor()));
            viewHolder.FCell.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    viewHolder.FCell.toggle(false);
                }
            });
            viewHolder.FCell.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Editing(key,key1,key2);
                    return true;
                }
            });
        } else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }

    private void Editing(String key,String key1,String key2) {
        Intent intent = new Intent(context, EditActivity.class);
        intent.putExtra("key",key);
        intent.putExtra("title",key1);
        intent.putExtra("content",key2);
        context.startActivity(intent);
    }

    private String Daynum(String time) {
        String result="";
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy kk:mm");
        result = formatter.format(new Date(Long.parseLong(time)));
        return result;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
    @Override
    public int getItemViewType(int position) {
        return items.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;// So sánh nếu item được get tại vị trí này là null thì view đó là loading view , ngược lại là item
    }
}
