package br.com.woobe.notepic.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.Space;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import br.com.woobe.notepic.R;
import br.com.woobe.notepic.task.InitTask;
import br.com.woobe.notepic.util.CameraUtils;
import br.com.woobe.notepic.util.Log;

public class SplashActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 101;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 102;
    private static final String CAMERA = "CAMERA";
    private static final String PERMISSIONS = "PERMISSIONS";

    private TextView txtStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        setContentView(R.layout.activity_splash);
        txtStatus = (TextView) findViewById(R.id.txtStatus);
        txtStatus.setText(getText(R.string.init));

        try {
            new InitTask(this).execute();
        } catch (Exception e) {
            Log.e(e.getMessage());
            e.printStackTrace();
        }
    }

    public void requestPermissions() {
        setStatus(getString(R.string.init_permissions));
        if (requestCameraPermission() && requestStoragePermission()) {
            testCamera();
        }
    }

    public void testCamera() {
        try {
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            setStatus(getString(R.string.init_camera));
            if (CameraUtils.testCamera()) {
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                        Log.d("SplashActivity - starting MainActivity...");
                        finish();
                    }
                }, 1000L);
            }
        } catch (Exception e) {
            Log.e(e.getMessage());
            e.printStackTrace();
            showDialog(CAMERA);
        }
    }

    private boolean requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
            return false;
        }
        return true;
    }

    private boolean requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    MY_PERMISSIONS_REQUEST_CAMERA);
            return false;
        }
        return true;
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
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("onRequestPermissionsResult - CAMERA");
                    requestPermissions();
                } else {
                    showDialog(PERMISSIONS);
                }
                return;
            }
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("onRequestPermissionsResult - STORAGE");
                    requestPermissions();
                } else {
                    showDialog(PERMISSIONS);
                }
                return;
            }
        }
    }

    private void showDialog(final String flag) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String msg = "";
                if (CAMERA.equals(flag)) {
                    msg = getString(R.string.error_camera);
                } else if (PERMISSIONS.equals(flag)) {
                    msg = getString(R.string.error_permissions);
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
                builder.setCancelable(false)
                    .setTitle("Ops!")
                    .setMessage(msg)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            finish();
                        }
                    })
                    .show();
            }
        });
    }

    public void setStatus(final String status) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                txtStatus.setText(status);
            }
        });
    }
}
