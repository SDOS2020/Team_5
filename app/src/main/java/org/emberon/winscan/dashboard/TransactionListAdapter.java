package org.emberon.winscan.dashboard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.emberon.winscan.R;
import org.emberon.winscan.domain.entity.Transaction;
import org.emberon.winscan.domain.entity.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TransactionListAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {

        final List<Transaction> transactions = new ArrayList<Transaction>();
        public TransactionListAdapter(int seed) {
//        final User user = localRepository.getUser();
        transactions.add(new Transaction("TID78087", "vaibhav",
                "smoeone", "upi@paytm", "mypiy@paytm",
                1000, new Date(2000, 11, 21)));
        transactions.add(new Transaction("TID78027", "someone",
                        "vaibhav", "upi@paytm", "mypiy@paytm",
                        1000, new Date(2000, 11, 21)));
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
                holder.getTextviewView().setText(transactions.get(position).getId());
                holder.getSnoView().setText(transactions.get(position).getPayeeName());
        }

        @Override
        public int getItemCount() {
                return transactions.size();
        }
}
