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

public class RiskFactor {
	private static RiskFactor rf = new RiskFactor();
	protected static double[] factors = { 0.0025, 0.0075, 0.0125, 0.0275, 0.0350 };

	static RiskFactor getFactors() {
		return rf;
	}

	public double forRating(int customerRating) {
		return factors[customerRating];
	}

	private RiskFactor() {
	}
}

