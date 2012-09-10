package com.nusabahana.view;

import java.security.MessageDigest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nusabahana.controller.BackgroundMusicController;
import com.nusabahana.controller.InstrumentController;
import com.nusabahana.controller.RecordController;
import com.nusabahana.controller.BackgroundMusicController.OnBackgroundMusicStartListener;
import com.nusabahana.controller.RecordController.OnRecordStartPlayingListener;
import com.nusabahana.instrument.Instrument;
import com.nusabahana.partiture.Element;
import com.nusabahana.partiture.Partiture;
import com.nusabahana.partiture.PartiturePlayer;
import com.nusabahana.partiture.PartitureTutorial;
import com.nusabahana.partiture.Row;
import com.nusabahana.partiture.Segment;
import com.nusabahana.partiture.SubElement;
import com.nusabahana.partiture.PartitureTutorial.OnPartitureHasNextAction;
import com.nusabahana.view.R;

/**
 * Kelas yang merupakan view dari fitur simulasi alat musik. Implementasi fitur
 * merekam permainan dan memainkan musik latar juga terdapat pada view ini.
 * 
 * @author PPL-B1
 * 
 */
public class InstrumentSimulationMenu extends NusabahanaView {
	/** Konstanta yang menunjukan request result terhadap background musik */
	private static final int BM_INTENT = 101;

	// private Animation anim;
	private TextView[] tvParts;
	private DistributorView dv;

	// Bagian view yang akan diberi listener
	private ImageButton bRecordStart, bRecordStop, bBMStart, bBMStop;
	private ImageView bBMBrowse;
	private TextView timerText;
	private NoteImage[] insParts;
	private int time;

	// Progress bar beserta handler yang mengupdatenya
	private Handler progressHandler = new Handler();
	private ProgressLooper pl = new ProgressLooper();
	private ProgressLooper2 pl2 = new ProgressLooper2();
	private ProgressLooper3 pl3 = new ProgressLooper3();

	// Kontantata durasi maksimal rekaman dibuat
	private final int FOUR_MINUTES = 4 * 60 * 1000;

	// Nama file musik latar yang digunakan
	private String selectedFilename;
	private boolean isRecord;

	// Instrumen yang disimulasikan
	private Instrument simulatedInstrument;
	private Animation[] animationSet;

