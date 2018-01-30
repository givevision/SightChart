package com.givevision.sightchart;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glViewport;
import static android.opengl.Matrix.orthoM;

/**
 * Created by piotr on 24/08/2017.
 */

class MainRenderer implements GLSurfaceView.Renderer{
    private static final String TAG = "MainRenderer";

    private RightSprite sprite1;
    private LeftSprite sprite2;
    private final Context context;
    private MainView mView;
    private WeakReference<MainActivity> mWeakActivity;
    public MainActivity activity;

    private boolean mGLInit = false;
    private boolean isImageDownloaded = false;
    private boolean mUpdateST = false;

    private final float[] mMVPMatrix = new float[16];
    private final float[] mVMatrix = new float[16];
    private float[] mRotationMatrix = new float[16];
    private final float[] projectionMatrix = new float[16];
    public volatile float mAngle=0f;

    private Bitmap bitmap;
    private boolean mSetupImg = false;
    private List<Integer> imageId= new ArrayList<Integer>();
    private List<Integer> contrastId= new ArrayList<Integer>();
    public static List<Bitmap> imageBitmap= new ArrayList<Bitmap>();;
    private int pos=-1;
    private int posSaved=-1;
    private int task = -1;
    private int taskSaved=-1;
    MainRenderer (MainView view, MainActivity act ) {
        this.context = view.getContext();
        LogManagement.Log_i(TAG, "MainRenderer:: started" );
        mView = view;
        mWeakActivity = new WeakReference<MainActivity>(act);
        activity = mWeakActivity.get();

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;   // No pre-scaling
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        bitmap = BitmapFactory.decodeResource( context.getResources(), R.drawable.testimg, options);
//        bitmap=inversBitmap(bitmap);
        LogManagement.Log_i(TAG, "MainRenderer:: drawPicTexture: imageBitmap = "+imageBitmap.size() );

        imageId.add(R.drawable.logmar_25);
        imageId.add(R.drawable.logmar_24);
        imageId.add(R.drawable.logmar_23);
        imageId.add(R.drawable.logmar_22);
        imageId.add(R.drawable.logmar_21);
        imageId.add(R.drawable.logmar_20);
        imageId.add(R.drawable.logmar_19);
        imageId.add(R.drawable.logmar_18);
        imageId.add(R.drawable.logmar_17);
        imageId.add(R.drawable.logmar_16);
        imageId.add(R.drawable.logmar_15);
        imageId.add(R.drawable.logmar_14);
        imageId.add(R.drawable.logmar_13);
        imageId.add(R.drawable.logmar_12);
        imageId.add(R.drawable.logmar_11);
        imageId.add(R.drawable.logmar_10);
        imageId.add(R.drawable.logmar_09);
        imageId.add(R.drawable.logmar_08);
        imageId.add(R.drawable.logmar_07);
        imageId.add(R.drawable.logmar_06);
        imageId.add(R.drawable.logmar_05);
        contrastId.add(R.drawable.contrast_11);
        contrastId.add(R.drawable.contrast_12);
        contrastId.add(R.drawable.contrast_21);
        contrastId.add(R.drawable.contrast_22);
        contrastId.add(R.drawable.contrast_31);
        contrastId.add(R.drawable.contrast_32);
        contrastId.add(R.drawable.contrast_41);
        contrastId.add(R.drawable.contrast_42);
        contrastId.add(R.drawable.contrast_51);
        contrastId.add(R.drawable.contrast_52);
        contrastId.add(R.drawable.contrast_61);
        contrastId.add(R.drawable.contrast_62);
        contrastId.add(R.drawable.contrast_71);
        contrastId.add(R.drawable.contrast_72);
        contrastId.add(R.drawable.contrast_81);
    }
    public void onResume() {
        LogManagement.Log_i(TAG, "MainRenderer:: onResume: started" );
        mGLInit = false;
        mUpdateST = false;
    }

