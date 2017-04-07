package com.marcelorsjr.simplex.integer;

import com.marcelorsjr.simplex.Restriction;
import com.marcelorsjr.simplex.ObjectiveFunction.Type;

public class Node {
	
	public enum PrumedReason {
		INFEASIBLE, OPTIMALITY, QUALITY
	}
	
	public Restriction[] restrictions;

	public Node right;
	public Node left;
	public double solution;
	public double results[];
	public boolean prumed;
	public PrumedReason prumedReason;
	
	public Node() {
		this.right = null;
		this.left = null;
		this.restrictions = null;
		prumedReason = null;
		solution = Double.MIN_VALUE;
	}
	
	public Node(Restriction[] restrictions) {
		this.right = null;
		this.left = null;
		this.restrictions = restrictions.clone();
		prumedReason = null;
		solution = Double.MIN_VALUE;
	}
	
	
}
