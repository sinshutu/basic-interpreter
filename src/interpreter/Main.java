package interpreter;

import java.io.FileInputStream;

public class Main {
	public static boolean debug = false;

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		FileInputStream fin = null;
		LexicalAnalyzer lex;
		LexicalUnit first;
		Environment env;
		Node program;

		fin = new FileInputStream("input.bsc");
		lex = new LexicalAnalyzerImpl(fin);
		env = new Environment(lex);
		first = lex.get();
		lex.unget(first);

		program = ProgramNode.isMatch(env, first);
		if (program != null && program.Parse()) {
//			System.out.println(program);
			program.getValue();
		} else {
			System.out.println("syntax error");
		}
	}

}