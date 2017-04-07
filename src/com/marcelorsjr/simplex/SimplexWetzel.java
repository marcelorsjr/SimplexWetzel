package com.marcelorsjr.simplex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.DoubleStream;

import com.marcelorsjr.simplex.ObjectiveFunction.Type;


public class SimplexWetzel {
	
	public enum SolutionResponse {
		OPTIMAL_SOLUTION, UNLIMITED_SOLUTION, PERMISSIVE_SOLUTION_DOES_NOT_EXIST
	}
	
	private Table table;
	Restriction[] restrictions;
	ObjectiveFunction of;
	SolutionResponse solutionResponse;
	
	public SimplexWetzel(Restriction[] restrictions, ObjectiveFunction of) {
		this.restrictions = restrictions;
		this.of = of;
		table = new Table(restrictions.length, of.getCoefficients().length);
		
	}
	
	public void printSolution() {		
		

	
		if (of.getType() == ObjectiveFunction.Type.MAXIMIZATION) {
			System.out.println("FO(x) -> MAX Z = "+Math.abs(table.cells[0][0].topSubcell.getValue()));
		} else {
			System.out.println("FO(x) -> MIN Z = "+Math.abs(table.cells[0][0].topSubcell.getValue()));
		}
		

		double results[] = getResultsForObjectiveFunction();
		
		for (int i = 0; i < results.length; i++)
			System.out.println("x"+(i+1)+" = "+results[i]);
		
		System.out.println();
		
		for (int i = 0 ; i<restrictions.length; i++) 
			System.out.println("x"+(i+1+results.length)+" = "+restrictions[i].solveEquationWithBasicVariablesValues(results));
	
	}
	
	public double[] solve() {
		fillFieldsWithCoefficients();
		firstPhase();
		
		table.showTable();
		table.showBasic();
		table.showNoBasic();

		double results[] = getResultsForObjectiveFunction();
		double response[] = new double[results.length + 1];
		response[0] = Math.abs(table.cells[0][0].topSubcell.getValue());
		for (int i = 1; i < response.length; i++)
			response[i] = results[i-1];
		
		return response;
		
	}
	
	void fillFieldsWithCoefficients() {
		
		table.cells[0][0].topSubcell.setValue(of.getFreeElement());
		for (int i = 1; i < table.cells.length; i++) {
			table.cells[i][0].topSubcell.setValue(restrictions[i-1].getFreeElement());
		}
		
		for (int j = 0; j < of.getCoefficients().length; j++) {
			table.cells[0][j+1].topSubcell.setValue(of.getCoefficients()[j]);
		}
		
		for (int i = 1; i <= restrictions.length; i++) {
			for (int j = 1; j <= restrictions[0].getCoefficients().length; j++) {
				table.cells[i][j].topSubcell.setValue(restrictions[i-1].getCoefficients()[j-1]);
			}
		}
	}
	
	SolutionResponse firstPhase() {
		int col = 1;
		int row = 1;
		
		while (row < table.cells.length && col < table.cells[0].length) {
			if (table.cells[row][0].topSubcell.getValue() < 0) {
				if (table.cells[row][col].topSubcell.getValue() < 0) {
					break;
				} else {
					col++;
				}
			} else {
				col = 1;
				row++;
			}
			
		
		}
		

		
		if (row == table.cells.length) {
			return secondPhase();
		} else {
			
			if (col == table.cells[row].length) {
				System.out.println("Solução permissiva não existe.");
				solutionResponse = SolutionResponse.PERMISSIVE_SOLUTION_DOES_NOT_EXIST;
				return SolutionResponse.PERMISSIVE_SOLUTION_DOES_NOT_EXIST;
			}
			
			table.selectedCol = col;
			
			double division = 0;
			double smallerDivision = Double.MAX_VALUE;
			
			for (int i = 1; i < table.cells.length; i++) {
					if (table.cells[i][col].topSubcell.getValue() != 0) {

						if (table.cells[i][0].topSubcell.getValue() >= 0 && table.cells[i][col].topSubcell.getValue() > 0) {
							division = table.cells[i][0].topSubcell.getValue()/table.cells[i][col].topSubcell.getValue();
						} else if (table.cells[i][0].topSubcell.getValue() <= 0 && table.cells[i][col].topSubcell.getValue() < 0) {
							division = table.cells[i][0].topSubcell.getValue()/table.cells[i][col].topSubcell.getValue();
						} else {
							division = Double.MAX_VALUE;
						}
						
						if (division < smallerDivision) {
							smallerDivision = division;
							table.selectedRow = i;
						}
						
					
				}
				
				
			}
			return swap();
			
		}
		
		
		
	}
	
