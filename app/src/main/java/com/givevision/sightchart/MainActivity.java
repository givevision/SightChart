package com.givevision.sightchart;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    public static final String BROADCAST_TTS_ACTION = "TTS service";
    public static final String TTS_STARTED = "started talk";
    public static final int KEY_UP = 19;
    public static final int KEY_DOWN = 20;
    public static final int KEY_LEFT = 21;
    public static final int KEY_RIGHT = 22;
    public static final int KEY_TRIGGER = 96;
    public static final int KEY_BACK = 97;
    public static final int KEY_POWER1 = 100;
    public static final int KEY_POWER2 = 62;


    private Context context;
    private RelativeLayout   relativeLayout  ;
    private MainView mView;
    private boolean isStarted=false;
    private int maxPos=0;
    private int pos=0;
    private boolean isKeyPower;
    private int task=1;
    private int maxContrastPos=0;
    private String packageToStart="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        packageToStart=intent.getStringExtra("startedByPackage");
        LogManagement.Log_d(TAG, "MainActivity:: onCreate");
        //setContentView(R.layout.activity_main);
        context=this;
        screenGL();
        intGL();
    }

    @Override
    protected void onStart() {
        super.onStart();
        LogManagement.Log_d(TAG, "MainActivity:: onStart");

    }
    @Override
    protected void onResume() {
        super.onResume();
        LogManagement.Log_d(TAG, "MainActivity:: onResume ");
        startGL();
        maxPos=mView.getNbrImages();
        maxContrastPos=mView.getNbrContrast();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                speak("Sight Chart application started");
//            }
//        }).start();
        speak(".");
        new Handler().postDelayed(new Runnable() {
			public void run() {
                speak("Sight Chart application started");
                isSpeaking=false;
                speak("Task one, click trigger button to start. Or. Middle button to change the task");
                isSpeaking=true;
			}
		}, 1000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogManagement.Log_d(TAG, "MainActivity:: onPause startToClose:");
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogManagement.Log_d(TAG, "MainActivity:: onStop");
        stopTTSService();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogManagement.Log_d(TAG, "MainActivity:: onDestroy");

    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        LogManagement.Log_v(TAG, "MainActivity:: dispatchKeyEvent event.getKeyCode() " + event.getKeyCode() +
                " event.getAction():" + event.getAction()+" pos= "+pos+ " isKeyPower ="+isKeyPower);
        if(event.getKeyCode()==KEY_POWER1 && event.getAction()==KeyEvent.ACTION_UP){ //last code before RC go OFF ... KeyEvent.ACTION_UP detected
            if(isKeyPower) {
                speak("Application closing");
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        if(packageToStart!=null && !packageToStart.isEmpty()){
                            PackageManager pm = getPackageManager();
                            Intent intent = pm.getLaunchIntentForPackage(packageToStart);
                            intent.putExtra("startedByPackage","com.givevision.sightchart" );
                            setResult(Activity.RESULT_OK, intent);
                            startActivity(intent);
                        }
                        finish();
                        System.exit(0);
                    }
                }, 2000);
            }else {
                isKeyPower = true;
            }
//            new Handler().postDelayed(new Runnable() {
//                public void run() {
//                    isKeyPower=false;
//                }
//            }, 3000);
            return true;
        }

        if(event.getKeyCode()==KEY_TRIGGER && event.getAction()==KeyEvent.ACTION_UP){
            isKeyPower=false;
            if(task==1){
                if(maxPos>0){
                    if(pos>maxPos-1){
                        pos=0;
                        mView.setSetupImg(pos,task);
                        pos++;
                    }else {
                        mView.setSetupImg(pos,task);
                        pos++;
                    }
                    speak("please wait, we are downloading image"+pos);
                }
            }else{
                if(maxContrastPos>0){
                    if(pos>maxContrastPos-1){
                        pos=0;
                        mView.setSetupImg(pos,task);
                        pos++;
                    }else {
                        mView.setSetupImg(pos,task);
                        pos++;
                    }
                    speak("please wait, we are downloading image "+pos);
                }
            }

            return true;
        }
        if(event.getKeyCode()==KEY_BACK && event.getAction()==KeyEvent.ACTION_UP){
            if(task==1){
                speak("task two, click trigger button to start.");
                task=2;
                pos=0;
                mView.setSetupImg(-1,-1);
            }else{
                speak("task one, click trigger button to start.");
                task=1;
                pos=0;
                mView.setSetupImg(-1,task);
            }
            return true;
        }

        return super.dispatchKeyEvent(event);
    }

    private void screenGL(){
        LogManagement.Log_d(TAG, "MainActivity:: screenGL");
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        int ui = getWindow().getDecorView().getSystemUiVisibility();
        ui = ui | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
        final Window win = getWindow();
        win.getDecorView().setSystemUiVisibility(ui);
        win.setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        win.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        win.addFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                        |WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                        |WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                        |WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                        |WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON
        );
    }

    private void intGL(){

        LogManagement.Log_d(TAG, "MainActivity:: intGL");
        relativeLayout = new RelativeLayout(this);
        relativeLayout.setId(AndroidUtils.generateViewId());
        relativeLayout.setKeepScreenOn(true);
        relativeLayout.setBackgroundColor(Color.DKGRAY);; //or whatever your image is

        mView = new MainView(this,this);
        mView.setId(AndroidUtils.generateViewId());
//		mView.setKeepScreenOn(true);
        relativeLayout.addView(mView);
    }

    private void startGL(){
        if(isStarted)
            return;

        isStarted=true;

        LogManagement.Log_d(TAG, "MainActivity:: startGL");

        if(mView==null)
            intGL();
        resumeGL();
        setContentView ( relativeLayout );


    }

    private void resumeGL(){
        LogManagement.Log_d(TAG, "MainActivity:: resumeGL");
        mView.onResume();
    }

    boolean isSpeaking=false;

    protected void speak(String myText){
        LogManagement.Log_d(TAG, "speak : " + myText);
        if(!isSpeaking){
            startTTSService(myText,"QUEUE_ADD");
        }else{
            startTTSService(myText, null);
        }

    }

    protected void speak(String myText, String queue){
        LogManagement.Log_d(TAG, "speak : " + myText);
        startTTSService(myText,queue);
    }
    protected void startTTSService(String str, String queue) {
            Intent serviceIntent = new Intent(getBaseContext(), TTSService.class);
            LogManagement.Log_d(TAG, "startService string= "+str);
            serviceIntent.putExtra("str", str);
            if(queue!=null)
                serviceIntent.putExtra("queue", queue);

            startService(serviceIntent);
    }
    protected void stopTTSService() {
        LogManagement.Log_d(TAG, "stopSDService");
        stopService(new Intent(getBaseContext(), TTSService.class));
    }


//    public void startProgress() {
//        // do something long
//        Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
//                for (int i = 0; i < maxPos; i++) {
//                    mView.setSetupImg(i);
//                    doFakeWork();
//                }
//            }
//        };
//        new Thread(runnable).start();
//    }
//    private void doFakeWork() {
//        SystemClock.sleep(5000);
//    }

}
