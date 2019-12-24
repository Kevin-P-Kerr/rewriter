package com.rewrite.grammar.parse;

import com.rewrite.grammar.parse.Tokenizer.Token;
import com.rewrite.grammar.parse.Tokenizer.TokenStream;
import com.rewrite.grammar.parse.Tokenizer.Token.TokenType;

public class GenericParser {
	private static class GenericParserTuple {
		boolean required;
		GenericParserTuple next;
		GenericParser parser;
	}

	private GenericParserTuple head;
	private GenericParserTuple current;

	public static GenericParser from(String name, TokenStream tokens) {
		GenericParser ret = new GenericParser();
		Token t = tokens.getNext();
		while (t.getType() != TokenType.TT_PERIOD) {

		}
	}

	public boolean accepts(String str) {
		return false; // TODO: do this
	}

	public SyntaxNode parse(String str) {
		return null;
	}

}
