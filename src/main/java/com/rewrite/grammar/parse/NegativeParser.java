package com.rewrite.grammar.parse;

public class NegativeParser extends GenericParser {
	private final GenericParser gp;

	public NegativeParser(GenericParser gp) {
		this.gp = gp;
	}

	@Override
	public boolean accepts(StringPump sp) {
		return !gp.accepts(sp);
	}

	@Override
	public SyntaxNode parse(StringPump sp) {
		return null;
	}
}
