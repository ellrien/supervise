package com.baosaas.supervise.fragment;


import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.baosaas.supervise.R;
import com.baosaas.supervise.bean.VersionOut;
import com.baosaas.supervise.util.Config;
import com.baosaas.supervise.util.StringUtils;

/**
 * Created by leipe on 2015/10/14.
 */
public class VersionTipsDialogFragment extends DialogFragment {
    public static Fragment newInstance(VersionOut versionOut, String fileSize) {
        VersionTipsDialogFragment fragment = new VersionTipsDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("versionOut", versionOut);
        bundle.putString("fileSize", fileSize);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        Window win = getDialog().getWindow();
        win.setLayout(dm.widthPixels * 9 / 10, getDialog().getWindow().getAttributes().height);
        win.setBackgroundDrawable(new ColorDrawable(0xff000000));
        win.getAttributes().gravity = Gravity.CENTER;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialogfragment_versiontips, container);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        TextView tvRelease = (TextView) view.findViewById(R.id.tv_release);
        TextView tvFileSize = (TextView) view.findViewById(R.id.tv_fileSize);
        TextView tvUpdateTitle = (TextView) view.findViewById(R.id.tv_updateTitle);
        TextView tvUpdateContents = (TextView) view.findViewById(R.id.tv_updateContents);

        Button btnCancel = (Button) view.findViewById(R.id.btn_tips_cancel);
        Button btnConfirm = (Button) view.findViewById(R.id.btn_tips_confirm);
        ScrollView svContents = (ScrollView) view.findViewById(R.id.sv_contents);

        Bundle arguments = getArguments();
        VersionOut versionOut = (VersionOut) arguments.getSerializable("versionOut");
        String fileSize = arguments.getString("fileSize");
        tvRelease.setText("有新版本:" + versionOut.getCurrentVersion());
        tvFileSize.setText("安装包大小为:" + fileSize + "M");

        tvUpdateContents.setText(versionOut.getUpgradeDetail());

        if (StringUtils.isEmpty(versionOut.getUpgradeDetail())) {
            svContents.setVisibility(View.GONE);
            tvUpdateTitle.setVisibility(View.GONE);
        }
        if (versionOut.getStatus() == Config.UPDATE_MANDATORY) {
            btnCancel.setVisibility(View.GONE);
        }
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (versionTipsDialogListener != null) {
                    versionTipsDialogListener.goLogin();
                }
                getDialog().dismiss();
            }
        });
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (versionTipsDialogListener != null) {
                    versionTipsDialogListener.openDialogFragment();
                }
                getDialog().dismiss();
            }
        });
        return view;
    }

    public VersionTipsDialogListener versionTipsDialogListener;

    public interface VersionTipsDialogListener {
        public void openDialogFragment();

        public void goLogin();
    }
}
