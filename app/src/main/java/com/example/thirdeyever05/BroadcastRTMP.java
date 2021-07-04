package com.example.thirdeyever05;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.arthenica.mobileffmpeg.Config;
import com.arthenica.mobileffmpeg.ExecuteCallback;
import com.arthenica.mobileffmpeg.FFmpeg;

import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_CANCEL;
import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_SUCCESS;
import static com.example.thirdeyever05.MainActivity.url;
import static com.example.thirdeyever05.MainActivity.url2;

public class BroadcastRTMP {

    long broadcastId;
    String imageUri = getURLForResource(R.drawable.pineracam);

    private String getURLForResource(int resId) {
        return Uri.parse("file:android.resource://" + R.class.getPackage().getName() + "/" + resId).toString();
    }

    public void broadcastStart(Context context, String streamUrl) {

        /**initial command***/
//        String[] ffmpegCommand = new String[]{"-rtsp_transport", "tcp", "-i", url, "-tune", "zerolatency","-an", "-vcodec", "libx264", "-pix_fmt",
//                "yuv420p","-threads", "6", "-c:v", "copy", "-f", "flv", "-flvflags", "no_duration_filesize", streamUrl };


        /** without dummy audio **/
//        String[] ffmpegCommand = new String[]{"-rtsp_transport", "tcp", "-i", "rtsp://wowzaec2demo.streamlock.net/vod/mp4:BigBuckBunny_115k.mov",
//                "-tune", "zerolatency", "-ar", "44100", "-vcodec", "libx264", "-pix_fmt",
//                "yuv420p", "-threads", "6", "-c:v", "copy", "-f", "flv", "-flvflags", "no_duration_filesize", streamUrl};

        /** live streaming with dummy audio **/
        String[] ffmpegCommand = new String[]{"-rtsp_transport", "tcp", "-i", url2,"-f", "lavfi", "-i", "anullsrc",
                "-tune", "zerolatency", "-acodec", "libmp3lame", "-ar", "44100", "-b:a", "128k", "-vcodec", "libx264", "-pix_fmt",
                "yuv420p", "-threads", "6", "-c:v", "copy", "-f", "flv", "-flvflags", "no_duration_filesize", streamUrl};

        /** testing overlay image **/
//        String[] ffmpegCommand = new String[]{"-i", url,"-i", url,
//                "-f", "lavfi", "-i", "anullsrc",
//                "-filter_complex", "[0:v] setpts=PTS-STARTPTS,scale=800x448,setsar=1[upperleft]; [1:v] setpts=PTS-STARTPTS, scale=800x448,setsar=1[upperright]; [upperleft][upperright]hstack[base]",
//                "-map", "[base]", "-map", "2", "-f", "flv", streamUrl};
        //보내지기는 하지만, 뭔가 byte issue로 인하여 잘 안가는듯..

//        String[] ffmpegCommand = new String[]{"-i", url, "-filter_complex",
//                "pad=1920:1080:0:0:black[1:v]; [0:a]showvolume=v=0:o=1:t=0:f=0.1,drawbox=x=iw-40:y=0:w=40:h=ih[volume]; [0:v]drawtext=fontfile=/system/fonts/DroidSans.ttf:fontsize=30:fontcolor=white:borderw=1:text='Stream Label'[label]; [label][volume]overlay=x=W-40:y=0[output]",
//                "-map", "[output]", "-f", "flv", streamUrl};

//        String[] ffmpegCommand = new String[]{"-i", url, "-filter_complex",
//                "pad=1920:1080:0:0:black[1:v]; [0:a]showvolume=v=0:o=1:t=0:f=0.1,drawbox=x=ih-40:y=0:w=40:h=ih[volume]; [1:v]drawtext=fontfile=/system/fonts/DroidSans.ttf:fontsize=30:fontcolor=white:borderw=1:text='Stream Label',scale=-1:-1[label]; [label][volume]overlay=x=main_w-40:y=0[output]",
//                "-map", "[output]", "-f", "flv", streamUrl};
        //뭔가 계속 바이트 문제로 인하여 깨진다


//        String[] ffmpegCommand = new String[]{"-rtsp_transport", "tcp","-i", url2, "-f", "lavfi", "-i", "anullsrc",
//                "-tune", "zerolatency", "-acodec", "libmp3lame", "-ar", "44100", "-b:a", "128k", "-pix_fmt",
//                "yuv420p", "-threads", "6", "-filter_complex",
//                "[0:a]showvolume=v=0:o=1:t=0:f=0.1,drawbox=x=iw-40:y=0:w=40:h=ih[volume]; [0:v]drawtext=fontfile=/system/fonts/DroidSans.ttf:fontsize=30:fontcolor=white:borderw=1:text='Stream Label'[label]; [label][volume]overlay=x=W-40:y=0[output]",
//                "-map", "[output]", "-map", "0:a", "-f", "flv", "-flvflags", "no_duration_filesize", streamUrl};
        //그냥 튕겨버리네




        broadcastId = FFmpeg.executeAsync(ffmpegCommand, new ExecuteCallback() {

            @Override
            public void apply(final long executionId, final int returnCode) {
                if (returnCode == RETURN_CODE_SUCCESS) {
                    Log.i(Config.TAG, "Async command execution completed successfully.");
                } else if (returnCode == RETURN_CODE_CANCEL) {
                    Log.i(Config.TAG, "Async command execution cancelled by user.");
                } else {
                    Log.i(Config.TAG, String.format("Async command execution failed with rc=%d.", returnCode));
                }
            }
        });
    }

    public void broadcastStop(Context context) {
        FFmpeg.cancel(broadcastId);
    }

    public String broadcastTimeCalculator(long startTime) {
        int minute = (int) (((System.currentTimeMillis() - startTime) / 1000) / 60);
        int second = (((int) (System.currentTimeMillis() - startTime) / 1000) % 60);
        String Time = "00:00";
        if (minute == 0 && second < 10) {
            Time = "00:0" + second;
        } else if (minute == 0) {
            Time = "00:" + second;
        } else if (minute < 10 && second < 10) {
            Time = "0" + minute + ":0" + second;
        } else if (minute < 10) {
            Time = "0" + minute + ":" + second;
        } else {
            Time = minute + ":" + second;
        }
        return Time;
    }
}
