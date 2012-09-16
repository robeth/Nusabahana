package com.nusabahana.model;
/**
 * Kelas yang merepresentasikan note dari suatu alat musik
 * @author PPL-B1
 * 
 */
public class Note {
	/** index note*/
	private int index;
	/** path audio file note ini */
	private String path;
	/** note sebenarnya pada alat musik */
	private String label;
	private int key;
	
	/**
	 * Contructor default
	 * 
	 * @param i
	 *            index file
	 * @param p
	 *			  path file
	 * @param l
	 * 			  label file
	 */
	public Note(int i, String p, String l, int key){
		this.index = i;
		this.path = p;
		this.label = l;
		this.key = key;
	}

	/**
	 * Mengambil label file dalam direktori
	 * 
	 * @return label file
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * Mengganti label file terkini
	 * 
	 * @param  label file terbaru
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * Mengambil index file dalam direktori
	 * 
	 * @return index file
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * Mengganti index file terkini
	 * 
	 * @param  index file terbaru
	 */
	public void setIndex(int index) {
		this.index = index;
	}

	/**
	 * Mengambil path file dalam direktori
	 * 
	 * @return path file
	 */
	public String getPath() {
		return path;
	}

	/**
	 * Mengganti path file terkini
	 * 
	 * @param  path file terbaru
	 */
	public void setPath(String path) {
		this.path = path;
	}

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}
	
	
}
