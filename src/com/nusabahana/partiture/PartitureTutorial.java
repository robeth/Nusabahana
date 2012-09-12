package com.nusabahana.partiture;

import android.content.Context;
import android.text.Html;
import android.text.Spannable;
import android.text.Spanned;
import android.util.Log;

import com.nusabahana.model.Instrument;
import com.nusabahana.model.InstrumentPlayer;

public class PartitureTutorial {
	private Partiture partiture;
	private TimedAction timedAction;
	private int currentRow, currentSegment, currentElement, currentSubElement;
	private long timePerElement;
	private OnPartitureNextRowListener onNextRowListener;
	private OnPartitureNextSegmentListener onNextSegmentListener;
	private OnPartitureNextElementListener onNextElementListener;
	private OnPartitureNextSubElementListener onNextSubElementListener;
	private OnPartitureHasNextAction onNextAction;
	private OnPartitureEnd onEnd;
	private OnPlayListener onPlayListener;
	private OnPauseListener onPauseListener;
	private OnResumeListener onResumeListener;
	private OnStopListener onStopListener;
	private int tutorialState;
	public static final int STANDBY = 0, PLAYING = 1, PAUSED = 2;

	public PartitureTutorial(Partiture partiture, long timePerElement) {
		this.partiture = partiture;
		this.timePerElement = timePerElement;
		this.timedAction = new TimedAction(new PartitureAction(),
				this.timePerElement);
		reset();
	}

	public int reset() {
		if (tutorialState != STANDBY) {
			stop();
		}
		currentRow = -1;
		nextRow();
		return 0;
	}

	public int play() {
		if (tutorialState == STANDBY) {
			tutorialState = PLAYING;
			timedAction.setTime(getNextInterval());
			timedAction.start();
			if (onPlayListener != null)
				onPlayListener.onPlay();
		}

		return 0;
	}

	public int pause() {
		if (tutorialState == PLAYING) {
			tutorialState = PAUSED;
			timedAction.pause();
			if (onPauseListener != null)
				onPauseListener.onPause();
		}
		return 0;
	}

	public int resume() {
		if (tutorialState == PAUSED) {
			tutorialState = PLAYING;
			timedAction.resume();
			if (onResumeListener != null)
				onResumeListener.onResume();
		}
		return 0;
	}

	public int stop() {
		if (tutorialState != STANDBY) {
			timedAction.stop();
			tutorialState = STANDBY;
			if (onStopListener != null)
				onStopListener.onStop();
			reset();
		}
		return 0;
	}

	public boolean hasNextRow() {
		return currentRow < partiture.getNumRows() - 1;
	}

	public boolean hasPrevRow() {
		return currentRow > 0;
	}

	public boolean hasNextSegment() {
		return currentSegment < partiture.getRows()[currentRow]
				.getNumSegments() - 1;
	}

	public boolean hasNextElement() {
		return currentElement < partiture.getRows()[currentRow].getSegments()[currentSegment]
				.getNumElements() - 1;
	}

	public boolean hasNextSubElement() {
		return currentSubElement < partiture.getRows()[currentRow]
				.getSegments()[currentSegment].getElements()[currentElement]
				.getNumSubElements() - 1;
	}

	public void nextRow() {
		currentRow++;
		currentSegment = -1;
		nextSegment();
		if (onNextRowListener != null)
			onNextRowListener.onNextRow();
	}

	public void nextSegment() {
		currentSegment++;
		currentElement = -1;
		nextElement();
		if (onNextSegmentListener != null)
			onNextSegmentListener.onNextSegment();
	}

	public void nextElement() {
		currentElement++;
		currentSubElement = -1;
		nextSubElement();
		if (onNextElementListener != null)
			onNextElementListener.onNextElement();
	}

	public void nextSubElement() {
		currentSubElement++;
		if (onNextSubElementListener != null)
			onNextSubElementListener.onNextSubElement();
	}

	public long getNextInterval() {
		return timePerElement
				/ partiture.getRows()[currentRow].getSegments()[currentSegment]
						.getElements()[currentElement].getNumSubElements();
	}

	public int getCurrentSubElementValue() {
		return partiture.getRows()[currentRow].getSegments()[currentSegment]
				.getElements()[currentElement].getSubElements()[currentSubElement]
				.getValue();
	}

	private class PartitureAction extends OnTickedListener {

		@Override
		public void onTicked() {
			Log.d("Partiture Player", "OnTicked");

			boolean hasNextAction = true;
			if (hasNextSubElement()) {

				nextSubElement();

			} else if (hasNextElement()) {

				nextElement();

			} else if (hasNextSegment()) {

				nextSegment();

			} else if (hasNextRow()) {

				nextRow();
			} else {
				hasNextAction = false;
			}

			if (hasNextAction) {
				Log.d("Partiture Player", "Next Action");

				timedAction.stop();
				timedAction.setTime(getNextInterval());
				timedAction.start();
				if (onNextAction != null)
					onNextAction.onNextAction();
			} else {

				Log.d("Partiture Player", "Stop");
				stop();
				if (onEnd != null)
					onEnd.onEnd();
			}
		}

	}