	/**
	 * Inisialisasi activity
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Menentukan mode permainan dan alat musik simulasi
		Bundle tikiJNE = getIntent().getExtras();
		String chosenInstrumentName = tikiJNE.getString("chosenInstrument");

		simulatedInstrument = INSTRUMENT_CONTROLLER
				.getInstrument(chosenInstrumentName);
		INSTRUMENT_CONTROLLER.setCurrentInstrument(chosenInstrumentName);
		int instrumentXMLID = getResources().getIdentifier(
				"instrument_" + simulatedInstrument.getNickname(), "layout",
				getPackageName());

		// Inisialisasi bagian instrument simulasi: gambar, suara dan listener
		setContentView(R.layout.instrument_simulation_record);
		FrameLayout lv = (FrameLayout) findViewById(R.id.instrument_frame);
		dv = (DistributorView) findViewById(R.id.distributor);
		LayoutInflater layoutInflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		lv.addView(layoutInflater.inflate(instrumentXMLID, lv, false), 0);
		lv.bringChildToFront(dv);
		

		String bgPath = INSTRUMENT_CONTROLLER.getGroup(simulatedInstrument)
				.getBgPath();
		RelativeLayout rl = (RelativeLayout) findViewById(R.id.simulation);

		int a = getResources().getIdentifier(getFileName(bgPath), "drawable",
				getPackageName());
		Log.e("Background",
				getFileName(bgPath) + " -- " + a + " --- " + rl.getHeight());
		rl.setBackgroundResource(a);

		// anim =

		insParts = new NoteImage[simulatedInstrument.getNotes().length];
		tvParts = new TextView[insParts.length];
		animationSet = new Animation[insParts.length];
		for (int i = 0; i < insParts.length; i++) {

			insParts[i] = (NoteImage) findViewById(getResources()
					.getIdentifier(simulatedInstrument.getNickname() + i, "id",
							getPackageName()));
			tvParts[i] = (TextView) findViewById(getResources().getIdentifier(
					simulatedInstrument.getNickname() + "_text" + i, "id",
					getPackageName()));
			insParts[i].setIndex(i);
			insParts[i].setActivity(this);
			tvParts[i].setText(simulatedInstrument.getNoteLabel(i));
			animationSet[i] = AnimationUtils.loadAnimation(this, R.anim.shake);

			dv.registerView(insParts[i]);
		}

		// Mengambil semua reference view yang akan diberi listener
		bRecordStart = (ImageButton) findViewById(R.id.button_start_record);
		bRecordStop = (ImageButton) findViewById(R.id.button_stop_record);
		// bHome = (ImageButton) findViewById(R.id.button_home);
		bBMStart = (ImageButton) findViewById(R.id.button_start_bm);
		bBMStop = (ImageButton) findViewById(R.id.button_stop_bm);
		bBMBrowse = (ImageView) findViewById(R.id.button_browse_bm);
		timerText = (TextView) findViewById(R.id.text_record_timer);

		bRecordStart.setOnClickListener(recordStartListener);
		bRecordStop.setOnClickListener(recordStopListener);
		// bHome.setOnClickListener(homeListener);
		bBMStart.setOnClickListener(musicStartListener);
		bBMStop.setOnClickListener(musicStopListener);
		bBMBrowse.setOnClickListener(musicBrowseListener);
		timerText.setText("00:00");

		bBMStop.setEnabled(true);
		bRecordStop.setEnabled(true);
		BM_CONTROLLER.setOnBackgroundMusicStartListener(bmStartListener);
		RECORD_CONTROLLER.setOnRecordStartPlayingListener(orStartListener);
		
		
		Partiture partiture = getDummyPartiture();
		PartitureTutorial tutorial = new PartitureTutorial(partiture,1000);
		
	}
	
	public void activateInstrumentsBackgroundMode(){
		
		Partiture[] partiture = new Partiture[1];
		partiture[0] = getDummyPartiture();
		
		Instrument[] instruments = this.INSTRUMENT_CONTROLLER.getInstrumentGroups()[0].getInstrumentElements();
		PartiturePlayer pp = new PartiturePlayer(this, instruments, partiture, 2000);
		pp.play();
	}
	
	private Partiture getDummyPartiture(){
		SubElement[] ss = new SubElement[4];
		ss[0] = new SubElement(-1);
		ss[1] = new SubElement(3);
		ss[2] = new SubElement(-1);
		ss[3] = new SubElement(5);
		
		SubElement[] ss2 = new SubElement[4];
		ss2[0] = new SubElement(-1);
		ss2[1] = new SubElement(6);
		ss2[2] = new SubElement(-1);
		ss2[3] = new SubElement(5);
		
		SubElement[] ss3 = new SubElement[4];
		ss3[0] = new SubElement(-1);
		ss3[1] = new SubElement(4);
		ss3[2] = new SubElement(-1);
		ss3[3] = new SubElement(2);
		
		SubElement[] ss4 = new SubElement[4];
		ss4[0] = new SubElement(-1);
		ss4[1] = new SubElement(1);
		ss4[2] = new SubElement(-1);
		ss4[3] = new SubElement(6);
		
		Element[] elements1 = new Element[4];
		
		elements1[0] = new Element(ss);
		elements1[1] = new Element(ss2);
		elements1[2] = new Element(ss3);
		elements1[3] = new Element(ss4);
		
		Segment[] segments = new Segment[1];
		segments[0] = new Segment(elements1);
		
		Row[] row = new Row[1];
		row[0] = new Row(segments);
		
		Partiture partiture = new Partiture(row, null);
		return partiture;
	}
	
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if(hasFocus)
			dv.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
	}
	// Menerima hasil dari activity lain
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Jika hasil berasal dari DetailFileMenu
		if (requestCode == DetailFileMenu.FROM_SIMULATION) {
			if (resultCode == Activity.RESULT_OK) {
				String s = data.getExtras().getString("savedFileName");
				Toast.makeText(InstrumentSimulationMenu.this,
						"File Saved as : " + s, Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(InstrumentSimulationMenu.this, "File not saved",
						Toast.LENGTH_SHORT).show();
				RECORD_CONTROLLER.deleteFile(RECORD_CONTROLLER
						.getLastSavedFile());
			}
		}
		// Jika hasil berasal dari BackgroundMusicGalleryMenu
		else if (requestCode == BM_INTENT) {
			if (resultCode == Activity.RESULT_OK) {
				Bundle b = data.getExtras();
				selectedFilename = b.getString("filename");
				isRecord = b.getBoolean("isRecord");
				bBMStart.setEnabled(true);
			}
		}
	}

	/** Listener untuk gambar rekam */
	private OnClickListener recordStartListener = new OnClickListener() {
		public void onClick(View v) {
			// Kalo lagi standby
			if (RECORD_CONTROLLER.getRecordingState() == RecordController.STANDBY) {
				time = 0;
				RECORD_CONTROLLER.startRecord();
				bRecordStart.setEnabled(false);
				bRecordStop.setEnabled(true);
				progressHandler.post(pl2);
			}
		}
	};

