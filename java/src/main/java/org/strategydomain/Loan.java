// ***************************************************************************
// Copyright (c) 2023, Industrial Logic, Inc., All Rights Reserved.
//
// This code is the exclusive property of Industrial Logic, Inc. It may ONLY be
// used by students during Industrial Logic's workshops or by individuals
// who are being coached by Industrial Logic on a project.
//
// This code may NOT be copied or used for any other purpose without the prior
// written consent of Industrial Logic, Inc.
// ****************************************************************************


//$CopyrightHeader()$

package org.strategydomain;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class Loan {
	private static final int MILLIS_PER_DAY = 86400000;
	private static final int DAYS_PER_YEAR = 365;
	
	private double commitment;
	private double outstanding;
	private Date start;
	private Date maturity;
	private Date expiry;
	private Date today;
	private int riskRating;
	private double unusedPercentage;
	private List<Payment> payments = new ArrayList<Payment>();
	
	
	public class Payment {
		private double amount;
		private Date date;
		
		Payment(double amount, Date date) {
			this.amount = amount;
			this.date = date;
		}
			
		double amount() {
			return amount;
		}
		
		Date date() {
			return date;
		}
	};

	private Loan(double commitment, double outstanding, Date start, Date expiry, Date maturity, int riskRating) {
		this.commitment = commitment;
		this.outstanding = outstanding;
		this.start = start;
		this.expiry = expiry;
		this.maturity = maturity;
		this.riskRating = riskRating;
		this.unusedPercentage = 1.0;
	}

	public static Loan newTermLoan(
		double commitment, Date start, Date maturity, int riskRating) {
		return new Loan(commitment, commitment, start, null, maturity, riskRating);
	}

	public static Loan newRevolver(
		double commitment, Date start, Date expiry, int riskRating) {
		return new Loan(commitment, 0, start, expiry, null, riskRating);
	}
	
	public static Loan newAdvisedLine(
		double commitment, Date start, Date expiry, int riskRating) {
		if (riskRating > 3) return null;
		Loan advisedLine = new Loan(commitment, 0, start, expiry, null, riskRating);
		advisedLine.setUnusedPercentage(0.1);
		return advisedLine;
	}	

	public double capital() {
		if (expiry == null && maturity != null)
		   return commitment * duration() * riskFactor();
		if (expiry != null && maturity == null) {
			if (getUnusedPercentage() != 1.0)
				return commitment * getUnusedPercentage() * duration() * riskFactor();
			else	    
			return 
				  (outstandingRiskAmount() * duration() * riskFactor())
				+ (unusedRiskAmount() * duration() * unusedRiskFactor());
		}
		return 0.0;
	}

	private double outstandingRiskAmount() {
		return outstanding;
	}

	private double unusedRiskAmount() {
	   return (commitment - outstanding);
	}

	public void setOutstanding(double newOutstanding) {
		this.outstanding = newOutstanding;
	}
	
	private double yearsTo(Date endDate) {
		Date beginDate = (today == null ? start : today);
		return ((endDate.getTime() - beginDate.getTime()) / MILLIS_PER_DAY) / DAYS_PER_YEAR; 
	}

	public double duration() {
		if (expiry == null && maturity != null)
		   return weightedAverageDuration();
		else if (expiry != null && maturity == null)
		   return yearsTo(expiry);
		return 0.0;
	}
	
	private double weightedAverageDuration() {
		double duration = 0.0;
		double weightedAverage = 0.0;
		double sumOfPayments = 0.0;
		Iterator<Payment> loanPayments = payments.iterator();
		while (loanPayments.hasNext()) {
			Payment payment = (Payment)loanPayments.next();
			sumOfPayments += payment.amount();
			weightedAverage += yearsTo(payment.date()) * payment.amount();
		}
		if (commitment != 0.0)
			duration = weightedAverage / sumOfPayments;
		return duration;
	}

	private double riskFactor() {
		return RiskFactor.getFactors().forRating(riskRating);
	}

	private double unusedRiskFactor() {
		return UnusedRiskFactors.getFactors().forRating(riskRating);
	}


	public void payment(double paymentAmount, Date paymentDate) {
		Payment payment = new Payment(paymentAmount, paymentDate);
		outstanding = outstanding - payment.amount(); 
		payments.add(payment);
	}

	public double getUnusedPercentage() {
		return unusedPercentage;
	}

	private void setUnusedPercentage(double unusedPercentage) {
		this.unusedPercentage = unusedPercentage;
	}
}
