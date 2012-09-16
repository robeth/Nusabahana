package com.nusabahana.controller;

import android.app.Activity;
import android.content.res.Resources;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

import com.nusabahana.model.Instrument;
import com.nusabahana.model.InstrumentGroup;
import com.nusabahana.model.Note;

/**
 * Kelas yang merupakan controller dari semua aktivitas yang berhubungan dengan
 * instrument alat musik
 * 
 * @author PPL-B1
 * 
 */
public class InstrumentController {
	/** Context dari controller ini */
	private Activity activity;

	private InstrumentGroup[] instrumentGroups;

	/** Objek instrumen musik yang sedang disimulasikan/dimainkan */
	private Instrument currentInstrument;
	/** Objek yang memungkinkan musik dimainkan dengan cepat dan simultan */
	private SoundPool soundPool;
	/** ID dari musik yang dimainkan dalam soundPool */
	private int[] activeSoundId;

	/**
	 * Constructor default
	 * 
	 * @param c
	 *            context dari controller ini
	 */
	public InstrumentController(Activity c) {
		this.activity = c;
		initializeInstruments();
	}

	/**
	 * Mengganti instrumen yang sedang disimulasikan
	 * 
	 * @param instName
	 *            nama instrumen musik yang baru
	 */
	public void setCurrentInstrument(String instName) {
		this.currentInstrument = getInstrument(instName);
		prepareSoundPool(this.currentInstrument);
	}

	/**
	 * Mengambil objek instrument
	 * 
	 * @param instName
	 *            nama dari instrumen musik yang diinginkan
	 * @return objek instrument yang diinginkan
	 */
	public Instrument getInstrument(String instName) {
		for (InstrumentGroup ig : instrumentGroups) {
			Instrument[] is = ig.getInstrumentElements();
			for (Instrument ins : is) {
				if (ins.getName().equals(instName))
					return ins;
			}
		}
		return null;
	}

	/**
	 * Mengambil objek instrument yang sedang disimulasikan
	 * 
	 * @return objek instrument yang sedang disimulasikan
	 */
	public Instrument getCurrentInstrument() {
		return this.currentInstrument;
	}

	public InstrumentGroup[] getInstrumentGroups() {
		return instrumentGroups;
	}

	public void setInstrumentGroups(InstrumentGroup[] instrumentGroups) {
		this.instrumentGroups = instrumentGroups;
	}

	public void setCurrentInstrument(Instrument currentInstrument) {
		this.currentInstrument = currentInstrument;
	}
	public InstrumentGroup getGroup(Instrument instrument) {
		InstrumentGroup a = null ;
		for(int i = 0; i < instrumentGroups.length ; i++) {
			for(int j=0; j < instrumentGroups[i].getInstrumentElements().length ; j++){
				if(instrumentGroups[i].getInstrumentElements()[j].getName().equals(instrument.getName())) {
					return instrumentGroups[i];
				}
			} 
		}
		return a;
	}

	/**
	 * Memainkan note ke-'index' dari alat musik yang sedang disimulasikan
	 * 
	 * @param index
	 *            note yang ingin dimainkan
	 */
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

	/**
	 * Fungsi bantuan untuk mengekstrak nama file tanpa ekstensi
	 * 
	 * @param fileName
	 *            nama utuh file
	 * @return nama file tanpa ekstensi
	 */
	private String getFileName(String fileName) {
		return fileName.substring(0, fileName.lastIndexOf("."));
	}

	/**
	 * Menginisiasi semua objek instrumen
	 */
	private void initializeInstruments() {

		Resources res = activity.getResources();

		// Read all instrument group
		int groupsID = res.getIdentifier("instrument_group", "array",
				activity.getPackageName());
		String[] insGroups = res.getStringArray(groupsID);
		instrumentGroups = new InstrumentGroup[insGroups.length];

		// Untuk setiap instrument group
		for (int i = 0; i < instrumentGroups.length; i++) {

			// Load the group. Read all Instrument elements on that group and
			// load
			String[] temp = insGroups[i].split(",");

			int groupID = res.getIdentifier(temp[0], "array",
					activity.getPackageName());
			String[] insElements = res.getStringArray(groupID);
			Instrument[] instrumentElements = new Instrument[insElements.length];

			for (int j = 0; j < instrumentElements.length; j++) {
				Log.d("Load", insElements[j]);
				int insID = res.getIdentifier(insElements[j], "array",
						activity.getPackageName());
				String[] ins = res.getStringArray(insID);
				Note[] notes = new Note[ins.length - 6];

				for (int k = 0; k < notes.length; k++) {
					String[] temp2 = ins[k + 6].split(",");
					notes[k] = new Note(Integer.parseInt(temp2[0]), temp2[1],
							temp2[2], Integer.parseInt(temp2[3]));
				}

				instrumentElements[j] = new Instrument(ins[0], ins[1], ins[2],
						ins[3], ins[4], ins[5], notes);
			}

			instrumentGroups[i] = new InstrumentGroup(temp[1], temp[2],
					instrumentElements,temp[3]);
		}

	}

	/**
	 * Mempersiapkan objek soundPool agar siap memainkan suatu alat instrument
	 * 
	 * @param ins
	 *            alat instrument yang ingin disimulasikan
	 */
	private void prepareSoundPool(Instrument ins) {
		this.activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);

		Note[] notes = ins.getNotes();
		this.soundPool = new SoundPool(ins.getNotes().length,
				AudioManager.STREAM_MUSIC, 0);
		this.activeSoundId = new int[notes.length];

		for (int i = 0; i < notes.length; i++) {
			Log.d("Cari path", getFileName(notes[i].getPath()));
			activeSoundId[notes[i].getIndex()] = soundPool.load(
					activity,
					activity.getResources().getIdentifier(
							getFileName(notes[i].getPath()), "raw",
							activity.getPackageName()), 1);
		}

	}

	/**
	 * Mengambil context yang aktif dari controller ini
	 * 
	 * @return context dimana controller ini aktif
	 */
	public Activity getActivity() {
		return activity;
	}

	/**
	 * Mengganti context dari controller ini
	 * 
	 * @param activity
	 *            context baru
	 */
	public void setActivity(Activity activity) {
		this.activity = activity;
	}
}
