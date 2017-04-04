
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		ObjectiveFunction of = new ObjectiveFunction("MIN x1 + 2x2");
		Restriction[] r = new Restriction[3];
		r[0] = new Restriction("8x1 + 2x2 >= 16", 2);
		r[1] = new Restriction("x1 + x2 <= 6", 2);
		r[2] = new Restriction("2x1 + 7x2 >= 28", 2);
		
		SimplexWetzel simplex = new SimplexWetzel(r, of);
		simplex.solve();

	}

}
