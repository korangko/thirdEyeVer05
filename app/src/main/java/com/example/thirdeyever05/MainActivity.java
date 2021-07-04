package com.example.thirdeyever05;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;

import static com.example.thirdeyever05.ReceiveRTSP.mMediaPlayer;
import static com.example.thirdeyever05.ReceiveRTSP.vout;

public class MainActivity extends AppCompatActivity implements Button.OnClickListener {
    /**
     * Common Variable
     **/
    TextureView mTextureview;
    public TextView record_btn_text, stream_btn_text, streaming_live_time;
    public static ImageView loadingImage;
    public static TextView loadingText;
    public ImageView  flashlight, minimize_hud, maximize_hud, video_thumbnail, stream_btn_background, record_btn_image, streaming_live_layout_image;
    public LinearLayout zoom_btn, setting_btn, reconnect_btn, faceblur_btn, capture_btn, youtube_btn, facebook_btn, twitch_btn, customRtmp_btn, instagram_btn, record_btn;
    public LinearLayout main_upper_layout, main_lower_layout, streaming_live_layout, stream_upperLayout;
    ConstraintLayout stream_btn;

    /**if using Wifi router**/
    public static String url2 = "rtsp://192.168.0.222:8554/ch0";
    /**if using smartphone hotspot**/
    public static String url = "rtsp://192.168.43.222:8554/ch0";
    /**rtsp testable url**/
//    public static String url2 = "rtsp://wowzaec2demo.streamlock.net/vod/mp4:BigBuckBunny_115k.mov";

    /**
     * Video Stream Variable
     **/
    ReceiveRTSP receiveRTSP = new ReceiveRTSP();
    private int mVideoWidth;
    private int mVideoHeight;
    public static Context mContext;

    /**
     * Video Recording Variable
     */
    public static Handler mHandler;
    RecordStream recordStream = new RecordStream();
    public static boolean recording = false;
    public static String fileSavePath;
    final int GET_GALLERY_IMAGE = 200;

    /**
     * Video Brodcasting Variable
     **/
    public static String selectedPlatform = null;
    BroadcastRTMP broadcastRTMP = new BroadcastRTMP();
    public static boolean broadcasting = false;

    /**
     * Orientation Variable
     **/
    private CustomOrientationEventListener customOrientationEventListener;
    final int ROTATION_O = 1;
    final int ROTATION_90 = 2;
    final int ROTATION_180 = 3;
    final int ROTATION_270 = 4;
    private LinearLayout[] btns = null;
    private ConstraintLayout[] btns3 = null;
    private TextView[] texts = null;
    private ImageView[] btns2 = null;
    private int rotAngle;

    /**
     * flash Light
     **/
    private boolean flashOn = false;

    /**
     * digital zoom in out
     **/
    TextView zoomInText, zoomOutText;
    ConstraintLayout seekbar_layout;
    SeekBar zoomSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextureview = findViewById(R.id.textureView);
        mTextureview.setSurfaceTextureListener(mSurfaceTextureListener);
        main_lower_layout = findViewById(R.id.main_lower_layout);
        main_upper_layout = findViewById(R.id.main_upper_layout);

