package jolien;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Vector;

import clojure.lang.PersistentList;
/*
 * Class to write the classified results to a CSV-file
 */
public class WriteResults {
	public static File FILE;
	public static FileWriter FILEWRITER;

	/*
	 * Creation of CSV-file, overwritten if already exists
	 */
	public static void createFile(){
		FILE = new File("/Users/joliendeclerck/Documents/THESIS/output.csv");
		try {
			FILEWRITER = new FileWriter (FILE);
		} catch (IOException e) {
		}
	}
	/*
	 * Write string to file
	 */
	public static void writeString(String aString){
		try {
			FILEWRITER.write(aString);
		} catch (IOException e) {
		}
	}
	/*
	 * Flush buffered string to file
	 */
	public static void flushWriting(){
		try{
			FILEWRITER.flush();
		}catch (Exception e){}
		
	}
	/*
	 * Finish writing, close file
	 */
	public static void stopWriting(){
		try {
			FILEWRITER.close();
		} catch (IOException e) {
		}
	}
}

