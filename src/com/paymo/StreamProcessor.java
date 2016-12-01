package com.paymo;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class StreamProcessor {
	private final DataBase m_db;
	private final String m_inputDir;
	
	public StreamProcessor(String inputDir, DataBase db) {
		m_db = db;
		m_inputDir = inputDir;
	}
	
	public void process() throws ParseException, IOException {
		String runDir = System.getProperty("user.dir");

		File streamFile = new File(runDir + m_inputDir + "stream_payment.txt");
		Scanner fileScanner = new Scanner(streamFile).useDelimiter("[|\n]");
		
		if (!fileScanner.hasNextLine()) {
			System.out.println("Error: Bad input file format");
			fileScanner.close();
			return;
		}

		String header = fileScanner.next();

		inputFileParser fileParser = new inputFileParser(header);
		DateFormat df = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");

		while (fileScanner.hasNextLine()) {
			String str = fileScanner.next();

			fileParser.processLine(str);
			
			Integer userIdPay = new Integer(fileParser.get("id1"));
			Integer userIdReceive = new Integer(fileParser.get("id2"));
			Date paymentDate = df.parse(fileParser.get("time"));
			BigDecimal amount = new BigDecimal(fileParser.get("amount"));
			String message = fileParser.get("message");
			
			m_db.processPayment(userIdPay, userIdReceive, amount, paymentDate, message); 
		}
		
		
		fileScanner.close();
		
		
	}

}
