package com.nusabahana.model;

import java.util.ArrayList;
import java.util.Arrays;



/**
 * Kelas yang merupakan model dari semua instrumen alat musik dalam nusabahana
 * 
 * @author PPL-B1
 * 
 */
public class Instrument {
	/** Nama alat musik */
	protected String name;
	/** Nama lain alat musik */
	protected String nickname;
	/** Daerah asal alat musik */
	protected String origin;
	/** Trah alat musik */
	protected String family;
	/** Profile alat musik */
	protected String profile;
	/** path gambar alat musik */
	protected String imagePath;
	/** catatan-catatan file */
	protected Note[] notes;
	
	/**
	 * Contructor  default
	 * 
	 * @param n nama alat musik
	 * @param nn nama lain alat musik
	 * @param o asal alat musik
	 * @param f trah alat musik
	 * @param p profil alat musik
	 * @param ip path image alat musik
	 * @param ns note-note dari alat musik ini
	 */
	public Instrument(String n, String nn, String o, String f, String p, String ip, Note[] ns){
		this.name = n;
		this.nickname = nn;
		this.origin = o;
		this.family = f;
		this.profile = p;
		this.imagePath = ip;
		this.notes = ns;
	}

	/**
	 * Mengambil nama lain alat musik
	 * 
	 * @return nama lain alat musik
	 */
	public String getNickname() {
		return nickname;
	}

	/**
	 * Mengganti nama lain alat musik
	 * 
	 * @param  nama lain terbaru
	 */
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	/**
	 * Mengambil nama alat musik
	 * 
	 * @return nama alat musik
	 */
	public String getName() {
		return name;
	}

	/**
	 * Mengganti nama alat musik terkini
	 * 
	 * @param  nama terbaru
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Mengambil daerah asal alat musik
	 * 
	 * @return daerah asal alat musik
	 */
	public String getOrigin() {
		return origin;
	}

	/**
	 * Mengganti asal daerah alat musik
	 * 
	 * @param  asal daerah
	 */
	public void setOrigin(String origin) {
		this.origin = origin;
	}

	/**
	 * Mengambil trah alat musik
	 * 
	 * @return nama trah alat musik
	 */
	public String getFamily() {
		return family;
	}

	/**
	 * Mengganti trah alat musik
	 * 
	 * @param  trah terbaru
	 */
	public void setFamily(String family) {
		this.family = family;
	}

	/**
	 * Mengambil profil alat musik
	 * 
	 * @return profil alat musik
	 */
	public String getProfile() {
		return profile;
	}

	/**
	 * Mengganti profil alat musik
	 * 
	 * @param  profil terbaru
	 */
	public void setProfile(String profile) {
		this.profile = profile;
	}

	/**
	 * Mengambil path image alat musik
	 * 
	 * @return path image
	 */
	public String getImagePath() {
		return imagePath;
	}

	/**
	 * Mengganti path image alat musik
	 * 
	 * @param  path image terbaru
	 */
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	/**
	 * Mengambil catatan-catatan file
	 * 
	 * @return catatan
	 */
	public Note[] getNotes() {
		return notes;
	}

	/**
	 * Mengganti catatan-catatan file terbaru
	 * 
	 * @param  catatan file terbaru
	 */
	public void setNotes(Note[] notes) {
		this.notes = notes;
	}
	
	/**
	 * Mengambil label dari suatu file dalam catatan
	 * 
	 * @param index dari file
	 * @return label file
	 */
	public String getNoteLabel(int index){
		
		return notes[index].getLabel();
	}

	public int[] getInstrumentIndexes(int key){
		int[] temp = new int[4];
		int latestIndex = 0;
		
		Arrays.fill(temp, -1);
		
		for(int i = 0; i < notes.length; i++){
			if(notes[i].getKey() == key)
				temp[latestIndex++] = i;
		}
		return temp;
	}
	
}
