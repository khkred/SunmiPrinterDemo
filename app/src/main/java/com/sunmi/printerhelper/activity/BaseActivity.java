package com.sunmi.printerhelper.activity;

import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.sunmi.printerhelper.R;
import com.sunmi.printerhelper.utils.BluetoothUtil;
import com.sunmi.printerhelper.utils.ESCUtil;
import com.sunmi.printerhelper.utils.SunmiPrintHelper;

public abstract class BaseActivity extends AppCompatActivity {

    Handler handler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new Handler();
        initPrinterStyle();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(handler != null){
            handler.removeCallbacksAndMessages(null);
        }
    }

    /**
     *  Initialize the printer
     *  All style settings will be restored to default
     */
    private void initPrinterStyle() {
        if(BluetoothUtil.isBlueToothPrinter){
            BluetoothUtil.sendData(ESCUtil.init_printer());
        }else{
            SunmiPrintHelper.getInstance().initPrinter();
        }
    }

    /**
     * set title
     * @param title title name
     */
    void setMyTitle(String title){
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setTitle(title);
        }
    }

    /**
     * set title
     * @param title title res
     */
    void setMyTitle(@StringRes int title){
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setTitle(title);
        }
        setSubTitle();
    }

    /**
     * set sub title
     */
    void setSubTitle(){
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            if(BluetoothUtil.isBlueToothPrinter){
                actionBar.setSubtitle("bluetoothÂ®");
            }else{
                if(SunmiPrintHelper.getInstance().sunmiPrinter == SunmiPrintHelper.NoSunmiPrinter){
                    actionBar.setSubtitle("no printer");
                }else if(SunmiPrintHelper.getInstance().sunmiPrinter == SunmiPrintHelper.CheckSunmiPrinter){
                    actionBar.setSubtitle("connecting");
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            setSubTitle();
                        }
                    }, 2000);
                }else if(SunmiPrintHelper.getInstance().sunmiPrinter == SunmiPrintHelper.FoundSunmiPrinter){
                    actionBar.setSubtitle("");
                }else{
                    SunmiPrintHelper.getInstance().initSunmiPrinterService(this);
                }
            }
        }
    }

    /**
     * set back
     */
    void setBack(){
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.back);
        }
    }

}
