package com.example.moneysaver;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moneysaver.database.Entry;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EntryAdapter extends RecyclerView.Adapter<EntryAdapter.EntryViewHolder> {
    private List<Entry> entries;
    Context mContext;
    public ItemClickListener itemClickListener;

    public interface ItemClickListener {
        void onItemClickListener(int itemId);
    }
    public EntryAdapter(Context context, ItemClickListener itemClickListener){
        mContext = context;
       this.itemClickListener = itemClickListener;
    }
    @NonNull
    @Override
    public EntryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutIdForListItem = R.layout.entry_item_list;
        View v = LayoutInflater.from(mContext).inflate(layoutIdForListItem, parent, false);
        return new EntryViewHolder(v);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull EntryViewHolder holder, int position) {
        Entry entry = entries.get(position);
        holder.salary.setText(Double.toString(entry.getSalary()));
        holder.expenses.setText(Double.toString(entry.getExpenses()));
        holder.savings.setText(Double.toString(entry.getSavings()));

        double percentage = (entry.getSavings()/entry.getSalary())*100;
        int percent = (int) Math.round(percentage);
        holder.progressBar.setProgress(percent);
        if(percent >= 0  && percent < 40) {
            holder.cardView.setCardBackgroundColor(Color.parseColor("#FF3A3A"));
        }else if (percent < 70 && percent >= 40){
            holder.cardView.setCardBackgroundColor(Color.parseColor("#FFA251"));
        }else if(percent >= 70) {
            holder.cardView.setCardBackgroundColor(Color.parseColor("#034170"));
        }
    }

    public void setEntries(List<Entry> entries){
        this.entries = entries;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
       if(entries.isEmpty() || entries.size() == 0){
           return -1;
       }
       return entries.size();
    }

    public class EntryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.salary_amt)
        TextView salary;
        @BindView(R.id.expenses_amt)
        TextView expenses;
        @BindView(R.id.savings_amt)
        TextView savings;
        @BindView(R.id.progress_bar)
        ProgressBar progressBar;
        @BindView(R.id.card_view)
        CardView cardView;

        public EntryViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int id = entries.get(getAdapterPosition()).getId();
            itemClickListener.onItemClickListener(id);
        }
    }
}
