package com.rewrite.grammar.parse;

public class DisjunctiveParser extends GenericParser {

	private final GenericParser left;
	private final GenericParser right;

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
}
