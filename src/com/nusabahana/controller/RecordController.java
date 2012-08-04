package com.nusabahana.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import com.nusabahana.controller.BackgroundMusicController.OnBackgroundMusicStartListener;
import com.nusabahana.model.Action;
import com.nusabahana.model.Instrument;
import com.nusabahana.model.OnRecordEndListener;
import com.nusabahana.model.Record;
import com.nusabahana.model.RecordPlayer;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * Kelas yang merupakan controller dari semua aktivitas yang berhubungan dengan
 * rekaman
 * 
 * @author PPL-B1
 * 
 */
public class RecordController {

	public static final int STANDBY = 0;
	public static final int RECORD_RECORDING = 1;
	public static final int PLAY_PLAYING = 3;
	public static final int PLAY_PAUSING = 4;

	private int modeRecord = STANDBY;
	private int modePlay = STANDBY;
	
	private String lastSavedFile;
	private String currentFile;

	private InstrumentController INSTRUMENT_CONTROLLER;
	public static String DEFAULT_RECORD_FOLDER_PATH;


	/** Context terkini dari controller */
	private Activity activity;

	/**
	 * Contructor default
	 * 
	 * @param a
	 *            context controller ini
	 * @param ic
	 *            objek dari instrument controller
	 */
	public RecordController(Activity a, InstrumentController ic) {
		this.activity = a;
		this.INSTRUMENT_CONTROLLER = ic;
		this.DEFAULT_RECORD_FOLDER_PATH = Environment
				.getExternalStorageDirectory().getAbsolutePath()
				+ "/Nusabahana/records";
		makeDirectory();
	}
//
//	/**
//	 * Melanjutkan proses rekaman
//	 */
//	public void continueRecording() {
//		mode = RECORDING;
//		start = SystemClock.uptimeMillis() - temp;
//	}

//	/**
//	 * Menghapus suatu file rekaman
//	 * 
//	 * @param fName
//	 *            nama file rekaman yang ingin dihapus
//	 */
	public void deleteFile(String fName) {
		File file = new File(DEFAULT_RECORD_FOLDER_PATH + "/" + fName);
		file.delete();
	}

	/**
	 * Mengambil nama dari semua file rekaman
	 * 
	 * @return nama dari semua file rekaman
	 */
	public String[] getAllRecordsName() {
		File root = new File(DEFAULT_RECORD_FOLDER_PATH);
		String[] nsrFiles = root.list(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String filename) {
				Log.e("FILE RECORD", filename);
				// TODO Auto-generated method stub
				String ext = filename.substring(filename.lastIndexOf(".") + 1,
						filename.length());
				if (ext.equalsIgnoreCase("3gp"))
					return true;
				return false;
			}

		});
		if (nsrFiles != null)
			Arrays.sort(nsrFiles);
		return nsrFiles;
	}

//	/**
//	 * Mengambil lamanya rekaman telah berlangsung
//	 * 
//	 * @return
//	 */
//	public long getDuration() {
//		if (mode == STANDBY)
//			return 0;
//		else if (mode == RECORDING)
//			return SystemClock.uptimeMillis() - start;
//		else
//			return temp;
//	}

	/**
	 * Mengambil nama file rekaman yang terakhir kali disimpan
	 * 
	 * @return nama file rekaman yang terakhir kali disimpan
	 */
	public String getLastSavedFile() {
		return lastSavedFile;
	}

	/**
	 * Mendapatkan nama default file rekaman
	 * 
	 * @param path
	 *            directory file baru akan disimpan
	 * @param name
	 *            nama alat musik file rekaman
	 * @param counter
	 *            counter
	 * @return nama default file rekaman
	 */
	private String getNewName(String path, String name, int counter) {
		File f = new File(path + "/" + name + counter + ".3gp");
		while (f.exists()) {
			Log.e("Exist?", f.getAbsolutePath());
			f = new File(path + "/" + name + ++counter + ".3gp");
		}
		return name + counter + ".3gp";
	}

