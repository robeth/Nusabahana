package com.nusabahana.model;

import java.util.ArrayList;

import android.app.Activity;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;
import android.util.Log;

/**
 * Kelas yang merupakan pemutar file rekaman
 * 
 * 
 * @author PPL-B1
 * 
 */
public class RecordPlayer {
	/** Context dari Android */
	private Activity activity;
	/** Rekaman yang dimainkan oleh pemutar rekaman ini */
	private Record record;
	/** Objek yang digunakan untuk memainkan audio file*/
	private SoundPool soundPool;
	/** ID dari audio file yang dimainkan oleh soundPool*/
	private int[] activeSoundId;
	/** Listener yang dipanggil ketika durasi permainan rekaman habis */
	private OnRecordEndListener onRecordEndListener;
	
	/**
	 * Mengambil listener pemutar rekaman ini
	 * @return 
	 */
	public OnRecordEndListener getOnRecordEndListener() {
		return onRecordEndListener;
	}

	/**
	 * Mengganti listener pemutar rekaman ini
	 * 
	 * @param onRecordEndListener listener yang baru
	 * @return 
	 */
	public void setOnRecordEndListener(OnRecordEndListener onRecordEndListener) {
		this.onRecordEndListener = onRecordEndListener;
	}

	/**
	 * Contructor default
	 * 
	 * @param a context
	 *            
	 * @param r rekaman yang ingin diputar
	 *      	  
	 * @param or listener yang akan dipanggil ketika durasi permainan rekaman habis 
	 * 			   
	 */
	public RecordPlayer (Activity a, Record r, OnRecordEndListener or){
		this.activity = a;
		this.record = r;
		this.onRecordEndListener = or;
	}
	
	/**
	 * Mempersiapkan objek soundPool sehingga siap memainkan audio file
	 * 
	 * @return 
	 */
	private void prepareSoundPool(){
		this.activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
		Note[] notes = record.getInstrument().getNotes();
		this.soundPool = new SoundPool(notes.length, AudioManager.STREAM_MUSIC, 0);
		this.activeSoundId = new int[notes.length];
		
		for(int i = 0; i < notes.length; i++){
			activeSoundId[notes[i].getIndex()] = soundPool.load(activity,activity.getResources().getIdentifier(getFileName(notes[i].getPath()), "raw", activity.getPackageName()), 1);
		}
	
	}
	
	/**
	 * Mengambil nama file rekaman
	 * 
	 * @param nama file utuh 
	 * @return nama file tanpa ekstensi
	 */
	private String getFileName(String fileName){
		return fileName.substring(0,fileName.lastIndexOf("."));
	}
	
	/**
	 * Memainkan audio file dari note ke-'index'
	 * @param  index dari note yang ingin dimainkan
	 */
	private void playIndex(int index){
		AudioManager audioManager = (AudioManager) activity.getSystemService(activity.AUDIO_SERVICE);
		float actualVolume = (float) audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		float maxVolume = (float) audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		float volume = actualVolume / maxVolume;
		soundPool.play(activeSoundId[index], volume, volume, 1, 0, 1f);
	}

	/**  Handler yang mengeksekusi runnable*/
	private Handler h;
	/**  Runnable yang dieksekusi handler*/
	private Runnable r;
	/**  Counter dari action yang telah dilakukan*/
	private int counter;
	/**  Sekumpulan action dari rekaman yang dimainkan*/
	private ArrayList<Action> actions;
	/**  Waktu dari action terakhir yang telah dimainkan oleh pemutar rekaman ini*/
	private long previousTimeStamp;
	
	/**
	 * Wrapper untuk mulai memainkan rekaman
	 */
	public void playRecord(){
		prepareSoundPool();
		h = new Handler();
		counter = 0;
		actions = record.getActions();
		previousTimeStamp = 0;
		play();
	}

	/**
	 * Memainkan satuan action
	 * 
	 */
	private void play(){
		
		if(counter < actions.size()){
			long currentTimeStamp = actions.get(counter).getTime();
			r = new AtomicAction(actions.get(counter).getIndex());
			counter++;
			
//			Log.e("Lanjut", "JUUUUUT in : "+ (currentTimeStamp - previousTimeStamp));
			h.postDelayed(r, currentTimeStamp - previousTimeStamp);
			previousTimeStamp = currentTimeStamp;
			
		} else {
			if(onRecordEndListener != null){
				onRecordEndListener.onEnded();
//				Log.e("END", "EMDD");
			}
		}
	}
	
	/**
	 * Satuan aksi (berupa runnable) yang dieksekusi oleh handler
	 * 
	 */
	private class AtomicAction implements Runnable{
		private int index;
		
		public AtomicAction(int index){
			this.index = index;
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			RecordPlayer.this.playIndex(index);
			RecordPlayer.this.play();
		}
		
	}
	
	/**
	 * Menghentikan rekaman yang sedang diputar
	 * 
	 */
	public void stopPlayRecord(){
		if(h != null)
			h.removeCallbacks(r);
	}

	
}
