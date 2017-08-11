package br.com.woobe.notepic.activity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.List;

import br.com.woobe.notepic.R;
import br.com.woobe.notepic.fragments.DisciplinesFragment;
import br.com.woobe.notepic.fragments.FirstAccessFragment;
import br.com.woobe.notepic.fragments.MenuFragment;
import br.com.woobe.notepic.persistence.DisciplinaDAO;
import br.com.woobe.notepic.util.Constants;
import br.com.woobe.notepic.util.FileUtils;
import br.com.woobe.notepic.util.Log;
import br.com.woobe.notepic.util.PreferenceUtils;
import br.com.woobe.notepic.util.TimeUtils;

public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback, ScaleGestureDetector.OnScaleGestureListener {

    private Camera camera;
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private Button btnTakePhoto;
    private ImageView btnMenu;
    private RelativeLayout previewLayout;

    private Camera.PictureCallback rawCallback;
    private Camera.ShutterCallback shutterCallback;
    private Camera.PictureCallback jpegCallback;

    private ScaleGestureDetector mScaleDetector;
    private int orientationControl = Configuration.ORIENTATION_PORTRAIT;
    private Fragment actualFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        loadObjects();
        verifyFirstAccess();
        Log.d("MainActivity.onCreate");
    }

    private void init() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        if (getActionBar() != null) {
            getActionBar().hide();
        } else if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        configureSensor();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopPreview();
        releaseCameraAndPreview();
        Log.d("MainActivity.onPause");
    }

    private void loadObjects() {
        surfaceView = (SurfaceView) findViewById(R.id.surface_view);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        mScaleDetector = new ScaleGestureDetector(this, this);
        btnTakePhoto = (Button) findViewById(R.id.btn_take_photo);
        rawCallback = new Camera.PictureCallback() {
            public void onPictureTaken(byte[] data, Camera camera) {
            }
        };
        /** Handles data for jpeg picture */
        shutterCallback = new Camera.ShutterCallback() {
            public void onShutter() {
            }
        };
        jpegCallback = new Camera.PictureCallback() {
            public void onPictureTaken(byte[] data, Camera camera) {
                Log.d("onPictureTaken - jpeg");
                showPreviewImage(data);
                camera.stopPreview();
                resetZoomCamera();
                camera.startPreview();
            }
        };
        btnTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("btnTakePhoto.onClick");
                camera.autoFocus(new Camera.AutoFocusCallback() {
                    @Override
                    public void onAutoFocus(boolean success, Camera camera) {
                        Log.d("onAutoFocus - success:" + success);
                        if (success && camera != null) {
                            camera.takePicture(shutterCallback, rawCallback, jpegCallback);
                        }
                    }
                });
            }
        });
        btnMenu = (ImageView) findViewById(R.id.btn_menu);
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                MenuFragment menuFragment = new MenuFragment();
                menuFragment.setActivity(MainActivity.this);
                actualFragment = menuFragment;
                ft.replace(R.id.fragment_container, menuFragment, "menuFragment");
                ft.commit();
                showActionButtons(false);
            }
        });
    }

    private void showPreviewImage(byte[] data) {
        String jpgFile = FileUtils.saveJpgFile(data, isPortrait());
        Intent intent = new Intent(MainActivity.this, PreviewActivity.class);
        intent.putExtra("jpgFile", jpgFile);
        startActivity(intent);
    }

    private void stopPreview() {
        if (camera != null)
            camera.stopPreview();
    }

    private void startPreview(SurfaceHolder holder) {
        try {
            stopPreview();
            releaseCameraAndPreview();
            camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
            configureCameraParameters();
            camera.setPreviewDisplay(holder);
            camera.startPreview();
        } catch (Exception e) {
            Log.e("init_camera: " + e);
            e.printStackTrace();
        }
    }

    private void releaseCameraAndPreview() {
        if (camera != null) {
            camera.release();
            camera = null;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mScaleDetector.onTouchEvent(event);
        return false;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d("surfaceCreated");
        startPreview(holder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d("surfaceCreated - w:" + width + ", h:" + height);
        if (holder.getSurface() == null) {
            // previewLayout surface does not exist
            return;
        }
        startPreview(holder);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d("surfaceDestroyed");
    }

    protected void configureCameraParameters() {
        if (camera != null) {
            Camera.Parameters parameters = camera.getParameters();
            List<Camera.Size> pictureSizes = parameters.getSupportedPictureSizes();
            List<Camera.Size> previewSizes = parameters.getSupportedPreviewSizes();
            parameters.setPreviewSize(previewSizes.get(0).width, previewSizes.get(0).height);
            parameters.setPictureSize(pictureSizes.get(0).width, pictureSizes.get(0).height);
            Log.d("Preview size - w: " + previewSizes.get(0).width + ", h: " + previewSizes.get(0).height);
            Log.d("Picture size - w: " + pictureSizes.get(0).width + ", h: " + pictureSizes.get(0).height);
            List<String> focusModes = parameters.getSupportedFocusModes();
            /*if (focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            } else */
            if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            }
            camera.setParameters(parameters);
            setCameraOrientation();
        }
    }

    private void setCameraOrientation(Camera.Parameters cameraParams) {
        if (camera != null) {
            if (isPortrait()) {
                cameraParams.set("orientationControl", Configuration.ORIENTATION_PORTRAIT);
                camera.setDisplayOrientation(90);
                cameraParams.setRotation(90);
            } else {
                cameraParams.set("orientationControl", Configuration.ORIENTATION_LANDSCAPE);
            }
        }
    }

    public void resetZoomCamera() {
        if (camera != null) {
            Camera.Parameters parameter = camera.getParameters();
            if (parameter.isZoomSupported()) {
                parameter.setZoom(0);
            }
            camera.setParameters(parameter);
        }
    }

    public boolean isPortrait() {
        return (this.orientationControl == Configuration.ORIENTATION_PORTRAIT);
    }

    private void setCameraOrientation() {
        if (camera != null)
            setCameraOrientation(camera.getParameters());
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        if (camera != null) {
            int scaleFactor = 2;
            if (detector.getScaleFactor() < 1) scaleFactor = -2;

            int maxZoom = 0;
            int currentZoom = 0;
            int zoom = 0;
            Camera.Parameters parameters = camera.getParameters();
            if (parameters.isZoomSupported()) {
                maxZoom = parameters.getMaxZoom();
                currentZoom = parameters.getZoom();
                zoom = currentZoom + scaleFactor;
                if (zoom < maxZoom && zoom >= 0)
                    parameters.setZoom(zoom);
                camera.setParameters(parameters);
            } else {
                Toast.makeText(this, "Zoom Not Avaliable", Toast.LENGTH_LONG).show();
            }
        }
        return true;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {
    }

    private void configureSensor() {
        SensorManager sensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
        sensorManager.registerListener(new SensorEventListener() {
            int orientation = -1;

            @Override
            public void onSensorChanged(SensorEvent event) {
                if (event.values[1] < 6.5 && event.values[1] > -6.5) {
                    if (orientation != 1) {
                        orientationControl = Configuration.ORIENTATION_LANDSCAPE;
                        setCameraOrientation();
                    }
                    orientation = 1;
                } else {
                    if (orientation != 0) {
                        orientationControl = Configuration.ORIENTATION_PORTRAIT;
                        setCameraOrientation();
                    }
                    orientation = 0;
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }
        }, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
    }

    public void showActionButtons(boolean show) {
        if (show) {
            btnTakePhoto.setVisibility(View.VISIBLE);
            btnMenu.setVisibility(View.VISIBLE);
        } else {
            btnTakePhoto.setVisibility(View.GONE);
            btnMenu.setVisibility(View.GONE);
        }
    }

    public void closeFirstAccessFragment() {
        PreferenceUtils.putString(this, Constants.SharedPreferences.FIRST_ACCESS, String.valueOf(TimeUtils.getTimeMillis()));
        showActionButtons(true);
        getFragmentManager().beginTransaction().remove(actualFragment).commit();
        verifyDatabase();
    }

    private void verifyFirstAccess() {
        String firstAccess = PreferenceUtils.getString(this, Constants.SharedPreferences.FIRST_ACCESS);
        Log.d("FIRST_ACCESS: " + firstAccess);
        if (TextUtils.isEmpty(firstAccess)) {
            showActionButtons(false);
            FirstAccessFragment fragment = new FirstAccessFragment();
            fragment.setActivity(this);
            actualFragment = fragment;
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment, Constants.Fragments.FRAGMENT_FIRST_ACCESS)
                    .addToBackStack(null)
                    .commit();

        } else {
            verifyDatabase();
        }
    }

    private void verifyDatabase() {
        boolean hasDisciplines = new DisciplinaDAO(this).hasDisciplines();
        if (!hasDisciplines) {
            showActionButtons(false);
            DisciplinesFragment fragment = new DisciplinesFragment();
            fragment.setActivity(this);
            actualFragment = fragment;
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment, Constants.Fragments.FRAGMENT_DISCIPLINES)
                    .addToBackStack(null)
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        //Do Nothing...
    }

    public void closeActualFragment() {
        getFragmentManager().beginTransaction().remove(actualFragment).commit();
        showActionButtons(true);
    }

    public void changeActualFragment(Fragment fragment) {
        actualFragment = fragment;
    }
}