//	/**
//	 * Mendapatkan objek rekaman yang dinginkan
//	 * 
//	 * @param fileName
//	 *            nama file rekaman yang dinginkan
//	 * @return objek file rekaman yang diinginkan
//	 */
//	public Record getRecord(String fileName) {
//		Record aa = null;
//		try {
//			File recordFile = new File(DEFAULT_RECORD_FOLDER_PATH + "/"
//					+ fileName);
//			BufferedReader reader = new BufferedReader(new FileReader(
//					recordFile));
//
//			String line = "";
//			String instrumentName = reader.readLine();
//			Date d = new Date(reader.readLine());
//			ArrayList<Action> acts = new ArrayList<Action>();
//			while ((line = reader.readLine()) != null) {
//				String[] a = line.split(" ");
//				acts.add(new Action(Long.parseLong(a[0]), Integer
//						.parseInt(a[1])));
//			}
//			long duration = 0;
//			if (acts.size() > 0)
//				duration = acts.get(acts.size() - 1).getTime();
//			aa = new Record(fileName,
//					INSTRUMENT_CONTROLLER.getInstrument(instrumentName),
//					duration, d, acts);
//			reader.close();
//		} catch (IOException ioe) {
//			Log.e("Error IO", ioe.getMessage());
//		}
//		return aa;
//	}

	/**
	 * Mengambil status rekaman controller
	 * 
	 * @return status rekaman controller
	 */
	public int getRecordingState() {
		return this.modeRecord;
	}
	
	public int getPlayingState() {
		return this.modePlay;
	}
	

	/**
	 * Mengecek apakah nama file yang baru valid atau tidak
	 * 
	 * @param nName
	 *            nama file yang akan dicek
	 * @return status apakah nama baru valid atau tidak. Jika null, valid. Jika
	 *         tidak valid, fungsi ini mengembalikan pesan error
	 */
	public String isValidNewName(String nName) {

		File aFile = new File(DEFAULT_RECORD_FOLDER_PATH + "/" + nName);
		
		if (!aFile.exists()) {
			// Cek apakah nama baru tidak macem2
			try {
				if (aFile.createNewFile()) {
					aFile.delete();
				}
			} catch (IOException e) {
				return nName + "is invalid file name.";
			}

			// cek apakah nama baru berekstension benar
			if ((nName.endsWith(".3gp")) && (nName.length() <= 20)) {
				return null;
			} else {
				return "Allowed extension is 3gp and max 20 characters long";
			}
		} else {
			return nName + " already exist";
			}
	}
	/**
	 * Membuat direktori default
	 */
	private void makeDirectory() {
		// TODO Auto-generated method stub
		File f = new File(DEFAULT_RECORD_FOLDER_PATH);
		if (!f.exists())
			f.mkdirs();
	}

//	/**
//	 * Melakukan pause terhadap proses rekaman
//	 */
//	public void pauseRecording() {
//		mode = PAUSE;
//		temp = SystemClock.uptimeMillis() - start;
//	}

//	/**
//	 * Memainkan suatu file rekaman
//	 * 
//	 * @param fileName
//	 *            nama file rekaman yang ingin dimainkan
//	 * @param or
//	 *            listener yang dipanggil ketika rekaman selesai dimainkan
//	 */
//	public void playRecord(String fileName, OnRecordEndListener or) {
//		if (recordPlayer != null) {
//			stopPlayingRecord();
//		}
//		recordPlayer = new RecordPlayer(activity, getRecord(fileName), or);
//		recordPlayer.playRecord();
//	}

//	/**
//	 * Mencatat aksi-aksi selama proses rekaman berlangsung
//	 * 
//	 * @param index
//	 *            index aksi yang dicatat
//	 */
//	public void recordAction(int index) {
//		// Log.e("new action", "" + (SystemClock.uptimeMillis() - start) + "\t"+
//		// index);
//		actions.add(new Action(SystemClock.uptimeMillis() - start, index));
//	}

	/**
	 * Mengganti nama file rekaman
	 * 
	 * @param oldName
	 *            nama file rekaman yang lama
	 * @param nName
	 *            nama file rekaman yang baru
	 */
	public void renameFile(String oldName, String nName) {
		// TODO Auto-generated method stub
		File oldFile = new File(DEFAULT_RECORD_FOLDER_PATH + "/" + oldName);
		File newFile = new File(DEFAULT_RECORD_FOLDER_PATH + "/" + nName);
		oldFile.renameTo(newFile);
	}

	/**
	 * Mengganti context terkini dari controller
	 * 
	 * @param activity
	 *            context yang baru
	 */
	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	

//	/**
//	 * Memulai proses rekaman
//	 */
//	public void startRecord() {
//		actions = new ArrayList<Action>();
//		mode = RECORDING;
//		start = SystemClock.uptimeMillis();
//	}

//	/**
//	 * Menghentikan proses memainkan rekaman
//	 */
//	public void stopPlayingRecord() {
//		if (recordPlayer != null)
//			recordPlayer.stopPlayRecord();
//		recordPlayer = null;
//		System.gc();
//	}

