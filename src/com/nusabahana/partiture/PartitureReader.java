package com.nusabahana.partiture;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;


public class PartitureReader {
	public static final String PARTITURE_PATH = "Partiture";
	public static final String GAMELAN_JAWA = "Gamelan Jawa";
	public static final String GAMELAN_BALI = "Gamelan Bali";
	public static final String ANGKLUNG = "Angklung";
	private static Context context;
	private static AssetManager am;
	
	public static String[] getAllPartituresFilename(String category){
		String[] filenames = null;
		
		try {
			filenames = am.list(PARTITURE_PATH+"/"+category);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return filenames;
	}
	
	private static void printArrayContent(String[] arrays, String header){
		Log.d("Partiture Reader", header);
		for(int i = 0 ; i < arrays.length; i++){
			Log.d("Partiture Reader", "Index "+i+": "+arrays[i]);
		}
	}
	
	
	
	public static Partiture getPartiture(String category, String filename,String instrumentKeyName){
		return getInstrumentPartiture(readPartituresFromFile(category, filename), instrumentKeyName);	
	}
	
	public static Partiture[] readPartituresFromFile (String category, String filename){
		String path = PartitureReader.PARTITURE_PATH +"/"+ category + "/"+ filename;
		BufferedReader br= null;
		Partiture[] partitures = null;
		try {
//			File f = new File();
			br = new BufferedReader(new InputStreamReader(am.open(path)));
			int totalPartitures = Integer.parseInt(br.readLine());
			int totalRows = Integer.parseInt(br.readLine());
			partitures = new Partiture[totalPartitures];
			
			for(int i = 0; i< totalPartitures; i++){
				String instrumentKeyname = br.readLine();
				Row[] rows = new Row[totalRows];
				for(int j = 0; j < totalRows; j++){
					String[] rawSegments = br.readLine().split(" ");
					Segment[] segments = new Segment[rawSegments.length];
					for(int k = 0; k < segments.length; k++){
						String[] rawElements = rawSegments[k].split("-");
						Element[] elements = new Element[rawElements.length];
						for(int l = 0; l < elements.length; l++){
							String[] rawSubelements = rawElements[l].split("_");
							SubElement[] subelements = new SubElement[rawSubelements.length];
							for(int m = 0; m < subelements.length;m++){
								subelements[m] = new SubElement(Integer.parseInt(rawSubelements[m]));
							}
							elements[l] = new Element(subelements);
						}
						segments[k] = new Segment(elements);
					}
					rows[j] = new Row(segments);
				}
				partitures[i] = new Partiture(rows,instrumentKeyname);
				Log.d("Debug Tutorial", partitures[i].toString());
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return partitures;
	}
	
	private static Partiture getInstrumentPartiture(Partiture[] partitures, String instrumentKeyName ){
		for(int i = 0; i < partitures.length; i++){
			if(partitures[i].getActiveInstrumentKey().equals(instrumentKeyName)){
				return partitures[i];
			}
		}
		
		return null;
	}

	public static Context getContext() {
		return context;
	}

	public static void setContext(Context context) {
		PartitureReader.context = context;
		PartitureReader.am = context.getAssets();
	}
	
	
	
	/*
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		 try{
	           BufferedReader br = new BufferedReader(new FileReader("partitur1.txt"));

	           String nama;
	           String row1string;
	           String row2string;
	           String row3string;
	           
	           String[] section1;
	           String[] section2;
	           String[] section3;
	           
	           String[] element1string;
	           String[] element2string;
	           String[] element3string;
	           
	           String[] element4string;
	           String[] element5string;
	           String[] element6string;
	           String[] element7string;
	           
	           String[] element8string;
	           String[] element9string;
	           String[] element10string;
	           String[] element11string;
	           Partiture[] partitur = new Partiture[5];        
	           
	           // ambil 4 partitur
	           for (int i = 0; i < 4 ; i++) {
		           // baca 3 baris
		           nama = br.readLine();
		           row1string = br.readLine();
		           row2string = br.readLine();
		           row3string = br.readLine();
		           
		           
	//	           System.out.println(nama);
	//	           
	//	           System.out.println(row1string);
	//	           System.out.println(row2string);
	//	           System.out.println(row3string);
		           
		           
		           // baris 1
		           section1 = row1string.split(" ");
		           element1string = section1[0].split("-");
		           element2string = section1[1].split("-");
		           element3string = section1[2].split("-");
		           
		           Element element1 = new Element(Integer.parseInt(element1string[0]));
		           Element element2 = new Element(Integer.parseInt(element1string[1]));
		           Element element3 = new Element(Integer.parseInt(element1string[2]));
		           Element element4 = new Element(Integer.parseInt(element1string[3]));
		           Element[] arrayElement1 = {element1, element2,element3, element4   } ;
		           
		           Element element5 = new Element(Integer.parseInt(element2string[0]));
		           Element element6 = new Element(Integer.parseInt(element2string[1]));
		           Element element7 = new Element(Integer.parseInt(element2string[2]));
		           Element element8 = new Element(Integer.parseInt(element2string[3]));
		           Element[] arrayElement2 = { element5,element6,element7, element8} ;
		           
		           
		           Element element9 = new Element(Integer.parseInt(element3string[0]));
		           Element element10 = new Element(Integer.parseInt(element3string[1]));
		           Element element11 = new Element(Integer.parseInt(element3string[2]));
		           Element element12 = new Element(Integer.parseInt(element3string[3]));
		           Element[] arrayElement3 = { element9, element10, element11, element12 } ;
		           
		           Segment segment1 = new Segment(arrayElement1);
		           Segment segment2 = new Segment(arrayElement2);
		           Segment segment3 = new Segment(arrayElement3);
		           Segment[] arraySegment = {segment1, segment2, segment3};
		           Row row1 = new Row(arraySegment);
		           
		          
		           // baris 2 
		           section2 = row2string.split(" ");
		           element4string = section2[0].split("-");
		           element5string = section2[1].split("-");
		           element6string = section2[2].split("-");
		           element7string = section2[3].split("-");
		           
		           Element element13 = new Element(Integer.parseInt(element4string[0]));
		           Element element14 = new Element(Integer.parseInt(element4string[1]));
		           Element element15 = new Element(Integer.parseInt(element4string[2]));
		           Element element16 = new Element(Integer.parseInt(element4string[3]));
		           Element[] arrayElemen4 = { element13, element14, element15, element16 } ;
		           
		           Element element17 = new Element(Integer.parseInt(element5string[0]));
		           Element element18 = new Element(Integer.parseInt(element5string[1]));
		           Element element19 = new Element(Integer.parseInt(element5string[2]));
		           Element element20 = new Element(Integer.parseInt(element5string[3]));
		           Element[] arrayElemen5 = { element17, element18, element19, element20 } ;
		           
		           Element element21 = new Element(Integer.parseInt(element6string[0]));
		           Element element22 = new Element(Integer.parseInt(element6string[1]));
		           Element element23 = new Element(Integer.parseInt(element6string[2]));
		           Element element24 = new Element(Integer.parseInt(element6string[3]));
		           Element[] arrayElemen6 = { element21, element22, element23, element24 } ;
		           
		           Element element25 = new Element(Integer.parseInt(element7string[0]));
		           Element element26 = new Element(Integer.parseInt(element7string[1]));
		           Element element27 = new Element(Integer.parseInt(element7string[2]));
		           Element element28 = new Element(Integer.parseInt(element7string[3]));
		           Element[] arrayElemen7 = { element25, element26, element27, element28 } ;
		           
		           Segment segment4 = new Segment(arrayElemen4);
		           Segment segment5 = new Segment(arrayElemen5);
		           Segment segment6 = new Segment(arrayElemen6);
		           Segment segment7 = new Segment(arrayElemen7);
		           Segment[] arraySegment2 = { segment4, segment5, segment6, segment7};
		           Row row2 = new Row(arraySegment2);
		           
		           
		           // baris 3 
		           section3 = row3string.split(" ");
		           element8string = section3[0].split("-");
		           element9string = section3[1].split("-");	           
		           element10string = section3[2].split("-");
		           element11string = section3[3].split("-");
		           
		           Element element29 = new Element(Integer.parseInt(element8string[0]));
		           Element element30 = new Element(Integer.parseInt(element8string[1]));
		           Element element31 = new Element(Integer.parseInt(element8string[2]));
		           Element element32 = new Element(Integer.parseInt(element8string[3]));
		           Element[] arrayElemen8 = { element29, element30, element31, element32 } ;
		           
		           Element element33 = new Element(Integer.parseInt(element9string[0]));
		           Element element34 = new Element(Integer.parseInt(element9string[1]));
		           Element element35 = new Element(Integer.parseInt(element9string[2]));
		           Element element36 = new Element(Integer.parseInt(element9string[3]));
		           Element[] arrayElemen9 = { element33, element34, element35, element36 } ;
		           
		           Element element37 = new Element(Integer.parseInt(element10string[0]));
		           Element element38 = new Element(Integer.parseInt(element10string[1]));
		           Element element39 = new Element(Integer.parseInt(element10string[2]));
		           Element element40 = new Element(Integer.parseInt(element10string[3]));
		           Element[] arrayElemen10 = { element37, element38, element39, element40 } ;
		           
		           Element element41 = new Element(Integer.parseInt(element11string[0]));
		           Element element42 = new Element(Integer.parseInt(element11string[1]));
		           Element element43 = new Element(Integer.parseInt(element11string[2]));
		           Element element44 = new Element(Integer.parseInt(element11string[3]));
		           Element[] arrayElemen11 = { element41, element42, element43, element44 } ;
		           
		           Segment segment8 = new Segment(arrayElemen8);
		           Segment segment9 = new Segment(arrayElemen9);
		           Segment segment10 = new Segment(arrayElemen10);
		           Segment segment11 = new Segment(arrayElemen11);
		           Segment[] arraySegment3 = { segment8, segment9, segment10, segment11};
		           Row row3 = new Row(arraySegment3);
		           
		           Row[] rowPartitur = { row1, row2, row3};
		           Partiture partiturMusik = new Partiture(rowPartitur);
		           partitur[i] = partiturMusik;
	           }
	           
	           /// coba ambil partitur ke 4 baris ke 3 segment 1 element kedua
	           Row[] asem = partitur[3].getRows();
	           Segment[] sem = asem[2].getSegments();
	           Element[] el = sem[0].getElements();
	           System.out.println(el[1].value);
	           
	           
	           
	           // partitur kendang
	           // t = 1
	           // p = 2
	           // d = 3
	           nama = br.readLine();
	           row1string = br.readLine();
	           row2string = br.readLine();
	           row3string = br.readLine();
	           
	           System.out.println(nama);
	           System.out.println(row1string);
	           System.out.println(row2string);
	           System.out.println(row3string);
	           
	           // kendang baris 1
	           section1 = row1string.split(" ");
	           element1string = section1[0].split("-");
	           element2string = section1[1].split("-");
	           element3string = section1[2].split("-");
	          
//	           System.out.println(element3string[0]);
//	           System.out.println(element3string[1]);
//	           System.out.println(element3string[2]);
//	           System.out.println(element3string[3]);

	           
	           Element el1 = new Element(Integer.parseInt(element1string[0]));
	           Element el2 = new Element(Integer.parseInt(element1string[1]));
	           Element el3 = new Element(Integer.parseInt(element1string[2]));
	           Element el4 = new Element(Integer.parseInt(element1string[3]));
	           Element[] arrayEl1 = {el1, el2,el3, el4   } ;
	           
	           
	           Element el5 = new Element(Integer.parseInt(element2string[0]));
	           Element el6 = new Element(Integer.parseInt(element2string[1]));
	           Element el7 = new Element(Integer.parseInt(element2string[2]));
	           Element el8 = new Element(Integer.parseInt(element2string[3]));
	           Element[] arrayEl2 = { el5,el6,el7, el8} ;
	           
	           
	           Element el9 = new Element(Integer.parseInt(element3string[0]));
	           Element el10 = new Element(Integer.parseInt(element3string[1]));
	           Element el11 = new Element(Integer.parseInt(element3string[2]));
	           Element el12 = new Element(Integer.parseInt(element3string[3]));
	           Element ele9 = new Element(Integer.parseInt(element3string[4]));
	           Element ele10 = new Element(Integer.parseInt(element3string[5]));
	           Element ele11 = new Element(Integer.parseInt(element3string[6]));
	           Element ele12 = new Element(Integer.parseInt(element3string[7]));
	           Element[] arrayEl3 = { el9, el10, el11, el12, ele9, ele10, ele11, ele12} ;
	           
	           
	           Segment seg1 = new Segment(arrayEl1);
	           Segment seg2 = new Segment(arrayEl2);
	           Segment seg3 = new Segment(arrayEl3);
	           Segment[] arraySeg1 = {seg1, seg2, seg3};
	           Row baris1 = new Row(arraySeg1);
	           
	           // kendang baris 2
	           section2 = row2string.split(" ");
	           element4string = section2[0].split("-");
	           element5string = section2[1].split("-");
	           element6string = section2[2].split("-");
	           element7string = section2[3].split("-");
	           
	           Element el13 = new Element(Integer.parseInt(element4string[0]));
	           Element el14 = new Element(Integer.parseInt(element4string[1]));
	           Element el15 = new Element(Integer.parseInt(element4string[2]));
	           Element el16 = new Element(Integer.parseInt(element4string[3]));
	           Element[] arrayEl4 = { el13, el14, el15, el16 } ;
	           
	           Element el17 = new Element(Integer.parseInt(element5string[0]));
	           Element el18 = new Element(Integer.parseInt(element5string[1]));
	           Element el19 = new Element(Integer.parseInt(element5string[2]));
	           Element el20 = new Element(Integer.parseInt(element5string[3]));
	           Element[] arrayEl5 = { el17, el18, el19, el20 } ;
	           
	           
	           Segment seg4 = new Segment(arrayEl4);
	           Segment seg5 = new Segment(arrayEl5);
	           Segment seg6 = new Segment(arrayEl5);
	           Segment seg7 = new Segment(arrayEl5);
	           Segment[] arraySeg2 = { seg4, seg5, seg6, seg7};
	           Row baris2 = new Row(arraySeg2);
	           
	           
	           // baris 3          
	           Segment seg8 = new Segment(arrayEl5);
	           Segment seg9 = new Segment(arrayEl5);
	           Segment seg10 = new Segment(arrayEl5);
	           Segment seg11 = new Segment(arrayEl5);
	           Segment[] arraySeg3 = { seg8, seg9, seg10, seg11};
	           Row baris3 = new Row(arraySeg3);
	           
	           Row[] rowPartitur = { baris1, baris2, baris3};
	           Partiture partiturKendang = new Partiture(rowPartitur);
	           partitur[4] = partiturKendang;
	           
	         ///  ambil partitur kendang  baris 1 segment 3 element 4 -----> d = 3
	           Row[] a = partitur[4].getRows();
	           Segment[] b = a[0].getSegments();
	           Element[] c = b[2].getElements();
	           System.out.println(c[3].value);
	           
	           
	           
	           
	           
	           br.close();
		 }catch(Exception e){}
	}
	

	public Partiture[] partiturReader () {
		
		
		
		return null;
	}
	*/
}
