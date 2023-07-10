class Main {
	public static void main(String[] args) {
		// Initialize the scanner with the input file
		Scanner S = new Scanner(args[0]);
		//Scanner S = new Scanner("./Correct/9.code");
		Parser.scanner = S;
		
		Program prog = new Program();
		
		prog.parse();
		
		//prog.print();
		
		prog.execute(args[1]);
		//prog.execute("./Correct/9.data");


		// x share y
		// when both not null, dec then inc
		// when x null, inc only, when y null, dec only
		// when both null nothing happens
		// only check to print during dec
		// check to inc/dec during assign and wherever you pop a frame
		// print number of reachable objects whenever it changes
	}
}