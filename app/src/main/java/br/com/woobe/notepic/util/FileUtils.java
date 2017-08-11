package br.com.woobe.notepic.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by willian alfeu on 25/11/2016.
 */

public class FileUtils {
    public static String saveJpgFile(byte[] data, boolean portrait) {
        FileOutputStream outStream = null;
        String fileName = "";
        try {
            String path = getTempDirectory();
            fileName = String.format("/%d.jpg", System.currentTimeMillis());
            outStream = new FileOutputStream(path + fileName);
            Bitmap realImage = BitmapFactory.decodeByteArray(data, 0, data.length);
            if (portrait) {
                realImage = rotate(realImage, 90);
            }
            if (!realImage.compress(Bitmap.CompressFormat.JPEG, 100, outStream)) {
                throw new IOException("Erro ao salvar arquivo");
            }
            outStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileName;
    }

    public static Bitmap rotate(Bitmap bitmap, int degree) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Matrix mtx = new Matrix();
        mtx.setRotate(degree);
        return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
    }

    public static Bitmap getBitmap(String name) {
        String filePath = getTempFilePath(name);
        return BitmapFactory.decodeFile(filePath);
    }

    private static String getTempDirectory() {
        String path = String.format("%s/%s", Environment.getExternalStorageDirectory().getPath(), "Notepic/Temp");
        File file = new File(path);
        file.mkdirs();
        return path;
    }

    private static String getTempFilePath(String name) {
        return getTempDirectory() + "/" + name;
    }
}
