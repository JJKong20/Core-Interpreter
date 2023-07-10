import java.util.*;
class FuncCall implements Stmt{
    Formals formals;
    String id;
    public void parse() {
        Parser.scanner.nextToken();
        Parser.expectedToken(Core.ID);
        id = Parser.scanner.getID();
        Parser.scanner.nextToken();
        Parser.expectedToken(Core.LPAREN);
        Parser.scanner.nextToken();
        formals = new Formals();
        formals.parse();
        Parser.expectedToken(Core.RPAREN);
        Parser.scanner.nextToken();
        Parser.expectedToken(Core.SEMICOLON);
        Parser.scanner.nextToken();
    }

    public void print(int indent) {
        for (int i=0; i<indent; i++) {
			System.out.print("\t");
		}
        System.out.print("begin " + id + "(");
        formals.print(indent);
        System.out.print(");\n");
    }

    public void execute() {
        Stack<HashMap<String, CoreVar>> stack = new Stack<>();
        HashMap<String, CoreVar> scope = new HashMap<>();
        FuncDecl func = Executor.functions.get(id);
        if (func == null) {
            System.out.println("ERROR: Function call has no target");
            System.exit(0);
        }
        ArrayList<String> formalParameters = Executor.getFormals(func.formals);
        ArrayList<String> actualParameters = Executor.getFormals(formals);
        for (int i = 0; i < actualParameters.size(); i++) {
            CoreVar newParam = new CoreVar(Core.REF);
            newParam.value = Executor.getStackOrStatic(actualParameters.get(i)).value;
            scope.put(formalParameters.get(i), newParam);
            Executor.refCountList.set(Executor.getStackOrStatic(actualParameters.get(i)).value, Executor.refCountList.get(Executor.getStackOrStatic(actualParameters.get(i)).value) + 1);
        }
        stack.push(scope);
        Executor.callStack.push(stack);
        func.stmtSeq.execute();
        stack = Executor.callStack.pop();
        HashMap<String, CoreVar> top = stack.pop();
        for (Map.Entry<String, CoreVar> entry : top.entrySet()) {
            if (entry.getValue().type == Core.REF && entry.getValue().value != null) {
                int heapIndex = entry.getValue().value;
                Executor.refCountList.set(heapIndex, Executor.refCountList.get(heapIndex) - 1);
                if (Executor.refCountList.get(heapIndex) == 0) {
                    System.out.println("gc:" + Executor.totalRefs());
                }
				
                
            }
        }
    }
}
