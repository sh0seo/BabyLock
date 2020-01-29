package io.animal.monkey.ui.admob;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import io.animal.monkey.R;
import io.animal.monkey.ui.main.MainViewModel;

public class AdMobFragment extends Fragment {

    private AdMobViewModel viewModel;

    public static AdMobFragment newInstance() {
        return new AdMobFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.admob_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(AdMobViewModel.class);
        // TODO: Use the ViewModel
    }

}
