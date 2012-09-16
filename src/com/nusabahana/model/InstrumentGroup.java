package com.nusabahana.model;


public class InstrumentGroup {
	private Instrument[] instrumentElements;
	private String name;
	private String imagePath;
	private String bgPath;
	
	public InstrumentGroup(String name, String imagePath, Instrument[] ins, String bgPath){
		this.instrumentElements = ins;
		this.name=name;
		this.imagePath = imagePath;
		this.bgPath = bgPath;
	}

	public String getBgPath() {
		return bgPath;
	}

	public void setBgPath(String bgPath) {
		this.bgPath = bgPath;
	}

	public Instrument[] getInstrumentElements() {
		return instrumentElements;
	}

	public void setInstrumentElements(Instrument[] instrumentElements) {
		this.instrumentElements = instrumentElements;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	
	
}
