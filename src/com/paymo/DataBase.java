package com.paymo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;

public class DataBase {
	private final String m_inputDir;
	private final String m_outputDir;
	private final String m_inputFile = "batch_payment.txt";
	private final String m_outputFileName1 = "output1.txt";
	private final String m_outputFileName2 = "output2.txt";
	private final String m_outputFileName3 = "output3.txt";
	private final BufferedWriter m_outputWriter1;
	private final BufferedWriter m_outputWriter2;
	private final BufferedWriter m_outputWriter3;	
	private final HashMap<Integer, User> m_dataBase;
	
	
	public DataBase(String inputDir, String outputDir) throws IOException {
		m_inputDir = inputDir;
		m_outputDir = outputDir;
		
		m_dataBase = new HashMap<Integer, User>();
		
		String runDir = System.getProperty("user.dir");
		
		File outputFile1 = new File(runDir + m_outputDir + "/" + m_outputFileName1);
		File outputFile2 = new File(runDir + m_outputDir + "/" + m_outputFileName2);
		File outputFile3 = new File(runDir + m_outputDir + "/" + m_outputFileName3);
		
		m_outputWriter1 = new BufferedWriter(new FileWriter(outputFile1));
		m_outputWriter2 = new BufferedWriter(new FileWriter(outputFile2));
		m_outputWriter3 = new BufferedWriter(new FileWriter(outputFile3));
	}
	
	public void finalize() {
		try {
			m_outputWriter1.close();
			m_outputWriter2.close();
			m_outputWriter3.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void initialaize() throws FileNotFoundException, ParseException {
		String runDir = System.getProperty("user.dir");

		File batchFile = new File(runDir + m_inputDir + "/" + m_inputFile);
		
		Scanner fileScanner = new Scanner(batchFile).useDelimiter("[|\n]");
		
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
			
			updateUserPaymentHistory(userIdPay, userIdReceive, amount, paymentDate, message);
		}
				
		fileScanner.close();
	}

	public void updateUserPaymentHistory(Integer userPayId, Integer userReceiveId, BigDecimal amount, Date paymentDate, String message) {
		User userPay = m_dataBase.get(userPayId);
		if (userPay == null) {
			userPay = new User(userPayId);
			m_dataBase.put(userPayId,  userPay);
		}
		
		User userReceive  = m_dataBase.get(userReceiveId);
		if (userReceive == null) {
			userReceive = new User(userReceiveId);
			m_dataBase.put(userReceiveId,  userReceive);
		}
		
		Payment payment = new Payment(userPay, userReceive, paymentDate, amount, message);
		
		userPay.makePayment(userReceive, payment);
		userReceive.receivePayment(userPay, payment);
	}
	
	public void processPayment(Integer userPayId, Integer userReceiveId, BigDecimal amount, Date paymentDate, String message) throws IOException {
		User userPay = m_dataBase.get(userPayId);
		User userReceive  = m_dataBase.get(userReceiveId);
		

		if (userPay != null && userReceive != null) {
			Payment payment = new Payment(userPay, userReceive, paymentDate, amount, message);

			int depth = userPay.findConnection(userReceive);
			System.out.println("Is friend: " + depth);
			
			if (depth > 3) {
				m_outputWriter1.write("unverified");
				m_outputWriter2.write("unverified");
				m_outputWriter3.write("unverified");
			} else if (depth > 2) {
				m_outputWriter1.write("unverified");
				m_outputWriter2.write("unverified");
				m_outputWriter3.write("trusted");				
			} else if (depth > 0) {
				m_outputWriter1.write("unverified");
				m_outputWriter2.write("trusted");
				m_outputWriter3.write("trusted");								
			} else {
				m_outputWriter1.write("trusted");
				m_outputWriter2.write("trusted");
				m_outputWriter3.write("trusted");												
			}
			
			m_outputWriter1.flush();
			m_outputWriter2.flush();
			m_outputWriter3.flush();
			
		} else {
			System.out.println("Error: unknown user");
		}
	}	
}
