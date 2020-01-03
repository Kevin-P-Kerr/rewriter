package com.rewrite.grammar.parse;

import java.util.Map;

public class NegativeParser extends GenericParser {
	private final GenericParser gp;

	public NegativeParser(GenericParser gp) {
		this.gp = gp;
	}

	@Override
	public boolean accepts(StringPump sp) {
		int n = sp.getIndex();
		boolean b = !gp.accepts(sp);
		if (!b) {
			sp.setIndex(n);
			return b;
		}
		sp.setIndex(n);
		return b;
	}

	@Override
	public SyntaxNode parse(StringPump sp) {
		return null;
	}

	@Override
	public void unStub(Map<String, GenericParser> namedParsers) throws Exception {
		gp.unStub(namedParsers);
	}
}
