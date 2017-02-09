package interpreter;

import java.util.HashSet;
import java.util.Set;

public class IfPrefixNode extends Node {
	Node body;

	private IfPrefixNode(Environment env) {
		super.env = env;
		super.type = NodeType.SUBST;
	}

	private static Set<LexicalType> firstSet = new HashSet<LexicalType>();
	static {
		// if
		firstSet.add(LexicalType.IF);
	}

	public static Node isMatch(Environment env, LexicalUnit first) {
		if (!firstSet.contains(first.type)) {
			return null;
		}

		return new IfPrefixNode(env);
	}

	@Override
	public boolean Parse() throws Exception {
		LexicalUnit lu = env.getInput().get();
		env.getInput().unget(lu);

		body = StmtListNode.isMatch(env, lu);
		if (body != null) {
			return body.Parse();
		}

		if (lu.getType() == LexicalType.END) {
			super.type = NodeType.END;
			return true;
		}

		return false;

	}
}
