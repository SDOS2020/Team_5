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
import org.emberon.winscan.dashboard.TransactionListAdapter;
import org.emberon.winscan.dashboard.presenter.DashboardPresenter;
import org.emberon.winscan.databinding.FragmentDashboardBinding;

import javax.inject.Inject;

public class DashboardFragment extends Fragment  implements DashboardContract.DashboardView {
    @Inject
    DashboardPresenter presenter;
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
        binding.trasactionView.setHasFixedSize(true);
        binding.trasactionView.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));
        binding.trasactionView.setAdapter(new TransactionListAdapter(presenter.getTransactionList()));

        return binding.getRoot();
    }
}