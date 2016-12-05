package newlang4;

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

		System.out.println("basic parser");
		fin = new FileInputStream("input.bsc");
		lex = new LexicalAnalyzerImpl(fin);
		env = new Environment(lex);
		first = lex.get();

		program = Variable.isMatch(env, first);
		if (program != null && program.Parse()) {
			System.out.println(program);
			System.out.println("value = " + program.getValue());
		} else
			System.out.println("syntax error");
	}

}
