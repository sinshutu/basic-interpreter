package interpreter;

import java.util.HashSet;
import java.util.Set;

public class CondNode extends Node {
	Node rightExpr, leftExpr;
	LexicalUnit comparison;

	private CondNode(Environment env) {
		super.env = env;
		super.type = NodeType.PROGRAM;
	}

	private static Set<LexicalType> firstSet = new HashSet<LexicalType>();
	static {
		// expr
		firstSet.add(LexicalType.SUB);
		firstSet.add(LexicalType.LP);
		firstSet.add(LexicalType.NAME);
		firstSet.add(LexicalType.INTVAL);
		firstSet.add(LexicalType.DOUBLEVAL);
		firstSet.add(LexicalType.LITERAL);
		// call_func
		// NAME+LPがつながってる時のみ関数呼び出しにする必要がある
		firstSet.add(LexicalType.NAME);
	}
	private static Set<LexicalType> comparisonSet = new HashSet<LexicalType>();
	static {
		comparisonSet.add(LexicalType.EQ);
		comparisonSet.add(LexicalType.GT);
		comparisonSet.add(LexicalType.LT);
		comparisonSet.add(LexicalType.GE);
		comparisonSet.add(LexicalType.LE);
		comparisonSet.add(LexicalType.NE);
	}

	public static Node isMatch(Environment env, LexicalUnit first) {
		if (!firstSet.contains(first.type)) {
			return null;
		}
		if (Main.debug)
			System.out.println("CondNode::");
		return new CondNode(env);
	}

	@Override
	public boolean Parse() throws Exception {
		LexicalUnit lu = env.getInput().get();
		env.getInput().unget(lu);
		leftExpr = ExprNode.isMatch(env, lu);
		if (leftExpr == null || !leftExpr.Parse()) {
			System.out.println("cond: leftExpr error");
			return false;
		}
		comparison = env.getInput().get();
		if (!comparisonSet.contains(comparison.getType())) {
			env.getInput().unget(lu);
			System.out.println("cond: comparison error");
			return false;
		}
		lu = env.getInput().get();
		env.getInput().unget(lu);
		rightExpr = ExprNode.isMatch(env, lu);
		if (rightExpr == null || !rightExpr.Parse()) {
			System.out.println("cond: rightExpr error");
			return false;
		}
		node_s = comparison2s(comparison.getType()) + "[" + rightExpr + " : " + leftExpr + "]";
		return true;
	}

	public String comparison2s(LexicalType comparison) {
		switch (comparison) {
		case EQ:
			return "=";
		case GT:
			return ">";
		case LT:
			return "<";
		case GE:
			return ">=";
		case LE:
			return "<=";
		case NE:
			return "<>";
		default:
			return null;
		}
	}

	public boolean comparison() throws Exception {
//		System.out.println("leftExpr: " + leftExpr.getValue().getIValue());
//		System.out.println("rightExpr: " + rightExpr.getValue().getIValue());
		switch (this.comparison.getType()) {
		case LT:
			return this.leftExpr.getValue().getIValue() < this.rightExpr.getValue().getIValue();
		}
		return false;
	}

	@Override
	public Value getValue() throws Exception {
//		System.out.println(comparison());
		return new ValueImpl(comparison());
	}

}
