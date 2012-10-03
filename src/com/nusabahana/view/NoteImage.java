package com.nusabahana.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Salah satu kelas prototipe untuk implementasikan multitouch
 * @author PPL-B1
 *
 */
public class NoteImage extends ImageView {

	private int lastState;
	private static int STANDBY = 0;
	private static int PRESSED = 1;
	private int index;
	private InstrumentSimulationMenu nv;
	
	public NoteImage(final Context context, final AttributeSet attrs) {
		super(context, attrs);
		lastState = STANDBY;
	}
	
	public void setIndex(int index){
		this.index = index;
	}
	
	public void setActivity(InstrumentSimulationMenu nv){
		this.nv = nv;
	}
	
	public void play(){
		nv.shakePlay(index);
	}

}
