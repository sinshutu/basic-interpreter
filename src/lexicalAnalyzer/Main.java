package lexicalAnalyzer;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Main {
	public static boolean debug = false;

	public static void main(String[] args) throws IOException {
		String fileName = "input.bsc";
		InputStream is = new FileInputStream(fileName);
		InputStreamReader ir = new InputStreamReader(is);
		LexicalAnalyzer lex = new LexicalAnalyzerImpl(ir);
		int count = 0;
		while (true) {
			if (Main.debug)
			System.out.print(count++ + ": ");
			LexicalUnit unit = lex.get();
			System.out.println(unit);
			if (unit.getType() == LexicalType.EOF)
				break;
		}
	}
}