//	/**
//	 * Menghentikan proses rekaman
//	 */
//	public void stopRecording() {
//		mode = STANDBY;
//		writeFile();
//	}

//	/**
//	 * Menuliskan rekaman yang sedang dilakukan ke dalam file
//	 */
//	private void writeFile() {
//		try {
//			String newName = getNewName(DEFAULT_RECORD_FOLDER_PATH,
//					recordingInstrument.getNickname(), 0);
//			FileWriter out = new FileWriter(new File(
//					DEFAULT_RECORD_FOLDER_PATH, newName));
//			out.write(recordingInstrument.getName() + "\n");
//			out.write((new Date()).toString() + "\n");
//			for (int i = 0; i < actions.size(); i++) {
//				out.write(actions.get(i).getTime() + " "
//						+ actions.get(i).getIndex() + "\n");
//			}
//			out.close();
//			lastSavedFile = newName;
//		} catch (IOException e) {
//			Log.e("Test", "What? " + e);
//		}
//	}
	
    
	private  MediaPlayer mPlayer;
	private MediaRecorder mRecorder;
	
    public void playRecord(String filename, final OnCompletionListener ocl) {
    	if(modePlay == STANDBY){
    		modePlay = PLAY_PLAYING;
	    	currentFile = filename;
	        mPlayer = new MediaPlayer();
	        mPlayer.setOnCompletionListener(new OnCompletionListener() {

				@Override
				public void onCompletion(MediaPlayer mp) {
					// TODO Auto-generated method stub
					if(ocl != null)
						ocl.onCompletion(mPlayer);
					if(mPlayer != null){
						mPlayer.stop();
						mPlayer.release();
					}
					modePlay = STANDBY;
				}
			});
	        mPlayer.setOnPreparedListener(new OnPreparedListener() {
				@Override
				public void onPrepared(MediaPlayer mp) {
					if (onRecordStartPlayingListener != null)
						onRecordStartPlayingListener.onStart();
					mPlayer.start();

				}
			});
	        try {
	            mPlayer.setDataSource(DEFAULT_RECORD_FOLDER_PATH+"/"+currentFile);
	            mPlayer.prepare();
	        } catch (IOException e) {
	            Log.e("Record Controller", "prepare() failed");
	        }
    	}
    }
    
    public MediaPlayer getRecord(String filename){
    	MediaPlayer temp = new MediaPlayer();
    	
    	try {
    		temp.setDataSource(DEFAULT_RECORD_FOLDER_PATH + "/"+filename);
    		temp.prepare();
    	}catch (IOException e) {
            Log.e("Record Controller", "prepare() failed");
        }
        return temp;
    }

    public void stopPlaying() {
    	modePlay = STANDBY;
    	if(mPlayer != null){
    		mPlayer.reset();
    		mPlayer.release();
        	mPlayer = null;
    	}
    }
    
    public void pausePlaying(){
    	modePlay = PLAY_PAUSING;
    	mPlayer.pause();
    }
    
    public void resumePlaying(){
    	modePlay = PLAY_PLAYING;
    	mPlayer.start();
    }

    public void startRecord() {
        lastSavedFile = getNewName(DEFAULT_RECORD_FOLDER_PATH, "temp", 0);
    	mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
        mRecorder.setOutputFile(DEFAULT_RECORD_FOLDER_PATH+"/"+lastSavedFile);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        //mRecorder.setAudioEncodingBitRate(2000); buat versi 2.2 keatas

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e("Media Recorder", "prepare() failed");
        }

        mRecorder.start();
        modeRecord = RECORD_RECORDING;
    }

    public void stopRecording() {
    	if(mRecorder != null){
	        mRecorder.stop();
	        mRecorder.release();
	        mRecorder = null;
    	}
        modeRecord = STANDBY;
    }
    
    public int getCurrent(){
    	if(mPlayer != null)
    		return mPlayer.getCurrentPosition();
    	return 0;
    }
    
    public int getDuration(){
    	return mPlayer.getDuration();
    }
    
    private OnRecordStartPlayingListener onRecordStartPlayingListener;
    public abstract class OnRecordStartPlayingListener {
		public abstract void onStart();
	}

	public OnRecordStartPlayingListener getOnRecordStartPlayingListener() {
		return onRecordStartPlayingListener;
	}

	public void setOnRecordStartPlayingListener(
			OnRecordStartPlayingListener onRecordStartPlayingListener) {
		this.onRecordStartPlayingListener = onRecordStartPlayingListener;
	}

}
