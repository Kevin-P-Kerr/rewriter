package com.rewrite.grammar.parse;

import java.util.Map;

public class DisjunctiveParser extends GenericParser {

	private GenericParser left;
	private GenericParser right;

	public DisjunctiveParser(GenericParser gp, GenericParser ggp) {
		this.left = gp;
		this.right = ggp;
	}

	@Override
	public boolean accepts(StringPump sp) {
		int n = sp.getIndex();
		if (left.accepts(sp)) {
			return true;
		}
		sp.setIndex(n);
		if (right.accepts(sp)) {
			return true;
		}
		return false;
	}

	@Override
	public SyntaxNode parse(StringPump sp) throws Exception {

		int n = sp.getIndex();
		if (left.accepts(sp)) {
			sp.setIndex(n);
			return left.parse(sp);
		}
		sp.setIndex(n);
		if (right.accepts(sp)) {
			sp.setIndex(n);
			return right.parse(sp);

		}
		sp.setIndex(n);
		return null;
	}

	@Override
	public void unStub(Map<String, GenericParser> namedParsers) throws Exception {
		if (left instanceof NamedStubParser) {
			GenericParser gp = namedParsers.get(((NamedStubParser) left).getName());
			if (gp == null) {
				throw new Exception();
			}
			left = gp;
		} else {
			left.unStub(namedParsers);
		}
		if (right instanceof NamedStubParser) {
			GenericParser gp = namedParsers.get(((NamedStubParser) right).getName());
			if (gp == null) {
				throw new Exception();
			}
			right = gp;
		} else {
			right.unStub(namedParsers);
		}
	}

}