        loadingImage = findViewById(R.id.loadingImage);
        loadingText = findViewById(R.id.loadingText);
        /*buttons*/
        reconnect_btn = findViewById(R.id.reconnect_btn);
        reconnect_btn.setOnClickListener(this);
        setting_btn = findViewById(R.id.setting_btn);
        setting_btn.setOnClickListener(this);
        record_btn = findViewById(R.id.record_btn);
        record_btn.setOnClickListener(this);
        record_btn_image = findViewById(R.id.record_btn_image);
        faceblur_btn = findViewById(R.id.faceblur_btn);
        faceblur_btn.setOnClickListener(this);
        zoom_btn = findViewById(R.id.zoom_btn);
        zoom_btn.setOnClickListener(this);
        stream_btn = findViewById(R.id.stream_btn);
        stream_btn_text = findViewById(R.id.stream_btn_text);
        stream_btn_background = findViewById(R.id.stream_btn_background);
        stream_btn.setOnClickListener(this);
        capture_btn = findViewById(R.id.capture_btn);
        capture_btn.setOnClickListener(this);
        /*bottom buttons*/
        flashlight = findViewById(R.id.flashlight);
        flashlight.setOnClickListener(this);
        minimize_hud = findViewById(R.id.minimize_hud);
        minimize_hud.setOnClickListener(this);
        maximize_hud = findViewById(R.id.maximize_hud);
        maximize_hud.setOnClickListener(this);
        video_thumbnail = findViewById(R.id.video_thumbnail);
        video_thumbnail.setOnClickListener(this);
        mHandler = new Handler();
        /*digital zoom seekBar */
        seekbar_layout = findViewById(R.id.seekbar_layout);
        zoomSeekBar = findViewById(R.id.zoom_seekBar);
        zoomSeekBar.setOnSeekBarChangeListener(seekBarChangeListener);
        zoomInText = findViewById(R.id.zoomIn_text);
        zoomOutText = findViewById(R.id.zoomOut_text);
        /*upper layouts*/
        streaming_live_layout = findViewById(R.id.streaming_live_layout);
        streaming_live_time = findViewById(R.id.streaming_live_time);
        streaming_live_layout_image = findViewById(R.id.streaming_live_layout_image);
        stream_upperLayout = findViewById(R.id.stream_upperLayout);
        /*recording*/
        record_btn_text = findViewById(R.id.record_btn_text);
        /* streaming  btns */
        youtube_btn = findViewById(R.id.youtube_Btn);
        youtube_btn.setOnClickListener(this);
        facebook_btn = findViewById(R.id.facebook_Btn);
        facebook_btn.setOnClickListener(this);
        twitch_btn = findViewById(R.id.twitch_Btn);
        twitch_btn.setOnClickListener(this);
        instagram_btn = findViewById(R.id.instagram_Btn);
        instagram_btn.setOnClickListener(this);
        customRtmp_btn = findViewById(R.id.customRtmp_Btn);
        customRtmp_btn.setOnClickListener(this);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        mVideoHeight = displayMetrics.heightPixels;
        mVideoWidth = displayMetrics.widthPixels;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mContext = this;

        //getting saved device ip
        SharedPreferences sharedPreferences = getSharedPreferences("device_ip", MODE_PRIVATE);
        url = sharedPreferences.getString("ip", "");
        System.out.println("josh ip address is = " + url);

        customOrientationEventListener = new
                CustomOrientationEventListener(getBaseContext()) {
                    @Override
                    public void onSimpleOrientationChanged(int orientation) {
                        btns = new LinearLayout[]{reconnect_btn, zoom_btn, setting_btn, faceblur_btn, capture_btn, youtube_btn,
                                facebook_btn, twitch_btn, customRtmp_btn, instagram_btn, record_btn};
                        texts = new TextView[]{zoomInText, zoomOutText};
                        btns2 = new ImageView[]{flashlight, minimize_hud};
                        btns3 = new ConstraintLayout[]{stream_btn};
                        switch (orientation) {
                            case ROTATION_O:
                                rotAngle = 0;
                                break;
                            case ROTATION_90:
                                rotAngle = -90;
                                break;
                            case ROTATION_270:
                                rotAngle = 90;
                                break;
                            case ROTATION_180:
                                rotAngle = 180;
                                break;
                        }
                        for (int i = 0; i < btns.length; i++) {
                            btns[i].animate().rotation(rotAngle).setDuration(500).start();
                        }
                        for (int i = 0; i < btns2.length; i++) {
                            btns2[i].animate().rotation(rotAngle).setDuration(500).start();
                        }
                        for (int i = 0; i < btns3.length; i++) {
                            btns3[i].animate().rotation(rotAngle).setDuration(500).start();
                        }
                        for (int i = 0; i < texts.length; i++) {
                            texts[i].animate().rotation(rotAngle).setDuration(500).start();
                        }
                        mTextureview.animate().rotation(rotAngle).setDuration(500).start();
                        if (rotAngle == -90 || rotAngle == 90) {
                            mVideoWidth = 1920;
                            mVideoHeight = 1080;
                        } else {
                            mVideoWidth = 1080;
                            mVideoHeight = 1920;
                        }
                        updateTextureViewScaling(mVideoWidth, mVideoHeight);
                        if (vout != null) {
                            vout.setWindowSize(mVideoWidth, mVideoHeight);
                        }
                    }
                };

