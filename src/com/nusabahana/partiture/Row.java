package com.nusabahana.partiture;

import java.util.Arrays;

public class Row {
	private Segment[] segments;

	public Row(Segment[] segments) {
		super();
		this.segments = segments;
	}

	public Segment[] getSegments() {
		return segments;
	}

	public void setSegments(Segment[] segments) {
		this.segments = segments;
	}
	
	public int getNumSegments(){
		return this.segments.length;
	}

	@Override
	public String toString() {
		String res = "";
		for (int i = 0; i < getNumSegments(); i++){
			res+= segments[i].toString();
			if(i+1 < getNumSegments()) res += " ";
		}
		return res;
	}
	
	
}
