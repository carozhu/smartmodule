package com.caro.smartmodule.helpers.AppUpdateHelper;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.caro.smartmodule.R;
import com.caro.smartmodule.utils.DisplayMetricsUtil;

import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by caro on 2016/10/22.
 */
public class AppUpdateDialog extends Dialog implements View.OnClickListener{
    private TextView updateDesTV,versionTV;
    private View cancelIV;
    private FancyButton updateFB;
    private String updatedesc;
    private String version;
    private String downloadUrl;

    public AppUpdateDialog(Context context,String version ,String updatedesc,String downloadUrl) {
        super(context, R.style.alert_dialog_style);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        this.version = version;
        this.updatedesc = updatedesc;
        this.downloadUrl = downloadUrl;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_update);
        initView();
    }


    private void initView() {
        updateFB = (FancyButton)findViewById(R.id.btn_update);
        updateDesTV = (TextView)findViewById(R.id.updatedesc);
        versionTV = (TextView)findViewById(R.id.version);
        cancelIV = (View)findViewById(R.id.cancel);

        updateFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (updateClickListener!=null){
                    updateClickListener.OnUpdateClick();
                    //download url
                    Intent updateIntent = new Intent(getContext(),ApkUpdateService.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("dwnUrl",downloadUrl);
                    updateIntent.putExtras(bundle);
                    getContext().startService(updateIntent);
                    dismiss();
                }
            }
        });

        cancelIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();

            }
        });

        if (!TextUtils.isEmpty(updatedesc)){
            updateDesTV.setText(updatedesc);
        }

        if (!TextUtils.isEmpty(version)){
            versionTV.setText(version);
        }
    }

    private UpdateClickListener updateClickListener;
    public interface UpdateClickListener{
        public void OnUpdateClick();
    }

    public void setUpdateClickListener(UpdateClickListener updateClickListener){
        this.updateClickListener = updateClickListener;
    }

    @Override
    public void onClick(View view) {

    }
}
