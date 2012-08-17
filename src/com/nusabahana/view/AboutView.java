package com.nusabahana.view;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class AboutView extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		
		View animatedView[] = new View [11]; 
		
		animatedView[0] = findViewById(R.id.imageView1);
		animatedView[1] = findViewById(R.id.imageView2);
		animatedView[2] = findViewById(R.id.imageView3);
		animatedView[3] = findViewById(R.id.imageView4);
		animatedView[4] = findViewById(R.id.imageDosen);
		
		animatedView[5] = findViewById(R.id.nameTata);
		animatedView[6] = findViewById(R.id.nameTika);
		animatedView[7] = findViewById(R.id.nameRobeth);
		animatedView[8] = findViewById(R.id.namePanji);
		animatedView[9] = findViewById(R.id.nameSiheq);
		animatedView[10] = findViewById(R.id.dosenText);
		
		for (int i=0; i < 5; i++){
			animatedView[i].bringToFront();
			if(animatedView[i] instanceof ImageView){
				ImageView iv = (ImageView) animatedView[i];
			}
		}
		
		Animation[] animations = new Animation[10];
		for(int i = 0; i < animations.length/2; i++){
			animations[i] = AnimationUtils.loadAnimation(this, R.anim.appear_down);
			animations[i].setStartOffset(i*250);
			animatedView[i].startAnimation(animations[i]);
			
			animations[i+animations.length/2] = AnimationUtils.loadAnimation(this, R.anim.appear_down);
			animations[i+animations.length/2].setStartOffset(i*250);
			animatedView[i+animations.length/2].startAnimation(animations[i+animations.length/2]);
		}
	}
}
