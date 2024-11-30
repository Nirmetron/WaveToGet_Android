package com.infoempire.wavetoget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.MyViewHolder>{

    private final OnItemClickListener listener;
    private LayoutInflater inflater;
    private Context context;
    //    private ArrayList<String> storeNameList;
    private ArrayList<CustomerDetails> customerList;

    public CustomerAdapter(Context context, ArrayList<CustomerDetails> customerList, OnItemClickListener listener) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.customerList = customerList;
        this.listener = listener;
    }

    public void setFilteredCustomers(ArrayList<CustomerDetails> filteredCustomers) {
        this.customerList = filteredCustomers;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.store_list_layout, parent, false);
        MyViewHolder holder = new MyViewHolder(view, listener);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.customerEmail.setText(customerList.get(position).email);
        holder.selectedCustomer = customerList.get(position);
        holder.customerEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(customerList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return customerList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(CustomerDetails customerDetails);
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView customerEmail;
        CustomerDetails selectedCustomer;

        public MyViewHolder(View itemView, final OnItemClickListener mListener) {
            super(itemView);
            customerEmail = (TextView)itemView.findViewById(R.id.tv_store_item);

            customerEmail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onItemClick(selectedCustomer);
                }
            });

        }
    }
}