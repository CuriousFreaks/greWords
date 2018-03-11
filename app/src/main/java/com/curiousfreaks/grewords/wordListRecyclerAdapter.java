package com.curiousfreaks.grewords;


import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by gasaini on 2/24/2018.
 */

public class wordListRecyclerAdapter extends RecyclerView.Adapter<wordListRecyclerAdapter.ViewHolder> implements View.OnClickListener{

    private List<wordDefinition> completeWordList;
    myRecyclerItemClickListner myListner;

    public wordListRecyclerAdapter(List<wordDefinition> alist)
    {

        completeWordList=alist;
    }

    public interface myRecyclerItemClickListner{
        void onIDClicked(View view, int position, long uniqueId);
        void onWordClicked(View view, int position, long uniqueId);
        void onMeaningClicked(View view, int position, long uniqueId);
        void onStarClicked(View view, int position, long uniqueId);

    }
    public void setOnItemCLickListner(myRecyclerItemClickListner lstn)
    {
        this.myListner=lstn;
    }

    @Override
    public void onClick(View view) {
        if(view instanceof TextView)
        {
            Log.v(MainActivity.TAG,"id clickeddddddd");
        }

    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView id,word,meaning;
        ImageView starImage;
        public ViewHolder(View view)
        {
            super(view);
            id=(TextView)view.findViewById(R.id.wid);
            word=(TextView)view.findViewById(R.id.word);
            meaning=(TextView)view.findViewById(R.id.meaning);
            starImage=(ImageView)view.findViewById(R.id.starImage);
        }

    }

    @Override
    public wordListRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.one_word_layout, parent, false);
        ViewHolder vh = new ViewHolder(v);
        /*v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v(MainActivity.TAG,"click on view");
            }
        });*/
        return vh;
    }

    @Override
    public void onBindViewHolder(wordListRecyclerAdapter.ViewHolder holder, final int position) {
        wordDefinition wd= completeWordList.get(position);
        final long uniqueID=wd.getId();
        holder.id.setText(Long.toString(wd.getId()));
        holder.meaning.setText(wd.getMeaning());
        holder.word.setText(wd.getWord());
        if((wd.getBookmarked()).equals("YES"))
            holder.starImage.setImageResource(R.mipmap.yellow_star);
        else
            holder.starImage.setImageResource(R.mipmap.bw_star);

        holder.id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myListner.onIDClicked(view,position,uniqueID);
            }
        });
        holder.word.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myListner.onWordClicked(view,position,uniqueID);
            }
        });
        holder.meaning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myListner.onMeaningClicked(view,position,uniqueID);
            }
        });
        holder.starImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myListner.onStarClicked(view,position,uniqueID);
            }
        });

    }

    @Override
    public int getItemCount() {

        return completeWordList.size();
    }



}
