package com.nusabahana.model;

import java.util.ArrayList;
import java.util.Date;


/**
 * Kelas yang merupakan model dari semua file rekaman nusabahana
 * 
 * 
 * @author PPL-B1
 * 
 */
public class Record {
	/** Nama file rekaman */
	private String name;
	/** Nama instrumen yang digunakan pada file rekaman */
	private Instrument instrument;
	/** Lama durasi rekaman */
	private long duration;
	/** Tanggal pembuatan rekaman*/
	private Date date;
	/** Kumpulan Action dari pengguna*/
	private ArrayList<Action> actions;
	
	/**
	 * Contructor  
	 * 
	 * @param n
	 *            nama rekaman
	 * @param i
	 *            instrumen yang digunakan
	 * @param d
	 *			  panjang durasi rekaman
	 * @param date
	 * 			  tanggal pembuatan rekaman
	 * @param as
	 * 			  kumpulan action
	 */
	public Record(String n, Instrument i, long d, Date date, ArrayList<Action> as){
		this.name = n;
		this.instrument = i;
		this.duration = d;
		this.date = date;
		this.actions = as;
	}
	
	/**
	 * Contructor 
	 * 
	 * @param n
	 *            nama rekaman
	 */
	public Record(String n){
		this(n, null, 0, null, null);
	}
	
	/**
	 * Mengambil nama rekaman
	 * 
	 * @return nama rekaman
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Mengganti nama rekaman terkini
	 * 
	 * @param  nama rekaman yang terbaru
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Mengambil objek instrument
	 * 
	 * @return objek instrument pada rekaman
	 */
	public Instrument getInstrument() {
		return instrument;
	}
	
	/**
	 * Mengganti objek instrument terkini
	 * 
	 * @param instrumen yang dimainkan terkini
	 */
	public void setInstrument(Instrument instrument) {
		this.instrument = instrument;
	}
	
	/**
	 * Mengambil nilai panjang durasi rekaman
	 * 
	 * @return panjang durasi rekaman
	 */
	public long getDuration() {
		return duration;
	}
	
	/**
	 * mengganti nilai panjang durasi rekaman
	 * 
	 * @param panjang durasi rekaman terkini
	 */
	public void setDuration(long duration) {
		this.duration = duration;
	}
	
	/**
	 * Mengambil tanggal pembuatan rekaman
	 * 
	 * @return tanggal pembuatan rekaman
	 */
	public Date getDate() {
		return date;
	}
	
	/**
	 * mengganti tanggal pembuatan terkini
	 * 
	 * @param tanggal terbaru
	 */
	public void setDate(Date date) {
		this.date = date;
	}
	
	/**
	 * Mengambil objek tindakan/action pengguna
	 * 
	 * @return objek tindakan/action pengguna
	 */
	public ArrayList<Action> getActions() {
		return actions;
	}
	
	/**
	 * mengganti nilai objek tindakan/action terbaru
	 * 
	 * @param tindakan/action terbaru
	 */
	public void setActions(ArrayList<Action> actions) {
		this.actions = actions;
	}

	@Override
	/**
	 * memberikan nilai unik
	 * 
	 * @return nilai unik
	 */
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	/**
	 * membandingkan kesamaan dua buah objek rekaman
	 * 
	 * @param objek yang akan dibandingkan
	 * @return kebenaran sama tidaknya objek
	 */
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Record other = (Record) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	
}
