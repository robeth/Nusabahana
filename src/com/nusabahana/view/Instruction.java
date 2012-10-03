package com.nusabahana.view;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

public class Instruction extends Activity {
	private RelativeLayout inflatedLayout;
	private int inflaterID;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Bundle extras = this.getIntent().getExtras();
        Log.d("Test", "Instruction for"+ extras.getString("instructionInstrument"));
        setContentView(R.layout.instruction);
        
        inflatedLayout = (RelativeLayout) findViewById(R.id.instruction_content);
		inflaterID = R.layout.instruction_bonang;
		LayoutInflater layoutInflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflatedLayout.addView(layoutInflater.inflate(inflaterID, inflatedLayout, false));
		
    }
}
