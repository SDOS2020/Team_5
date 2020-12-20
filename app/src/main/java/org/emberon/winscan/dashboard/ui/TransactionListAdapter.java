package org.emberon.winscan.dashboard.ui;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.emberon.winscan.R;
import org.emberon.winscan.domain.entity.Transaction;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

public class TransactionListAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {

    private List<Transaction> transactionList = new ArrayList<>();

    @Inject
    public TransactionListAdapter() {
    }

    @Override
    public int getItemViewType(final int position) {
        return R.layout.framelayout_transactions;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {

        DateFormat dateFormatDayMonth = new SimpleDateFormat("dd MMM");
        DateFormat dateFormatYear = new SimpleDateFormat("yy");
        Date transactionDate = transactionList.get(position).getTransactionDate();
        holder.getDateOfTransaction().setText(dateFormatDayMonth.format(transactionDate)+"\'"+dateFormatYear.format(transactionDate));
        holder.getPayeeName().setText(transactionList.get(position).getPayeeName());
        holder.getAmountPaid().setText("₹" + String.valueOf(transactionList.get(position).getAmount()/100));
        Transaction.transactionStatus status = transactionList.get(position).getCurrentStatus();
        holder.getTransactionStatus().setText(status.toString());
        if (status == Transaction.transactionStatus.CANCELLED || status == Transaction.transactionStatus.FAILED)
            holder.getTransactionStatus().setTextColor(Color.RED);

        else if(status == Transaction.transactionStatus.SUCCESSFUL){
            holder.getTransactionStatus().setTextColor(Color.GREEN);
        }
        else
            holder.getTransactionStatus().setTextColor(Color.GRAY);


    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    public void setTransactionList(List<Transaction> transactionList) {
        this.transactionList = transactionList;
    }
}
