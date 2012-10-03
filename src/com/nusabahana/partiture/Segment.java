package com.nusabahana.partiture;


public class Segment {
	private Element[] elements;

	
	
	public Segment(Element[] elements) {
		super();
		this.elements = elements;
	}

	public Element[] getElements() {
		return elements;
	}

	public void setElements(Element[] elements) {
		this.elements = elements;
	}
	
	public int getNumElements(){
		return this.elements.length;
	}

	@Override
	public String toString() {
		String res = "";
		for (int i =0; i < getNumElements(); i++){
			res+= elements[i].toString();
			if(i + 1  < getNumElements()) res+= "-";
		}
		return res;
	}
	
	
}
