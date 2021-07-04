package com.example.thirdeyever05;

import android.net.Uri;
import android.view.Gravity;
import android.view.TextureView;
import android.view.View;
import android.widget.Toast;

import org.videolan.libvlc.IVLCVout;
import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import static com.example.thirdeyever05.MainActivity.loadingImage;
import static com.example.thirdeyever05.MainActivity.loadingText;
import static com.example.thirdeyever05.MainActivity.mContext;

public class ReceiveRTSP implements IVLCVout.Callback {

    private LibVLC libvlc;
    static public MediaPlayer mMediaPlayer = null;
    private Media m;
    public static IVLCVout vout;
    private MediaPlayer.EventListener mPlayerListener = new MyPlayerListener(this);

    public void createPlayer(String url, TextureView mTextureView, int mVideoHeight, int mVideoWidth) {
        releasePlayer();
        try {
            ArrayList<String> options = new ArrayList<>();
            options.add("--aout=opensles");
//            options.add("--no-audio");
            options.add("--audio-time-stretch"); // time stretching
            options.add("-vvv"); // verbosity
            options.add("--avcodec-codec=h265");
//            options.add("--avcodec-codec=h264");
            options.add("--file-logging");
            options.add("--logfile=vlc-log.txt");

            /** to enable rtsp over rtp **/
//            options.add("--rtsp-tcp");
//            options.add(":network-caching=500");
            options.add("--drop-late-frames");
//            options.add("--live-caching=150");
//            options.add(":clock-jitter=0");
//            options.add(":clock-synchro=0");
            libvlc = new LibVLC(mContext, options);
            mTextureView.setKeepScreenOn(true);
            mMediaPlayer = new MediaPlayer(libvlc);
            mMediaPlayer.setEventListener(mPlayerListener);
            float rate = mMediaPlayer.getRate();
            System.out.println("josh fps= " + rate);
            vout = mMediaPlayer.getVLCVout();
            vout.setVideoView(mTextureView);
            vout.setWindowSize(mVideoWidth, mVideoHeight);
            vout.addCallback(this);
            vout.attachViews();
            m = new Media(libvlc, Uri.parse(url));
            m.setHWDecoderEnabled(true, false);
            m.addOption(":fullscreen");
            mMediaPlayer.setMedia(m);
            mMediaPlayer.play();
        } catch (Exception e) {
        }
    }

    private static class MyPlayerListener implements MediaPlayer.EventListener {
        private WeakReference<ReceiveRTSP> mOwner;

        //액티비티 변수를 받아오기 위하여 지정
        private MyPlayerListener(ReceiveRTSP owner) {
            mOwner = new WeakReference<>(owner);
        }

        @Override
        public void onEvent(MediaPlayer.Event event) {
            ReceiveRTSP player = mOwner.get();
            switch (event.type) {
                case MediaPlayer.Event.EndReached:
                    player.releasePlayer();
                    break;
                case MediaPlayer.Event.Playing:
                    System.out.println("josh playing image");
                    break;
                case MediaPlayer.Event.Paused:
                    break;
                case MediaPlayer.Event.Stopped:
                    loadingImage.setVisibility(View.GONE);
                    loadingText.setVisibility(View.GONE);
                    loadingImage.clearAnimation();

                    Toast myToast = Toast.makeText(mContext,"Connection Fail", Toast.LENGTH_SHORT);
                    myToast.setGravity(Gravity.CENTER, 0,0 );
                    myToast.show();

                    System.out.println("josh video stopped");
                    player.releasePlayer();
                    break;
                case MediaPlayer.Event.Buffering:
                    break;
                case MediaPlayer.Event.Vout:
                    loadingImage.setVisibility(View.GONE);
                    loadingText.setVisibility(View.GONE);
                    loadingImage.clearAnimation();
                    System.out.println("joshh playing video voutr 1");
                    break;
                default:
                    break;
            }
        }
    }

    public void releasePlayer() {
        if (libvlc == null)
            return;
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            final IVLCVout vout = mMediaPlayer.getVLCVout();
            vout.removeCallback(this);
            vout.detachViews();
        }
        libvlc.release();
        libvlc = null;
    }

    @Override
    public void onSurfacesCreated(IVLCVout vlcVout) {
    }

    @Override
    public void onSurfacesDestroyed(IVLCVout vlcVout) {
    }
}
