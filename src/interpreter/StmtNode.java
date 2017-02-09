package interpreter;

import java.util.HashSet;
import java.util.Set;

public class StmtNode extends Node {
	Node body;

	private StmtNode(Environment env) {
		super.env = env;
		super.type = NodeType.STMT;
	}

	private static Set<LexicalType> firstSet = new HashSet<LexicalType>();
	static {
		firstSet.add(LexicalType.NAME);
		firstSet.add(LexicalType.FOR);
		firstSet.add(LexicalType.END);
	}

	public static Node isMatch(Environment env, LexicalUnit first) {
		if (!firstSet.contains(first.type)) {
			return null;
		}
		if (Main.debug)
			System.out.println("Stmt::");
		return new StmtNode(env);
	}

	@Override
	public boolean Parse() throws Exception {
		LexicalUnit lu = env.getInput().get();
		env.getInput().unget(lu);

		if (lu.getType() == LexicalType.NAME) {
			body = SubstNode.isMatch(env, lu);
			if (body != null && body.Parse()) {
				this.node_s = body.toString();
				return true;
			}

			body = CallSubNode.isMatch(env, lu);
			if (body != null && body.Parse()) {
				this.node_s = body.toString();
				return true;
			}
			return false;
		}

		if (lu.getType() == LexicalType.FOR) {
			// body = ForNode.isMatch(env, lu);
			this.node_s = "FOR[asdafds]";
			return true;
		}

		if (lu.getType() == LexicalType.END) {
			super.type = NodeType.END;
			this.node_s = "END";
			return true;
		}

		return false;

	}

	@Override
	public Value getValue() throws Exception {
		if (body != null) {
			body.getValue();
		}
		return null;
	}
}
