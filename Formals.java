class Formals {
    Formals formals;
    String id;
    public void parse() {
        Parser.expectedToken(Core.ID);
        id = Parser.scanner.getID();
        Parser.scanner.nextToken();
        if (Parser.scanner.currentToken() == Core.COMMA) {
            Parser.scanner.nextToken();
            formals = new Formals();
            formals.parse();
        }
    }

    public void print(int indent) {
        System.out.print(id);
        if (formals != null) {
            System.out.print(", ");
            formals.print(indent);
        }
    }
}