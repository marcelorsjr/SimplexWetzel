
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
	
	public Restriction(String typedInequation, int size) {
		this.typedInequation = typedInequation;
		this.size = size;
		try {
			setCoefficientsFromTypedInequation();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
		freeElement = Double.parseDouble(allCoefficients[allCoefficients.length-1]);
		int xCount = 1;
		for (int i = 0; i < coefficients.length; i++) {
			String[] portions = allCoefficients[i].split("(x)|(X)");
			
			if (allCoefficients[i].toLowerCase().equals("x")) {
				break;
			}
			
			if (portions.length > 1) {
				if (allCoefficients.length == 1) {
					throw new Exception("WRONG EXPRESSION FORMAT");
				}
			}

			if (portions[1].equals(String.valueOf(xCount)) == false) {
				xCount++;
				coefficients[xCount-1] = 0;
				i--;
				continue;
			}
			
			if (portions[0].equals("")) {
				coefficients[xCount-1] = 1;
			} else {
				coefficients[xCount-1] = Double.parseDouble(portions[0]);
			}
			
			xCount++;
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
				coefficients[portionCount] *= -1;
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
