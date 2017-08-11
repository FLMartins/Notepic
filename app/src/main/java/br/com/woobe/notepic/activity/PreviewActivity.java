package br.com.woobe.notepic.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import br.com.woobe.notepic.R;
import br.com.woobe.notepic.util.FileUtils;

public class PreviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();

        String jpgFile = (String) getIntent().getSerializableExtra("jpgFile");

        ImageView imgViewer = (ImageView) findViewById(R.id.image_preview);
        Bitmap bm = FileUtils.getBitmap(jpgFile);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        imgViewer.setMinimumHeight(dm.heightPixels);
        imgViewer.setMinimumWidth(dm.widthPixels);
        imgViewer.setImageBitmap(bm);
    }

    private void init() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_preview);
        if (getActionBar() != null) {
            getActionBar().hide();
        } else if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
    }
}
