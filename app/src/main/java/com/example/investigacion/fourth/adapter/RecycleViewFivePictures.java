package com.example.investigacion.fourth.adapter;



import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.investigacion.fourth.R;
import com.example.investigacion.fourth.model.Ns;
import com.example.investigacion.fourth.model.UserData;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RecycleViewFivePictures extends RecyclerView.Adapter<RecycleViewFivePictures.ViewHolder> {
    private ArrayList<Bitmap> mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private  LinearLayout mLinearViewFrame;
        public ImageView mTextViewperson;
       // public TextView mTextViewdate;
        // each data item is just a string in this case
        public TextView mTextView;
        public ViewHolder(View v ) {
            super(v);


            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);


            mTextViewperson = (ImageView) v.findViewById(R.id.iv_five_pictures);
            mTextViewperson.getLayoutParams().height = LinearLayout.LayoutParams.WRAP_CONTENT;
            mTextViewperson.getLayoutParams().width = LinearLayout.LayoutParams.WRAP_CONTENT;


            mLinearViewFrame  = (LinearLayout) v.findViewById(R.id.recyclerViewFrame);

            mLinearViewFrame.getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;
            mLinearViewFrame.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
           // mTextViewdate =  (TextView) v.findViewById(R.id.txt_dob);
           /* mLinearViewFrame  = (LinearLayout) v.findViewById(R.id.recyclerViewFrame);

            mLinearViewFrame.setLayoutParams(lp);*/
        //  mLinearViewFrame.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
        //    mLinearViewFrame.getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;



        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public RecycleViewFivePictures(ArrayList<Bitmap>  myDataset) {
        mDataset = myDataset;
    }







    // Create new views (invoked by the layout manager)
    @Override
    public RecycleViewFivePictures.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v =  LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_view_five_pictures,parent,false);




        ViewHolder vh = new ViewHolder(v);
        return vh;
    }






    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.mTextViewperson.setImageBitmap(mDataset.get(position));



    }







    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
