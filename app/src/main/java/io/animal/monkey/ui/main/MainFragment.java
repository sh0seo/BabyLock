package io.animal.monkey.ui.main;

import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import io.animal.monkey.R;
import io.animal.monkey.bus.events.AlertBoxStatusEvent;
import io.animal.monkey.util.PermissionHelper;

public class MainFragment extends Fragment {

    private final static int REQ_CODE_OVERLAY_PERMISSION = 101;

    private MainViewModel mViewModel;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_fragment, container, false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        // TODO: Use the ViewModel

        Button floating = getView().findViewById(R.id.overlay_test);
        floating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // check floating permission
                PermissionHelper permissionHelper = new PermissionHelper(getContext());
                if (!permissionHelper.hasSystemAlertWindowsPermission()) {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                            Uri.parse("package:" + getContext().getPackageName()));
                    startActivityForResult(intent, REQ_CODE_OVERLAY_PERMISSION);
                    return;
                }

                Toast.makeText(getContext(), "show overlay", Toast.LENGTH_SHORT).show();
            }
        });

        Button accessibility = getView().findViewById(R.id.accessibility_test);
        accessibility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PermissionHelper permissionHelper = new PermissionHelper(getContext());
                if (!permissionHelper.isAccessibilitySettingsOn()) {
                    Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
                                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivityForResult(intent, REQ_CODE_OVERLAY_PERMISSION);
                    return;
                }

                Toast.makeText(getContext(), "accessibility", Toast.LENGTH_SHORT).show();
            }
        });

        Button box = getView().findViewById(R.id.box_test);
        box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new AlertBoxStatusEvent());
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
    }

}
