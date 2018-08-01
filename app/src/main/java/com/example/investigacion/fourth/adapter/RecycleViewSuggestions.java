package com.example.investigacion.fourth.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.investigacion.fourth.R;

import com.example.investigacion.fourth.model.Suggestions;
import com.example.investigacion.fourth.model.UserData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RecycleViewSuggestions extends RecyclerView.Adapter<RecycleViewSuggestions.ViewHolder> {



    public interface OnItemClickListener {
        void onItemClick( Suggestions.UserCode item);
    }

    private  ArrayList<Suggestions.UserCode> mDataset;
    private final OnItemClickListener listener;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {



        private LinearLayout mLinearViewFrame;
        public TextView mTextViewperson;
        public TextView mTextViewdate;
        // each data item is just a string in this case
        public TextView mTextView;





        public ViewHolder(View v ) {
            super(v);

            mTextViewperson = (TextView) v.findViewById(R.id.txt_people);
            mTextViewdate =  (TextView) v.findViewById(R.id.txt_dob);
            mLinearViewFrame  = (LinearLayout) v.findViewById(R.id.recyclerViewFrame);
            mLinearViewFrame.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
        }


        public void bind(final Suggestions.UserCode item, final OnItemClickListener listener) {
            mLinearViewFrame.setOnClickListener(new View.OnClickListener() {


                @Override
                public void onClick(View view) {
                    listener.onItemClick(item);
                }
            });
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public RecycleViewSuggestions(ArrayList<Suggestions.UserCode>  myDataset, OnItemClickListener listener) {
        this.mDataset = myDataset;
        this.listener = listener;
    }







    // Create new views (invoked by the layout manager)
    @Override
    public RecycleViewSuggestions.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v =  LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleview_suggestions,parent,false);




        RecycleViewSuggestions.ViewHolder vh = new RecycleViewSuggestions.ViewHolder(v);
        return vh;
    }





    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecycleViewSuggestions.ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        holder.mTextViewperson.setText(mDataset.get(position).value);
        holder.bind(mDataset.get(position),listener);




    }






    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }




    public void swap(ArrayList<Suggestions.UserCode> list){
        if (mDataset != null) {
            mDataset.clear();
            mDataset.addAll(list);
        }
        else {
            mDataset = list;
        }
        notifyDataSetChanged();
    }
}
