package com.nusabahana.controller;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Arrays;

import com.nusabahana.view.InstrumentSimulationMenu;

import android.app.Activity;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Environment;
import android.util.Log;
import android.widget.ProgressBar;

/**
 * Kelas yang merupakan Controller dari segala aktivitas yang berhubungan dengan
 * musik latar/background music aplikasi Nusabahana
 * 
 * @author PPL B1
 * 
 */
public class BackgroundMusicController {
	/** Context android */
	private Activity activity;
	/** Konstanta yang menunjukkan status stand by */
	public static final int MODE_STANDBY = 0;
	/** Konstanta yang menunjukkan status playing */
	public static final int MODE_PLAYING = 1;
	/** Konstanta yang menunjukkan status pausing */
	public static final int MODE_PAUSING = 2;
	/** Status dari controller ini */
	private int mode = MODE_STANDBY;
	/** Path default untuk mengambil background music dari pengguna */
	public String DEFAULT_MUSICS_FOLDER_PATH;

	private String[] allowedExtension = { "mp3", "ogg" };

	// private String DEFAULT_MUSICS_FOLDER_PATH;

	/** Media player yang digunakan untuk memainkan background music */
	private MediaPlayer mediaPlayer;
	/**
	 * Listener yang dipanggil ketika controller siap memainkan background musik
	 */
	private OnBackgroundMusicStartListener onBackgroundMusicStartListener;

	/**
	 * Constructor default
	 * 
	 * @param activity
	 *            context android dimana controller ini aktif berjalan
	 */
	public BackgroundMusicController(Activity activity) {
		this.activity = activity;
		this.DEFAULT_MUSICS_FOLDER_PATH = Environment
				.getExternalStorageDirectory().getAbsolutePath()
				+ "/Nusabahana/musics";
		Log.v("BM DEFAULT", DEFAULT_MUSICS_FOLDER_PATH);
		makeDirectory();

	}

	private void makeDirectory() {
		// TODO Auto-generated method stub
		File f = new File(DEFAULT_MUSICS_FOLDER_PATH);
		if (!f.exists())
			f.mkdirs();

	}

	/**
	 * Memainkan background music
	 * 
	 * @param filename
	 *            nama file dari background music
	 */
	public void play(String filename, final OnCompletionListener ocl) {
		try {
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnPreparedListener(new OnPreparedListener() {
				@Override
				public void onPrepared(MediaPlayer mp) {
					if (onBackgroundMusicStartListener != null)
						onBackgroundMusicStartListener.onStart();
					mediaPlayer.start();

				}
			});

			mediaPlayer.setOnCompletionListener(new OnCompletionListener() {

				@Override
				public void onCompletion(MediaPlayer mp) {
					// TODO Auto-generated method stub
					if (ocl != null)
						ocl.onCompletion(mediaPlayer);
					if (mediaPlayer != null) {
						mediaPlayer.stop();
						mediaPlayer.release();
					}
					mode = MODE_STANDBY;
				}
			});

			Log.d("BackgroundMusic", "Start opening:" + filename);
			if (filename.startsWith(DEFAULT_MUSICS_FOLDER_PATH)) {
				Log.d("BackgroundMusic", "in folder:" + filename);
				// mediaPlayer.setDataSource(filename);
				// MediaPlayer mPlayer = new MediaPlayer();
				// example of mediaFile
				// =/data/data/package/cache/playingMedia0.dat
				File f = new File(filename);
				Log.v("BackgroundMusic",
						"File status [exist|write|read]:" + f.exists() + "-"
								+ f.canWrite() + "-" + f.canRead()
								+ "\nfilename:" + f.getAbsolutePath());
				// FileInputStream fis = new FileInputStream(filename);
				// FileDescriptor afd = fis.getFD();
				mediaPlayer.setDataSource(f.getAbsolutePath());
			} else {
				Log.d("BackgroundMusic", "in asset:" + filename);
				mediaPlayer.setDataSource(activity.getAssets().openFd(filename)
						.getFileDescriptor());
			}
			mediaPlayer.prepare();
		} catch (Exception ex) {
			Log.d("BackgroundMusic", "error opening:" + filename
					+ "\nErrorMessage:" + ex.getMessage());
			ex.printStackTrace();
		}
		mode = MODE_PLAYING;
	}

	/**
	 * Mengambil context terkini
	 * 
	 * @return context dimana controller ini sedang aktif
	 */
	public Activity getActivity() {
		return activity;
	}

