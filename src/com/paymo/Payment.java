package com.paymo;

import java.math.BigDecimal;
import java.util.Date;

public class Payment {
	final User m_userPay;
	final User m_userReceive;
	final Date m_paymentDate;
	final BigDecimal m_amount;
	final String m_message;
	
	public Payment(User userPay, User userReceive, Date paymentDate, BigDecimal amount, String message) {
		m_userPay = userPay;
		m_userReceive = userReceive;
		m_paymentDate = paymentDate;
		m_amount = amount;
		m_message = message;
	}

}