	public OnPartitureNextRowListener getOnNextRowListener() {
		return onNextRowListener;
	}

	public void setOnNextRowListener(
			OnPartitureNextRowListener onNextRowListener) {
		this.onNextRowListener = onNextRowListener;
	}

	public OnPartitureNextSegmentListener getOnNextSegmentListener() {
		return onNextSegmentListener;
	}

	public void setOnNextSegmentListener(
			OnPartitureNextSegmentListener onNextSegmentListener) {
		this.onNextSegmentListener = onNextSegmentListener;
	}

	public OnPartitureNextElementListener getOnNextElementListener() {
		return onNextElementListener;
	}

	public void setOnNextElementListener(
			OnPartitureNextElementListener onNextElementListener) {
		this.onNextElementListener = onNextElementListener;
	}

	public OnPartitureNextSubElementListener getOnNextSubElementListener() {
		return onNextSubElementListener;
	}

	public void setOnNextSubElementListener(
			OnPartitureNextSubElementListener onNextSubElementListener) {
		this.onNextSubElementListener = onNextSubElementListener;
	}

	public OnPartitureHasNextAction getOnNextAction() {
		return onNextAction;
	}

	public void setOnNextAction(OnPartitureHasNextAction onNextAction) {
		this.onNextAction = onNextAction;
	}

	public OnPartitureEnd getOnEnd() {
		return onEnd;
	}

	public void setOnEnd(OnPartitureEnd onEnd) {
		this.onEnd = onEnd;
	}

	public OnPlayListener getOnPlayListener() {
		return onPlayListener;
	}

	public void setOnPlayListener(OnPlayListener onPlayListener) {
		this.onPlayListener = onPlayListener;
	}

	public OnPauseListener getOnPauseListener() {
		return onPauseListener;
	}

	public void setOnPauseListener(OnPauseListener onPauseListener) {
		this.onPauseListener = onPauseListener;
	}

	public OnResumeListener getOnResumeListener() {
		return onResumeListener;
	}

	public void setOnResumeListener(OnResumeListener onResumeListener) {
		this.onResumeListener = onResumeListener;
	}

	public int getTutorialState() {
		return tutorialState;
	}

	public OnStopListener getOnStopListener() {
		return onStopListener;
	}

	public void setOnStopListener(OnStopListener onStopListener) {
		this.onStopListener = onStopListener;
	}

	public static abstract class OnPartitureNextRowListener {
		public abstract void onNextRow();
	}

	public static abstract class OnPartitureNextSegmentListener {
		public abstract void onNextSegment();
	}

	public static abstract class OnPartitureNextElementListener {
		public abstract void onNextElement();
	}

	public static abstract class OnPartitureNextSubElementListener {
		public abstract void onNextSubElement();
	}

	public static abstract class OnPartitureHasNextAction {
		public abstract void onNextAction();
	}

	public static abstract class OnPartitureEnd {
		public abstract void onEnd();
	}

	public static abstract class OnPlayListener {
		public abstract void onPlay();
	}

	public static abstract class OnPauseListener {
		public abstract void onPause();
	}

	public static abstract class OnResumeListener {
		public abstract void onResume();
	}

	public static abstract class OnStopListener {
		public abstract void onStop();
	}

	public Spanned getRowSpannable() {
		StringBuilder sb = new StringBuilder();
		Row printedRow = partiture.getRows()[currentRow];
		
		for (int i = 0; i < printedRow.getNumSegments(); i++) {
			int totalSegments = printedRow.getNumSegments();
			for (int j = 0; j < printedRow.getSegments()[currentSegment] .getNumElements(); j++) {
				int totalSubElements = printedRow.getSegments()[i].getElements()[j].getNumSubElements();
				if (totalSubElements > 1) sb.append("<u>");
				for (int l = 0; l < totalSubElements; l++) {
					if (i == currentSegment && j == currentElement && l == currentSubElement) {
						sb.append("<font color='red'>");
						sb.append(printedRow.getSegments()[i].getElements()[j].getSubElements()[l].getValue());
						sb.append("</font>");
					} else {
						sb.append(printedRow.getSegments()[i].getElements()[j].getSubElements()[l].getValue());
					}
					sb.append("&nbsp;");
				}
				if (totalSubElements > 1)sb.append("</u>");
				sb.append("&nbsp; &nbsp;");
			}
			if(i+1 < totalSegments)sb.append("&nbsp; &nbsp; &nbsp;");
		}

		return Html.fromHtml(sb.toString());
	}

	public Partiture getPartiture() {
		return partiture;
	}

	public void setPartiture(Partiture partiture) {
		this.partiture = partiture;
	}

	
}
