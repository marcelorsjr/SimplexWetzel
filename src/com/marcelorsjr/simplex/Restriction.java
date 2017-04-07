package com.marcelorsjr.simplex;


import java.util.Arrays;


public class Restriction {
    
    public enum Type {
        LESS_THAN_EQUAL, GREATER_THAN_EQUAL;
    }
	
	private String typedInequation;
	private String equivalentEquation;
	private double[] coefficients;
	private double freeElement;
	private Type type;
	private int size;
	
	public Restriction(String typedInequation, int size) throws Exception {
		this.typedInequation = typedInequation;
		this.size = size;
		setCoefficientsFromTypedInequation();

		setExpressionSignals();
		handleRestrictionToSimplex();
	}
	
	private void setCoefficientsFromTypedInequation() throws Exception {

		if (typedInequation.contains("<=")) {
			equivalentEquation = typedInequation.replace("<=", "+ x =");
			type = Type.LESS_THAN_EQUAL;
		} else if (typedInequation.contains(">=")) {
			equivalentEquation = typedInequation.replace(">=", "- x =");
			type = Type.GREATER_THAN_EQUAL;
		} else {
			throw new Exception("WRONG EXPRESSION FORMAT");
		}
		String function = equivalentEquation.replace(" ", "");
		String[] allCoefficients = function.split("(\\-)|(\\+)|(=)");
		coefficients = new double[size];
		freeElement = 0;

		Arrays.fill(coefficients, 0);
		
		for (int i = 0; i < allCoefficients.length; i++) {
			
			String[] portions = allCoefficients[i].split("(x)|(X)");
			
			try {

				freeElement += Double.parseDouble(allCoefficients[i]);
				//System.out.println(allCoefficients[i]);
				//System.out.println(freeElement);
			} catch(NumberFormatException e) {
				if (allCoefficients[i].toLowerCase().equals("x")) {
					continue;
				}
				
				if (portions.length > 1) {
					if (allCoefficients.length == 1) {
						throw new Exception("WRONG EXPRESSION FORMAT");
					}
				} else {
					throw new Exception("WRONG EXPRESSION FORMAT");
				}
				
				if (portions[0].equals("")) {
					coefficients[Integer.valueOf(portions[1])-1] += 1;

				} else {
					coefficients[Integer.valueOf(portions[1])-1] += Double.parseDouble(portions[0]);
				}

			}
			
			
		}

	}
	
	private void setExpressionSignals() {
		int portionCount = 1;

		String function = equivalentEquation.replaceAll("\\s{2,}", " ").trim();
		String[] portions = function.split(" ");
		for (int i = 0; i < portions.length; i++) {
			if (coefficients.length == portionCount) {
				break;
			}
			
			if (portions[i].equals("+")) {
				portionCount++;
			} else if (portions[i].equals("-")) {
				if (coefficients[portionCount] == 0) {
					portionCount++;
					continue;
				}
				if (!portions[i+1].toLowerCase().equals("x")) {
					coefficients[portionCount] *= -1;
				}
				portionCount++;
			}
		}
		
	}
	
	private void handleRestrictionToSimplex() {
		if (type == Type.GREATER_THAN_EQUAL) {
			freeElement *= -1;
			for (int i = 0; i < coefficients.length; i++) {
				coefficients[i] *= -1;
			}
		}
	}
	
	public double solveEquationWithBasicVariablesValues(double values[]) {
		
		double result = 0.0;

		result = freeElement;


		
		for (int i = 0; i < coefficients.length; i++) {
			result = result - coefficients[i]*values[i];
		}
		
		
		return result;
	}

	public String getTypedInequation() {
		return typedInequation;
	}

	public void setTypedInequation(String typedInequation) {
		this.typedInequation = typedInequation;
	}

	public String getEquivalentEquation() {
		return equivalentEquation;
	}

	public void setEquivalentEquation(String equivalentEquation) {
		this.equivalentEquation = equivalentEquation;
	}

	public double[] getCoefficients() {
		return coefficients;
	}

	public void setCoefficients(double[] coefficients) {
		this.coefficients = coefficients;
	}

	public double getFreeElement() {
		return freeElement;
	}

	public void setFreeElement(double freeElement) {
		this.freeElement = freeElement;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

}