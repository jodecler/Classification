package jolien.csv;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class CsvParser {

	
	public ArrayList readFileToArrayList(String filename) {
		BufferedReader buffer = null;
		ArrayList data = new ArrayList();
		try {
			String line;
			buffer = new BufferedReader(
					new FileReader(filename));
			while ((line = buffer.readLine()) != null) {
				data.add(dataToArrayList(line));
			}
		} catch (IOException e) {
		} finally {
			try {
				if (buffer != null)
					buffer.close();
			} catch (IOException e) {
			}
		}
		return data;
	}

	
	public String makeString(ArrayList lijst){
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < lijst.size(); i ++){
			ArrayList item = (ArrayList) lijst.get(i);
			String line = "";
			for(int j = 0; j < item.size(); j ++){
				if(j != 0 ){
					line += ",";
				}
				line += item.get(j).toString();
			}
			sb.append(line);
			sb.append("\n");
		}
		return sb.toString();
	}
	
	public void writeToFile(ArrayList lijst, String filename){
		String content = makeString(lijst);
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
			bw.write(content);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<String> dataToArrayList(String line) {
		ArrayList<String> lineArray = new ArrayList<String>();

		if (line != null) {
			String[] splitData = line.split(",");
			for (int i = 0; i < splitData.length; i++) {
				if (!(splitData[i] == null) || !(splitData[i].length() == 0)) {
					lineArray.add(splitData[i].trim());
				}
			}
		}
		return lineArray;
	}
}
