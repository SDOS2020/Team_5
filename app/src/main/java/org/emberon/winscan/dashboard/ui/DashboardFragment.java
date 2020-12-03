package org.emberon.winscan.dashboard.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import org.emberon.winscan.base.BaseActivity;
import org.emberon.winscan.dashboard.DashboardContract;
import org.emberon.winscan.dashboard.presenter.DashboardPresenter;
import org.emberon.winscan.databinding.FragmentDashboardBinding;
import org.emberon.winscan.domain.entity.Transaction;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class DashboardFragment extends Fragment implements DashboardContract.DashboardView {
    private List<Transaction> transactionList = new ArrayList<>();
    @Inject
    DashboardPresenter presenter;
    @Inject
    TransactionListAdapter adapter;
    private FragmentDashboardBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((BaseActivity) getActivity()).getActivityComponent().inject(this);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        presenter.attachView(this);
        transactionList = presenter.getTransactionList();
        binding.transactionView.setHasFixedSize(true);
        binding.transactionView.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));
        binding.transactionView.setAdapter(adapter);
        adapter.setTransactionList(transactionList);
        if (transactionList.isEmpty()) {
            binding.textViewNoTransaction.setVisibility(View.VISIBLE);
            binding.transactionView.setVisibility(View.INVISIBLE);
        }
        else {
            binding.textViewNoTransaction.setVisibility(View.INVISIBLE);
            binding.transactionView.setVisibility(View.VISIBLE);
        }

        return binding.getRoot();
    }
}