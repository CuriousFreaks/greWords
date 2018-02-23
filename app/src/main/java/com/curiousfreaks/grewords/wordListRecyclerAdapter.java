package com.curiousfreaks.grewords;


import android.content.Context;
import android.graphics.Movie;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gasaini on 2/24/2018.
 */

public class wordListRecyclerAdapter extends RecyclerView.Adapter<wordListRecyclerAdapter.ViewHolder> {

    private List<wordDefinition> completeWordList;
    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView id,word,attr1;
        public ViewHolder(View view)
        {
            super(view);
            id=(TextView)view.findViewById(R.id.wid);
            word=(TextView)view.findViewById(R.id.word);
            attr1=(TextView)view.findViewById(R.id.attr1);
        }

    }
    public wordListRecyclerAdapter(List<wordDefinition> alist)
    {
        completeWordList=alist;
    }
    @Override
    public wordListRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.onerowspecification, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }
    @Override
    public void onBindViewHolder(wordListRecyclerAdapter.ViewHolder holder, int position) {
        wordDefinition wd= completeWordList.get(position);
        holder.id.setText(Long.toString(wd.getId()));
        holder.attr1.setText(wd.getAttr1());
        holder.word.setText(wd.getWord());

    }

    @Override
    public int getItemCount() {
        return completeWordList.size();
    }
}
