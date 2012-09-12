package com.nusabahana.partiture;

import java.util.Arrays;

public class Partiture {
	private Row[] rows;
	private String activeInstrumentKey;
	
	public Partiture(Row[] rows, String activeInstrumentKey) {
		super();
		this.rows = rows;
		this.activeInstrumentKey = activeInstrumentKey;
	}

	public Row[] getRows() {
		return rows;
	}

	public void setRows(Row[] rows) {
		this.rows = rows;
	}

	public int getNumRows(){
		return rows.length;
	}

	public static Partiture getPartitureFromFile(String filename){
		
		return null;
	}

	
	public String getActiveInstrumentKey() {
		return activeInstrumentKey;
	}

	public void setActiveInstrumentKey(String activeInstrumentKey) {
		this.activeInstrumentKey = activeInstrumentKey;
	}

	public int getSubElementValue(int row, int segment, int element, int subElement){
		Element tempElement =rows[row].getSegments()[segment].getElements()[element]; 
		return (subElement < tempElement.getNumSubElements())? tempElement.getSubElements()[subElement].getValue() : -1;		
	}

	@Override
	public String toString() {
		String res = activeInstrumentKey +"\n";
		for(int i =0; i< getNumRows(); i++){
			res += rows[i]+"\n";
		}
		return res;
	}
	
	
}