	/**
	 * Mengganti context
	 * 
	 * @param activity
	 *            context baru dimana controller ini aktif
	 */
	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	/**
	 * Melakukan pause permainan background music
	 */
	public void pause() {
		if (mediaPlayer.isPlaying())
			mediaPlayer.pause();
		mode = MODE_PAUSING;
	}

	/**
	 * Menghentikan permainan background music
	 */
	public void stop() {
		if (mediaPlayer != null) {
			mediaPlayer.reset();
			mediaPlayer.release();
			mediaPlayer = null;
		}
		mode = MODE_STANDBY;
	}

	/**
	 * Mengambil status dari controller
	 * 
	 * @return status terkini dari controller
	 */
	public int getMode() {
		return mode;
	}

	/**
	 * Mengganti status terkini dari controller
	 * 
	 * @param mode
	 *            status baru
	 */
	public void setMode(int mode) {
		this.mode = mode;
	}

	/**
	 * Melanjutkan permainan background music yang sebelumnya berada pada mode
	 * pause
	 */
	public void resume() {
		// TODO Auto-generated method stub
		mediaPlayer.start();
		mode = MODE_PLAYING;
	}

	/**
	 * Mengambil nama file dari semua background music yang mungkin dimainkan
	 * 
	 * @return nama file dari semua background music yang mungkin dimainkan
	 */
	public String[] getAllMusicsName() {
		File root = new File(DEFAULT_MUSICS_FOLDER_PATH);

		String[] musicFiles = null;
		try {
			String[] musicFilesDefault = filterString(activity.getAssets()
					.list(""));
			String[] musicFilesUser = root.list(new FilenameFilter() {

				@Override
				public boolean accept(File dir, String filename) {
					// TODO Auto-generated method stub
					if (filename.contains(".")) {
						String ext = filename.substring(
								filename.lastIndexOf(".") + 1,
								filename.length());
						if (satisfyExtension(ext))
							return true;
						return false;
					} else
						return false;
				}

			});
			appendDefaultPath(musicFilesUser);
			musicFiles = new String[(musicFilesDefault == null ? 0
					: musicFilesDefault.length)
					+ (musicFilesUser == null ? 0 : musicFilesUser.length)];
			if (musicFilesDefault != null)
				System.arraycopy(musicFilesDefault, 0, musicFiles, 0,
						musicFilesDefault.length);
			if (musicFilesUser != null)
				System.arraycopy(musicFilesUser, 0, musicFiles,
						musicFilesDefault.length, musicFilesUser.length);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (musicFiles != null)
			Arrays.sort(musicFiles);
		return musicFiles;
	}

	private boolean satisfyExtension(String ext) {
		for (int i = 0; i < allowedExtension.length; i++)
			if (ext.equalsIgnoreCase(allowedExtension[i]))
				return true;
		return false;
	}

	/**
	 * Mengambil posisi byte terkini yang dibaca oleh media player
	 * 
	 * @return posisi byte terkini yang dibaca oleh media player
	 */
	public int getCurrent() {
		return mediaPlayer.getCurrentPosition();
	}

	/**
	 * Mengambil durasi dari file musik yang sedang dimainkan oleh media player
	 * 
	 * @return Durasi dari file musik yang sedang dimainkan oleh media player
	 */
	public int getDuration() {
		return mediaPlayer.getDuration();
	}

	/**
	 * mengambil semua string yang berakhiran 'filter' pada 'input'
	 * 
	 * @param input
	 *            string yang akan difilter
	 * @param filter
	 *            akhiran yang diinginkan
	 * @return semua string yang berakhiran 'filter' pada 'input'
	 */
	private String[] filterString(String[] input) {
		String[] temp = new String[input.length];
		int i = 0;
		for (String s : input) {
			for (String f : allowedExtension)
				if (s.endsWith(f)) {
					temp[i++] = s;
					break;
				}
		}
		String[] result = new String[i];
		System.arraycopy(temp, 0, result, 0, i);
		return result;
	}

	public abstract class OnBackgroundMusicStartListener {
		public abstract void onStart();
	}

	public OnBackgroundMusicStartListener getOnBackgroundMusicStartListener() {
		return onBackgroundMusicStartListener;
	}

	public void setOnBackgroundMusicStartListener(
			OnBackgroundMusicStartListener onBackgroundMusicStartListener) {
		this.onBackgroundMusicStartListener = onBackgroundMusicStartListener;
	}

	private void appendDefaultPath(String[] paths) {
		if (paths == null)
			return;
		for (int i = 0; i < paths.length; i++)
			paths[i] = DEFAULT_MUSICS_FOLDER_PATH + "/" + paths[i];
	}
}
