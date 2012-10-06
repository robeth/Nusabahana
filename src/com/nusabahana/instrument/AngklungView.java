package com.nusabahana.instrument;

import java.util.Arrays;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.nusabahana.model.Instrument;
import com.nusabahana.view.DistributorView;
import com.nusabahana.view.InstructionImage;
import com.nusabahana.view.NoteImage;
import com.nusabahana.view.R;

public class AngklungView extends InstrumentView {
	private final int NORMAL_OCTAVE = 0, HIGH_OCTAVE = 1, LOW_OCTAVE = 2,
			OFFSET_INDEX = 0, NUMBER = 1, START_INDEX = 2, END_INDEX = 3,
			INSTRUMENT_NEXT = 100, INSTRUMENT_PREV = 101;

	private int state;
	private InstructionImage instructionNextImage, instructionPrevImage;
	private int[][] mappingIndex = { { 0, 12, 0, 11 }, { 0, 10, 12, 21 },
			{ 4, 8, 22, 29 } };
	private int prevInstructionID = 0;

	public AngklungView(Activity activity, DistributorView distributorView,
			Instrument instrument, int instrumentViewID) {
		super(activity, distributorView, instrument, instrumentViewID);
		state = NORMAL_OCTAVE;
		instructionNextImage = (InstructionImage) activity.findViewById(R.id.toggle_button_next);
		instructionNextImage.setOnClickListener(instructionNextListener);
		instructionPrevImage = (InstructionImage) activity.findViewById(R.id.toggle_button_prev);
		instructionPrevImage.setOnClickListener(instructionPrevListener);
		instructionNextImage.bringToFront();
		instructionPrevImage.bringToFront();
	}

	@Override
	public void init() {
		instrumentParts = new NoteImage[12];
		instrumentLabels = new TextView[12];
		instrumentAnimations = new Animation[12];
		for (int i = 0; i < instrumentParts.length; i++) {
			instrumentParts[i] = (NoteImage) activity.findViewById(activity
					.getResources().getIdentifier(instrument.getNickname() + i,
							"id", activity.getPackageName()));
			instrumentLabels[i] = (TextView) activity.findViewById(activity
					.getResources().getIdentifier(
							instrument.getNickname() + "_text" + i, "id",
							activity.getPackageName()));
			instrumentParts[i].setIndex(i);
			instrumentParts[i].setInstrumentView(this);
			instrumentLabels[i].setText(instrument.getNoteLabel(i));
			instrumentAnimations[i] = AnimationUtils.loadAnimation(activity,
					R.anim.shake);
			distributorView.registerView(instrumentParts[i]);
		}
	}

	private void refresh() {
		distributorView.clear();
		int[] temp = new int[12];
		Arrays.fill(temp, -1);

		for (int i = mappingIndex[state][OFFSET_INDEX]; i < mappingIndex[state][OFFSET_INDEX]+mappingIndex[state][NUMBER]; i++) {
			
			temp[i] = mappingIndex[state][START_INDEX] + i - mappingIndex[state][OFFSET_INDEX];
			Log.d("Refresh","index("+i+") :"+ temp[i] );
		}

		// Yang -1 dibuat invisible dan unregister
		// Yang ada di set new index & new Text

		for (int i = 0; i < 12; i++) {
			if (temp[i] == -1) {
				// distributorView.unregisterView(i);
				instrumentParts[i].setVisibility(View.INVISIBLE);
			} else {
				distributorView.registerView(instrumentParts[i]);
				instrumentParts[i].setVisibility(View.VISIBLE);
				instrumentParts[i].setIndex(temp[i]);
				instrumentLabels[i].setText(instrument
						.getNoteLabel(temp[i]));
			}
		}

		// Refresh instrument Image
		if (state == NORMAL_OCTAVE) {
			instructionNextImage.setVisibility(View.VISIBLE);
			instructionPrevImage.setVisibility(View.VISIBLE);
		} else if (state == HIGH_OCTAVE) {
			instructionNextImage.setVisibility(View.INVISIBLE);
			instructionPrevImage.setVisibility(View.VISIBLE);

		} else if (state == LOW_OCTAVE) {
			instructionNextImage.setVisibility(View.VISIBLE);
			instructionPrevImage.setVisibility(View.INVISIBLE);
		}
	}

