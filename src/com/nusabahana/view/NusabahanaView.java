package com.nusabahana.view;

import com.nusabahana.controller.BackgroundMusicController;
import com.nusabahana.controller.InstrumentController;
import com.nusabahana.controller.RecordController;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Kelas yang merupakan kerangka utama semua activity pada aplikasi Nusabahana.
 * Kelas ini memastikan semau Controller yang ada memiliki context yang tepat
 * 
 * @author PPL-B1
 * 
 */
public class NusabahanaView extends Activity {

	/** Controller dari instrumen musik*/
	public static InstrumentController INSTRUMENT_CONTROLLER;
	/** Controller dari rekaman*/
	public static RecordController RECORD_CONTROLLER;
	/** Controller dari musik latar*/
	public static BackgroundMusicController BM_CONTROLLER;

	/**
	 * Inisialisasi activity
	 */
	public void onCreate(Bundle parcel) {
		super.onCreate(parcel);
		initController();
	}

	/**
	 * Mengupdate context dari semua Controller yang ada
	 */
	private void initController() {
		if (INSTRUMENT_CONTROLLER == null)
			INSTRUMENT_CONTROLLER = new InstrumentController(this);
		if (RECORD_CONTROLLER == null)
			RECORD_CONTROLLER = new RecordController(this,
					INSTRUMENT_CONTROLLER);
		if (BM_CONTROLLER == null)
			BM_CONTROLLER = new BackgroundMusicController(this);

		INSTRUMENT_CONTROLLER.setActivity(this);
		RECORD_CONTROLLER.setActivity(this);
		BM_CONTROLLER.setActivity(this);
	}
	
	@Override
	public void onResume(){
		super.onResume();
		initController();
	}
}