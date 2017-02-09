package interpreter;

import java.util.HashMap;
import java.util.Map;

public class Operator {
	private final static Map<LexicalType, Integer> operators = new HashMap() {
		{
			put(LexicalType.LP, 1);
			put(LexicalType.MUL, 2);
			put(LexicalType.DIV, 3);
			put(LexicalType.ADD, 4);
			put(LexicalType.SUB, 5);
			put(LexicalType.RP, 6);
		}
	};

	public static boolean isPrior(LexicalType pre, LexicalType next) {
		return (operators.get(pre) <= operators.get(next));
	}
}
