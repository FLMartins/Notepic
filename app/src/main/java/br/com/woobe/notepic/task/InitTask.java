package br.com.woobe.notepic.task;

import android.os.AsyncTask;

import br.com.woobe.notepic.R;
import br.com.woobe.notepic.activity.SplashActivity;

/**
 * Created by willian alfeu on 16/01/2017.
 */

public class InitTask extends AsyncTask<Void, Integer, Void> {

    private SplashActivity activity;

    public InitTask(SplashActivity activity) {
        this.activity = activity;
    }

    @Override
    protected Void doInBackground(Void... params) {
        activity.requestPermissions();
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }
}
