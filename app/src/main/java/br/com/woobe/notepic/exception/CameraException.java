package br.com.woobe.notepic.exception;

/**
 * Created by willian alfeu on 16/01/2017.
 */

public class CameraException extends RuntimeException {

    public CameraException() {
    }

    public CameraException(String message) {
        super(message);
    }

    public CameraException(String message, Throwable cause) {
        super(message, cause);
    }

    public CameraException(Throwable cause) {
        super(cause);
    }

}
