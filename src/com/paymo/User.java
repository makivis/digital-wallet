package com.paymo;

import java.util.ArrayList;
import java.util.HashMap;

public class User {
	public final static int MAX_DEPTH = 3;
	private final Integer m_userId;
	private final HashMap<User, ArrayList<Payment>> m_friends;
	
	
	public User(Integer userId) {
		m_userId = userId;
		m_friends = new HashMap<User, ArrayList<Payment>>();
	}
	
	public Integer getUserId() {
		return m_userId;
	}
	
	@Override
	public boolean equals(Object object) {
		if (object == null)
			return false;
		
		if ( !(object instanceof User) )
			return false;

		User user = (User)object;
		
		return this.m_userId == user.m_userId ? true : false;
	}
	
	@Override
	public String toString() {
		return "[" + m_userId + "]";
	}
	
	public void makePayment(User receiver, Payment payment) {		
		boolean freindExists = m_friends.containsKey(receiver);
		if (!freindExists) {
			ArrayList<Payment> paymentArray = new ArrayList<Payment>();
			paymentArray.add(payment);
			m_friends.put(receiver, paymentArray);
		}
	}

	public void receivePayment(User payer, Payment payment) {
		boolean freindExists = m_friends.containsKey(payer);
		if (!freindExists) {
			ArrayList<Payment> paymentArray = new ArrayList<Payment>();
			paymentArray.add(payment);
			m_friends.put(payer, paymentArray);
		}
	}
	
	public int findConnection(User user) {
		return findFriendDepth(user, 0);
	}
	
	private int findFriendDepth(User user, int depth) {
		System.out.println("\nDepth=" + depth + " CU=" + this + "RU=" + user);

		if (depth > MAX_DEPTH) 
			return Integer.MAX_VALUE;
		
		boolean freindExists = m_friends.containsKey(user); 
		
		int foundDepth = Integer.MAX_VALUE;
		
		if (freindExists)
			foundDepth = depth;
		else {
			for (User entry : m_friends.keySet()) {
				System.out.print("CU=" + this + " friend with " + entry);
				foundDepth = entry.findFriendDepth(user, depth + 1);
				if (foundDepth <= MAX_DEPTH) {
					System.out.println("Return depth: " + foundDepth);
					break;
				}
			}
			System.out.println(" foundDepth=" + foundDepth);			
		}
		
		return foundDepth;
	}	
}
