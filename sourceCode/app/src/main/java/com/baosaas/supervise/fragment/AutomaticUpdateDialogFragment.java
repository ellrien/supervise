package com.baosaas.supervise.fragment;


import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.baosaas.supervise.R;
import com.baosaas.supervise.bean.VersionOut;
import com.baosaas.supervise.util.Config;
import com.baosaas.supervise.util.NetSupport;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by leipe on 2015/10/13.
 */
public class AutomaticUpdateDialogFragment extends DialogFragment {

    ProgressBar progressBar;
    TextView progressBarTxt;
    private VersionOut versionOut;

    public static Fragment newInstance(VersionOut versionOut) {
        AutomaticUpdateDialogFragment fragment = new AutomaticUpdateDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("versionOut", versionOut);
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
        View view = inflater.inflate(R.layout.dialogfragment_automaticupdate, container);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        progressBarTxt = (TextView) view.findViewById(R.id.tv_progressBarTxt);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        Bundle arguments = getArguments();
        versionOut = (VersionOut) arguments.getSerializable("versionOut");
        new UpdateAsyncTask().execute();
        return view;
    }

    //    ProgressDialog progressDialog;
    private long mFileSize = 0;
    private long mReadSize = 0;
    private float mPercentage = 0;

    private class UpdateAsyncTask extends AsyncTask<Void, Integer, Boolean> {

        @Override
        protected void onPreExecute() {
//            progressDialog.setMax(100);
            progressBar.setMax(100);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                while (true) {
                    File tmpFile = new File("//sdcard");
                    if (!tmpFile.exists()) {
                        tmpFile.mkdir();
                    }
                    final File file = new File("//sdcard//" + Config.FILE_NAME);
                    try {
                        URL url = new URL(versionOut.getUrl());
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        //获得文件大小
                        mFileSize = conn.getContentLength();
                        InputStream is = conn.getInputStream();
                        FileOutputStream fos = new FileOutputStream(file);
                        byte[] buf = new byte[256];
                        conn.connect();
                        if (conn.getResponseCode() >= 400) {
                            Toast.makeText(getActivity(), "连接超时", Toast.LENGTH_SHORT).show();
                        } else {
                            while (mPercentage <= 100) {
                                if (!NetSupport.checkNetWork(getActivity())) {
                                    return false;
                                }
                                if (is != null) {
                                    int numRead = is.read(buf);
                                    if (numRead <= 0) {
                                        break;
                                    } else {
                                        mReadSize += numRead;
                                        mPercentage = (float) mReadSize * 100 / (float) mFileSize;
                                        String str = mPercentage + "";
                                        String s = str.contains(".") ? str.substring(0, str.indexOf(".")) : str;
                                        publishProgress(Integer.parseInt(s));
                                        fos.write(buf, 0, numRead);
                                    }
                                } else {
                                    break;
                                }
                            }
                        }
                        conn.disconnect();
                        fos.close();
                        is.close();
                        return true;
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                return false;
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
//            progressDialog.setMessage("当前下载进度：" + values[0] + "%");
            progressBar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                Toast.makeText(getActivity(), "下载成功", Toast.LENGTH_SHORT).show();
                File file = new File("//sdcard//" + Config.FILE_NAME);
                openFile(file);
                getDialog().dismiss();
            } else {
                Toast.makeText(getActivity(), "下载失败", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void openFile(File file) {
        Log.e("OpenFile", file.getName());
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file),
                "application/vnd.android.package-archive");
        startActivity(intent);
    }
}