	private OnClickListener instructionNextListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Log.d("next", "Hello in next Listener");
			if (state == NORMAL_OCTAVE)
				state = HIGH_OCTAVE;
			else if (state == LOW_OCTAVE)
				state = NORMAL_OCTAVE;
			refresh();
		}

	};

	private OnClickListener instructionPrevListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Log.d("next", "Hello in next Listener");
			if (state == NORMAL_OCTAVE)
				state = LOW_OCTAVE;
			else if (state == HIGH_OCTAVE)
				state = NORMAL_OCTAVE;
			refresh();
		}

	};

	public void playInstruction(int instructionID) {
		if(instructionID == 777){
			instructionID = prevInstructionID;
		} else {
			prevInstructionID = instructionID;
		}
		
		if(!isOnOctave(state, instructionID)){
			changeOctave(instructionID);
		}
		
		int[] highlightedIndexes = instrument.getRelativeIndexes(instructionID,
				mappingIndex[state][OFFSET_INDEX],
				mappingIndex[state][START_INDEX],
				mappingIndex[state][END_INDEX]);

		for (int i = 0; i < instrumentParts.length; i++) {
			if (isIn(highlightedIndexes, i)) {
				instrumentParts[i].setColorFilter(0x99ff0000);
				shakePlay(instrumentParts[i].getIndex());
			} else
				instrumentParts[i].setColorFilter(Color.TRANSPARENT);
		}
	}
	
	private void changeOctave(int instructionID){
		if(state == NORMAL_OCTAVE){
			if(isOnOctave(HIGH_OCTAVE, instructionID)){
				Log.d("IN CHANGE","instructionID:"+instructionID+" is HIGH octave");
				state = HIGH_OCTAVE;
				refresh();
			} else if(isOnOctave(LOW_OCTAVE, instructionID)){
				Log.d("IN CHANGE","instructionID:"+instructionID+" is LOW octave");
				state = LOW_OCTAVE;
				refresh();
			}
		} else if (state == HIGH_OCTAVE){
			if(isOnOctave(NORMAL_OCTAVE, instructionID)){
				Log.d("IN CHANGE","instructionID:"+instructionID+" is NORMAL octave");
				state = NORMAL_OCTAVE;
				refresh();
			} else if(isOnOctave(LOW_OCTAVE, instructionID)){
				Log.d("IN CHANGE","instructionID:"+instructionID+" is LOW octave");
				state = LOW_OCTAVE;
				refresh();
			}
		} else {
			if(isOnOctave(HIGH_OCTAVE, instructionID)){
				Log.d("IN CHANGE","instructionID:"+instructionID+" is HIGH octave");
				state = HIGH_OCTAVE;
				refresh();
			} else if(isOnOctave(NORMAL_OCTAVE, instructionID)){
				Log.d("IN CHANGE","instructionID:"+instructionID+" is NORMAL octave");
				state = NORMAL_OCTAVE;
				refresh();
			}
		}
	}

	private boolean isOnOctave(int octaveState, int instructionID){
		for(int i = mappingIndex[octaveState][START_INDEX]; i <= mappingIndex[octaveState][END_INDEX];i++){
			if(instrument.getNotes()[i].getKey() == instructionID){
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void shakePlay(int index) {
		instrumentPlayer.play(index);
		int imageIndex = index - mappingIndex[state][START_INDEX] + mappingIndex[state][OFFSET_INDEX];
		instrumentParts[imageIndex].startAnimation(instrumentAnimations[imageIndex]);
	}

	protected boolean isIn(int[] arrayInt, int intValue) {
		for (int i = 0; i < arrayInt.length; i++)
			if (arrayInt[i] == intValue)
				return true;

		return false;
	}
}
