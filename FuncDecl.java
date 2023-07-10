class FuncDecl {
    Formals formals;
    StmtSeq stmtSeq;
    String id;
    public void parse() {
        id = Parser.scanner.getID();
        Parser.scanner.nextToken();
        Parser.expectedToken(Core.LPAREN);
        Parser.scanner.nextToken();
        Parser.expectedToken(Core.REF);
        Parser.scanner.nextToken();
        formals = new Formals();
        formals.parse();
        Parser.expectedToken(Core.RPAREN);
        Parser.scanner.nextToken();
        Parser.expectedToken(Core.LBRACE);
        Parser.scanner.nextToken();
        stmtSeq = new StmtSeq();
        stmtSeq.parse();
        Parser.expectedToken(Core.RBRACE);
        Parser.scanner.nextToken();
    }

    public void print(int indent) {
        for (int i=0; i<indent; i++) {
			System.out.print("\t");
		}
        System.out.print(id + "(ref ");
        formals.print(indent);
        System.out.print(") {\n");
        indent += 1;
        stmtSeq.print(indent);
        indent -= 1;
        for (int i=0; i<indent; i++) {
			System.out.print("\t");
		}
        System.out.print("}\n");
    }

    public void execute() {
    }
}
