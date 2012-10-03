package com.nusabahana.model;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.util.Log;

public class InstrumentPlayer {
	private Instrument[] instruments;
	private HashMap<Integer, ArrayList<Integer>> soundIds;
	private SoundPool soundPool;
	private Context activity;
	
	public InstrumentPlayer(Instrument[] instruments, Context activity) {
		this.instruments = instruments;
		soundPool = new SoundPool(100, AudioManager.STREAM_MUSIC, 0);
		soundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
			@Override
			public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
				Log.d("SoundPool Of Nusabahana", "Completed:"+ sampleId);
				
			}
		});
		this.activity = activity;
		soundIds = new HashMap<Integer, ArrayList<Integer>>();
	}

	public void prepareSound(){
		for(int i = 0; i < instruments.length; i++){
			Note[] notes = instruments[i].getNotes();
			
			for( int j = 0; j < notes.length; j++){
				int ids= soundPool.load( activity, activity.getResources().getIdentifier( getFileName(notes[j].getPath()), "raw", activity.getPackageName()), 1);
				if(!soundIds.containsKey(notes[j].getKey())){
					ArrayList<Integer> keyList = new ArrayList<Integer>();
					keyList.add(ids);
					soundIds.put(notes[j].getKey(), keyList);
				} else {
					ArrayList<Integer> oldList = soundIds.get(notes[j].getKey());
					oldList.add(ids);
				}
			}
		}
	}
	
	public void play(int key){
		if(!soundIds.containsKey(key)) return;
		
		ArrayList<Integer> keyList = soundIds.get(key);
		
		AudioManager audioManager = (AudioManager) activity
				.getSystemService(activity.AUDIO_SERVICE);
		float actualVolume = (float) audioManager
				.getStreamVolume(AudioManager.STREAM_MUSIC);
		float maxVolume = (float) audioManager
				.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		float volume = actualVolume / maxVolume;

		
		for(int i = 0; i < keyList.size(); i++){
			soundPool.play(keyList.get(i), volume, volume, 1,0,1f);
		}
	}
	
	public void reset(){
		soundPool.release();
		soundPool = new SoundPool(32, AudioManager.STREAM_MUSIC, 0);
		soundIds.clear();
	}
	
	private String getFileName(String fileName) {
		return fileName.substring(0, fileName.lastIndexOf("."));
	}
}
