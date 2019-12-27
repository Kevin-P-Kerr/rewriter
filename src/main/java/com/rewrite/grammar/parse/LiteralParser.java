package com.rewrite.grammar.parse;

public class LiteralParser extends GenericParser {

	private final String literal;

	public LiteralParser(String quotedValue) {
		this.literal = quotedValue;
	}

	@Override
	public boolean accepts(StringPump sp) {
		int i = 0;
		int ii = literal.length();
		for (; i < ii; i++) {
			if (!sp.hasChar()) {
				return false;
			}
			char c = sp.getChar();
			if (c != literal.charAt(i)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public SyntaxNode parse(StringPump sp) {
		int i = 0;
		int ii = literal.length();
		for (; i < ii; i++) {
			if (!sp.hasChar()) {
				return null;
			}
			char c = sp.getChar();
			if (c != literal.charAt(i)) {
				return null;
			}
		}
		return new SyntaxNode("LITERAL", literal);
	}

	public boolean isChar() {
		return literal.length() == 1;
	}

	public char getFirstChar() {
		return literal.charAt(0);
	}

}
