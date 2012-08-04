package com.nusabahana.view;

import com.nusabahana.model.OnRecordEndListener;
import com.nusabahana.view.R;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/**
 * Kelas yang merupakan view dari galeri file rekaman
 * 
 * @author PPL-B1
 * 
 */
public class RecordGalleryMenu extends NusabahanaView {
	/** Nama file rekaman yang sedang dipilih */
	private String currentFileName = "";

	// View yang akan diberi listener
	private TextView recordName;
	private ImageView playImage, settingImage;

	/** Status apakah pengguna telah memilih suatu file rekaman atau belum */
	private boolean hasChose = false;
	private boolean isRecordFile = false;
	private boolean isFirst = true;
	/** Nama dari semua file rekaman yang ada folder rekaman */
	private String[] recordsName;

	/** Status terkini dari view */
	private int mode = STANDBY;
	/** Konstanta yang menunjukkan view sedang standby */
	private static int STANDBY = 0;
	/** Konstanta yang menunjukkan view sedang memainkan file rekaman */
	private static int PLAYING = 1;

	private boolean isRecord = true;
	/**
	 * Inisialisasi activity
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.record_gallery);

		TabHost mTabHost = (TabHost) findViewById(R.id.tabhost);
		mTabHost.setup();
		mTabHost.addTab(mTabHost.newTabSpec("tab_test1").setIndicator("Record")
				.setContent(R.id.record_list_view));
		mTabHost.addTab(mTabHost.newTabSpec("tab_test2")
				.setIndicator("Background").setContent(R.id.bm_list_view));
		final TabHost th = mTabHost;
		

		ListView lv = (ListView) findViewById(R.id.record_list_view);
		ListView lv2 = (ListView) findViewById(R.id.bm_list_view);
		recordsName = RECORD_CONTROLLER.getAllRecordsName();

		recordName = (TextView) findViewById(R.id.current_record_name);
		playImage = (ImageView) findViewById(R.id.current_record_play);
		settingImage = (ImageView) findViewById(R.id.current_record_setting);
		

		playImage.setOnClickListener(playListener);
		settingImage.setOnClickListener(settingListener);
		

		final String[] recordsName2 = BM_CONTROLLER.getAllMusicsName();
		if (recordsName2 != null) {
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
					R.layout.recordrow, recordsName2);
			lv2.setAdapter(adapter);

			lv2.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					if(mode != STANDBY){
						toStopState();
					}
						
					recordName.setText(shorten(recordsName2[position]));
					currentFileName = recordsName2[position];
					hasChose = true;
					isRecordFile = false;
				}
			});
		}

		if (recordsName != null) {
			for (int i = 0; i < recordsName.length; i++)
				Log.e("Checking filtered", recordsName[i]);
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
					R.layout.recordrow, recordsName);
			lv.setAdapter(adapter);

			lv.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					
					if(mode != STANDBY){
						toStopState();
					}
					
					recordName.setText(shorten(recordsName[position]));
					currentFileName = recordsName[position];
					hasChose = true;
					isRecordFile = true;
					Log.e("isRecord", recordsName[position]);
				}
			});
		}
		
		
		mTabHost.setOnTabChangedListener(new OnTabChangeListener() {

			@Override
			public void onTabChanged(String tabId) {
				// TODO Auto-generated method stub
				if (th.getCurrentTab() == 0) {
					settingImage.setVisibility(View.VISIBLE);
				} else {
					settingImage.setVisibility(View.INVISIBLE);

				}
			}
		});
		
		if(isFirst){
			Toast.makeText(RecordGalleryMenu.this,
					"Record Directory:"+RECORD_CONTROLLER.DEFAULT_RECORD_FOLDER_PATH, Toast.LENGTH_SHORT).show();
			Toast.makeText(RecordGalleryMenu.this,
					"Background Music Directory:"+BM_CONTROLLER.DEFAULT_MUSICS_FOLDER_PATH, Toast.LENGTH_SHORT).show();
			isFirst = false;
		}
		
	}

	/**
	 * Dipanggil ketika activity yang dipanggil sebelumnya selesai. Memastikan
	 * nama semua file rekaman update
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		onCreate(null);
		hasChose = false;
	}

	/**
	 * Dipanggil ketika activity ini aktif kembali. Memastikan nama semua file
	 * rekaman update
	 */
	@Override
	public void onResume() {
		super.onResume();
		onCreate(null);
	}
	
	private String shorten(String filename){
		if(filename != null && filename.length() > 30 ){
			return "..."+filename.substring(filename.length()-27, filename.length());
		}
		return filename;
	}

	/**
	 * Listener yang dipanggil ketika tombol play ditekan
	 */
	private OnClickListener playListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (hasChose) {
				if (isRecordFile) {
					Log.e("Record Gallery", "let's play record file");
					if (mode == STANDBY) {
						RECORD_CONTROLLER.playRecord(currentFileName, ocl);
						mode = PLAYING;
						playImage.setImageResource(R.drawable.stopblack);

					} else {
						toStopState();
					}
				} else {
					Log.e("BM Gallery", "let's play BM file");
					if (mode == STANDBY) {
						BM_CONTROLLER.play(currentFileName, ocl);
						mode = PLAYING;
						playImage.setImageResource(R.drawable.stopblack);
					} else {
						toStopState();
					}

				}
			} else {
				Toast.makeText(RecordGalleryMenu.this,
						"Please choose a record", Toast.LENGTH_SHORT).show();
			}
		}
	};

	private OnCompletionListener ocl = new OnCompletionListener() {

		@Override
		public void onCompletion(MediaPlayer mp) {
			// TODO Auto-generated method stub
			toStopState();
		}
	};

	/**
	 * Listener yang digunakan ketika gambar setting ditekan
	 */
	private OnClickListener settingListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (hasChose) {
				if (isRecordFile) {
					if (mode == PLAYING) {
						toStopState();
					}
					Intent i = new Intent(RecordGalleryMenu.this,
							DetailFileMenu.class);
					Bundle b = new Bundle();
					b.putString("fileName", currentFileName);
					b.putInt("origin", DetailFileMenu.FROM_GALLERY);

					i.putExtras(b);
					RecordGalleryMenu.this.startActivityForResult(i,
							DetailFileMenu.FROM_GALLERY);
				}
			} else {
				Toast.makeText(RecordGalleryMenu.this,
						"Please choose a record", Toast.LENGTH_SHORT).show();
			}
		}
	};


	/**
	 * Dipanggil ketika tombol back ditekan
	 */
	public void onBackPressed() {
		if (mode == PLAYING) {
			toStopState();
		}
		super.onBackPressed();
	}
	
	public void toStopState(){
		RECORD_CONTROLLER.stopPlaying();
		BM_CONTROLLER.stop();
		mode = STANDBY;
		playImage.setImageResource(R.drawable.play);
	}
}