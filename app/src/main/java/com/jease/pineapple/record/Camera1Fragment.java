package com.jease.pineapple.record;

import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import com.jease.pineapple.R;
import com.jease.pineapple.gles.GLController;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class Camera1Fragment extends Fragment implements SurfaceHolder.Callback {

    private SurfaceView mSurfaceView;

    private GLController mGLController;

    public static Camera1Fragment newInstance() {
        return new Camera1Fragment();
    }

    private RenderCallback mRenderCallback = new RenderCallback() {
        @Override
        public void onSurfaceDestroyed() {
            CameraHelper.getInstance().stopPreview();
            CameraHelper.getInstance().release();
        }

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            CameraHelper.getInstance().prepareCameraThread();
            CameraHelper.getInstance().openCamera(0);
            int rotation = getActivity().getWindowManager().getDefaultDisplay().getRotation();
            CameraHelper.getInstance().initCamera(rotation, 9.0 / 16.0);
            SurfaceTexture surfaceTexture = mGLController.getSurfaceTexture();
            if (surfaceTexture == null)
                return;
            surfaceTexture.setOnFrameAvailableListener(new SurfaceTexture.OnFrameAvailableListener() {
                @Override
                public void onFrameAvailable(SurfaceTexture surfaceTexture) {
                    mGLController.requestRender();
                }
            });
            CameraHelper.getInstance().setPreviewTexture(surfaceTexture);
            CameraHelper.getInstance().startPreview();
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
        }

        @Override
        public void onDrawFrame(GL10 gl) {
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera1, container, false);
        mSurfaceView = view.findViewById(R.id.surface_view);
        mSurfaceView.getHolder().addCallback(this);
        mGLController = new GLController(getContext());
        mGLController.setRenderCallback(mRenderCallback);
        return view;
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (null != mGLController)
            mGLController.surfaceCreated(holder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (null != mGLController)
            mGLController.surfaceChanged(width, height);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (null != mGLController)
            mGLController.surfaceDestroyed();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (null != mGLController)
            mGLController.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (null != mGLController)
            mGLController.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != mGLController) {
            mGLController.release();
            mGLController = null;
        }
    }
}