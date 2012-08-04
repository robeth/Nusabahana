package com.nusabahana.model;

/**
 * Kelas yang merupakan model yang mencatat action/tindakan dari pengguna
 * @author PPL-B1
 * 
 */
public class Action {
	/** waktu dilakukan action */
	private long time;
	/** index action */
	private int index;

	/**
	 * Contructor default
	 * @param t waktu terjadinya action
	 * @param i index action
	 */
	public Action(long t, int i) {
		this.time = t;
		this.index = i;
	}

	/**
	 * Mengambil waktu terjadinya action
	 * @return waktu action
	 */
	public long getTime() {
		return time;
	}

	/**
	 * Mengganti waktu terjadinya action
	 * @param waktu terbaru
	 */
	public void setTime(long time) {
		this.time = time;
	}

	/**
	 * Mengambil index action
	 * @return index action
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * Mengganti index action terbaru
	 * @param index action terbaru
	 */
	public void setIndex(int index) {
		this.index = index;
	}
}
