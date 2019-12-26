package com.rewrite.grammar.parse;

public class LiteralParser extends GenericParser {

	private final String literal;

	public LiteralParser(String quotedValue) {
		this.literal = quotedValue;
	}

}
