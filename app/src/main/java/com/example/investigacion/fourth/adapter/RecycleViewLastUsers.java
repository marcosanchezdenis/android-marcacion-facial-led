package com.example.investigacion.fourth.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.investigacion.fourth.R;
import com.example.investigacion.fourth.model.Ns;
import com.example.investigacion.fourth.model.UserData;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RecycleViewLastUsers extends RecyclerView.Adapter<RecycleViewLastUsers.ViewHolder> {
    private ArrayList<Map<String,ArrayList<UserData>>> mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private  LinearLayout mLinearViewFrame;
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
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public RecycleViewLastUsers(ArrayList<Map<String,ArrayList<UserData>>>  myDataset) {
        mDataset = myDataset;
    }







    // Create new views (invoked by the layout manager)
    @Override
    public RecycleViewLastUsers.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v =  LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleview_last_users,parent,false);


      /*  if(v.getParent()!=null)
            ((ViewGroup)v.getParent()).removeView(v);


        if(dob.getParent()!=null)
            ((ViewGroup)dob.getParent()).removeView(dob);

        if(people.getParent()!=null)
            ((ViewGroup)people.getParent()).removeView(people);*/



        ViewHolder vh = new ViewHolder(v);
        return vh;
    }





    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        holder.mTextViewperson.setText(mDataset.get(position).get("n"+(position+1)).get(0).nombre);
        holder.mTextViewdate.setText(mDataset.get(position).get("n"+(position+1)).get(0).fecha);
        if(mDataset.get(position).get("n"+(position+1)).get(0).tipo.equals("E") ){
            holder.mLinearViewFrame.setBackgroundColor(Color.parseColor("#3ebc55"));
        }else{
            holder.mLinearViewFrame.setBackgroundColor(Color.parseColor("#d85454"));
        }


    }






    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}