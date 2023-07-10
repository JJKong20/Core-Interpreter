import java.util.*;
class DeclSeq {
	Decl decl;
	DeclSeq ds;
	FuncDecl funcDecl;
	
	void parse() {
		if (Parser.scanner.currentToken() == Core.INT || Parser.scanner.currentToken() == Core.REF) {
            decl = new Decl();
            decl.parse();
        } else {
            funcDecl = new FuncDecl();
            funcDecl.parse();
        }
		if (Parser.scanner.currentToken() != Core.BEGIN) {
			ds = new DeclSeq();
			ds.parse();
		}
	}
	
	void print(int indent) {
		if (decl != null) {;
            decl.print(indent);
        } else {
            funcDecl.print(indent);
        }
		if (ds != null) {
			ds.print(indent);
		}
	}
	
	void execute() {
		if (decl != null) {
			decl.execute();
		} else {
			if (!Executor.functions.containsKey(funcDecl.id)) {
				Executor.functions.put(funcDecl.id, funcDecl);
				ArrayList<String> formalParameters = Executor.getFormals(funcDecl.formals);
				for (int i = 0; i < formalParameters.size(); i++) {
					String parameter = formalParameters.remove(0);
					if (formalParameters.contains(parameter)) {
						System.out.println("ERROR: duplicate formal parameters");
						System.exit(0);
					}
					formalParameters.add(parameter);
				}
			} else {
				System.out.println("ERROR: Function already exists");
				System.exit(0);
			}
			
		}
		
		if (ds != null) {
			ds.execute();
		}
	}
}