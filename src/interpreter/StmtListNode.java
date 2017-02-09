package interpreter;

import java.util.HashSet;
import java.util.Set;

public class StmtListNode extends Node {
	Node stmtOrBlock;
	Node stmtList;

	private StmtListNode(Environment env) {
		super.env = env;
		super.type = NodeType.SUBST;
	}

	private static Set<LexicalType> firstSet = new HashSet<LexicalType>();
	static {
		// stmt
		firstSet.add(LexicalType.NAME);
		firstSet.add(LexicalType.FOR);
		firstSet.add(LexicalType.END);
		// block
		firstSet.add(LexicalType.IF);
		firstSet.add(LexicalType.WHILE);
		firstSet.add(LexicalType.DO);
	}

	public static Node isMatch(Environment env, LexicalUnit first) {
		if (!firstSet.contains(first.type)) {
			return null;
		}
		if (Main.debug)
			System.out.println("StmtList::");
		return new StmtListNode(env);
	}

	@Override
	public boolean Parse() throws Exception {
		if (stmt()) { // stmt
			nlAndStmtList();
			return true;
		} else if (block()) { // block
			stmtList();
			return true;
		}
		LexicalUnit lu = env.getInput().get();
		env.getInput().unget(lu);
		if (lu.getType() == LexicalType.END) {

			super.type = NodeType.END;
			node_s = "END";
			return true;
		}

		return false;

	}

	public boolean stmt() throws Exception {
		LexicalUnit lu = env.getInput().get();
		env.getInput().unget(lu);

		// stmt
		Node stmt = StmtNode.isMatch(env, lu);
		if (stmt != null && stmt.Parse()) {
			this.node_s = stmt.toString();
			this.stmtOrBlock = stmt;
			return true;
		}
		return false;
	}

	public boolean nlAndStmtList() throws Exception {
		// nl
		LexicalUnit lu = env.getInput().get();
		if (lu.getType() != LexicalType.NL) {
			env.getInput().unget(lu);
			return false;
		}
		// stmtlist
		return stmtList();
	}

	public boolean stmtList() throws Exception {
		LexicalUnit lu = env.getInput().get();
		env.getInput().unget(lu);

		Node stmtList = StmtListNode.isMatch(env, lu);
		if (stmtList != null && stmtList.Parse()) {
			this.node_s += ";" + stmtList.toString();
			this.stmtList = stmtList;
			return true;
		} else {
			return false;
		}
	}

	public boolean block() throws Exception {
		LexicalUnit lu = env.getInput().get();
		env.getInput().unget(lu);
		Node block = BlockNode.isMatch(env, lu);
		if (block != null && block.Parse()) {
			this.node_s += block.toString();
			this.stmtOrBlock = block;
			return true;
		} else {
			env.getInput().unget(lu);
			return false;
		}
	}

	@Override
	public Value getValue() throws Exception {
		// stmtOrBlock
		Value stmtOrBlock = this.stmtOrBlock.getValue();
		if (stmtList != null) {
			Value stmtList = this.stmtList.getValue();
		}
		return null;
	}
}
