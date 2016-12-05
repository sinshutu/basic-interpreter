package newlang4;

import java.util.HashMap;
import java.util.Map;

public class LexicalMap {
	private final static Map<String, LexicalType> lexicalMap = new HashMap() {
		{
			put("IF", LexicalType.IF);
			put("THEN", LexicalType.THEN);
			put("ELSE", LexicalType.ELSE);
			put("ELSEIF", LexicalType.ELSEIF);
			put("ENDIF", LexicalType.ENDIF);
			put("FOR", LexicalType.FOR);
			put("FORALL", LexicalType.FORALL);
			put("NEXT", LexicalType.NEXT);
			put("=", LexicalType.EQ);
			put("<", LexicalType.LT);
			put(">", LexicalType.GT);
			put("=<", LexicalType.LE);
			put("<=", LexicalType.LE);
			put("=>", LexicalType.GE);
			put(">=", LexicalType.GE);
			put("<>", LexicalType.NE);
			put("FUNC", LexicalType.FUNC);
			put("DIM", LexicalType.DIM);
			put("AS", LexicalType.AS);
			put("END", LexicalType.END);
			put("\n", LexicalType.NL);
			put(".", LexicalType.DOT);
			put("WHILE", LexicalType.WHILE);
			put("DO", LexicalType.DO);
			put("UNTIL", LexicalType.UNTIL);
			put("+", LexicalType.ADD);
			put("-", LexicalType.SUB);
			put("*", LexicalType.MUL);
			put("/", LexicalType.DIV);
			put("(", LexicalType.LP);
			put(")", LexicalType.RP);
			put(",", LexicalType.COMMA);
			put("LOOP", LexicalType.LOOP);
			put("TO", LexicalType.TO);
			put("WEND", LexicalType.WEND);

		}
	};

	public LexicalMap() {
	}

	public LexicalType getLexicalType(String key) {
		key = key.toUpperCase();
		if (Main.debug)
			System.out.println(lexicalMap.get(key));

		return lexicalMap.get(key);
	}

}
