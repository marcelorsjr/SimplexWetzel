package com.marcelorsjr.simplex;


public class Main {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

		ObjectiveFunction of = new ObjectiveFunction("MAX x1 + 2x2");
		Restriction[] r = new Restriction[2];
		r[0] = new Restriction("x1 <= 3", 2);
		r[1] = new Restriction("x2 <= 4", 2);
		
//		SimplexWetzel sw = new SimplexWetzel(r, of);
//		sw.solve();
//		sw.printSolution();
		
		SimplexWetzelInteger si = new SimplexWetzelInteger(r, of);
		si.solve();
		si.printSolution();

	}

}