        if (broadcasting) {
            StreamingTimer bcThread = new StreamingTimer();
            bcThread.start();
        }
        Animation animRot = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
        loadingImage.startAnimation(animRot);
    }

    //textureview 가 아직 가용이 안되서 시작할때 재생이 안되었던것
    private TextureView.SurfaceTextureListener mSurfaceTextureListener=
            new TextureView.SurfaceTextureListener() {
                @Override
                public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                    // cameraManager생성하는 메소드
                    System.out.println("josh texture ready");
                    receiveRTSP.createPlayer(url2, mTextureview, mVideoHeight, mVideoWidth);
                }

                @Override
                public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

                }

                @Override
                public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                    return false;
                }

                @Override
                public void onSurfaceTextureUpdated(SurfaceTexture surface) {

                }
            };


    private int wifiSignalChecker(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        int numberOfLevels = 5;
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int level = WifiManager.calculateSignalLevel(wifiInfo.getRssi(), numberOfLevels);
        return level;
    }

    @Override
    public void onClick(View view) {
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.wave);
        switch (view.getId()) {
            case R.id.record_btn:
                record_btn.startAnimation(animation);
                if (!recording) {
                    recording = true;
                    record_btn_image.setImageResource(R.drawable.ic_recording_background2);
                    recordStream.recordStart(this);
                    recordStream.recordingTimeCalculator(System.currentTimeMillis());
                    timerShow rtsThread = new timerShow();
                    rtsThread.start();
                } else {
                    recording = false;
                    recordStream.recordStop(this);
                    record_btn_image.setImageResource(R.drawable.ic_record);

                    /* to scan file immediately */
                    Intent mediaScannerIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    mediaScannerIntent.setData(Uri.fromFile(new File(fileSavePath)));
                    this.sendBroadcast(mediaScannerIntent);

                    /*thumbnail make*/
                    Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(fileSavePath, MediaStore.Video.Thumbnails.MINI_KIND);
                    final Bitmap thumbnail = ThumbnailUtils.extractThumbnail(bitmap, 30, 30);
                    if (thumbnail != null && bitmap != null) {
                        video_thumbnail.setImageBitmap(thumbnail);
                    }
                    System.out.println("josh file save");
                }
                break;
            case R.id.video_thumbnail:
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(
                        "content://media/internal/images/media"));
                startActivity(intent);
                break;
            case R.id.setting_btn:
                setting_btn.startAnimation(animation);
                Intent intentSa = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intentSa);
                finish();
                break;
            case R.id.reconnect_btn:
                reconnect_btn.startAnimation(animation);
                Intent intentCA = new Intent(MainActivity.this, ConnectActivity.class);
                startActivity(intentCA);
                finish();
                break;
            case R.id.faceblur_btn:
                faceblur_btn.startAnimation(animation);
                break;
            case R.id.stream_btn:
                stream_btn.startAnimation(animation);
                if (!broadcasting) {
                    if (stream_upperLayout.getVisibility() == View.VISIBLE) {
                        stream_upperLayout.setVisibility(View.GONE);
                        stream_btn_text.setText("STREAM");
                    } else {
                        stream_upperLayout.setVisibility(View.VISIBLE);
                        stream_btn_text.setText("SELECT");
                    }
                } else {
                    broadcastRTMP.broadcastStop(this);
                    broadcasting = false;
                    stream_btn_background.clearAnimation();
                    stream_btn_background.setImageResource(R.drawable.ic_streaming_background);
                    streaming_live_layout.setVisibility(View.GONE);
                    stream_btn_text.setText("STREAM");
                }
                break;
            case R.id.youtube_Btn:
            case R.id.facebook_Btn:
            case R.id.twitch_Btn:
            case R.id.instagram_Btn:
                stream_upperLayout.setVisibility(View.GONE);
                switch (view.getId()) {
                    case R.id.youtube_Btn:
                        selectedPlatform = "YOUTUBE";
                        break;
                    case R.id.facebook_Btn:
                        selectedPlatform = "FACEBOOK";
                        break;
                    case R.id.twitch_Btn:
                        selectedPlatform = "TWITCH";
                        break;
                    case R.id.instagram_Btn:
                        selectedPlatform = "INSTAGRAM";
                        break;
                }
                Intent platformDetail = new Intent(MainActivity.this, StreamPlatformDetailActivity.class);
                startActivity(platformDetail);
                finish();
                break;
            case R.id.capture_btn:
                capture_btn.startAnimation(animation);
                break;
            case R.id.zoom_btn:
                zoom_btn.startAnimation(animation);
                if (seekbar_layout.getVisibility() == View.GONE) {
                    seekbar_layout.setVisibility(View.VISIBLE);
                    zoom_btn.setBackgroundResource(R.drawable.btn_clicked_round);
                } else {
                    seekbar_layout.setVisibility(View.GONE);
                    zoom_btn.setBackgroundResource(0);
                }
                break;
            case R.id.flashlight:
                flashlight.startAnimation(animation);
                if (flashOn) {
                    flashlight.setImageResource(R.drawable.ic_baseline_flash_off_24);
                    flashOn = false;
                } else {
                    flashlight.setImageResource(R.drawable.ic_baseline_flash_on_24);
                    flashOn = true;
                }

                break;
            case R.id.minimize_hud:
                main_upper_layout.setVisibility(View.GONE);
                main_lower_layout.setVisibility(View.GONE);
                maximize_hud.setVisibility(View.VISIBLE);
                break;
            case R.id.maximize_hud:
                main_upper_layout.setVisibility(View.VISIBLE);
                main_lower_layout.setVisibility(View.VISIBLE);
                maximize_hud.setVisibility(View.GONE);
                break;
        }
    }

    private void updateTextureViewScaling(int viewWidth, int viewHeight) {
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) mTextureview.getLayoutParams();
        params.width = viewWidth;
        params.height = viewHeight;
        mTextureview.setLayoutParams(params);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        customOrientationEventListener.enable();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        receiveRTSP.releasePlayer();
    }


    /**
     * need to make another class file
     **/
    public class timerShow extends Thread {
        @Override
        public void run() {
            final long startTime = System.currentTimeMillis();
            mHandler.post(new Runnable() {
                @Override
                public void run() {  // 화면에 그려줄 작업
                    record_btn_text.setText("00:00");
                }
            });
            while (recording) {
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {  // 화면에 그려줄 작업
                        record_btn_text.setText(recordStream.recordingTimeCalculator(startTime));
                    }
                });
            }
            mHandler.post(new Runnable() {
                @Override
                public void run() {  // 화면에 그려줄 작업
                    record_btn_text.setText(null);
                }
            });
        }
    }

