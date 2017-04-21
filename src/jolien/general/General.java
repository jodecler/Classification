package jolien.general;

import java.util.ArrayList;

import jolien.csv.CsvParser;

public class General {
	private static CsvParser csvParser;
	private static final String FILENAME = "/Users/joliendeclerck/Documents/THESIS/jolien_java_fixers.csv";
	
	public General() {
		csvParser = new CsvParser();
	}

	public static void main(String[] args) {
		csvParser = new CsvParser();
		ArrayList data = getDataFromFile(FILENAME);
		System.out.println(data);
		writeToFile(FILENAME, data);
		data = getDataFromFile(FILENAME);
		System.out.println(data);
		
	}
	
	public static ArrayList getDataFromFile(String filename){
		return csvParser.readFileToArrayList(filename);
	}
	
	public static void writeToFile(String filename, ArrayList data){
		csvParser.writeToFile(data, filename);
	}
	
}
