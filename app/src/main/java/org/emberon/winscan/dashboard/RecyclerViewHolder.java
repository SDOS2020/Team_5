package org.emberon.winscan.dashboard;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.emberon.winscan.R;
import org.emberon.winscan.databinding.FragmentHomeBinding;

public class RecyclerViewHolder extends RecyclerView.ViewHolder {

    private TextView textview;
    private TextView snoview;
    public RecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
        textview = itemView.findViewById(R.id.TID);
        snoview = itemView.findViewById(R.id.Sno);
    }

    public TextView getTextviewView(){
        return textview;
    }

    public TextView getSnoView(){
        return snoview;
    }
}