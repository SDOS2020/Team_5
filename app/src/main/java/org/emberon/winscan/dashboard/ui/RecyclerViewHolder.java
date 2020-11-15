package org.emberon.winscan.dashboard.ui;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.emberon.winscan.R;

public class RecyclerViewHolder extends RecyclerView.ViewHolder {

    private final TextView transactionID;
    private final TextView dateOfTransaction;
    private final TextView payeeName;
    private final TextView amountPaid;
    public RecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
        transactionID = itemView.findViewById(R.id.TID);
        dateOfTransaction = itemView.findViewById(R.id.dateOfTransaction);
        payeeName = itemView.findViewById(R.id.payeeName);
        amountPaid = itemView.findViewById(R.id.amountPaid);
    }

    public TextView getTransactionID(){
        return transactionID;
    }
    public TextView getDateOfTransaction(){
        return dateOfTransaction;
    }
    public TextView getPayeeName(){
        return payeeName;
    }
    public TextView getAmountPaid(){
        return amountPaid;
    }
}