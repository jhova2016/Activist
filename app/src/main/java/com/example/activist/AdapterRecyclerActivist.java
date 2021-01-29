package com.example.activist;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterRecyclerActivist extends RecyclerView.Adapter<com.example.activist.AdapterRecyclerActivist.ViewHolderDatos> implements View.OnClickListener {


    //ArrayList<String> ListDatos;
    ArrayList<com.example.activist.ElementRecyclerActivist> ListDatos;
    private View.OnClickListener Listener;


    public AdapterRecyclerActivist(ArrayList<com.example.activist.ElementRecyclerActivist> listDatos, Context context) {
        this.ListDatos = listDatos;

    }



    @NonNull
    @Override
    public com.example.activist.AdapterRecyclerActivist.ViewHolderDatos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.element_recycler_activist,parent,false);
        view.setOnClickListener(this);
        return new com.example.activist.AdapterRecyclerActivist.ViewHolderDatos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull com.example.activist.AdapterRecyclerActivist.ViewHolderDatos holder, int position) {


        holder.Name.setText(ListDatos.get(position).getName());
        holder.Direction.setText(ListDatos.get(position).getDirection());
        holder.Phone.setText(ListDatos.get(position).getPhone());
        holder.ElectorKey.setText(ListDatos.get(position).getElectorKey());


        switch (ListDatos.get(position).getUp())
        {

            case "0":
                holder.Up.setBackgroundColor(Color.parseColor("#D97904"));

                break;

            case "1":
                holder.Up.setBackgroundColor(Color.parseColor("#4F7302"));
                break;


        }


       /* File imgFile = new File(ListDatos.get(position).getPhotoPath());
        if(imgFile.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            holder.PhotoPath.setImageBitmap(myBitmap);
        }





       /* Picasso.get()
                .load(new File(ListDatos.get(position).getPhotoPath()))
                .fit()
                .centerCrop()
                .into(holder.PhotoPath);*/

    }

    @Override
    public int getItemCount() {
        return ListDatos.size();
    }

    public  void  setOnClickListener(View.OnClickListener listener)
    {
        this.Listener=listener;
    }

    @Override
    public void onClick(View v) {
        if(Listener!=null)
        {
            Listener.onClick(v);
        }

    }

    public class ViewHolderDatos extends RecyclerView.ViewHolder {



        TextView Name;
        TextView Direction;
        TextView Phone ;
        TextView ElectorKey ;
        LinearLayout Up ;




        public ViewHolderDatos(@NonNull View itemView) {
            super(itemView);
            Name=itemView.findViewById(R.id.Name);
            Direction=itemView.findViewById(R.id.Direction);
            Phone=itemView.findViewById(R.id.Phone);
            ElectorKey=itemView.findViewById(R.id.ElectorKey);
            Up=itemView.findViewById(R.id.UP);


        }


    }
}