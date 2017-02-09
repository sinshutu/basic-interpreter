package interpreter;

import java.util.HashSet;
import java.util.Set;

public class ProgramNode extends Node {
	Node body;

	private ProgramNode(Environment env) {
		super.env = env;
		super.type = NodeType.PROGRAM;
	}

	private static Set<LexicalType> firstSet = new HashSet<LexicalType>();
	static {
		// stmtlist-stmt
		firstSet.add(LexicalType.NAME);
		firstSet.add(LexicalType.FOR);
		firstSet.add(LexicalType.END);
		// stmtlist-block
		firstSet.add(LexicalType.IF);
		firstSet.add(LexicalType.WHILE);
		firstSet.add(LexicalType.DO);
		// expr
		firstSet.add(LexicalType.SUB);
		firstSet.add(LexicalType.NAME);
		firstSet.add(LexicalType.LITERAL);
		firstSet.add(LexicalType.INTVAL);
		firstSet.add(LexicalType.DOUBLEVAL);
		firstSet.add(LexicalType.LP);
	}

	public static Node isMatch(Environment env, LexicalUnit first) {
		if (!firstSet.contains(first.type)) {
			return null;
		}
		if (Main.debug)
			System.out.println("ProgramNode::");
		return new ProgramNode(env);
	}

	@Override
	public boolean Parse() throws Exception {
		LexicalUnit lu = env.getInput().get();
		env.getInput().unget(lu);

		body = StmtListNode.isMatch(env, lu);
		if (body != null) {
			return body.Parse();
		}

		// expr test
		// body = ExprNode.isMatch(env, lu);
		// if (body != null) {
		// return body.Parse();
		// }

		if (lu.getType() == LexicalType.END) {
			super.type = NodeType.END;
			return true;
		}
		return false;
	}

	@Override
	public Value getValue() throws Exception {
		return this.body.getValue();
	}

	@Override
	public String toString() {
		return this.body.toString();
	}
}
