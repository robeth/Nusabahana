package com.nusabahana.instrument;

import android.app.Activity;
import android.view.animation.Animation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nusabahana.model.Instrument;
import com.nusabahana.view.DistributorView;
import com.nusabahana.view.NoteImage;

public class InstrumentView {
	protected int instrumentViewID;
	protected Activity activity;
	protected RelativeLayout instrumentView;
	protected NoteImage[] instrumentParts;
	protected TextView[] instrumentLabels;
	protected Animation[] instrumentAnimations;
	protected Instrument instrument;
	protected DistributorView distributorView;
	protected InstrumentPlayer instrumentPlayer;
	
	
	public InstrumentView(Activity activity, DistributorView distributorView, Instrument instrument, int instrumentViewID){
		this.activity = activity;
		this.distributorView = distributorView;
		this.instrument = instrument;
		this.instrumentViewID = instrumentViewID;
		instrumentPlayer = new InstrumentPlayer(activity);
		instrumentPlayer.prepareSoundPool(instrument);
		init();
	}

	public void playInstruction(int instructionID){};
	public void shakePlay(int index){};
	public void init(){};
	public void reset(){};
}
