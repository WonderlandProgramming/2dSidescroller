package parser.file;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class FreeFormatFileReader {
	public static String readAllLines(String path){
		StringBuilder b = new StringBuilder();
		try {
			BufferedReader br = new BufferedReader(new FileReader("res/" + path));
			String readline;
			while((readline = br.readLine()) != null){
				b.append(readline + "\n");
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return b.toString();
	}
}
