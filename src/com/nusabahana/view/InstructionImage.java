package com.nusabahana.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.nusabahana.instrument.InstrumentView;

/**
 * Salah satu kelas prototipe untuk implementasikan multitouch
 * @author PPL-B1
 *
 */
public class InstructionImage extends ImageView {

	private int lastState;
	private static int STANDBY = 0;
	private static int PRESSED = 1;
	private int index;
	private InstrumentView instrumentView;
	private InstructionListener instructionListener;
	
	public InstructionImage(final Context context, final AttributeSet attrs) {
		super(context, attrs);
		lastState = STANDBY;
	}
	
	public void setIndex(int index){
		this.index = index;
	}
	
	public void setInstrumentView(InstrumentView instrumentView){
		this.instrumentView= instrumentView;
	}
	
	public void doInstruction(){
		if(instructionListener!= null)
			instructionListener.doInstruction();
	}
	
	public InstructionListener getInstructionListener() {
		return instructionListener;
	}

	public void setInstructionListener(InstructionListener instructionListener) {
		this.instructionListener = instructionListener;
	}



	public static interface InstructionListener{
		public abstract void doInstruction();
	}
}
