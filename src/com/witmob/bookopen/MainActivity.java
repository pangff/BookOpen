package com.witmob.bookopen;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.widget.FrameLayout;

@SuppressLint("NewApi")
public class MainActivity extends GlobalActivity {
	private PerspectiveView perspectiveView;
	private FrameLayout container;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		container = (FrameLayout) this.findViewById(R.id.containers);
		perspectiveView = new PerspectiveView(this);
		container.addView(perspectiveView);
		
		Bitmap cover = BitmapFactory.decodeResource(this.getResources(), R.drawable.cover);
		Bitmap innerCover = BitmapFactory.decodeResource(this.getResources(), R.drawable.inner_cover);
		Bitmap spineImage = BitmapFactory.decodeResource(this.getResources(), R.drawable.spine);
		perspectiveView.setTextures(cover, spineImage, innerCover);
	}
	
	public void startAnimation() {
		perspectiveView.onResume();
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				perspectiveView.startAnimation();
			}
		}, 400);
	}

	@Override
	protected void onPause() {
		 if (perspectiveView != null) {
			 perspectiveView.onPause();
		 }
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		startAnimation();
	}
}
