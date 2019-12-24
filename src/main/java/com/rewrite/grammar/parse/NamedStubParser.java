package com.rewrite.grammar.parse;

import javax.naming.OperationNotSupportedException;

public class NamedStubParser extends GenericParser {
	private final String name;

	public NamedStubParser(String name) {
		super();
		this.name = name;
	}

	@Override
	public boolean accepts(String str) {
		return false;
	}

	@Override
	public SyntaxNode parse(String str) throws OperationNotSupportedException {
		throw new OperationNotSupportedException();
	}

}
