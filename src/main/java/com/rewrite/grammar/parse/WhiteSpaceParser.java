package com.rewrite.grammar.parse;

public class WhiteSpaceParser extends GenericParser {
	@Override
	public boolean accepts(StringPump sp) {
		if (!sp.hasChar()) {
			return false;
		}
		char c = sp.getChar();
		return Tokenizer.isWhite(c);
	}

	@Override
	public SyntaxNode parse(StringPump sp) {
		int n = sp.getIndex();
		if (!accepts(sp)) {
			return null;
		}
		sp.setIndex(n);
		char c = sp.getChar();
		return new SyntaxNode("WHTIE-SPACE", c + "");
	}
}
