import java.util.*;

class CoreVar {
	Core type;
	Integer value;
	
	public CoreVar(Core varType) {
		type = varType;
		if (type == Core.INT) {
			value = 0;
		} else {
			value = null;
		}
	}
}

class Executor {
	
	static HashMap<String, CoreVar> globalSpace;
	static Stack<HashMap<String, CoreVar>> stackSpace;
	static ArrayList<Integer> heapSpace;
	static Stack<Stack<HashMap<String, CoreVar>>> callStack;
	static HashMap<String, FuncDecl> functions;
	static ArrayList<Integer> refCountList;
	
	static Scanner dataFile;
	
	static void initialize(String dataFileName) {
		globalSpace = new HashMap<String, CoreVar>();
		stackSpace = new Stack<HashMap<String, CoreVar>>();
		heapSpace = new ArrayList<Integer>();
		dataFile = new Scanner(dataFileName);
		callStack = new Stack<Stack<HashMap<String, CoreVar>>>();
		callStack.push(stackSpace);
		functions = new HashMap<>();
		refCountList = new ArrayList<Integer>();
	}
	
	static void pushLocalScope() {
		callStack.peek().push(new HashMap<String, CoreVar>());
	}
	
	static void popLocalScope() {
		HashMap<String, CoreVar> top = callStack.peek().pop();
        for (Map.Entry<String, CoreVar> entry : top.entrySet()) {
            if (entry.getValue().type == Core.REF && entry.getValue().value != null) {
                int heapIndex = entry.getValue().value;
                refCountList.set(heapIndex, refCountList.get(heapIndex) - 1);
				if (refCountList.get(heapIndex) == 0) {
					System.out.println("gc:" + totalRefs());
				}
                
            }
        }
	}
	
	static int getNextData() {
		int data = 0;
		if (dataFile.currentToken() == Core.EOS) {
			System.out.println("ERROR: data file is out of values!");
			System.exit(0);
		} else {
			data = dataFile.getCONST();
			dataFile.nextToken();
		}
		return data;
	}
	
	static void allocate(String identifier, Core varType) {
		CoreVar record = new CoreVar(varType);
		// If we are in the DeclSeq, the local scope will not be created yet
		if (callStack.peek().size()==0) {
			globalSpace.put(identifier, record);
		} else {
			callStack.peek().peek().put(identifier, record);
		}
	}
	
	static CoreVar getStackOrStatic(String identifier) {
		CoreVar record = null;
		for (int i=callStack.peek().size() - 1; i>=0; i--) {
			if (callStack.peek().get(i).containsKey(identifier)) {
				record = callStack.peek().get(i).get(identifier);
				break;
			}
		}
		if (record == null) {
			record = globalSpace.get(identifier);
		}
		return record;
	}
	
	static void heapAllocate(String identifier) {
		CoreVar x = getStackOrStatic(identifier);
		if (x.type != Core.REF) {
			System.out.println("ERROR: " + identifier + " is not of type ref, cannot perform \"new\"-assign!");
			System.exit(0);
		}
		x.value = heapSpace.size();
		heapSpace.add(null);
		refCountList.add(1);
		System.out.println("gc:" + totalRefs());
	}
	
	static Core getType(String identifier) {
		CoreVar x = getStackOrStatic(identifier);
		return x.type;
	}
	
	static Integer getValue(String identifier) {
		CoreVar x = getStackOrStatic(identifier);
		Integer value = x.value;
		if (x.type == Core.REF) {
			try {
				value = heapSpace.get(value);
			} catch (Exception e) {
				System.out.println("ERROR: invalid heap read attempted!");
				System.exit(0);
			}
		}
		return value;
	}
	
	static void storeValue(String identifier, int value) {
		CoreVar x = getStackOrStatic(identifier);
		if (x.type == Core.REF) {
			try {
				heapSpace.set(x.value, value);
			} catch (Exception e) {
				System.out.println("ERROR: invalid heap write attempted!");
				System.exit(0);
			}
		} else {
			x.value = value;
		}
	}
	
	static void referenceCopy(String var1, String var2) {
		CoreVar x = getStackOrStatic(var1);
		CoreVar y = getStackOrStatic(var2);
		if (x.value != null && y.value != null) {
			refCountList.set(x.value, refCountList.get(x.value) - 1);
			refCountList.set(y.value, refCountList.get(y.value) + 1);
		} else if (x.value == null && y.value != null) {
			refCountList.set(y.value, refCountList.get(y.value) + 1);
		} else if (x.value != null && y.value == null) {
			refCountList.set(x.value, refCountList.get(x.value) - 1);
			if (refCountList.get(x.value) == 0) {
				System.out.println("gc:" + totalRefs());
			}
			
		}
		x.value = y.value;
	}

	static ArrayList<String> getFormals(Formals formals) {
        ArrayList<String> parameters = new ArrayList<>();
        parameters.add(formals.id);
        if (formals.formals != null) {
            parameters.addAll(getFormals(formals.formals));
        }
        return parameters;

    }

	static int totalRefs() {
		int total = 0;
		for (int ref : refCountList) {
			if (ref > 0) {
				total++;
			}
		}
		return total;
	}

}