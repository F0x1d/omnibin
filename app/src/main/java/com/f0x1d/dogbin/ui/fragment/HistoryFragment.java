package com.f0x1d.dogbin.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.f0x1d.dogbin.App;
import com.f0x1d.dogbin.R;
import com.f0x1d.dogbin.adapter.MyNotesAdapter;
import com.f0x1d.dogbin.ui.fragment.base.BaseFragment;
import com.google.android.material.appbar.MaterialToolbar;

public class HistoryFragment extends BaseFragment {

    private MaterialToolbar mToolbar;
    private RecyclerView mNotesRecycler;
    private ProgressBar mLoadingProgress;

    private MyNotesAdapter mAdapter;

    public static HistoryFragment newInstance() {
        Bundle args = new Bundle();

        HistoryFragment fragment = new HistoryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int layoutId() {
        return R.layout.fragment_my_notes;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mToolbar = findViewById(R.id.toolbar);
        mNotesRecycler = findViewById(R.id.my_notes_recycler);
        mLoadingProgress = findViewById(R.id.loading_progress);
        mLoadingProgress.setVisibility(View.GONE);

        mToolbar.setTitle(R.string.history);

        mNotesRecycler.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false));
        mNotesRecycler.setAdapter(mAdapter = new MyNotesAdapter(requireActivity()));

        App.getMyDatabase().myNoteDao().getAll().observe(getViewLifecycleOwner(), notes -> {
            mAdapter.setNotes(notes, true);
            mAdapter.notifyDataSetChanged();
        });
    }
}