	/**
	 * Listener untuk gambar stop pada mode rekaman
	 */
	private OnClickListener recordStopListener = new OnClickListener() {
		public void onClick(View v) {
			if (RECORD_CONTROLLER.getRecordingState() == RECORD_CONTROLLER.RECORD_RECORDING) {
				if (BM_CONTROLLER.getMode() == BM_CONTROLLER.MODE_PLAYING) {
					musicStopListener.onClick(null);
				}
				if (RECORD_CONTROLLER.getPlayingState() == RECORD_CONTROLLER.PLAY_PLAYING) {
					RECORD_CONTROLLER.stopPlaying();
				}

				RECORD_CONTROLLER.stopRecording();
				bRecordStart.setEnabled(true);
				bRecordStop.setEnabled(false);
				progressHandler.removeCallbacks(pl2);
				time = 0;
				timerText.setText("00:00");

				Intent i = new Intent(InstrumentSimulationMenu.this,
						DetailFileMenu.class);
				Bundle b = new Bundle();
				b.putString("fileName", RECORD_CONTROLLER.getLastSavedFile());
				b.putInt("origin", DetailFileMenu.FROM_SIMULATION);
				i.putExtras(b);
				InstrumentSimulationMenu.this.startActivityForResult(i,
						DetailFileMenu.FROM_SIMULATION);
			}
		}

	};


