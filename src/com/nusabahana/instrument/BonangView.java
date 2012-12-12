package com.nusabahana.instrument;

import java.util.Arrays;

import android.app.Activity;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.nusabahana.model.Instrument;
import com.nusabahana.view.DistributorView;
import com.nusabahana.view.InstrumentSimulationMenu;
import com.nusabahana.view.NoteImage;
import com.nusabahana.view.R;

public class BonangView extends InstrumentView {

	public BonangView(Activity activity, DistributorView distributorView,
			Instrument instrument, int instrumentViewID) {
		super(activity, distributorView, instrument, instrumentViewID);
	}

	@Override
	public void playInstruction(int instructionID) {
		// TODO Auto-generated method stub
		super.playInstruction(instructionID);
		// TODO Auto-generated method stub
		
		int[] highlightedIndexes = (instrument.getNickname()
				.equals("bonang")) ? getBonangHighlightedIndexes(instructionID)
				: instrument.getInstrumentIndexes(instructionID);

		for (int i = 0; i < instrumentParts.length; i++) {
			if (isIn(highlightedIndexes, i)) {
				instrumentParts[i].setColorFilter(0x99ff0000);
				shakePlay(i);
			} else
				instrumentParts[i].setColorFilter(Color.TRANSPARENT);
		}
		
	}
	
	@Override
	public void shakePlay(int index) {
		super.shakePlay(index);
		instrumentPlayer.play(index);
		instrumentParts[index].startAnimation(instrumentAnimations[index]);
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		super.init();
		instrumentParts = new NoteImage[instrument.getNotes().length];
		instrumentLabels = new TextView[instrumentParts.length];
		instrumentAnimations = new Animation[instrumentParts.length];
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
//			instrumentLabels[i].setTextSize(TypedValue.COMPLEX_UNIT_PX, (float)convertToPixels(10));
			instrumentAnimations[i] = AnimationUtils.loadAnimation(activity,
					R.anim.shake);
			distributorView.registerView(instrumentParts[i]);
		}
	}
	private int convertToPixels(int dps){
		final float scale = activity.getResources().getDisplayMetrics().density;
		return (int) (dps * scale + 0.5f);
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		super.reset();
	}

	protected boolean isIn(int[] arrayInt, int intValue) {
		for (int i = 0; i < arrayInt.length; i++)
			if (arrayInt[i] == intValue)
				return true;

		return false;
	}

	protected int[] getBonangHighlightedIndexes(int key) {
		int[] highlightedIndexes = new int[2];
		Arrays.fill(highlightedIndexes, -1);
		switch (key) {
		case 101:
			highlightedIndexes[0] = 8;
			highlightedIndexes[1] = 8;
			break;
		case 110:
			highlightedIndexes[0] = 5;
			highlightedIndexes[1] = 5;
			break;
		case 111:
			highlightedIndexes[0] = 5;
			highlightedIndexes[1] = 8;
			break;
		case 102:
			highlightedIndexes[0] = 9;
			highlightedIndexes[1] = 9;
			break;
		case 120:
			highlightedIndexes[0] = 4;
			highlightedIndexes[1] = 4;
			break;
		case 122:
			highlightedIndexes[0] = 4;
			highlightedIndexes[1] = 9;
			break;
		case 103:
			highlightedIndexes[0] = 10;
			highlightedIndexes[1] = 10;
			break;
		case 130:
			highlightedIndexes[0] = 3;
			highlightedIndexes[1] = 3;
			break;
		case 133:
			highlightedIndexes[0] = 3;
			highlightedIndexes[1] = 10;
			break;
		case 104:
			highlightedIndexes[0] = 13;
			highlightedIndexes[1] = 13;
			break;
		case 140:
			highlightedIndexes[0] = 0;
			highlightedIndexes[1] = 0;
			break;
		case 144:
			highlightedIndexes[0] = 0;
			highlightedIndexes[1] = 13;
			break;
		case 105:
			highlightedIndexes[0] = 11;
			highlightedIndexes[1] = 11;
			break;
		case 150:
			highlightedIndexes[0] = 2;
			highlightedIndexes[1] = 2;
			break;
		case 155:
			highlightedIndexes[0] = 2;
			highlightedIndexes[1] = 11;
			break;
		case 106:
			highlightedIndexes[0] = 12;
			highlightedIndexes[1] = 12;
			break;
		case 160:
			highlightedIndexes[0] = 1;
			highlightedIndexes[1] = 1;
			break;
		case 166:
			highlightedIndexes[0] = 1;
			highlightedIndexes[1] = 12;
			break;
		case 107:
			highlightedIndexes[0] = 7;
			highlightedIndexes[1] = 7;
			break;
		case 170:
			highlightedIndexes[0] = 6;
			highlightedIndexes[1] = 6;
			break;
		case 177:
			highlightedIndexes[0] = 6;
			highlightedIndexes[1] = 7;
			break;
		}
		return highlightedIndexes;
	}

}
