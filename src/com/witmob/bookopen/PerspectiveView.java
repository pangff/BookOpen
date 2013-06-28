package com.witmob.bookopen;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_DEPTH_BUFFER_BIT;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glViewport;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.Handler;
import android.util.Log;
import android.view.animation.AccelerateDecelerateInterpolator;

/**
 * Created with IntelliJ IDEA.
 * User: marshal
 * Date: 13-5-6
 * Time: 下午3:36
 * To change this template use File | Settings | File Templates.
 */
@SuppressLint("NewApi")
public class PerspectiveView extends GLSurfaceView implements GLSurfaceView.Renderer {

    //投影矩阵
    private float[] projectionMatrix = new float[16];

    //视图矩阵
    private float[] viewMatrix = new float[16];

    //模型矩阵
    private float[] modelMatrix = new float[16];

    //模型视图投影矩阵
    private float[] mvpMatrix = new float[16];

    private TextureMesh coverMesh, contentMesh, spineMesh;

    private float distance = 15.5f;

    private float width, height, ratio, factor;

    private long duration = 1000;

    private Bitmap coverTexture, spineTexture, contentTexture;
    
    public ManimatorListener animatorListener;
    
    
    public void setManimatorListener(ManimatorListener animatorListener) {
		this.animatorListener = animatorListener;
	}

	public interface ManimatorListener{
    	public void onAnimationEnd();
    }
	
    public PerspectiveView(Context context) {
        super(context);
        this.init();
    }

    public void setTextures(Bitmap coverTexture, Bitmap spineTexture, Bitmap contentTexture) {
        this.coverTexture = coverTexture;
        this.spineTexture = spineTexture;
        this.contentTexture = contentTexture;
    }

    private void init() {
        this.setEGLContextClientVersion(2);

        this.setEGLConfigChooser(8, 8, 8, 8, 0, 0);
        this.setZOrderOnTop(true);
        this.getHolder().setFormat(PixelFormat.TRANSPARENT);

        this.setRenderer(this);
        this.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        glClearColor(0, 0, 0, 0);

        //照相机位置
        float eyeX = 0.0f;
        float eyeY = 0.0f;
        float eyeZ = distance;
//        float eyeZ=1f;

        //照相机拍照方向
        float lookX = 0.0f;
        float lookY = 0.0f;
        float lookZ = -1.0f;

        //照相机的垂直方向
        float upX = 0.0f;
        float upY = 1.0f;
        float upZ = 0.0f;

        Matrix.setLookAtM(viewMatrix, 0, eyeX, eyeY, eyeZ, lookX, lookY, lookZ, upX, upY, upZ);

        coverMesh = new TextureMesh(getContext());
        contentMesh = new TextureMesh(getContext());
        spineMesh = new TextureMesh(getContext());
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        this.width = width;
        this.height = height;
        glViewport(0, 0, width, height);


        ratio = (float) width / height;

//        float k=0.48f;

//        Matrix.perspectiveM(projectionMatrix, 0, 5.88f, ratio, near, far);
    }

    float nr, nd, barW;

    @Override
    public void onDrawFrame(GL10 gl10) {
        float left = -ratio;
        float right = ratio;
        float top = 1;
        float bottom = -1;
        float near = distance;
        float far = 100;

        float alpha = (float) Math.atan(ratio / near);

        float c = (float) (ratio / Math.sin(alpha));
        float nr = Math.abs((float) (Math.sin(alpha) * c / (1 - 2 * Math.sin(alpha))));

        float nd = 2 * nr * near / c;

        Matrix.frustumM(projectionMatrix, 0, left, right, bottom, top, near, far);

        this.nr = nr;
        this.nd = nd;

        glClear(GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT);


        setMeshTexture();
//        Vertex[] vertexes = getTargetViewVertexes();
        Vertex[] vertexes = getTextureVertexes();

        coverMesh.setVertexes(vertexes);

        spineMesh.setVertexes(new Vertex[]{
                new Vertex(vertexes[0].positionX - barW, vertexes[0].positionY, 0, 1f),
                new Vertex(vertexes[1].positionX - barW, vertexes[1].positionY, 0, 1f),
                new Vertex(vertexes[0].positionX, vertexes[0].positionY, 0, 1f),
                new Vertex(vertexes[1].positionX, vertexes[1].positionY, 0, 1f)
        });

        contentMesh.setVertexes(getTextureVertexes());

//        Matrix.translateM(modelMatrix, 0, -ratio, 0, -nd);
//        Matrix.translateM(modelMatrix, 0, -nr, 0, -nd);

        Matrix.setIdentityM(modelMatrix, 0);
        Matrix.translateM(modelMatrix, 0, -nr + barW * (1 - factor), 0, -nd - (5 * (1 - factor)));
        Matrix.multiplyMM(mvpMatrix, 0, viewMatrix, 0, modelMatrix, 0);
        Matrix.multiplyMM(mvpMatrix, 0, projectionMatrix, 0, mvpMatrix, 0);
        contentMesh.draw(mvpMatrix);

        spineMesh.draw(mvpMatrix);

        Matrix.setIdentityM(modelMatrix, 0);

        Matrix.translateM(modelMatrix, 0, -nr + barW * (1 - factor), 0, -nd - (5 * (1 - factor)));
        Matrix.rotateM(modelMatrix, 0, -90 * factor, 0, 1, 0);

        Matrix.multiplyMM(mvpMatrix, 0, viewMatrix, 0, modelMatrix, 0);
        Matrix.multiplyMM(mvpMatrix, 0, projectionMatrix, 0, mvpMatrix, 0);

        long time = System.currentTimeMillis();
        coverMesh.draw(mvpMatrix);
        Log.d("origami4", "time: " + (System.currentTimeMillis() - time));

//        Matrix.setIdentityM(modelMatrix, 0);
//        Matrix.translateM(modelMatrix, 0, -nr + barW * (1 - factor), 0, -nd - (5 * (1 - factor)));
//        Matrix.multiplyMM(mvpMatrix, 0, viewMatrix, 0, modelMatrix, 0);
//        Matrix.multiplyMM(mvpMatrix, 0, projectionMatrix, 0, mvpMatrix, 0);
//        contentMesh.draw(mvpMatrix);
    }

