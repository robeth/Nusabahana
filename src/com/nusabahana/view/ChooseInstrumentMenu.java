package com.nusabahana.view;

import com.nusabahana.model.Instrument;
import com.nusabahana.model.InstrumentGroup;
import com.nusabahana.view.R;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Kelas yang merupakan view dari fitur pemilihan instrumen musik
 * 
 * @author PPL-B1
 * 
 */
public class ChooseInstrumentMenu extends NusabahanaView {
	/** Nama dari instrumen yang dipilih */
	String chosenInstrument;

	private InstrumentGroup[] instrumentGroups;

	private LinearLayout groupListLayout;
	private LinearLayout instrumentListLayout;
	private TextView instrumentInfo;
	private TextView title;
	private TextView instrumentName;
	private TextView instrumentOrigin;
	private ImageView instrumentLogo;
	private Button letsPlayButton;
	private ImageView[] gImages;
	private ImageView[] iImages;

	/** Inisialisasi activity */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choose_instrument);
		instrumentGroups = INSTRUMENT_CONTROLLER.getInstrumentGroups();

		groupListLayout = (LinearLayout) findViewById(R.id.instrument_groups);
		instrumentListLayout = (LinearLayout) findViewById(R.id.instrument_elements);
		instrumentInfo = (TextView) findViewById(R.id.textOut);
		title = (TextView) findViewById(R.id.title_group);
		instrumentName = (TextView) findViewById(R.id.instrument_name_info);
		instrumentOrigin = (TextView) findViewById(R.id.instrument_origin_info);
		instrumentLogo = (ImageView) findViewById(R.id.instrument_logo_info);
		letsPlayButton = (Button) findViewById(R.id.lets_play);
		letsPlayButton.setOnClickListener(letsPlayOnClickListener);
		
		setGroupImages();
		setActiveGroup(instrumentGroups[0],0);
	}

	private void setGroupImages() {
		LinearLayout.LayoutParams imageLayout = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		imageLayout.setMargins(convertToPixels(5), convertToPixels(5), convertToPixels(5), convertToPixels(5));
		
		gImages = new ImageView[instrumentGroups.length];
		for (int i = 0; i < instrumentGroups.length; i++) {
			int imageID = getResources().getIdentifier(
					getFileName(instrumentGroups[i].getImagePath()),
					"drawable", getPackageName());
			ImageView groupImage = new ImageView(this);
			groupImage.setImageResource(imageID);
			groupImage.setAdjustViewBounds(true);
			/******/
			//groupImage.setMaxHeight(convertToPixels(60));
			//groupImage.setMaxHeight(convertToPixels(60));
			/*****/
			groupImage.setLayoutParams(imageLayout);
			groupImage.setOnClickListener(new GroupClickListener(
					instrumentGroups[i],i));
			groupListLayout.addView(groupImage);
			gImages[i] = groupImage;
		}
	}

	private void setActiveGroup(InstrumentGroup ig, int index) {
		instrumentListLayout.removeAllViews();
		System.gc();
		LinearLayout.LayoutParams imageLayout = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		imageLayout.setMargins(convertToPixels(5), convertToPixels(5), convertToPixels(5), convertToPixels(5));
		
		Instrument[] instruments = ig.getInstrumentElements();
		iImages = new ImageView[instruments.length];
		for (int i = 0; i < instruments.length; i++) {
			int instrumentImageID = getResources().getIdentifier(
					getFileName(instruments[i].getImagePath()), "drawable",
					getPackageName());
			ImageView instrumentImage = new ImageView(this);
			
			instrumentImage.setImageResource(instrumentImageID);
			instrumentImage.setAdjustViewBounds(true);
			//instrumentImage.setMaxHeight(convertToPixels(50));
			//instrumentImage.setMaxHeight(convertToPixels(50));
			instrumentImage.setLayoutParams(imageLayout);

			instrumentImage.setOnClickListener(new InstrumentClickListener(
					instruments[i],i));

			title.setText(ig.getName());
			instrumentListLayout.addView(instrumentImage);
			iImages[i] = instrumentImage;
		}
		setSelected(gImages,index);
		setActiveInstrument(instruments[0],0);
	}
	
	private void setSelected(ImageView[] ivs, int index) {
		for(int i = 0; i < ivs.length; i++)
			if(i != index)
				ivs[i].setBackgroundDrawable(null);
			else
				ivs[i].setBackgroundResource(R.drawable.toast_frame2);
	}

	private void setActiveInstrument(Instrument ins, int index){
		int imageId = getResources().getIdentifier(getFileName(ins.getImagePath()), "drawable", getPackageName());
		instrumentLogo.setImageResource(imageId);
		instrumentInfo.setText(ins.getProfile());
		instrumentName.setText(ins.getName());
		instrumentOrigin.setText(ins.getOrigin());
		chosenInstrument = ins.getName();
		setSelected(iImages, index);
	}
	
	private void launchSimulation(){
		Intent newIntent = new Intent(ChooseInstrumentMenu.this,
				InstrumentSimulationMenu.class);

		Bundle b = new Bundle();
		b.putString("chosenInstrument", chosenInstrument);
		Log.e("Chosen Instrument:", chosenInstrument);

		newIntent.putExtras(b);
		startActivity(newIntent);
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

	private int convertToPixels(int dps){
		final float scale = getResources().getDisplayMetrics().density;
		return (int) (dps * scale + 0.5f);
	}
	private class GroupClickListener implements OnClickListener {
		private InstrumentGroup ig;
		private int index;

		public GroupClickListener(InstrumentGroup ig, int index) {
			this.ig = ig;
			this.index = index;
		}

		@Override
		public void onClick(View v) {
			setActiveGroup(ig, index);
		}

	}

	private class InstrumentClickListener implements OnClickListener {
		private Instrument ins;
		private int index;
		public InstrumentClickListener(Instrument ins, int index) {
			this.ins = ins;
			this.index = index;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			setActiveInstrument(ins,index);
		}
	}

	private OnClickListener letsPlayOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			launchSimulation();

		}
	};
}