	/**
	 * Listener untuk gambar start pada mode musik latar
	 */
	private OnClickListener musicStartListener = new OnClickListener() {
		public void onClick(View v) {
			if (selectedFilename != null) {
				Log.e("ERRRR", "yapari. play:" + selectedFilename);

				if (!isRecord) {
					// Klo lagi standby
					if (BM_CONTROLLER.getMode() == BackgroundMusicController.MODE_STANDBY) {
						BM_CONTROLLER.play(selectedFilename, ocl);
						bBMStart.setImageResource(R.drawable.pause);
					}
					// Klo lagi main
					else if (BM_CONTROLLER.getMode() == BackgroundMusicController.MODE_PLAYING) {
						BM_CONTROLLER.pause();
						bBMStart.setImageResource(R.drawable.playblack);
						progressHandler.removeCallbacks(pl);
					}

					// Klo lagi pause
					else if (BM_CONTROLLER.getMode() == BackgroundMusicController.MODE_PAUSING) {
						BM_CONTROLLER.resume();
						bBMStart.setImageResource(R.drawable.pause);
						progressHandler.post(pl);
					}
				} else {
					if (RECORD_CONTROLLER.getPlayingState() == RECORD_CONTROLLER.STANDBY) {
						RECORD_CONTROLLER.playRecord(selectedFilename, ocl);
						bBMStart.setImageResource(R.drawable.pause);
					}
					// Klo lagi main
					else if (RECORD_CONTROLLER.getPlayingState() == RECORD_CONTROLLER.PLAY_PLAYING) {
						RECORD_CONTROLLER.pausePlaying();
						bBMStart.setImageResource(R.drawable.playblack);
						progressHandler.removeCallbacks(pl3);
					}

					// Klo lagi pause
					else if (RECORD_CONTROLLER.getPlayingState() == RECORD_CONTROLLER.PLAY_PAUSING) {
						RECORD_CONTROLLER.resumePlaying();
						bBMStart.setImageResource(R.drawable.pause);
						progressHandler.post(pl3);
					}
				}
			}
		}
	};

	/**
	 * Listener untuk tombol stop pada mode musik latar
	 */
	private OnClickListener musicStopListener = new OnClickListener() {
		public void onClick(View v) {
			if (!isRecord
					&& BM_CONTROLLER.getMode() != BackgroundMusicController.MODE_STANDBY)
				BM_CONTROLLER.stop();
			if (isRecord
					&& RECORD_CONTROLLER.getPlayingState() != RecordController.STANDBY) {
				RECORD_CONTROLLER.stopPlaying();
			}
			bBMStart.setImageResource(R.drawable.playblack);
			bBMStop.setEnabled(false);

			if (!isRecord)
				progressHandler.removeCallbacks(pl);
			else
				progressHandler.removeCallbacks(pl3);
			//progressbar.setProgress(0);
		}
	};

	/**
	 * Listener untuk gambar folder pada mode musik latar
	 */
	private OnClickListener musicBrowseListener = new OnClickListener() {
		public void onClick(View v) {

			if (BM_CONTROLLER.getMode() == BackgroundMusicController.MODE_STANDBY
					&& RECORD_CONTROLLER.getPlayingState() == RECORD_CONTROLLER.STANDBY) {
				Intent nIntent = new Intent(InstrumentSimulationMenu.this,
						BackgroundMusicGalleryMenu.class);
				startActivityForResult(nIntent, BM_INTENT);
			} else {
				musicStopListener.onClick(null);
				this.onClick(v);
			}
		}
	};

	/**
	 * Listener yang di invoke ketika musik latar siap dimainkan oleh controller
	 */
	private OnBackgroundMusicStartListener bmStartListener = BM_CONTROLLER.new OnBackgroundMusicStartListener() {

		@Override
		public void onStart() {
			// TODO Auto-generated method stub
			//progressbar.setMax(BM_CONTROLLER.getDuration());
			progressHandler.post(pl);
			bBMStop.setEnabled(true);
		}

	};

	private OnRecordStartPlayingListener orStartListener = RECORD_CONTROLLER.new OnRecordStartPlayingListener() {

		@Override
		public void onStart() {
			// TODO Auto-generated method stub
			//progressbar.setMax(RECORD_CONTROLLER.getDuration());
			progressHandler.post(pl3);
			bBMStop.setEnabled(true);
		}

	};

	/**
	 * Dipanggil ketika tombol back ditekan.
	 */
	public void onBackPressed() {
		if (!isRecord && BM_CONTROLLER.getMode() != BM_CONTROLLER.MODE_STANDBY) {
			musicStopListener.onClick(null);
		}

		if (isRecord
				&& RECORD_CONTROLLER.getPlayingState() != RecordController.STANDBY) {
			musicStopListener.onClick(null);
		}

		if (RECORD_CONTROLLER.getRecordingState() != RecordController.STANDBY) {
			recordStopListener.onClick(null);
		}
		super.onBackPressed();
	}

