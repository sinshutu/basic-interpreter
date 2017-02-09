package interpreter;

import java.util.HashSet;
import java.util.Set;

public class SubstNode extends Node {
	LexicalUnit name;
	Node expr;

	private SubstNode(Environment env) {
		super.env = env;
		super.type = NodeType.SUBST;
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
			System.out.println("Subst::");
		return new SubstNode(env);
	}

	@Override
	public boolean Parse() throws Exception {
		// NAME
		name = env.getInput().get();
		if (name.getType() != LexicalType.NAME) {
			env.getInput().unget(name);
			return false;
		}
		// EQ
		LexicalUnit eq = env.getInput().get();
		if (eq.getType() != LexicalType.EQ) {
			env.getInput().unget(eq);
			env.getInput().unget(name);
			if (Main.debug)
				System.out.println("this is not subst");
			return false;
		}
		// expr
		LexicalUnit lu = env.getInput().get();
		env.getInput().unget(lu);
		expr = ExprNode.isMatch(env, lu);
		if (expr != null && expr.Parse()) {
			node_s = name.getValue().getSValue() + "[" + expr + "]";
			return true;
		}
		return false;

	}

	@Override
	public Value getValue() throws Exception {
		env.var_table.put(this.name.getValue().toString(), this.expr.getValue());
		// System.out.println("========================================");
		// System.out.println(env.var_table);
		// System.out.println(this.name.getValue().toString());
		// System.out.println("========================================");
		return null;
	}
}
