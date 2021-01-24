package com.example.simpletodo;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.List;

// responsible for displaying data from the model into a row in the recycler view
public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {

    public interface OnClickedListener{
        void onItemClicked(int Position);
    }

    public interface OnLongClickedListener{
        void onItemLongClicked(int Position, TextView tvItem);
    }
    
    List<String> items;
    OnLongClickedListener longClickedListener;
    OnClickedListener clickedListener;

    public ItemsAdapter(List<String> items, OnLongClickedListener longClickedListener, OnClickedListener clickedListener) {
        this.clickedListener = clickedListener;
        this.longClickedListener = longClickedListener;
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // use layout inflater to inflate a view

        View todoView = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ViewHolder(todoView);
    }

    // binding data to a particular view holder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //Grab the item at the position
        String item = items.get(position);
        //Bind the item into the specific view holder
        holder.bind(item);
    }

    // tells RV how many items are in the list
    @Override
    public int getItemCount() {
        return items.size();
    }

    //container to provide an easy access to view that represent each view
    class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItem = itemView.findViewById(android.R.id.text1);
        }
        // update the view inside of the view holder w/ this data
        public void bind(String item) {
            tvItem.setText(item);
            tvItem.setOnLongClickListener(new View.OnLongClickListener(){
                @Override
                public boolean onLongClick(View v) {
                    // getter for textview
                    longClickedListener.onItemLongClicked(getAdapterPosition(), tvItem);

                    return true;
                }
            });
            tvItem.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    clickedListener.onItemClicked(getAdapterPosition());
                }
            });
        }
    }

}
