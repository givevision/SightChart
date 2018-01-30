package com.givevision.sightchart;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.SurfaceHolder;

/**
 * Created by piotr on 24/08/2017.
 */

public class MainView extends GLSurfaceView {
    MainRenderer mRenderer;

    public MainView(Context context, MainActivity act) {
        super(context);
        mRenderer = new MainRenderer(this, act);
        setEGLContextClientVersion ( 2 );
        setRenderer ( mRenderer );
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }

    public void surfaceCreated ( SurfaceHolder holder ) {
        super.surfaceCreated ( holder );
    }

    public void surfaceDestroyed ( SurfaceHolder holder ) {
        super.surfaceDestroyed ( holder );
    }

    public void surfaceChanged ( SurfaceHolder holder, int format, int w, int h ) {
        super.surfaceChanged ( holder, format, w, h );
    }

    @Override
    public void onResume() {
        super.onResume();
        mRenderer.onResume();
    }

    @Override
    public void onPause() {
        mRenderer.onPause();
        super.onPause();
    }

    public void setSetupImg(int p, int t) {
        mRenderer.setSetupImg(p,t);
    }

    public int getNbrImages() {
        return mRenderer.getNbrImages();
    }

    public int getNbrContrast() {
        return mRenderer.getNbrContrast();
    }

}
