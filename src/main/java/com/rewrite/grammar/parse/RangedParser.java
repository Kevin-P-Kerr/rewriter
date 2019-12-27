package com.rewrite.grammar.parse;

public class RangedParser extends GenericParser {
	private final char start;
	private final char finish;

	public RangedParser(char start, char finish) {
		this.start = start;
		this.finish = finish;
	}

	@Override
	public boolean accepts(StringPump sp) {
		char c = sp.getChar();
		return start <= c && finish >= c;
	}

	@Override
	public SyntaxNode parse(StringPump sp) {
		char c = sp.peekChar();
		if (!accepts(sp)) {
			return null;
		}
		return new SyntaxNode("LITERAL", c + "");
	}
}
