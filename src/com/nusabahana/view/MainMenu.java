package com.nusabahana.view;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

import com.nusabahana.view.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

/**
 * Kelas yang merupakan view dari menu utama
 * 
 * @author PPL-B1
 * 
 */
public class MainMenu extends Activity {

	private boolean firstTime = true;

	/**
	 * Inisialisasi activity
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		ImageView playImage = (ImageView) findViewById(R.id.play);
		ImageView galleryImage = (ImageView) findViewById(R.id.gallery);
		ImageView aboutImage = (ImageView) findViewById(R.id.aboutImage);
		
		playImage.setOnClickListener(playListener);
		galleryImage.setOnClickListener(galleryListener);
		aboutImage.setOnClickListener(aboutListener);
	}

	/**
	 * Listener yang dipanggil ketika gambar play ditekan
	 */
	private OnClickListener playListener = new OnClickListener() {
		public void onClick(View v) {
			Intent newIntent = new Intent(
					MainMenu.this.getApplicationContext(),
					ChooseInstrumentMenu.class);
			MainMenu.this.startActivity(newIntent);
		}
	};

	/**
	 * Listener yang dipanggil ketika gambar gallery ditekan
	 */
	private OnClickListener galleryListener = new OnClickListener() {
		public void onClick(View v) {
			Intent newIntent = new Intent(
					MainMenu.this.getApplicationContext(),
					RecordGalleryMenu.class);
			MainMenu.this.startActivity(newIntent);
		}
	};
	
	private OnClickListener aboutListener = new OnClickListener() {
		public void onClick(View v) {
			Intent newIntent = new Intent(
					MainMenu.this.getApplicationContext(),
					AboutView.class);
			MainMenu.this.startActivity(newIntent);
		}
	};

	public void onWindowFocusChanged(boolean hasFocus) {

		if (firstTime) {
			firstTime = false;
			
			Animation a = AnimationUtils.loadAnimation(this, R.anim.anim);
			a.reset();
			final ImageView iv = (ImageView) findViewById(R.id.img);
			iv.clearAnimation();
			a.setAnimationListener(new AnimationListener() {

				@Override
				public void onAnimationEnd(Animation animation) {
					// TODO Auto-generated method stub
					iv.setVisibility(View.INVISIBLE);
				}

				@Override
				public void onAnimationRepeat(Animation animation) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onAnimationStart(Animation animation) {
					// TODO Auto-generated method stub

				}

			});

			iv.startAnimation(a);

			a = AnimationUtils.loadAnimation(this, R.anim.translateleft);
			a.reset();
			final ImageView iv2 = (ImageView) findViewById(R.id.img1);
			a.setAnimationListener(new AnimationListener() {

				@Override
				public void onAnimationEnd(Animation animation) {
					// TODO Auto-generated method stub
					iv2.setVisibility(View.INVISIBLE);
				}

				@Override
				public void onAnimationRepeat(Animation animation) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onAnimationStart(Animation animation) {
					// TODO Auto-generated method stub

				}

			});

			iv2.startAnimation(a);

			a = AnimationUtils.loadAnimation(this, R.anim.rotatepic);
			a.reset();
			ImageView iv3 = (ImageView) findViewById(R.id.playring);
			iv3.clearAnimation();
			iv3.startAnimation(a);

			a = AnimationUtils.loadAnimation(this, R.anim.rotatepicleft);
			a.reset();
			iv3 = (ImageView) findViewById(R.id.galleryring);
			iv3.clearAnimation();
			iv3.startAnimation(a);
			

		}
	}

}