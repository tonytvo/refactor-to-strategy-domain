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

public class UnusedRiskFactors {

	private static UnusedRiskFactors rf = new UnusedRiskFactors();
	protected static double[] factors = { 0.00, 0.0012, 0.019, 0.023, 0.029 };

	static UnusedRiskFactors getFactors() {
		return rf;
	}

	public double forRating(int customerRating) {
		return factors[customerRating];
	}

	private UnusedRiskFactors() {
	}
}

