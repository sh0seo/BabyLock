package io.animal.monkey.ui.alert;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import io.animal.monkey.R;
import io.animal.monkey.util.PermissionHelper;

public class PermissionFragment extends DialogFragment {

    public final static String TAG = "PermissionFragment";

    private final static int REQ_CODE_OVERLAY_PERMISSION = 101;

    private CheckedTextView floatingPermission;

    private CheckedTextView accessibility;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.permission_fragment, container, false);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PermissionFragment.super.dismiss();
            }
        });

        floatingPermission = view.findViewById(R.id.floating_permission);
        floatingPermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PermissionHelper permissionHelper = new PermissionHelper(getContext());
                if (!permissionHelper.hasSystemAlertWindowsPermission()) {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                            Uri.parse("package:" + getContext().getPackageName()));
                    startActivityForResult(intent, REQ_CODE_OVERLAY_PERMISSION);
                    return;
                }

//                CheckedTextView view = (CheckedTextView)v;
//                view.setChecked(!view.isChecked());
            }
        });

        accessibility = view.findViewById(R.id.accessibility_permission);
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
//                CheckedTextView view = (CheckedTextView)v;
//                view.setChecked(!view.isChecked());
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        PermissionHelper permissionHelper = new PermissionHelper(getContext());

        // 모든 권한을 확인 했다면
        if (permissionHelper.isAccessibilitySettingsOn()
                && permissionHelper.hasSystemAlertWindowsPermission()) {
            PermissionFragment.super.dismiss();
            return;
        }

        // 권한 체크는 Floating 부터.
        // Accessibility 는 Floating 권한이 존재하면 그 다음에 확인
        if (permissionHelper.hasSystemAlertWindowsPermission()) {
            floatingPermission.setChecked(true);
            floatingPermission.setEnabled(false);
        } else {
            floatingPermission.setChecked(false);
            floatingPermission.setEnabled(true);
        }

        if (permissionHelper.hasSystemAlertWindowsPermission()) {
            if (permissionHelper.isAccessibilitySettingsOn()) {
                accessibility.setChecked(true);
                accessibility.setEnabled(false);
            } else {
                accessibility.setChecked(false);
                accessibility.setEnabled(true);
            }
        } else {
            accessibility.setEnabled(false);

            if (permissionHelper.isAccessibilitySettingsOn()) {
                accessibility.setChecked(true);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        PermissionHelper permissionHelper = new PermissionHelper(getContext());

        // 모든 권한을 확인 했다면
        if (!permissionHelper.isAccessibilitySettingsOn()
                || !permissionHelper.hasSystemAlertWindowsPermission()) {
            Toast.makeText(getContext(),
                    "앱을 실행하기 위한 권한이 부족하여 앱을 종료합니다.",
                    Toast.LENGTH_LONG).show();

            // 실행 권한이 없다면 앱을 종료.
            getActivity().finish();
        }
    }
}
