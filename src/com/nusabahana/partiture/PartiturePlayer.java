package com.nusabahana.partiture;

import android.content.Context;
import android.util.Log;

import com.nusabahana.model.Instrument;
import com.nusabahana.model.InstrumentPlayer;


public class PartiturePlayer {
	private Instrument[] instruments;
	private Partiture[] partitures;
	private TimedAction timedAction;
	private PartitureAction partitureAction;
	private Partiture mainPartiture;
	private int currentRow, currentSegment, currentElement, currentSubElement;
	private long timePerElement;
	private int currentSubelementThres;
	private InstrumentPlayer instrumentPlayer;
	private Context activity;
	
	public PartiturePlayer(Context activity, Instrument[] instruments, Partiture[] partitures, long timePerElement) {
		this.instruments = instruments;
		this.partitures = partitures;
		this.mainPartiture = partitures[0];
		this.timePerElement = timePerElement;
		this.timedAction = new TimedAction(new PartitureAction(), timePerElement);
		this.instrumentPlayer = new InstrumentPlayer(instruments, activity);
		instrumentPlayer.prepareSound();
		reset();
	}

	public int reset(){
		currentRow = -1;
		nextRow();
		return 0;
	}
	
	public int play() {
		timedAction.setTime(getNextInterval());
		timedAction.start();
		return 0;
	}

	public int pause() {
		timedAction.pause();
		return 0;
	}

	public int resume() {
		timedAction.resume();
		return 0;
	}

	public int stop() {
		timedAction.stop();
		reset();
		return 0;
	}

	public boolean hasNextRow() {
		return currentRow < mainPartiture.getNumRows() - 1;
	}

	public boolean hasPrevRow() {
		return currentRow > 0;
	}
	
	public boolean hasNextSegment(){
		return currentSegment < mainPartiture.getRows()[currentRow].getNumSegments() - 1;
	}
	
	public boolean hasNextElement(){
		return currentElement < mainPartiture.getRows()[currentRow].getSegments()[currentSegment].getNumElements() - 1;
	}
	
	public boolean hasNextSubElement(){
		return currentSubElement < currentSubelementThres - 1;
	}
	
	private int pollMaxSubElement(int row, int segment, int element){
		int max  = 0;
		
		for(int i = 0 ; i < partitures.length; i++){
			max= Math.max(max,partitures[i].getRows()[row].getSegments()[segment].getElements()[element].getNumSubElements());			
		}
		
		return max;
	}
	
	public void nextRow(){
		currentRow++;
		currentSegment = currentElement = currentSubElement = 0;
		currentSubelementThres = pollMaxSubElement(currentRow, currentSegment, currentElement);
	}
	
	public void nextSegment(){
		currentSegment++;
		currentElement = currentSubElement = 0;
		currentSubelementThres = pollMaxSubElement(currentRow, currentSegment, currentElement);
	}
	
	public void nextElement(){
		currentElement++;
		currentSubElement = 0;
		currentSubelementThres = pollMaxSubElement(currentRow, currentSegment, currentElement);
	}
	
	public void nextSubElement(){
		currentSubElement++;
	}
	
	public void instructInstruments(){
		/**
		 * Need to be changed
		 */
		instrumentPlayer.play(0);
	}
	
	public long getNextInterval(){
		return timePerElement / currentSubelementThres; 
	}

	private class PartitureAction extends OnTickedListener{

		@Override
		public void onTicked() {
			Log.d("Partiture Player", "OnTicked");
			instructInstruments();
			
			boolean hasNextAction = true;
			if(hasNextSubElement()){
				nextSubElement();
			} else if(hasNextElement()){
				nextElement();
			} else if (hasNextSegment()){
				nextSegment();
			} else if (hasNextRow()){
				nextRow();
			} else {
				hasNextAction = false;
			}
			
			if(hasNextAction){
				Log.d("Partiture Player", "Next Action");
				timedAction.stop();
				timedAction.setTime(getNextInterval());
				timedAction.start();
			} else {
				Log.d("Partiture Player", "Stop");
				stop();
			}
		}
		
	}
}
