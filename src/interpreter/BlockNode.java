package interpreter;

import java.util.HashSet;
import java.util.Set;

public class BlockNode extends Node {
	Node loop;

	private BlockNode(Environment env) {
		super.env = env;
		super.type = NodeType.PROGRAM;
	}

	private static Set<LexicalType> firstSet = new HashSet<LexicalType>();
	static {
		// if-prefix
		firstSet.add(LexicalType.IF);
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
		return new BlockNode(env);
	}

	@Override
	public boolean Parse() throws Exception {
		LexicalUnit lu = env.getInput().get();
		env.getInput().unget(lu);

		Node ifPrefix = IfPrefixNode.isMatch(env, lu);
		if (ifPrefix != null && ifPrefix.Parse()) {
			// 未
			return true;
		}
		if (lu.getType() == LexicalType.WHILE) {
			// 未
			return true;
		}
		if (lu.getType() == LexicalType.DO) {
			// loop
			loop = LoopNode.isMatch(env, lu);
			if (loop != null && loop.Parse()) {
				this.node_s = loop.toString();
				return true;
			}
			return false;
		}
		return false;
	}

	@Override
	public Value getValue() throws Exception {
		return this.loop.getValue();
	}
}