    public void onPause() {
        LogManagement.Log_i(TAG, "MainRenderer:: onPause: started" );
        if(mGLInit){
            mGLInit = false;
        }
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        LogManagement.Log_i(TAG, "MainRenderer:: onSurfaceCreated: started" );
        if(mGLInit)
            return;

        mGLInit = true;

        sprite1 = new RightSprite(context);
        sprite2 = new LeftSprite(context);

        Point ss = new Point();

        mView.getDisplay().getRealSize(ss);
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        LogManagement.Log_i(TAG, "MainRenderer:: onSurfaceChanged: started" );

        glViewport(0, 0, width, height);
        orthoM(projectionMatrix, 0, -1f, 1f, -1f, 1f, -1f, 1f);
    }


    @Override
    public synchronized void onDrawFrame(GL10 gl10) {
        LogManagement.Log_i(TAG, "MainRenderer:: onDrawFrame: started task= "+task+ " taskSaved= "+taskSaved+" pos= "+pos +" posSaved= "+posSaved );
        glClear(GL_COLOR_BUFFER_BIT);

        //Set the camera position (View Matrix)
        Matrix.setLookAtM(mVMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        //Calculate the projection and view transformation
        Matrix.multiplyMM(mMVPMatrix, 0, projectionMatrix, 0, mVMatrix, 0);

        //Create a rotation transformation for the triangle
        Matrix.setRotateM(mRotationMatrix, 0, mAngle, 0, 0, -1.0f);

        //Combine the rotation matrix with the projection and camera view
        Matrix.multiplyMM(mMVPMatrix, 0, mRotationMatrix, 0, mMVPMatrix, 0);

        Bitmap imgBitmap=null;
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;   // No pre-scaling
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        if(posSaved!=pos){
            posSaved=pos;
            Bitmap bitmap=null;
            if(task==1 && taskSaved==task && pos>=0) {
                taskSaved=task;
                bitmap = BitmapFactory.decodeResource(context.getResources(), imageId.get(pos), options);
            }else if(task==2  && taskSaved==task  && pos>=0){
                taskSaved=task;
                bitmap = BitmapFactory.decodeResource(context.getResources(), contrastId.get(pos), options);
            }else{
                pos=0;
                posSaved=-1;
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.testimg, options);
            }
            this.bitmap=bitmap.copy(Bitmap.Config.ARGB_8888,true);
            bitmap.recycle();
        }

//        }else{
//            Bitmap bitmap = BitmapFactory.decodeResource( context.getResources(), R.drawable.testimg, options);
//            sprite1.Draw(mMVPMatrix,bitmap,0);
//        }
        imgBitmap=inversBitmap(this.bitmap);
        sprite2.Draw(mMVPMatrix,imgBitmap,0);
        sprite1.Draw(mMVPMatrix,imgBitmap,0);

        imgBitmap.recycle();
//        sprite1.Draw(mMVPMatrix,null,R.drawable.testimg);

    }

    public void setSetupImg(int p,int t) {
        LogManagement.Log_i(TAG, "MainRenderer:: setSetupImg: "+p );
        this.pos=p;
        if(t!=this.task){
            taskSaved=t;
        }
        this.task=t;

    }

    public int getNbrImages() {
        LogManagement.Log_i(TAG, "MainRenderer:: getNbrImages: "+this.imageId.size());
        return this.imageId.size();
    }

    public int getNbrContrast() {
        LogManagement.Log_i(TAG, "MainRenderer:: getNbrImages: "+this.imageId.size());
        return this.contrastId.size();
    }

    public static int loadShader(int type, String shaderCode){
        LogManagement.Log_i(TAG, "MainRenderer:: loadShader: started" );
        //Create a Vertex Shader Type Or a Fragment Shader Type (GLES20.GL_VERTEX_SHADER OR GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        //Add The Source Code and Compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }

    private static Bitmap inversBitmap(Bitmap image){
        int height=image.getHeight();
        int width=image.getWidth();

        Bitmap srcBitmap= Bitmap.createBitmap(width, height, image.getConfig());

        for (int y=width-1;y>=0;y--)
            for(int x=0;x<height;x++)
                srcBitmap.setPixel(y,height-x-1,image.getPixel(y,x));
        return srcBitmap;

    }

}
