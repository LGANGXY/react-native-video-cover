package com.video.tools;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.util.Log;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.WritableMap;
import com.video.tools.lib.FileUtils;

import java.io.File;

import javax.annotation.Nullable;

public class VideoToolsReactModule extends ReactContextBaseJavaModule {

    private static final String TAG = "VideoToolsReactModule";

    public VideoToolsReactModule(ReactApplicationContext reactContext) {
        super(reactContext);
        FileUtils.createApplicationFolder();
    }

    @Override
    public String getName() {
        return "RNVideoCover";
    }

    @ReactMethod
    public void compress(@Nullable String path, Promise promise) {
        Uri uri = Uri.parse(path);
        if (uri != null) {
            Cursor cursor = getReactApplicationContext().getContentResolver().query(uri, null, null, null, null, null);

            try {
                if (cursor != null && cursor.moveToFirst()) {

                    String displayName = cursor.getString(
                            cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    Log.i(TAG, "Display Name: " + displayName);

                    int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);
                    String size = null;
                    if (!cursor.isNull(sizeIndex)) {
                        size = cursor.getString(sizeIndex);
                    } else {
                        size = "Unknown";
                    }
                    Log.i(TAG, "Size: " + size);

                    File tempFile = FileUtils.saveTempFile(displayName, getReactApplicationContext(), uri);
                    promise.resolve(tempFile.getPath());
                    return;
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        promise.reject("1", path);
    }

    @ReactMethod
    public void getVideoInfo(String uri, Promise promise){
        WritableMap result = Arguments.createMap();
        if(TextUtils.isEmpty(uri)){
            promise.reject("1","uri is empty");
        }else {
            String path = getRealFilePath(getReactApplicationContext(), Uri.parse(uri));
            result.putDouble("duration", getDuration(path));
            result.putString("thumbnail", getThumbnail(path));
            promise.resolve(result);
        }
    }

    @ReactMethod
    public void getMetadata(@Nullable String uri, @Nullable ReadableArray options, Promise promise) {
        WritableMap result = Arguments.createMap();
        String path = getRealFilePath(getReactApplicationContext(), Uri.parse(uri));
        if(options != null){
            for(int i = 0; i < options.size();i ++){
                switch (options.getString(i)){
                    case "duration":
                        result.putDouble("duration",getDuration(path));
                        break;
                    case "thumbnail":
                        result.putString("thumbnail",getThumbnail(path));
                        break;
                }
            }
        }
        promise.resolve(result);
    }

    private String getThumbnail(String path) {
        Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(path, MediaStore.Video.Thumbnails.MICRO_KIND);

        File tempFile = FileUtils.saveTempFile(System.currentTimeMillis()+"",bitmap);
        return tempFile.getPath();
    }

    public String getRealFilePath(final Context context, final Uri uri ) {
        if ( null == uri ) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if ( scheme == null )
            data = uri.getPath();
        else if ( ContentResolver.SCHEME_FILE.equals( scheme ) ) {
            data = uri.getPath();
        } else if ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {
            Cursor cursor = context.getContentResolver().query( uri, new String[] { MediaStore.Images.ImageColumns.DATA }, null, null, null );
            if ( null != cursor ) {
                if ( cursor.moveToFirst() ) {
                    int index = cursor.getColumnIndex( MediaStore.Images.ImageColumns.DATA );
                    if ( index > -1 ) {
                        data = cursor.getString( index );
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    private long getDuration(String path){
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(path);
        String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        long timeInmillisec = Long.parseLong( time );
        long duration = timeInmillisec / 1000;
//        long hours = duration / 3600;
//        long minutes = (duration - hours * 3600) / 60;
//        long seconds = duration - (hours * 3600 + minutes * 60);
        return duration;
    }
}
