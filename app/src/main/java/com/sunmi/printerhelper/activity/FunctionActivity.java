package com.sunmi.printerhelper.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sunmi.printerhelper.R;
import com.sunmi.printerhelper.utils.SunmiPrintHelper;

import sunmi.sunmiui.dialog.DialogCreater;
import sunmi.sunmiui.dialog.HintOneBtnDialog;
import sunmi.sunmiui.dialog.TextHintDialog;

/**
 *  Print function display page
 *  @author kaltin
 */
public class FunctionActivity extends AppCompatActivity {
    HintOneBtnDialog mHintOneBtnDialog;
    boolean run;

    private final DemoDetails[] demos = {
            new DemoDetails(R.string.function_text, R.drawable.function_text,
                    TextActivity.class)
             };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_function);
        setupRecyclerView();
    }

    private void setupRecyclerView() {
        final GridLayoutManager layoutManage = new GridLayoutManager(this, 2);
        RecyclerView mRecyclerView = findViewById(R.id.worklist);
        mRecyclerView.setLayoutManager(layoutManage);
        mRecyclerView.setAdapter(new WorkTogetherAdapter());
    }

    class WorkTogetherAdapter extends RecyclerView.Adapter<WorkTogetherAdapter.MyViewHolder> {

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.work_item, parent, false);
            return new MyViewHolder(v);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.demoDetails = demos[position];
            holder.tv.setText(demos[position].titleId);
            holder.tv.setCompoundDrawablesWithIntrinsicBounds(null, getDrawable(demos[position].iconResID), null, null);
        }

        @Override
        public int getItemCount() {
            return demos.length;
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            TextView tv;
            DemoDetails demoDetails;

            MyViewHolder(View v) {
                super(v);
                tv = v.findViewById(R.id.worktext);
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(demoDetails == null){
                            return;
                        }
                        if(demoDetails.activityClass != null){
                            startActivity(new Intent(FunctionActivity.this, demoDetails.activityClass));
                        }
                        if(demoDetails.titleId == R.string.function_threeline){
                            SunmiPrintHelper.getInstance().print3Line();
                        }
                        if(demoDetails.titleId == R.string.function_cash){
                            SunmiPrintHelper.getInstance().openCashBox();
                        }
                        if(demoDetails.titleId == R.string.function_status){
                            SunmiPrintHelper.getInstance().showPrinterStatus(FunctionActivity.this);
                        }

                    }
                });
            }
        }
    }


    /**
     * Multi-threaded loop print esc cmd arrays
     */

    /**
     * Show About
     */
    private void showAbout() {
        CharSequence charSequenc = getResources().getText(R.string.about_content);
        TextHintDialog textHintDialog = DialogCreater.createTextHintDialog(this, getResources().getString(R.string.about), getResources().getString(R.string.upload_info), "GitHub", charSequenc.toString(), null,null, false);
        TextView textView = textHintDialog.getDialog().findViewById(R.id.dialog_msg);
        textView.setGravity(Gravity.START);
        textHintDialog.setCanceledOnTouchOutside(true);
        textHintDialog.show();
    }

    private class DemoDetails {
        @StringRes
        private final int titleId;
        @DrawableRes
        private final int iconResID;
        private final Class<? extends Activity> activityClass;

        private DemoDetails(@StringRes int titleId, @DrawableRes int descriptionId,
                           Class<? extends Activity> activityClass) {
            super();
            this.titleId = titleId;
            this.iconResID = descriptionId;
            this.activityClass = activityClass;
        }
    }
}
