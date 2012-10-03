package com.nusabahana.view;

import java.util.Arrays;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nusabahana.controller.BackgroundMusicController;
import com.nusabahana.controller.BackgroundMusicController.OnBackgroundMusicStartListener;
import com.nusabahana.controller.RecordController;
import com.nusabahana.controller.RecordController.OnRecordStartPlayingListener;
import com.nusabahana.model.Instrument;
import com.nusabahana.partiture.Element;
import com.nusabahana.partiture.Partiture;
import com.nusabahana.partiture.PartiturePlayer;
import com.nusabahana.partiture.PartitureReader;
import com.nusabahana.partiture.PartitureTutorial;
import com.nusabahana.partiture.PartitureTutorial.OnPartitureNextSubElementListener;
import com.nusabahana.partiture.PartitureTutorial.OnPauseListener;
import com.nusabahana.partiture.PartitureTutorial.OnPlayListener;
import com.nusabahana.partiture.PartitureTutorial.OnResumeListener;
import com.nusabahana.partiture.PartitureTutorial.OnStopListener;
import com.nusabahana.partiture.Row;
import com.nusabahana.partiture.Segment;
import com.nusabahana.partiture.SubElement;

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
	private static final int CHOOSE_PARTITURE_DIALOG = 11212;

	// private Animation anim;
	private TextView[] tvParts;
	private DistributorView dv;

	// Bagian view yang akan diberi listener
	private Button bRecordStart, bBMStart, bBMStop, bTutorialStart,
			bTutorialStop, bTutorialChoose, instructionButton;
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
	private Partiture partiture;
	private PartitureTutorial tutorial;
	private TextView tutorialText, tutorialTextHeader;
	private String[] availablePartituresPath;
	private String groupPath;

	private LinearLayout tutorialLayout;

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
		// Log.e("Background",
		// getFileName(bgPath) + " -- " + a + " --- " + rl.getHeight());
		rl.setBackgroundResource(R.drawable.background);

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

		tutorialLayout = (LinearLayout) findViewById(R.id.partiture_player);
		// Mengambil semua reference view yang akan diberi listener
		bRecordStart = (Button) findViewById(R.id.button_start_record);
		bBMStart = (Button) findViewById(R.id.button_start_bm);
		bBMStop = (Button) findViewById(R.id.button_stop_bm);
		bBMBrowse = (ImageView) findViewById(R.id.button_browse_bm);
		bTutorialStart = (Button) findViewById(R.id.partiture_start_button);
		bTutorialStop = (Button) findViewById(R.id.partiture_stop_button);
		bTutorialChoose = (Button) findViewById(R.id.partiture_change);
		instructionButton = (Button) findViewById(R.id.instruction_button);
		timerText = (TextView) findViewById(R.id.text_record_timer);
		tutorialText = (TextView) findViewById(R.id.partiture_row);
		tutorialTextHeader = (TextView) findViewById(R.id.partiture_name);

		bRecordStart.setOnClickListener(recordStartListener);
		// bHome.setOnClickListener(homeListener);
		bBMStart.setOnClickListener(musicStartListener);
		bBMStop.setOnClickListener(musicStopListener);
		bBMBrowse.setOnClickListener(musicBrowseListener);
		instructionButton.setOnClickListener(instructionListener);
		timerText.setText("00:00");

		bBMStop.setEnabled(true);
		BM_CONTROLLER.setOnBackgroundMusicStartListener(bmStartListener);
		RECORD_CONTROLLER.setOnRecordStartPlayingListener(orStartListener);

		prepareTutorial();
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		Dialog dialog = null;
		switch (id) {
		case CHOOSE_PARTITURE_DIALOG:

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Choose a Partiture");
			builder.setItems(availablePartituresPath,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int item) {
							changeTutorialPartiture(item);
						}
					});
			dialog = builder.create();
			break;
		}
		return dialog;
	}

	private void changeTutorialPartiture(int partitureIndex) {
		partiture = PartitureReader.getPartiture(groupPath,
				availablePartituresPath[partitureIndex],
				simulatedInstrument.getNickname());
		tutorial.setPartiture(partiture);
		tutorialText.setText(tutorial.getRowSpannable(),
				TextView.BufferType.SPANNABLE);
		tutorialTextHeader.setText(availablePartituresPath[partitureIndex]
				+ " (" + simulatedInstrument.getName() + ")");
	}

	private void prepareTutorial() {
		PartitureReader.setContext(this);
		groupPath = getGroupPath(simulatedInstrument.getNickname());
		availablePartituresPath = PartitureReader
				.getAllPartituresFilename(groupPath);

		if (availablePartituresPath != null
				&& availablePartituresPath.length > 0) {
			partiture = PartitureReader.getPartiture(groupPath,
					availablePartituresPath[0],
					simulatedInstrument.getNickname());

			tutorial = new PartitureTutorial(partiture, 500);
			tutorial.setOnNextSubElementListener(subelementListener);
			tutorial.setOnPlayListener(onTutorialStartListener);
			tutorial.setOnPauseListener(onTutorialPauseListener);
			tutorial.setOnResumeListener(onTutorialResumeListener);
			tutorial.setOnStopListener(onTutorialStopListener);
			tutorialText.setText(tutorial.getRowSpannable(),
					TextView.BufferType.SPANNABLE);
			tutorialTextHeader.setText(getFileName(availablePartituresPath[0]));

			bTutorialStart.setOnClickListener(startTutorialListener);
			bTutorialStop.setOnClickListener(stopTutorialListener);
			bTutorialChoose.setOnClickListener(changeTutorialListener);
		} else {
			tutorialText.setText("Empty");
			tutorialTextHeader
					.setText("No Partiture for this Instrument Group");
			RelativeLayout partitureContent = (RelativeLayout) findViewById(R.id.partiture_player_content);
			LinearLayout partitureHeader = (LinearLayout) findViewById(R.id.partiture_player_header);

			partitureContent
					.setVisibility(android.widget.RelativeLayout.INVISIBLE);
			partitureHeader.setLayoutParams(new LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		}
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
		super.onWindowFocusChanged(hasFocus);

		if (hasFocus) {
			DisplayMetrics metrics = new DisplayMetrics();
			this.getWindowManager().getDefaultDisplay().getMetrics(metrics);
			RelativeLayout.LayoutParams suppliedParams = (RelativeLayout.LayoutParams) tutorialLayout
					.getLayoutParams();
			suppliedParams.width = (int) (0.65 * metrics.widthPixels);
			tutorialLayout.setLayoutParams(suppliedParams);
		}
	}

	private String getGroupPath(String nickname) {
		if (nickname.equals("arumba") || nickname.equals("angklung")) {
			return PartitureReader.ANGKLUNG;
		} else if (nickname.equals("bonang") || nickname.equals("saron")
				|| nickname.equals("kendang") || nickname.equals("gong")
				|| nickname.equals("kenong")) {
			return PartitureReader.GAMELAN_JAWA;
		} else {
			return PartitureReader.GAMELAN_BALI;
		}
	}

	public void activateInstrumentsBackgroundMode() {

		Partiture[] partiture = new Partiture[1];
		partiture[0] = getDummyPartiture();

		Instrument[] instruments = this.INSTRUMENT_CONTROLLER
				.getInstrumentGroups()[0].getInstrumentElements();
		PartiturePlayer pp = new PartiturePlayer(this, instruments, partiture,
				2000);
		pp.play();
	}

	private Partiture getDummyPartiture() {
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
				bRecordStart.setOnClickListener(recordStopListener);
				bRecordStart.setBackgroundResource(R.drawable.button_stop);
				progressHandler.post(pl2);
				timerText.setTextColor(0xFFFF0000);
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
				bRecordStart.setOnClickListener(recordStartListener);
				bRecordStart.setBackgroundResource(R.drawable.button_play);
				progressHandler.removeCallbacks(pl2);
				time = 0;
				timerText.setText("00:00");
				timerText.setTextColor(0xFFFFFFFF);
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
				// Log.e("ERRRR", "yapari. play:" + selectedFilename);

				if (!isRecord) {
					// Klo lagi standby
					if (BM_CONTROLLER.getMode() == BackgroundMusicController.MODE_STANDBY) {
						BM_CONTROLLER.play(selectedFilename, ocl);
						bBMStart.setBackgroundResource(R.drawable.pause);
					}
					// Klo lagi main
					else if (BM_CONTROLLER.getMode() == BackgroundMusicController.MODE_PLAYING) {
						BM_CONTROLLER.pause();
						bBMStart.setBackgroundResource(R.drawable.playnormal);
						progressHandler.removeCallbacks(pl);
					}

					// Klo lagi pause
					else if (BM_CONTROLLER.getMode() == BackgroundMusicController.MODE_PAUSING) {
						BM_CONTROLLER.resume();
						bBMStart.setBackgroundResource(R.drawable.pause);
						progressHandler.post(pl);
					}
				} else {
					if (RECORD_CONTROLLER.getPlayingState() == RECORD_CONTROLLER.STANDBY) {
						RECORD_CONTROLLER.playRecord(selectedFilename, ocl);
						bBMStart.setBackgroundResource(R.drawable.pause);
					}
					// Klo lagi main
					else if (RECORD_CONTROLLER.getPlayingState() == RECORD_CONTROLLER.PLAY_PLAYING) {
						RECORD_CONTROLLER.pausePlaying();
						bBMStart.setBackgroundResource(R.drawable.playnormal);
						progressHandler.removeCallbacks(pl3);
					}

					// Klo lagi pause
					else if (RECORD_CONTROLLER.getPlayingState() == RECORD_CONTROLLER.PLAY_PAUSING) {
						RECORD_CONTROLLER.resumePlaying();
						bBMStart.setBackgroundResource(R.drawable.pause);
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
			bBMStart.setBackgroundResource(R.drawable.playnormal);
			bBMStop.setEnabled(false);

			if (!isRecord)
				progressHandler.removeCallbacks(pl);
			else
				progressHandler.removeCallbacks(pl3);
			// progressbar.setProgress(0);
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
			// progressbar.setMax(BM_CONTROLLER.getDuration());
			progressHandler.post(pl);
			bBMStop.setEnabled(true);
		}

	};

	private OnRecordStartPlayingListener orStartListener = RECORD_CONTROLLER.new OnRecordStartPlayingListener() {

		@Override
		public void onStart() {
			// TODO Auto-generated method stub
			// progressbar.setMax(RECORD_CONTROLLER.getDuration());
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

		if (tutorial != null)
			tutorial.stop();
		super.onBackPressed();
	}

	public void shakePlay(int index) {
		INSTRUMENT_CONTROLLER.play(index);
		insParts[index].startAnimation(animationSet[index]);
	}

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
				// Log.e("Progress", "Max(Prog/Media):" + progressbar.getMax()
				// + "/" + BM_CONTROLLER.getDuration()
				// + "-- Current(Prog/Media):" + progressbar.getProgress()
				// + "/" + BM_CONTROLLER.getCurrent());
				if (BM_CONTROLLER.getDuration() - current > 1000) {
					// progressbar.setProgress(current);
					progressHandler.postDelayed(this, 1000);
				} else {
					// progressbar.setProgress(BM_CONTROLLER.getDuration());
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
				// Log.e("Progress", "Max(Prog/Media):" + progressbar.getMax()
				// + "/" + RECORD_CONTROLLER.getDuration()
				// + "-- Current(Prog/Media):" + progressbar.getProgress()
				// + "/" + RECORD_CONTROLLER.getCurrent());
				if (RECORD_CONTROLLER.getDuration() - current > 1000) {
					// progressbar.setProgress(current);
					progressHandler.postDelayed(this, 1000);
				} else {
					// progressbar.setProgress(RECORD_CONTROLLER.getDuration());
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
			// Log.e("Record Timer", "time:" + time + " -- or " + minute + ":"
			// + second);
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

	private OnPlayListener onTutorialStartListener = new OnPlayListener() {
		@Override
		public void onPlay() {
			bTutorialStart.setBackgroundResource(R.drawable.pause);
			bTutorialStart.setOnClickListener(pauseTutorialListener);
		}
	};

	private OnPauseListener onTutorialPauseListener = new OnPauseListener() {
		@Override
		public void onPause() {
			bTutorialStart.setBackgroundResource(R.drawable.playnormal);
			bTutorialStart.setOnClickListener(startTutorialListener);

		}
	};

	private OnStopListener onTutorialStopListener = new OnStopListener() {
		@Override
		public void onStop() {
			bTutorialStart.setBackgroundResource(R.drawable.playnormal);
			bTutorialStart.setOnClickListener(startTutorialListener);
		}
	};

	private OnResumeListener onTutorialResumeListener = new OnResumeListener() {
		@Override
		public void onResume() {
			bTutorialStart.setBackgroundResource(R.drawable.pause);
			bTutorialStart.setOnClickListener(pauseTutorialListener);
		}
	};

	private OnClickListener startTutorialListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (tutorial.getTutorialState() == PartitureTutorial.STANDBY) {
				tutorial.play();
			} else {
				tutorial.resume();
			}
		}

	};

	private OnClickListener pauseTutorialListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			tutorial.pause();
		}

	};

	private OnClickListener stopTutorialListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			tutorial.stop();
			tutorialText.setText(tutorial.getRowSpannable(),
					TextView.BufferType.SPANNABLE);
		}

	};

	private OnClickListener changeTutorialListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			tutorial.stop();
			showDialog(CHOOSE_PARTITURE_DIALOG);
		}

	};

	private void launchInstructionView() {
		// TODO Auto-generated method stub
		Intent newIntent = new Intent(this,Instruction.class);

		Bundle b = new Bundle();
		b.putString("instructionInstrument", "This is bonang");
		// Log.e("Chosen Instrument:", chosenInstrument);

		newIntent.putExtras(b);
		startActivity(newIntent);
	}

	private OnClickListener instructionListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			launchInstructionView();
		}

	};

	private OnPartitureNextSubElementListener subelementListener = new OnPartitureNextSubElementListener() {

		@Override
		public void onNextSubElement() {
			// TODO Auto-generated method stub
			int subelementValue = tutorial.getCurrentSubElementValue();
			// Log.d("Tutorial", "subvalue:"+subelementValue);
			int[] highlightedIndexes = (simulatedInstrument.getNickname()
					.equals("bonang")) ? getBonangHighlightedIndexes(subelementValue)
					: simulatedInstrument.getInstrumentIndexes(subelementValue);

			for (int i = 0; i < insParts.length; i++) {
				if (isIn(highlightedIndexes, i)) {
					insParts[i].setColorFilter(0x99ff0000);
					shakePlay(i);
				} else
					insParts[i].setColorFilter(Color.TRANSPARENT);
			}
			tutorialText.setText(tutorial.getRowSpannable(),
					TextView.BufferType.SPANNABLE);
		}

		private boolean isIn(int[] arrayInt, int intValue) {
			for (int i = 0; i < arrayInt.length; i++)
				if (arrayInt[i] == intValue)
					return true;

			return false;
		}

		private int[] getBonangHighlightedIndexes(int key) {
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
	};

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
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

		if (tutorial != null)
			tutorial.stop();
		super.onPause();
	}

}
