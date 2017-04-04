
public class ObjectiveFunction {
    
    public enum Type {
        MINIMIZATION, MAXIMIZATION;
    }
	
	private String typedObjectiveFunction;
	private double[] coefficients;
	private double freeElement;
	private Type type;
	
	
	public ObjectiveFunction(String typedObjectiveFunction) {
		this.typedObjectiveFunction = typedObjectiveFunction;
		this.type = type;
		try {
			setType();
			setCoefficientsFromTypedObjectiveFunction();
			setExpressionSignals();
			handleObjectiveFunctionToSimplex();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

	private void setType() throws Exception {
		String[] functionType = typedObjectiveFunction.split(" ");
		if (functionType[0].toLowerCase().equals("max")) {
			this.type = Type.MAXIMIZATION;
			typedObjectiveFunction = typedObjectiveFunction.toLowerCase().replace("max ", "");
		} else if (functionType[0].toLowerCase().equals("min")) {
			this.type = Type.MINIMIZATION;
			typedObjectiveFunction = typedObjectiveFunction.toLowerCase().replace("min ", "");
		} else {
			 throw new Exception("FUNCTION TYPE (MAX/MIN) NOT FOUND");
		}
		
	}
	
	private void setCoefficientsFromTypedObjectiveFunction() throws Exception {
		String function = typedObjectiveFunction.replace(" ", "");
		String[] allCoefficients = function.split("(\\-)|(\\+)");
		coefficients = new double[allCoefficients.length];
		freeElement = 0;
		for (int i = 0; i < allCoefficients.length; i++) {
			String[] portions = allCoefficients[i].split("(x)|(X)");
			if (portions.length > 1) {
				if (allCoefficients.length == 1) {
					throw new Exception("WRONG EXPRESSION FORMAT");
				} else if (portions[0].equals("")) {
					coefficients[i] = 1;
				} else {
					coefficients[i] = Double.parseDouble(portions[0]);
				}
			} else {
				try {
					freeElement = Double.parseDouble(portions[0]);
				} catch (NumberFormatException e) {
					throw new Exception("WRONG EXPRESSION FORMAT");
				}
			}
		}
	}
	
	private void setExpressionSignals() {
		int portionCount = 0;
		
		String function = typedObjectiveFunction.replaceAll("\\s{2,}", " ").trim();
		String[] portions = function.split(" ");
		for (int i = 0; i < portions.length; i++) {
			if (portions[i].equals("+")) {
				portionCount++;
			} else if (portions[i].equals("-")) {
				coefficients[portionCount] *= -1;
				portionCount++;
			}
		}
		
	}
	
	private void handleObjectiveFunctionToSimplex() {
		if (type == Type.MINIMIZATION) {
			for (int i = 0; i < coefficients.length; i++) {
				coefficients[i] *= -1;
			}
		}
	}

	public String getTypedObjectiveFunction() {
		return typedObjectiveFunction;
	}

	public void setTypedObjectiveFunction(String typedObjectiveFunction) {
		this.typedObjectiveFunction = typedObjectiveFunction;
	}

	public double[] getCoefficients() {
		return coefficients;
	}

	public void setCoefficients(double[] coefficient) {
		this.coefficients = coefficient;
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
