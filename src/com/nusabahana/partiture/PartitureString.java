package com.nusabahana.partiture;

import java.util.HashMap;

public class PartitureString {
	private HashMap<Integer, String> library;
	private static PartitureString partitureString;
	
	private PartitureString(){
		library = new HashMap<Integer, String>(30);
		library.put(0,"0");
		library.put(1,"1");
		library.put(2,"2");
		library.put(3,"3");
		library.put(4,"4");
		library.put(5,"5");
		library.put(6,"6");
		library.put(7,"7");
		
		library.put(777, "&#183;");
		
		//Nada tinggi
		library.put(11, "1&#775;");
		library.put(12, "2&#775;");
		library.put(13, "3&#775;");
		library.put(14, "4&#775;");
		library.put(15, "5&#775;");
		library.put(16, "6&#775;");
		library.put(17, "7&#775;");
		
		//Nada Tinggi-kress
		library.put(61, "<font style='line-through'>1&#775;</font>");
		library.put(62, "<font style='line-thtough'>2&#775;</font>");
		library.put(63, "<font style='line-thtough'>3&#775;</font>");
		library.put(64, "<font style='line-thtough'>4&#775;</font>");
		library.put(65, "<font style='line-thtough'>5&#775;</font>");
		library.put(66, "<font style='line-thtough'>6&#775;</font>");
		library.put(67, "<font style='line-thtough'>7&#775;</font>");
				
		//Nada kress
		library.put(21, "<font style='line-through'>1</font>");
		library.put(22, "<font style='line-thtough'>2</font>");
		library.put(23, "<font style='line-thtough'>3</font>");
		library.put(24, "<font style='line-thtough'>4</font>");
		library.put(25, "<font style='line-thtough'>5</font>");
		library.put(26, "<font style='line-thtough'>6</font>");
		library.put(27, "<font style='line-thtough'>7</font>");
		
		//Nada bawah
		library.put(31, "1&#803;");
		library.put(32, "2&#803;");
		library.put(33, "3&#803;");
		library.put(34, "4&#803;");
		library.put(35, "5&#803;");
		library.put(36, "6&#803;");
		library.put(37, "7&#803;");
		
		//Nada Bawah-kress
		library.put(81, "<font style='line-through'>1&#803;</font>");
		library.put(82, "<font style='line-thtough'>2&#803;</font>");
		library.put(83, "<font style='line-thtough'>3&#803;</font>");
		library.put(84, "<font style='line-thtough'>4&#803;</font>");
		library.put(85, "<font style='line-thtough'>5&#803;</font>");
		library.put(86, "<font style='line-thtough'>6&#803;</font>");
		library.put(87, "<font style='line-thtough'>7&#803;</font>");
		
		//Special key for bonang
		library.put(101, "0/1");
		library.put(102, "0/2");
		library.put(103, "0/3");
		library.put(104, "0/4");
		library.put(105, "0/5");
		library.put(106, "0/6");
		library.put(107, "0/7");
		library.put(111, "1/1");
		library.put(122, "2/2");
		library.put(133, "3/3");
		library.put(144, "4/4");
		library.put(155, "5/5");
		library.put(166, "6/6");
		library.put(177, "7/7");
		library.put(110, "1/0");
		library.put(120, "2/0");
		library.put(130, "3/0");
		library.put(140, "4/0");
		library.put(150, "5/0");
		library.put(160, "6/0");
		library.put(170, "7/0");
		
		//Special key for kendang
		library.put(201, "t");
		library.put(202, "p");
		library.put(203, "d");
		
	}
	
	public static PartitureString getInstance(){
		if(partitureString == null){
			partitureString = new PartitureString();
		}
		return partitureString;
	}
	
	public String getValue(int key){
		return library.get(key);
	}
}