	public void shakePlay(int index) {
		INSTRUMENT_CONTROLLER.play(index);
		insParts[index].startAnimation(animationSet[index]);
	}

	/**
	 * Listener yang digunakan untuk invoke suara alat musik dan pencatatan aksi
	 * rekaman
	 * 
	 * @author PPL-B1
	 * 
	 */
	// private class NoteTouchListener implements OnTouchListener {
	// private int note;
	//
	// public NoteTouchListener(int note) {
	// this.note = note;
	// }
	//
	// @Override
	// public boolean onTouch(View view, MotionEvent event) {
	// Log.v("Touch Note Listener","Play -- "+note);
	// // TODO Auto-generated method stub
	// if (event.getAction() == MotionEvent.ACTION_DOWN) {
	// INSTRUMENT_CONTROLLER.play(note);
	// shake(note);
	// final int viewLocation[] = { 0, 0 };
	// dv.getLocationOnScreen(viewLocation);
	//
	// Log.v("View Location", "[x,y] = [" + viewLocation[0] + " ,"
	// + viewLocation[1] + "] -- Width,Heigt =(" + dv.getWidth()
	// + " ," + dv.getHeight() + ")");;
	// }
	// return true;
	// }
	//
	// }

	/**
	 * Runnable yang digunakan untuk mengupdate state dari ProgressBar
	 * 
	 * @author PPL-B1
	 * 
	 */
	private class ProgressLooper implements Runnable {
		@Override
		public void run() {
			if (BM_CONTROLLER.getMode() == BM_CONTROLLER.MODE_PLAYING) {
				int current = BM_CONTROLLER.getCurrent();
				//Log.e("Progress", "Max(Prog/Media):" + progressbar.getMax()
				//		+ "/" + BM_CONTROLLER.getDuration()
//						+ "-- Current(Prog/Media):" + progressbar.getProgress()
//						+ "/" + BM_CONTROLLER.getCurrent());
				if (BM_CONTROLLER.getDuration() - current > 1000) {
//					progressbar.setProgress(current);
					progressHandler.postDelayed(this, 1000);
				} else {
//					progressbar.setProgress(BM_CONTROLLER.getDuration());
				}
			} else {
				musicStopListener.onClick(null);
			}
		}
	}

	private class ProgressLooper3 implements Runnable {
		@Override
		public void run() {
			if (RECORD_CONTROLLER.getPlayingState() == RecordController.PLAY_PLAYING) {

				int current = RECORD_CONTROLLER.getCurrent();
//				Log.e("Progress", "Max(Prog/Media):" + progressbar.getMax()
//						+ "/" + RECORD_CONTROLLER.getDuration()
//						+ "-- Current(Prog/Media):" + progressbar.getProgress()
//						+ "/" + RECORD_CONTROLLER.getCurrent());
				if (RECORD_CONTROLLER.getDuration() - current > 1000) {
//					progressbar.setProgress(current);
					progressHandler.postDelayed(this, 1000);
				} else {
//					progressbar.setProgress(RECORD_CONTROLLER.getDuration());
				}
			} else {
				musicStopListener.onClick(null);
			}
		}
	}

	private class ProgressLooper2 implements Runnable {
		@Override
		public void run() {
			progressHandler.postDelayed(this, 1000);
			int minute = time / 60;
			int second = time % 60;
			timerText.setText(((minute > 9) ? minute : "0" + minute) + ":"
					+ ((second > 9) ? second : "0" + second));
			time++;
			Log.e("Record Timer", "time:" + time + " -- or " + minute + ":"
					+ second);
		}
	}

	private String getFileName(String fileName) {
		return fileName.substring(0, fileName.lastIndexOf("."));
	}

	private OnCompletionListener ocl = new OnCompletionListener() {

		@Override
		public void onCompletion(MediaPlayer mp) {
			// TODO Auto-generated method stub
			musicStopListener.onClick(null);
		}
	};

}