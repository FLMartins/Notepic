package br.com.woobe.notepic.util;

import android.content.res.Configuration;
import android.hardware.Camera;
import android.view.SurfaceHolder;

import java.util.List;

import br.com.woobe.notepic.exception.CameraException;

/**
 * Created by willian alfeu on 16/01/2017.
 */

public class CameraUtils {

    private static Camera camera;

    public static boolean testCamera() throws CameraException {
        try {
            stopPreview();
            releaseCameraAndPreview();
            camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
            configureCameraParameters();
            camera.startPreview();
            return true;
        } catch (Exception e) {
            throw new CameraException(e);
        }
    }

    private static void releaseCameraAndPreview() throws CameraException {
        try {
            if (camera != null) {
                camera.release();
                camera = null;
            }
        } catch (Exception e) {
            throw new CameraException(e);
        }
    }

    protected static void configureCameraParameters() throws CameraException {
        try {
            if (camera != null) {
                Camera.Parameters parameters = camera.getParameters();
                List<Camera.Size> pictureSizes = parameters.getSupportedPictureSizes();
                List<Camera.Size> previewSizes = parameters.getSupportedPreviewSizes();
                parameters.setPreviewSize(previewSizes.get(0).width, previewSizes.get(0).height);
                parameters.setPictureSize(pictureSizes.get(0).width, pictureSizes.get(0).height);
                List<String> focusModes = parameters.getSupportedFocusModes();
                if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
                    parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
                }
                camera.setParameters(parameters);
            }
        } catch (Exception e) {
            throw new CameraException(e);
        }
    }

    private static void stopPreview() throws CameraException {
        try {
            if (camera != null)
                camera.stopPreview();
        } catch (Exception e) {
            throw new CameraException(e);
        }
    }

}
