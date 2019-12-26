package com.rewrite.grammar.parse;

public class NamedStubParser extends GenericParser {
	private final String name;

	public NamedStubParser(String name) {
		super();
		this.name = name;
	}

	@Override
	public boolean accepts(StringPump str) {
		return false;
	}

	public String getName() {
		return name;
	}

}
