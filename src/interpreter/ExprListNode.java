package interpreter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ExprListNode extends Node {
	Node head;
	Node tail;

	private ExprListNode(Environment env) {
		super.env = env;
		super.type = NodeType.EXPR_LIST;
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

	public static Node isMatch(Environment env, LexicalUnit first) {
		if (!firstSet.contains(first.type)) {
			return null;
		}
		if (Main.debug)
			System.out.println("ExprListNode::");

		return new ExprListNode(env);
	}

	@Override
	public boolean Parse() throws Exception {
		// expr
		if (!isExpr()) {
			System.out.println("callSub: expr error");
			return false;
		}
		// comma and expr_list
		commaAndExprList();
		return true;
	}

	public boolean isExpr() throws Exception {
		LexicalUnit lu = env.getInput().get();
		env.getInput().unget(lu);
		Node expr = ExprNode.isMatch(env, lu);

		if (expr == null || !expr.Parse()) {
			return false;
		}
		this.head = expr;
		this.node_s = expr.toString();
		return true;
	}

	public boolean commaAndExprList() throws Exception {
		LexicalUnit lu = env.getInput().get();
		if (lu.getType() != LexicalType.COMMA) {
			env.getInput().unget(lu);
			return false;
		}

		lu = env.getInput().get();
		env.getInput().unget(lu);
		Node exprList = ExprListNode.isMatch(env, lu);
		if (exprList == null || !exprList.Parse()) {
			return false;
		}
		this.tail = exprList;
		this.node_s += "," + exprList.toString();
		return true;
	}
	@Override
	public Value getValue() throws Exception {
		return this.head.getValue();
	}
}
