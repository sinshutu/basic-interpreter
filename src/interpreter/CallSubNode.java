package interpreter;

import java.util.HashSet;
import java.util.Set;

public class CallSubNode extends Node {
	Node fuction;
	Node exprList;

	private CallSubNode(Environment env) {
		super.env = env;
		super.type = NodeType.STMT;
	}

	private static Set<LexicalType> firstSet = new HashSet<LexicalType>();
	static {
		firstSet.add(LexicalType.NAME);
	}

	public static Node isMatch(Environment env, LexicalUnit first) {
		if (!firstSet.contains(first.type)) {
			return null;
		}
		if (Main.debug)
			System.out.println("CallSub::");

		return new CallSubNode(env);
	}

	@Override
	public boolean Parse() throws Exception {
		LexicalUnit name = env.getInput().get();
		if (name.getType() != LexicalType.NAME) {
			System.out.println("callSub: name not found");
			env.getInput().unget(name);
			return false;
		}

		LexicalUnit lu = env.getInput().get();
		env.getInput().unget(lu);
		Node exprList = ExprListNode.isMatch(env, lu);
		if (exprList == null || !exprList.Parse()) {
			System.out.println("callSub: exprList error");
			return false;
		}
		this.exprList = exprList;
		this.node_s = name.getValue() + "[" + exprList + "]";
		return true;
	}

	@Override
	public Value getValue() throws Exception {
		// PRINT
		System.out.println(this.exprList.getValue());
		return null;
	}
}