	private SolutionResponse swap() {
		
		double inverseElement = 1 / table.cells[table.selectedRow][table.selectedCol].topSubcell.getValue();
		table.cells[table.selectedRow][table.selectedCol].bottomSubcell.setValue(inverseElement);
		table.cells[table.selectedRow][table.selectedCol].bottomSubcell.setValue(inverseElement);
		for (int i = 0; i < table.cells[0].length; i++) {
			if (i == table.selectedCol)
				continue;
			double topElement = table.cells[table.selectedRow][i].topSubcell.getValue();
			table.cells[table.selectedRow][i].bottomSubcell.setValue(topElement*inverseElement);
		}
		
		for (int i = 0; i < table.cells.length; i++) {
			if (i == table.selectedRow)
				continue;
			double topElement = table.cells[i][table.selectedCol].topSubcell.getValue();
			table.cells[i][table.selectedCol].bottomSubcell.setValue(topElement*(-inverseElement));
		}
		
		for (int i = 0; i < table.cells.length; i++) {
			for (int j = 0; j < table.cells[0].length; j++) {
				if (i != table.selectedRow && j != table.selectedCol) {
					
					double elemCol = table.cells[table.selectedRow][j].topSubcell.getValue();
					double elemRow = table.cells[i][table.selectedCol].bottomSubcell.getValue();
					
					table.cells[i][j].bottomSubcell.setValue(elemCol*elemRow);
				}
			}
		}
		
		Table table2 = new Table(restrictions.length, of.getCoefficients().length);
		
		table2.basicVariables = table.basicVariables;
		table2.nonBasicVariables = table.nonBasicVariables;
		
		int swap = table2.basicVariables[table.selectedRow-1];
		table2.basicVariables[table.selectedRow-1] = table2.nonBasicVariables[table.selectedCol-1];
		table2.nonBasicVariables[table.selectedCol-1] = swap;
		
		for (int i = 0; i < table.cells.length; i++) {
			for (int j = 0; j < table.cells[0].length; j++) {
				double bottomSubCell = table.cells[i][j].bottomSubcell.getValue();
				if (i != table.selectedRow && j != table.selectedCol) {
					
					double topSubCell = table.cells[i][j].topSubcell.getValue();
					table2.cells[i][j].topSubcell.setValue(topSubCell+bottomSubCell);
				} else {
					table2.cells[i][j].topSubcell.setValue(bottomSubCell);
				}
			}
		}
		
		table = table2;
		return firstPhase();
		
		
	}
	
	private SolutionResponse secondPhase() {
		int row;
		int col;
		for (col = 1; col < table.cells[0].length; col++) {
			if (table.cells[0][col].topSubcell.getValue() > 0) {
				break;
			}
		}
		
		if (col == table.cells[0].length) {
			System.out.println("********** OPTIMAL SOLUTION FOUND **********\n");
			solutionResponse = SolutionResponse.OPTIMAL_SOLUTION;
			return SolutionResponse.OPTIMAL_SOLUTION;
		}
		
		table.selectedCol = col;
		for (row = 1; row < table.cells.length; row++) {
			if (table.cells[row][col].topSubcell.getValue() > 0) {
				break;
			}
		}
		
		if (row == table.cells.length) {
			System.out.println("********** UNLIMITED SOLUTION **********\n");
			solutionResponse = SolutionResponse.UNLIMITED_SOLUTION;
			return SolutionResponse.UNLIMITED_SOLUTION;
		}
		
		double division = 0;
		double smallerDivision = Double.MAX_VALUE;
		
		for (int i = 1; i < table.cells.length; i++) {
				if (table.cells[i][col].topSubcell.getValue() != 0) {
					if (table.cells[i][0].topSubcell.getValue() >= 0 && table.cells[i][col].topSubcell.getValue() > 0) {
						division = table.cells[i][0].topSubcell.getValue()/table.cells[i][col].topSubcell.getValue();
					} else if (table.cells[i][0].topSubcell.getValue() <= 0 && table.cells[i][col].topSubcell.getValue() < 0) {
						division = table.cells[i][0].topSubcell.getValue()/table.cells[i][col].topSubcell.getValue();
					} else {
						continue;
					}
					
					if (division < smallerDivision) {
						smallerDivision = division;
						table.selectedRow = i;
					}
					
				}
			
		}

		return swap();	
		
	}
	
	double[] getResultsForObjectiveFunction() {
		
		
		double results[] = new double[of.getCoefficients().length];
		for (int i = 0; i < of.getCoefficients().length; i++) {
			results[i] = 0;
		}
		for (int i = 0; i < table.basicVariables.length; i++) {
			if (table.basicVariables[i] <= of.getCoefficients().length) {
				results[table.basicVariables[i]-1] = table.cells[i+1][0].topSubcell.getValue();
			}
		}
		
		return results;
	}
	

}