    boolean hasTexture;

    private void setMeshTexture() {
        if (!hasTexture) {
//            targetView.setDrawingCacheEnabled(true);
//            Bitmap texture = Bitmap.createBitmap(targetView.getDrawingCache());
//            coverMesh.setTexture(texture);
//            targetView.setDrawingCacheEnabled(false);
//            Log.d("origami4", "set mesh texture");
//            hasTexture = true;

            Bitmap bitmap = Bitmap.createBitmap(coverTexture);
            coverMesh.setTexture(bitmap);

//            BitmapDrawable drawable = (BitmapDrawable) getContext().getResources().getDrawable(R.drawable.loadings_0);
//            contentMesh.setTexture(drawable.getBitmap());
            bitmap = Bitmap.createBitmap(contentTexture);
            contentMesh.setTexture(bitmap);

//            targetView.setDrawingCacheEnabled(true);
//            texture = Bitmap.createBitmap(targetView.getDrawingCache());
//            contentMesh.setTexture(texture);
//            targetView.setDrawingCacheEnabled(false);
//            Log.d("origami4", "set mesh texture");

            bitmap = Bitmap.createBitmap(spineTexture);
            spineMesh.setTexture(bitmap);

            barW = 2f * bitmap.getWidth() / bitmap.getHeight();

//            Log.d("origami4", "bar w>>>>>>>>>>>>" + barW + ", bitmap w:" + bitmap.getWidth() + ", h: " + bitmap.getHeight());

            hasTexture = true;
        }

    }

    private Vertex[] getTextureVertexes() {
        float w = nr * 2;
        float h = nr * 2 / ratio;

        Vertex[] vertexes = new Vertex[]{
                new Vertex(0, h, 0, 1f),
                new Vertex(0, 0, 0, 1f),
                new Vertex(w, h, 0, 1f),
                new Vertex(w, 0, 0, 1f)
        };

        for (Vertex v : vertexes) {
            v.translate(0, -h / 2f);
        }

        return vertexes;
    }

    private Vertex[] getTargetViewVertexes() {

        float w, h;

        w = nr * 2;
        h = nr * 2 / ratio;

        Vertex[] vertexes = new Vertex[]{
                new Vertex(0, h, 0, 1f),
                new Vertex(0, 0, 0, 1f),
                new Vertex(w, h, 0, 1f),
                new Vertex(w, 0, 0, 1f)
        };

        for (Vertex v : vertexes) {
            v.translate(0, -h / 2f);
        }

        return vertexes;
    }

    public void startAnimation() {
        ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
        animator.setDuration(duration);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                final float f = (Float) valueAnimator.getAnimatedValue();
                queueEvent(new Runnable() {
                    @Override
                    public void run() {
                        factor = f;
                        requestRender();
                    }
                });
            }
        });

        animator.start();
        animator.addListener(new AnimatorListener() {
			
			@Override
			public void onAnimationStart(Animator animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animator animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animator animation) {
				// TODO Auto-generated method stub
				if(animatorListener!=null){
					animatorListener.onAnimationEnd();
				}
			}
			
			@Override
			public void onAnimationCancel(Animator animation) {
				// TODO Auto-generated method stub
				
			}
		});
    }
}
