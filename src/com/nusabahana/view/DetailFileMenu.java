package com.nusabahana.view;

import java.io.File;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nusabahana.controller.RecordController;

/**
 * Kelas yang merupakan view untuk menampilkan detil dari suatu file rekaman
 * @author PPL-B1
 *
 */
public class DetailFileMenu extends NusabahanaView {
	/** Menunjukkan bahwa view ini diakses dari activity InstrumentSimulationMenu*/
	public static final int FROM_SIMULATION = 0;
	/** Menunjukkan bahwa view ini diakses dari activity RecordGalleryMenu*/
	public static final int FROM_GALLERY = 1;
	/** Nama dari file sebelum dilakukan modifikasi*/
	private String oldName = null;
	/** Objek edit text yang menampung nama file yang akan/sedang/telah dimodifikas*/
	private EditText et = null;
	/** status dari mana view ini diakses*/
	private int mode;
	
	private ImageView twitterButton;
	private ImageView facebookButton;
	private ImageView soundcloudButton;

	/**
	 * Inisialisasi activity
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_file);

		// Ambil pesan dari activity sebelumnya: Nama file rekaman dan mode
		Bundle b = getIntent().getExtras();
		String fileName = b.getString("fileName");
		mode = b.getInt("origin");
		MediaPlayer mp = RECORD_CONTROLLER.getRecord(fileName);

		// Siapkan komponen view yang akan diisi beserta listenernya
		et = (EditText) findViewById(R.id.filename);
		TextView duration = (TextView) findViewById(R.id.duration);
		ImageView trashImage = (ImageView) findViewById(R.id.trash);
		ImageView instrumentLogo = (ImageView) findViewById(R.id.instrument_logo_info);
		Button buttonOk = (Button) findViewById(R.id.button_ok);
		Button buttonCancel = (Button) findViewById(R.id.button_cancel);
		twitterButton = (ImageView) findViewById(R.id.twitter_button);
		facebookButton = (ImageView) findViewById(R.id.facebook_button);
		soundcloudButton = (ImageView) findViewById(R.id.soundcloud_button);
		
		twitterButton.setOnClickListener(twitterListener);
		facebookButton.setOnClickListener(facebookListener);
		soundcloudButton.setOnClickListener(soundcloudListener);
		
		buttonOk.setOnClickListener(okListener);
		buttonCancel.setOnClickListener(cancelListener);
		
		if(mode == FROM_GALLERY)
			trashImage.setOnClickListener(trashListener);
		else
			trashImage.setVisibility(View.INVISIBLE);
		

		// Isikan value ke komponen record terkait
		oldName = fileName;
		et.setText(fileName);
		duration.setText(formatTime(mp.getDuration()));
	}

	/**
	 * Mendapatkan format "mm:dd" dari suatu durasi dalam detik
	 * @param t durasi dalam detik
	 * @return format waktu "mm:dd"
	 */
	public String formatTime(long t) {
		t = t / 1000;
		int minute = (int) t / 60;
		int second = (int) t % 60;
		return ((minute > 9) ? "" : "0") + minute + ":"
				+ ((second > 9) ? "" : "0") + second;
	}

	/**
	 * Listener yang dipanggil ketika tombol OK ditekan
	 */
	private OnClickListener okListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// Ambil nama baru
			char[] newName = new char[et.getText().length()];
			et.getText().getChars(0, newName.length, newName, 0);
			String nName = new String(newName);

			// no change, no problem
			if (oldName.equals(nName)) {
				// Dari SIMULATION --> kembalikan result berhasil simpan file
				// baru
				if (mode == FROM_SIMULATION) {
					Intent i = new Intent();
					Bundle b = new Bundle();
					b.putString("savedFileName", nName);
					i.putExtras(b);
					DetailFileMenu.this.setResult(RESULT_OK, i);
					DetailFileMenu.this.finish();
					return;
				}
				// Dari GALLERY --> Cukup finish saja
				DetailFileMenu.this.finish();
				return;
			}

			String exc = "";
			if ((exc = RECORD_CONTROLLER.isValidNewName(nName)) == null) {
				// Nama baru valid --> rename
				RECORD_CONTROLLER.renameFile(oldName, nName);
				// Dari SIMULATION --> kembalikan result berhasil simpan file
				// baru
				if (mode == FROM_SIMULATION) {
					Intent i = new Intent();
					Bundle b = new Bundle();
					b.putString("savedFileName", nName);
					i.putExtras(b);
					DetailFileMenu.this.setResult(RESULT_OK, i);
				}
				// Dari GALLERY --> Cukup finish saja
				DetailFileMenu.this.finish();
			} else {
				Toast.makeText(DetailFileMenu.this, exc, Toast.LENGTH_SHORT)
						.show();
			}

		}
	};

	/**
	 * Listener yang dipanggil ketika tombol cancel ditekan
	 */
	private OnClickListener cancelListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// Jika dari SIMULATION, kembalikan result batal, baru finish
			if (mode == FROM_SIMULATION) {
				DetailFileMenu.this.setResult(RESULT_CANCELED);
			}
			DetailFileMenu.this.finish();
		}
	};

	/**
	 * Tombol yang dipanggil ketika tombol trash ditekan
	 */
	private OnClickListener trashListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// Munculkan dialog warning kepada pengguna
			AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(
					DetailFileMenu.this);
			myAlertDialog.setTitle("Delete " + oldName);
			myAlertDialog.setMessage("Are you sure?");
			myAlertDialog.setPositiveButton("Yes",
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface arg0, int arg1) {
							// do something when the OK button is clicked
							RECORD_CONTROLLER.deleteFile(oldName);
							DetailFileMenu.this.finish();
						}
					});
			myAlertDialog.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface arg0, int arg1) {
							// do something when the Cancel button is clicked
						}
					});
			myAlertDialog.show();
		}
	};
	
	private OnClickListener twitterListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			startActivity(new Intent(DetailFileMenu.this, TweetToTwitterActivity.class));
		}
	};
	
	private OnClickListener facebookListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			startActivity(new Intent(DetailFileMenu.this, com.nusabahana.facebook.AndroidFacebookSample.class));
		}
	};
	
	private OnClickListener soundcloudListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			File myAudiofile = new File(RecordController.DEFAULT_RECORD_FOLDER_PATH+"/"+oldName);
			Intent intent = new Intent("com.soundcloud.android.SHARE")
			  .putExtra(Intent.EXTRA_STREAM, Uri.fromFile(myAudiofile))
			  .putExtra("com.soundcloud.android.extra.title", "Demo");
			  // more metadata can be set, see below

			try {
			    // takes the user to the SoundCloud sharing screen
			    startActivityForResult(intent, 1000);
			} catch (ActivityNotFoundException e) {
//				Log.e("SOUNDCLOUD", e.getMessage());
				Toast.makeText(DetailFileMenu.this, "Please install SoundCloud apps", Toast.LENGTH_SHORT)
				.show();
			}
		}
	};
	
	/**
	 * Fungsi bantuan untuk mengekstrak nama file tanpa ekstensi
	 * @param fileName nama utuh file
	 * @return nama file tanpa ekstensi
	 */
	private String getFileName(String fileName){
		return fileName.substring(0,fileName.lastIndexOf("."));
	}
}