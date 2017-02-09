package interpreter;

import java.util.HashSet;
import java.util.Set;

public class LoopNode extends Node {
	Node cond;
	Node stmtList;

	private LoopNode(Environment env) {
		super.env = env;
		super.type = NodeType.PROGRAM;
	}

	private static Set<LexicalType> firstSet = new HashSet<LexicalType>();
	static {
		// while
		firstSet.add(LexicalType.WHILE);
		// do
		firstSet.add(LexicalType.DO);
	}

	public static Node isMatch(Environment env, LexicalUnit first) {
		if (!firstSet.contains(first.type)) {
			return null;
		}
		if (Main.debug)
			System.out.println("BlockNode::");
		return new LoopNode(env);
	}

	@Override
	public boolean Parse() throws Exception {
		LexicalUnit lu = env.getInput().get();

		if (lu.getType() == LexicalType.WHILE) {
			// 未
			return true;
		}
		if (lu.getType() == LexicalType.DO) {
			if (doWhile()) {
				return true;
			} else if (doUntil()) {
				return true;
			} else if (doNl()) {
				return true;
			}
			return false;
		}
		return false;
	}

	public boolean doWhile() throws Exception {
		LexicalUnit lu = env.getInput().get();
		env.getInput().unget(lu);
		if (lu.getType() == LexicalType.WHILE) {
			if (Main.debug)
				System.out.println("dowhile:");
			return true;
		}
		return false;
	}

	public boolean doUntil() throws Exception {
		// until
		LexicalUnit lu = env.getInput().get();
		if (lu.getType() != LexicalType.UNTIL) {
			env.getInput().unget(lu);
			return false;
		}
		// cond
		lu = env.getInput().get();
		env.getInput().unget(lu);
		cond = CondNode.isMatch(env, lu);
		if (cond == null || !cond.Parse()) {
			return false;
		}
		// nl
		lu = env.getInput().get();
		if (lu.getType() != LexicalType.NL) {
			env.getInput().unget(lu);
			System.out.println(lu);
			System.out.println("doUntil: stmtlist end nl error");
			return false;
		}

		// stmt_list
		lu = env.getInput().get();
		env.getInput().unget(lu);
		stmtList = StmtListNode.isMatch(env, lu);
		if (stmtList == null || !stmtList.Parse()) {
			System.out.println(((LexicalAnalyzerImpl) env.getInput()).getStack());
			System.out.println(lu);
			if (Main.debug)
				System.out.println("doUntil: stmt_list error");
			return false;
		}
		// loop
		lu = env.getInput().get();
		if (lu.getType() != LexicalType.LOOP) {
			env.getInput().unget(lu);
			if (Main.debug)
				System.out.println(lu.getValue());
			System.out.println(lu.getType());
			System.out.println("doUntil: loop not found");
			return false;
		}
		// nl
		lu = env.getInput().get();
		if (lu.getType() != LexicalType.NL) {
			env.getInput().unget(lu);
			System.out.println(lu);
			System.out.println("doUntil: last nl error");
			return false;
		}
		this.node_s = "LOOP [" + cond + "[" + stmtList + "][]]";
		return true;
	}

	public boolean doNl() throws Exception {
		LexicalUnit lu = env.getInput().get();
		env.getInput().unget(lu);
		if (Main.debug)
			System.out.println("doNl:");
		return false;
	}

	@Override
	public Value getValue() throws Exception {
		while (!this.cond.getValue().getBValue()) {
			stmtList.getValue();
		}
		return null;
	}
}
