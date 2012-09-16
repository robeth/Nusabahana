package com.nusabahana.partiture;

import android.os.Handler;
import android.util.Log;

public class TimedAction {
	private Handler handler;
	private TimerAction timerAction;
	private long time;
	private OnTickedListener onTickedListener;
	private long startTime;
	private long pauseTime;
	private long resumeTime;
	
	public TimedAction(OnTickedListener onTickedListener, long time) {
		this.onTickedListener = onTickedListener;
		this.time = time;
		handler = new Handler();
		timerAction = new TimerAction();
	}

	public boolean start(){
		startTime = System.currentTimeMillis();
		
		Log.e("TimedAction", "Start");
		handler.postDelayed(timerAction, time);
		return false;
	}
	
	public boolean pause(){
		pauseTime = System.currentTimeMillis();
		
		handler.removeCallbacks(timerAction);
		return false;
	}
	
	public boolean stop(){
		handler.removeCallbacks(timerAction);
		
		return false;
	}
	
	public boolean resume(){
		resumeTime = System.currentTimeMillis();
		handler.postDelayed(timerAction, time - (pauseTime-startTime));
		
		startTime += resumeTime - pauseTime;
		return false;
	}
	
	private class TimerAction implements Runnable {
		@Override
		public void run() {
			Log.e("TimedAction", "OnRun");
			onTickedListener.onTicked();
		}
	}


	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}
	
	
}

abstract class OnTickedListener{
	public abstract void onTicked();
}