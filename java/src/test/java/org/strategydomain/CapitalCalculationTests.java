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

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

import org.junit.jupiter.api.Test;

public class CapitalCalculationTests {
	private static double LOAN_AMOUNT = 3000.0;
	private static double PENNY_PRECISION = 0.01;
	private static int HIGH_RISK_RATING = 4;
	private static int LOW_RISK_RATING = 1;

	private Date november(int day, int year) {
		java.util.Calendar calendar = java.util.Calendar.getInstance();
		calendar.set(year, java.util.Calendar.NOVEMBER, day);
		return calendar.getTime();
	}

	@Test
	public void termLoanSamePayments() {
		Date start = november(20, 2003);
		Date maturity = november(20, 2006);
		Loan termLoan = Loan.newTermLoan(LOAN_AMOUNT, start, maturity, HIGH_RISK_RATING);
		termLoan.payment(1000.00, november(20, 2004));
		termLoan.payment(1000.00, november(20, 2005));
		termLoan.payment(1000.00, november(20, 2006));
		assertEquals(2.0, termLoan.duration(), PENNY_PRECISION, "duration");
		assertEquals(210.00, termLoan.capital(), PENNY_PRECISION, "capital");
	}

	@Test
	public void termLoanDifferentPayments() {
		Date start = november(20, 2003);
		Date maturity = november(20, 2006);
		Loan termLoan = Loan.newTermLoan(LOAN_AMOUNT, start, maturity, HIGH_RISK_RATING);
		termLoan.payment(500.00, november(20, 2004));
		termLoan.payment(1500.00, november(20, 2005));
		termLoan.payment(1000.00, november(20, 2006));
		assertEquals(2.16, termLoan.duration(), PENNY_PRECISION, "duration");
		assertEquals(227.50, termLoan.capital(), PENNY_PRECISION, "capital");
	}

	@Test
	public void revolverNoPayments() {
		Date start = november(20, 2003);
		Date expiry = november(20, 2010);
		Loan revolver = Loan.newRevolver(LOAN_AMOUNT, start, expiry, HIGH_RISK_RATING);
		assertEquals(7.0, revolver.duration(), PENNY_PRECISION, "duration");
		assertEquals(609.00, revolver.capital(), PENNY_PRECISION, "capital");
	}

	@Test
	public void revolverWithPayments() {
		Date start = november(20, 2003);
		Date expiry = november(20, 2010);
		Loan revolver = Loan.newRevolver(LOAN_AMOUNT, start, expiry, HIGH_RISK_RATING);
		revolver.setOutstanding(1000.00);
		assertEquals(7.0, revolver.duration(), PENNY_PRECISION, "duration");
		assertEquals(651.00, revolver.capital(), PENNY_PRECISION, "capital");
	}

	@Test
	public void advisedLineMustHaveLowRiskRating() {
		Date start = november(20, 2003);
		Date expiry = november(20, 2010);
		Loan advisedLine = Loan.newAdvisedLine(LOAN_AMOUNT, start, expiry, HIGH_RISK_RATING);
		assertNull(advisedLine, "null loan because not at right risk level");
	}

	@Test
	public void advisedLine() {
		Date start = november(20, 2003);
		Date expiry = november(20, 2010);
		Loan advisedLine = Loan.newAdvisedLine(LOAN_AMOUNT, start, expiry, LOW_RISK_RATING);
		assertEquals(7.0, advisedLine.duration(), PENNY_PRECISION, "duration");
		assertEquals(15.75, advisedLine.capital(), PENNY_PRECISION, "capital");
	}

}
