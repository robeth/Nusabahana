package com.nusabahana.view;

import com.nusabahana.view.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsoluteLayout;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.AdapterView.OnItemClickListener;

/**
 * Kelas yang merupakan view dari Galeri musik latar
 * @author PPL-B1
 */
public class BackgroundMusicGalleryMenu extends NusabahanaView {
	
    /** Tahap inisialisasi activity */
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
        
        //Ambil semua nama musik latar dari controller, lalu isikan ke ListView
//        final String[] recordsName = BM_CONTROLLER.getAllMusicsName();
//        if(recordsName!=null){
//        	ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.recordrow,recordsName);
//			lv.setAdapter(adapter);
//			
//			lv.setOnItemClickListener(new OnItemClickListener() {
//				@Override
//				public void onItemClick(AdapterView<?> parent, View view,
//					int position, long id) {
//					Bundle b = new Bundle();
//					b.putString("filename", recordsName[position]);
//					setResult(Activity.RESULT_OK, new Intent().putExtras(b));
//					BackgroundMusicGalleryMenu.this.finish();
//				}
//			});
//        }
		final String[] recordsName = RECORD_CONTROLLER.getAllRecordsName();
        final String[] recordsName2 = BM_CONTROLLER.getAllMusicsName();
		if (recordsName2 != null) {
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
					R.layout.recordrow, recordsName2);
			lv2.setAdapter(adapter);

			lv2.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					Bundle b = new Bundle();
					b.putString("filename", recordsName2[position]);
					b.putBoolean("isRecord", false);
					setResult(Activity.RESULT_OK, new Intent().putExtras(b));
					BackgroundMusicGalleryMenu.this.finish();
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
					Bundle b = new Bundle();
					b.putString("filename", recordsName[position]);
					b.putBoolean("isRecord", true);
					setResult(Activity.RESULT_OK, new Intent().putExtras(b));
					BackgroundMusicGalleryMenu.this.finish();
				}
			});
		}
		
		RelativeLayout al = (RelativeLayout) findViewById(R.id.player);
		al.setVisibility(View.INVISIBLE);
    }
    
    /**
     * Dipanggil ketika tombol back ditekan
     */
    @Override
    public void onBackPressed(){
		setResult(Activity.RESULT_CANCELED, null);
		BackgroundMusicGalleryMenu.this.finish();
    }
    
    /**
     * Listener yang digunakan sehingga pengguna kembali ke menu utama 
     */
//    private OnClickListener homeListener = new OnClickListener() {
//		public void onClick(View v) {
//				Intent nIntent = new Intent(BackgroundMusicGalleryMenu.this,
//						MainMenu.class);
//				nIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//				startActivity(nIntent);
//		}
//	};
}