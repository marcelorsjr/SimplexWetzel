package com.marcelorsjr.simplex;

import java.util.ArrayList;
import java.util.Arrays;

import com.marcelorsjr.simplex.ObjectiveFunction.Type;
import com.marcelorsjr.simplex.SimplexWetzel.SolutionResponse;
import com.marcelorsjr.simplex.integer.Node;
import com.marcelorsjr.simplex.integer.Node.PrumedReason;
import com.marcelorsjr.simplex.integer.Tree;

public class SimplexWetzelInteger {
	
	public double solution;
	public double variablesValues[];
	public Tree tree;
	private SimplexWetzel simplexWetzel;
	
	public SimplexWetzelInteger(Restriction[] restrictions, ObjectiveFunction of) throws Exception {
		
		simplexWetzel = new SimplexWetzel(restrictions, of);
		
		double results[] = simplexWetzel.solve();
		solution = results[0];
		variablesValues = Arrays.copyOfRange(results, 1, results.length);
		tree = new Tree();
	}
	
	public void printSolution() {		
		
		if (simplexWetzel.of.getType() == ObjectiveFunction.Type.MAXIMIZATION) {
			System.out.println("FO(x) -> MAX Z = "+tree.bestNode.solution);
		} else {
			System.out.println("FO(x) -> MIN Z = "+tree.bestNode.solution);
		}
		
		
		for (int i = 0; i < tree.bestNode.results.length; i++)
			System.out.println("x"+(i+1)+" = "+ tree.bestNode.results[i]);

		
	}
	private void solveForInteger(Node node, SimplexWetzel simplexWetzel) throws Exception {

		double results[] = simplexWetzel.solve();
		
		if (simplexWetzel.solutionResponse != SolutionResponse.OPTIMAL_SOLUTION) {
			node.prumed = true;
			node.prumedReason = PrumedReason.INFEASIBLE;
			//simplexWetzel.printSolution();
			return;
		}
		

		solution = results[0];


		node.solution = solution;
		double result;
		if (simplexWetzel.of.getType() == Type.MAXIMIZATION) {
			result = Double.MIN_VALUE;
		} else {
			result = Double.MAX_VALUE;
		}
		int iterator = 0;
		
		for (iterator = 1; iterator < results.length; iterator++) {
			double r = results[iterator];
			if (!((r == Math.floor(r)) && !Double.isInfinite(r))) {
				result = r;
			    break;
			}
		}
	
		
		if (result != Double.MIN_VALUE && result != Double.MAX_VALUE) {
			String leftRestriction = "x"+iterator+" <= "+ Math.floor(result);
			String rightRestriction = "x"+iterator+" >= " + Math.ceil(result);	

			ArrayList<Restriction> list = new ArrayList(Arrays.asList(simplexWetzel.restrictions));

			list.add(new Restriction(leftRestriction, simplexWetzel.of.getCoefficients().length));
			Restriction[] newRestrictions = list.toArray(new Restriction[0]);
			
			node.left = new Node(newRestrictions.clone());
			list = new ArrayList(Arrays.asList(simplexWetzel.restrictions));
			list.add(new Restriction(rightRestriction, simplexWetzel.of.getCoefficients().length));
			newRestrictions = list.toArray(new Restriction[0]);
			node.right = new Node(newRestrictions.clone());

		} else {
			node.prumed = true;
			node.prumedReason = PrumedReason.OPTIMALITY;
			node.results = results;
		
			
			if (simplexWetzel.of.getType() == Type.MAXIMIZATION) {
				if (tree.bestNode.solution < node.solution)
					tree.bestNode = node;
			} else {
				if (tree.bestNode.solution > node.solution)
					tree.bestNode = node;
			}
			
			node.left = null;
			node.right = null;
			
		}
		if (node.left != null) {
			solveForInteger(node.left, new SimplexWetzel(node.left.restrictions.clone(), simplexWetzel.of));
		}
		if (node.right != null) {
			solveForInteger(node.right, new SimplexWetzel(node.right.restrictions.clone(), simplexWetzel.of));
		}		
		
		
		

		
		
		
	}
	
	
	public void solve() throws Exception {

		int iterator = 0;
		for (iterator = 0; iterator < variablesValues.length; iterator++) {
			double r = variablesValues[iterator];
			if (!((r == Math.floor(r)) && !Double.isInfinite(r))) {
			    break;
			}
		}
		
		if (iterator != variablesValues.length) {
			iterator++;
			String leftRestriction = "x"+iterator+" <= "+ Math.floor(variablesValues[iterator-1]);
			String rightRestriction = "x"+iterator+" >= " + Math.ceil(variablesValues[iterator-1]);	

			ArrayList<Restriction> list = new ArrayList(Arrays.asList(simplexWetzel.restrictions));

			list.add(new Restriction(leftRestriction, simplexWetzel.of.getCoefficients().length));
			Restriction[] newRestrictions = list.toArray(new Restriction[0]);
			
			tree.root.left = new Node(newRestrictions.clone());
			list = new ArrayList(Arrays.asList(simplexWetzel.restrictions));
			list.add(new Restriction(rightRestriction, simplexWetzel.of.getCoefficients().length));
			newRestrictions = list.toArray(new Restriction[0]);
			tree.root.right = new Node(newRestrictions.clone());

			
			
			tree.root.restrictions = simplexWetzel.restrictions;
			solveForInteger(tree.root.left, new SimplexWetzel(tree.root.left.restrictions.clone(), simplexWetzel.of));
			solveForInteger(tree.root.right, new SimplexWetzel(tree.root.right.restrictions.clone(), simplexWetzel.of));
			

			
		} else {
			tree.bestNode.solution = solution;
			tree.bestNode.results = variablesValues.clone();
		}
		
		
	}
	
	
	
	
	

}
