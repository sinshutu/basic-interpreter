package interpreter;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class ExprNode extends Node {
	static boolean debug = false;
	Node left;
	Node right;
	LexicalType operator;
	Value value;

	private Stack<String> calc_tree = new Stack<String>();
	private Stack<LexicalUnit> ope_stack = new Stack<LexicalUnit>();
	private Stack<Node> expr_tree = new Stack<Node>();

	private ExprNode(Environment env) {
		super.env = env;
		super.type = NodeType.EXPR;
	}

	// generate constant node
	private ExprNode(Environment env, LexicalUnit value) {
		this(env);
		switch (value.getType()) {
		case NAME:
			this.type = NodeType.VAR;
			break;
		case INTVAL:
			this.type = NodeType.INT_CONSTANT;
			break;
		case LITERAL:
			this.type = NodeType.STRING_CONSTANT;
			break;
		}
		this.value = value.getValue();
	}

	// generate expr node
	private ExprNode(Environment env, LexicalType operator, Node left, Node right) {
		this(env);
		super.type = NodeType.EXPR;
		this.operator = operator;
		this.left = left;
		this.right = right;
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
			System.out.println("ExprNode::");
		return new ExprNode(env);
	}

	@Override
	public boolean Parse() throws Exception {
		LexicalUnit value;
		LexicalUnit ope;
		value = env.getInput().get();
		// [-,+,(,"]から始まった場合
		if (isParentheses(value) || isOpe(value)) {
			if (Main.debug)
				System.out.println("expr first is not number");
			switch (value.getType()) {
			case LP:
				return true;
			}
		}
		// 数字から始まった場合
		if (!isValue(value)) {
			return false;
		}
		this.expr_tree.add(new ExprNode(env, value));
		this.calc_tree.add(value.getValue().toString());
		// 数字とオペランドを交互にスタックしその都度計算する
		// exprの要素でないもの or ) がきたら終了
		while (true) {
			ope = env.getInput().get();
			if (!isOpe(ope) || ope.getType() == LexicalType.RP) {
				env.getInput().unget(ope);
				// 計算しなくて良いとき
				if (this.expr_tree.size() == 1) {
					this.node_s = this.calc_tree.pop().toString();
					this.type = NodeType.FACT;
					break;
				}
				// 計算する場合
				while (this.ope_stack.size() > 0) {
					calc_tree();
				}
				this.node_s = this.calc_tree.pop();
				// ???????????????????
				this.type = NodeType.FACT;
				break;
			}
			// 計算
			while (this.expr_tree.size() >= 2 && this.ope_stack.size() >= 1
					&& Operator.isPrior(this.ope_stack.peek().getType(), ope.getType())) {
				calc_tree();
			}
			this.ope_stack.add(ope);

			value = env.getInput().get();
			if (!isValue(value)) {
				return false;
			}
			this.expr_tree.add(new ExprNode(env, value));
			this.calc_tree.add(value.getValue().toString());
		}
		return true;
	}

	public void calc_tree() {
		String tree_right = this.calc_tree.pop();
		String tree_left = this.calc_tree.pop();
		Node expr_right = this.expr_tree.pop();
		Node expr_left = this.expr_tree.pop();
		LexicalUnit ope = this.ope_stack.pop();
		switch (ope.getType()) {
		case ADD:
			this.expr_tree.add(new ExprNode(env, LexicalType.ADD, expr_left, expr_right));
			this.calc_tree.add("+[" + tree_left + ", " + tree_right + "]");
			break;
		case SUB:
			this.expr_tree.add(new ExprNode(env, LexicalType.SUB, expr_left, expr_right));
			this.calc_tree.add("-[" + tree_left + ", " + tree_right + "]");
			break;
		case MUL:
			this.expr_tree.add(new ExprNode(env, LexicalType.MUL, expr_left, expr_right));
			this.calc_tree.add("*[" + tree_left + ", " + tree_right + "]");
			break;
		case DIV:
			this.expr_tree.add(new ExprNode(env, LexicalType.DIV, expr_left, expr_right));
			this.calc_tree.add("/[" + tree_left + ", " + tree_right + "]");
			break;
		}
		if (Main.debug)
			System.out.println("expr::calc: " + expr_tree.peek());
	}

	private Value calc() throws Exception {
		Value left, right;
		left = this.left.getValue();
		right = this.right.getValue();
		switch (this.operator) {
		case ADD:
			return new ValueImpl(left.getIValue() + right.getIValue());
		case SUB:
			return new ValueImpl(left.getIValue() - right.getIValue());
		case MUL:
			return new ValueImpl(left.getIValue() * right.getIValue());
		case DIV:
			return new ValueImpl(left.getIValue() / right.getIValue());
		default:
			System.out.println("operator known");
			return null;
		}
	}

	public boolean isParentheses(LexicalUnit lu) throws Exception {
		LexicalType lt = lu.getType();
		// ( expr )
		if (lt == LexicalType.LP) {
			return true;
		}
		return false;
	}

	public boolean isOpe(LexicalUnit lu) throws Exception {
		LexicalType lt = lu.getType();
		// expr [-,+,*,/]
		if (lt == LexicalType.SUB || lt == LexicalType.ADD || lt == LexicalType.MUL || lt == LexicalType.DIV) {
			return true;
		}
		return false;
	}

	public boolean isLiteral(LexicalUnit lu) throws Exception {
		LexicalType lt = lu.getType();
		if (lt == LexicalType.LITERAL) {
			this.node_s = lu.getValue().toString();
			this.type = NodeType.STRING_CONSTANT;
			return true;
		}
		return false;
	}

	public boolean isValue(LexicalUnit expr) {

		if (expr.getType() == LexicalType.INTVAL || expr.getType() == LexicalType.DOUBLEVAL) {
			this.type = NodeType.INT_CONSTANT;
			return true;
		} else if (expr.getType() == LexicalType.LITERAL) {
			this.type = NodeType.STRING_CONSTANT;
			return true;
		} else if (expr.getType() == LexicalType.NAME) {
			return true;
		}

		return false;
	}

	public String operator2s(LexicalUnit operator) {
		switch (operator.getType()) {
		case ADD:
			return "+";
		case SUB:
			return "-";
		case MUL:
			return "*";
		case DIV:
			return "/";
		default:
			System.out.println("error:: operator is not found");
			break;
		}
		return "";
	}

	@Override
	public Value getValue() throws Exception {
		switch (this.type) {
		case EXPR:
			if (Main.debug && this.debug)
				System.out.println("getValue::expr");
			return calc();
		case FACT:
			if (Main.debug && this.debug)
				System.out.println("getValue::fact");
			return this.expr_tree.peek().getValue();
		case INT_CONSTANT:
			if (Main.debug && this.debug)
				System.out.println("getValue::int");
			return this.value;
		case STRING_CONSTANT:
			if (Main.debug && this.debug)
				System.out.println("getValue::string");
			return this.value;
		case VAR:
			if (Main.debug && this.debug)
				System.out.println("getValue::var");
			return (Value) env.var_table.get(this.value.toString());
		default:
			System.out.println("Expr::getValue: error, Type: " + this.type);
			return null;
		}
	}
}