//    public class fpscheck extends Thread {
//        @Override
//        public void run() {
//            while (true) {
//                if(mMediaPlayer != null) {
//                    float rate = mMediaPlayer.getRate();
//                    System.out.println("josh fps= " + rate);
//                }
//            }
//        }
//    }

    public class timerShow2 extends Thread {
        //        Animation rotate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
        @Override
        public void run() {
            final long startTime = System.currentTimeMillis();
            mHandler.post(new Runnable() {
                @Override
                public void run() {  // 화면에 그려줄 작업
                    streaming_live_time.setText("00:00");
                }
            });
            while (broadcasting) {
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {  // 화면에 그려줄 작업
                        streaming_live_time.setText(broadcastRTMP.broadcastTimeCalculator(startTime));
                    }
                });
            }
            mHandler.post(new Runnable() {
                @Override
                public void run() {  // 화면에 그려줄 작업
                    streaming_live_time.setText("00:00");
                }
            });
        }
    }

    /**
     * zoom in zoom out seekbar
     **/
    SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            /** 0이 가장 기본이 되는 scale point로서 알아서 full 화면에 맞춰서 형성된다. 이후 나머지는 Zoom in zooom out으로 보면될듯*/
            // updated continuously as the user slides the thumb
            float value = progress;
            value = value / 10;
//            textView.setText(String.valueOf(value));
            System.out.println(value);
            String value2 = value + "f";
            if (mMediaPlayer != null) {
                mMediaPlayer.setScale(Float.parseFloat(value2));
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            // called when the user first touches the SeekBar
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // called after the user finishes moving the SeekBar
        }
    };

}