package com.nusabahana.view;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

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
		if ((getResources().getConfiguration().screenLayout &      Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE) {     
	        Toast.makeText(this, "Large screen",Toast.LENGTH_LONG).show();

	    }
	    else if ((getResources().getConfiguration().screenLayout &      Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL) {     
	        Toast.makeText(this, "Normal sized screen" , Toast.LENGTH_LONG).show();

	    } 
	    else if ((getResources().getConfiguration().screenLayout &      Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_SMALL) {     
	        Toast.makeText(this, "Small sized screen" , Toast.LENGTH_LONG).show();
	    }
	    else {
	        Toast.makeText(this, "Screen size is neither large, normal or small" , Toast.LENGTH_LONG).show();
	    }
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
			

			Animation a = AnimationUtils.loadAnimation(this, R.anim.rotatepic);
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