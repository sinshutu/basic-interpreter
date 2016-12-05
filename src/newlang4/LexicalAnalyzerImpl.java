package newlang4;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PushbackReader;
import java.util.regex.Pattern;

public class LexicalAnalyzerImpl implements LexicalAnalyzer {
	private PushbackReader in;
	private final Pattern isInteger = Pattern.compile("^\\d$");
	private final Pattern isDoubleval = Pattern.compile("^\\d\\.?\\d*$");
	private final Pattern isLexcal = Pattern.compile("[A-Z]");
	private final Pattern isSymbol = Pattern.compile("[=+-/*()<>\t\n ]");
	private final Pattern isLiteral = Pattern.compile("[\"\']");
	private final Pattern isIgnore = Pattern.compile("[\t ]");
	private final LexicalUnit eof = new LexicalUnit(LexicalType.EOF);

	public LexicalAnalyzerImpl(FileInputStream is) {
		InputStreamReader ir = new InputStreamReader(is);
		this.in = new PushbackReader(ir);
	}

	@Override
	public LexicalUnit get() throws IOException {

		LexicalMap lm = new LexicalMap();
		LexicalUnit lu = null;
		int ch = in.read();
		String token = "" + (char) ch;
		if (ch == -1) {
			return new LexicalUnit(LexicalType.EOF);
		}
		// 無視してよい文字
		while (isIgnore((char) ch)) {
			// System.out.print("isIgnore:");
			// System.out.println(token);
			ch = in.read();
			token = "" + (char) ch;
		}

		if (isLiteral((char) ch)) {// リテラルの場合
			if (Main.debug)
				System.out.print("isLiteral:");
			// リテラルの開始文字を保持("or'両方で処理できるように)
			int start = ch;
			// System.out.println(token);
			token = "";
			ch = in.read();
			while (start != ch) { // 2度目"or'になったとき
				token += (char) ch;
				ch = in.read();
			}
			lu = new LexicalUnit(LexicalType.LITERAL, new ValueImpl(token));

		} else if (isSymbol((char) ch)) { // 記号の場合
			if (Main.debug)
				System.out.print("isSymbol:");
			// System.out.println(token);
			ch = in.read();
			// System.out.println(ch);
			while (true) {
				// System.out.println(ch);
				if (isNumbar((char) ch) || isAlpha((char) ch) || isIgnore((char) ch) || ch == -1 || ch == '\n') { // 記号以外になったとき
					if (ch != -1)
						in.unread(ch);
					lu = new LexicalUnit(lm.getLexicalType(token));
					break;
				}
				token += (char) ch;
			}

		} else if (isAlpha((char) ch)) { // 文字の場合
			if (Main.debug)
				System.out.print("isAlpha:");
			// System.out.println((char) ch);
			while (true) {
				ch = in.read();
				// System.out.println((char)ch);
				if (isSymbol((char) ch) || ch == -1) { // 記号やEOFになったとき
					// System.out.println("token: " + token);
					if (ch != -1)
						in.unread(ch);
					// System.out.println(lm.getLexicalType(token));
					LexicalType type = lm.getLexicalType(token);
					if (type != null) {
						// System.out.println("type is token");
						lu = new LexicalUnit(type);
					} else {
						// System.out.println("type is null");
						// System.out.println((char)ch);
						lu = new LexicalUnit(LexicalType.NAME, new ValueImpl(token));
					}
					break;
				}
				token += (char) ch;
			}

		} else if (isNumbar((char) ch)) { // 数字の場合
			if (Main.debug)
				System.out.print("isNumber:");
			while (true) {
				if (Main.debug)
					System.out.println(token);
				ch = in.read();
				if (isAlpha((char) ch) || isSymbol((char) ch) || ch == -1) { // 数字以外になったとき
					if (ch != -1)
						in.unread(ch);
					// TODO: int, doubleの判定処理を追加
					lu = new LexicalUnit(LexicalType.INTVAL, new ValueImpl(Integer.parseInt(token)));
					break;
				}
				token += (char) ch;
			}
		}
		return lu;
	}

	@Override
	public boolean expect(LexicalType type) {
		return false;
	}

	@Override
	public void unget(LexicalUnit token) {

	}

	private boolean isIgnore(char c) {
		return isIgnore.matcher("" + c).find();
	}

	private boolean isLiteral(char c) {
		return isLiteral.matcher("" + c).find();
	}

	private boolean isSymbol(char c) {
		return isSymbol.matcher("" + c).find();
	}

	private boolean isAlpha(char c) {
		return (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z');
	}

	private boolean isNumbar(char c) {
		if (Main.debug)
			System.out.print(c);
		return c >= '0' && c <= '9';
	}

}