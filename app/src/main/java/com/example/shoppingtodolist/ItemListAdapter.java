package com.example.shoppingtodolist;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppingtodolist.db.Category;
import com.example.shoppingtodolist.db.Items;

import java.util.List;

public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.MyViewHolder> {

    private Context context;
    private List<Items> itemList;
    private  HandleItemClick clickListener;

    public ItemListAdapter(Context context, HandleItemClick clickListener){

        this.context=context;
        this.clickListener=clickListener;
    }
    public void setCategoryList(List<Items> itemList){
        this.itemList=itemList;
        notifyDataSetChanged ();
    }
    @NonNull
    @Override
    public ItemListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View view= LayoutInflater.from(context).inflate (R.layout.recyclerview_root,parent,false);
      return  new MyViewHolder (view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemListAdapter.MyViewHolder holder, int position) {

        holder.itemView.setOnClickListener (new View.OnClickListener (){

            @Override
            public void onClick(View v) {

                clickListener.itemClick (itemList.get (position));
            }
        });
        holder.tvItemName.setText (this.itemList.get (position).itemName);
        holder.editCategory.setOnClickListener (new View.OnClickListener (){

            @Override
            public void onClick(View v) {

                clickListener.editItem (itemList.get (position));
            }
        });
        holder.removeCategory.setOnClickListener (new View.OnClickListener (){

            @Override
            public void onClick(View v) {

                clickListener.removeItem (itemList.get (position));
            }
        });

        if(this.itemList.get (position).completed){
            holder.tvItemName.setPaintFlags (holder.tvItemName.getPaintFlags () | Paint.STRIKE_THRU_TEXT_FLAG);

        }
        else
            holder.tvItemName.setPaintFlags (0);




    }


    @Override
    public int getItemCount() {
        if(itemList==null || itemList.size ()==0)
            return 0;

        else
            return itemList.size ();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tvItemName;
        ImageView removeCategory;
        ImageView editCategory;
        public MyViewHolder(View view){

            super(view);
            tvItemName=view.findViewById (R.id.tvCategoryName);
            removeCategory=view.findViewById (R.id.recyclerView);
            editCategory=view.findViewById (R.id.editCategory);
        }

    }
    public  interface  HandleItemClick{
        void itemClick(Items item);
        void removeItem(Items item);
        void  editItem(Items item);
    }
}
