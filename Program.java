import java.util.*;

class Program {
	DeclSeq ds;
	StmtSeq ss;
	
	void parse() {
		Parser.expectedToken(Core.PROGRAM);
		Parser.scanner.nextToken();
		Parser.expectedToken(Core.LBRACE);
		Parser.scanner.nextToken();
		if (Parser.scanner.currentToken() != Core.BEGIN) {
			ds = new DeclSeq();
			ds.parse();
		}
		Parser.expectedToken(Core.BEGIN);
		Parser.scanner.nextToken();
		Parser.expectedToken(Core.LBRACE);
		Parser.scanner.nextToken();
		ss = new StmtSeq();
		ss.parse();
		Parser.expectedToken(Core.RBRACE);
		Parser.scanner.nextToken();
		Parser.expectedToken(Core.RBRACE);
		Parser.scanner.nextToken();
		Parser.expectedToken(Core.EOS);
	}
	
	void print() {
		System.out.println("program {");
		if (ds != null) {
			ds.print(1);
		}
		System.out.println("begin {");
		ss.print(1);
		System.out.println("} }");
	}
	
	void execute(String dataFileName) {
		Executor.initialize(dataFileName);
		if (ds != null) {
			ds.execute();
		}
		Executor.pushLocalScope();
		ss.execute();
		HashMap<String, CoreVar> top = Executor.globalSpace;
        for (Map.Entry<String, CoreVar> entry : top.entrySet()) {
            if (entry.getValue().type == Core.REF && entry.getValue().value != null) {
                int heapIndex = entry.getValue().value;
                Executor.refCountList.set(heapIndex, Executor.refCountList.get(heapIndex) - 1);
				if (Executor.refCountList.get(heapIndex) == 0) {
					System.out.println("gc:" + Executor.totalRefs());
				}
                
            }
        }
		Executor.popLocalScope();
	}
}