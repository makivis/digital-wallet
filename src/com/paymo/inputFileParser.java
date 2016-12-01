package com.paymo;

import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.Vector;

public class inputFileParser {
	private HashMap<String, Integer> m_fileMap; 
	private Vector<String> m_currentLine;

	public inputFileParser(String headerLine) {
		m_fileMap = new HashMap<String, Integer>();
		m_currentLine = new Vector<String>();

		StringTokenizer st = new StringTokenizer(headerLine, ",");

		Integer count = 0;
		while (st.hasMoreTokens()) {
			String nextWord = st.nextToken().trim();
//			System.out.println("[" + nextWord + "]");
			m_fileMap.put(nextWord, count);
			count++;
		}
	}

	public void processLine(String str) {
		m_currentLine.clear();
		StringTokenizer st = new StringTokenizer(str, ",");

		while (st.hasMoreTokens()) {
			String nextWord = st.nextToken().trim();
//			System.out.println("[" + nextWord + "]");
			m_currentLine.add(nextWord);
		}
	}

	public String get(String key) {
		Integer index = m_fileMap.get(key);

		String result = "";
		if (index != null) {
			result = m_currentLine.elementAt(index); 
//			System.out.println("[" + result + "]");
		} else {
			System.out.println("Error: Element not found!");				
		}

		return result;
	}
}
