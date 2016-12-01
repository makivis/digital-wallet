// example of program that detects suspicious transactions
// fraud detection algorithm

import java.io.IOException;
import java.text.ParseException;

import com.paymo.*;

public class antifraud {
//	private static String m_inputDir = "\\paymo_input\\";
//	private static String m_outputDir = "\\paymo_output\\";
	private static String m_inputDir = "\\insight_testsuite\\tests\\test-5-paymo-trans\\paymo_input\\";
	private static String m_outputDir = "\\insight_testsuite\\tests\\test-5-paymo-trans\\paymo_output\\";

			
	public static void main(String args[]) {
		
		System.out.println(System.getProperty("user.dir"));
		
		if (args.length == 2) {
			m_inputDir = args[0];
			m_outputDir = args[1];
		}
		
		try {
			DataBase db = new DataBase(m_inputDir, m_outputDir);
			db.initialaize();
			
			StreamProcessor processor = new StreamProcessor(m_inputDir, db);
			processor.process();						
		} catch (ParseException | IOException e) {
			e.printStackTrace();
		}
		
	}
}