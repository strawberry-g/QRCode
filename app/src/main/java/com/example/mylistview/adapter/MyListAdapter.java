package com.example.mylistview.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.mylistview.R;
import com.example.mylistview.model.Goods;

import java.util.List;

public class MyListAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<Goods> goodsList;

    public MyListAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public MyListAdapter(Context context, List<Goods> goodsList) {
        this.context = context;
        this.goodsList = goodsList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        //return goodsList.size();
        return 10;
    }

    @Override
    public Object getItem(int i) {
        //return goodsList.get(i);
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    static class ViewHolder{
        public TextView date;
        public TextView type;
        public TextView number;
        public TextView person;
        public TextView state;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        //Goods goods = (Goods) getItem(i);

        if (viewHolder == null){
            view = inflater.inflate(R.layout.lv_item,null);
            viewHolder = new ViewHolder();
            viewHolder.date = view.findViewById(R.id.date);
            viewHolder.type = view.findViewById(R.id.type);
            viewHolder.number = view.findViewById(R.id.number);
            viewHolder.person = view.findViewById(R.id.person);
            viewHolder.state = view.findViewById(R.id.state);
            view.setTag(viewHolder);
        }else{
            viewHolder  = (ViewHolder) view.getTag();
        }

        //viewHolder.date.setText(goods.getDate());
        viewHolder.date.setText("date");
        viewHolder.type.setText("type");
        viewHolder.number.setText("number");
        viewHolder.person.setText("person");
        viewHolder.state.setText("state");

        return view;
    }
}
