package jolien;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Vector;

import clojure.lang.PersistentList;

public class WriteResults {
	public static File FILE;
	public static FileWriter FILEWRITER;
	public static void aTest(Object information){
		System.out.println("dit is de test");
		System.out.println(information.getClass().getTypeName());
		System.out.println(information instanceof PersistentList);

	}
	
	public static void createFile(){
		FILE = new File("/Users/joliendeclerck/Documents/THESIS/output.csv");
		try {
			FILEWRITER = new FileWriter (FILE);
		} catch (IOException e) {
		}
	}
	public static void writeString(String aString){
		try {
			FILEWRITER.write(aString);
		} catch (IOException e) {
		}
	}
	public static void flushWriting(){
		try{
			FILEWRITER.flush();
		}catch (Exception e){}
		
	}
	public static void stopWriting(){
		
		try {
			
			FILEWRITER.close();
		} catch (IOException e) {
		}
	}
}

