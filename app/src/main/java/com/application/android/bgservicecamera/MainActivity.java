package com.application.android.bgservicecamera;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceView;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private Camera camera;
    private int cameraId = 0;
    private ImageView display;
    private Timer timer;
    private Handler threadHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    camera.takePicture(null, null, mCall);
                    break;
            }
        }
    };
    Camera.PictureCallback mCall = new Camera.PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {
            //decode the data obtained by the camera into a Bitmap
            Bitmap bitmapPicture = BitmapFactory.decodeByteArray(data, 0, data.length);
            display.setImageBitmap(bitmapPicture);
            Message.obtain(threadHandler, 1, "").sendToTarget();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        display = (ImageView) findViewById(R.id.imageView);

        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            Toast.makeText(this, "No camera on this device", Toast.LENGTH_LONG).show();
        } else {
            CameraOpen(cameraId);
        }

        SurfaceView view = new SurfaceView(this);
        try {
            camera.setPreviewDisplay(view.getHolder());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        camera.startPreview();
        Camera.Parameters params = camera.getParameters();
        params.setJpegQuality(100);
        camera.setParameters(params);

        timer = new Timer(getApplicationContext(), threadHandler);
        timer.execute();
    }

    @Override
    protected void onPause() {
        if (timer != null) {
            timer.cancel(true);
        }
        releaseCamera();
        super.onPause();
    }

    private void CameraOpen(int id) {
        try {
            releaseCamera();
            camera = Camera.open(id);
        } catch (Exception e) {
            Log.e(getString(R.string.app_name), "failed to open Camera");
            e.printStackTrace();
        }
    }

    private void releaseCamera() {
        if (camera != null) {
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }


}
