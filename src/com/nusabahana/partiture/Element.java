package com.nusabahana.partiture;


public class Element {
	private SubElement[] subElements;

	public Element(SubElement[] subElements) {
		super();
		this.subElements = subElements;
	}

	public SubElement[] getSubElements() {
		return subElements;
	}

	public void setSubElements(SubElement[] subElements) {
		this.subElements = subElements;
	}
	
	public int getNumSubElements(){
		return this.subElements.length;
	}

	@Override
	public String toString() {
		String res = "";
		
		for(int i = 0; i < getNumSubElements(); i++){
			res += subElements[i].toString();
			if(i+1 < getNumSubElements()) res += "_";
		}
		return res;
	}
	
	
}
