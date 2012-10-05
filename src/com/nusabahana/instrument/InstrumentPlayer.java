package com.nusabahana.instrument;

import android.app.Activity;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

import com.nusabahana.model.Instrument;
import com.nusabahana.model.Note;

public class InstrumentPlayer {
	private SoundPool soundPool;
	private Activity activity;
	private int[] activeSoundId;
	private int[] activeChannels;
	
	
	
	public InstrumentPlayer(Activity activity) {
		this.activity = activity;
	}

	public void play(int index) {
		AudioManager audioManager = (AudioManager) activity
				.getSystemService(activity.AUDIO_SERVICE);
		float actualVolume = (float) audioManager
				.getStreamVolume(AudioManager.STREAM_MUSIC);
		float maxVolume = (float) audioManager
				.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		float volume = actualVolume / maxVolume;

		soundPool.play(activeSoundId[index], volume, volume, 1, 0, 1f);
	}
	
	public void prepareSoundPool(Instrument ins) {
		if(soundPool != null){
			soundPool.release();
			soundPool = null;
		}
		soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
		this.activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);

		Note[] notes = ins.getNotes();
		this.soundPool = new SoundPool(ins.getNotes().length,
				AudioManager.STREAM_MUSIC, 0);
		this.activeSoundId = new int[notes.length];
		this.activeChannels = new int[notes.length];
		for (int i = 0; i < notes.length; i++) {
			Log.d("Cari path", getFileName(notes[i].getPath()));
			activeSoundId[notes[i].getIndex()] = soundPool.load(
					activity,
					activity.getResources().getIdentifier(
							getFileName(notes[i].getPath()), "raw",
							activity.getPackageName()), 1);
			activeChannels[i] = -1;
		}

	}
	
	private String getFileName(String fileName) {
		return fileName.substring(0, fileName.lastIndexOf("."));
	}
	
}
