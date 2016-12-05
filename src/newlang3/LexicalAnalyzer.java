package newlang3;

import java.io.IOException;

public interface LexicalAnalyzer {
	public LexicalUnit get() throws IOException;

	public boolean expect(LexicalType type);

	public void unget(LexicalUnit token);
}
