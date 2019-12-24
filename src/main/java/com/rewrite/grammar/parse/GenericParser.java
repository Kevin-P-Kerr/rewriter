package com.rewrite.grammar.parse;

import com.rewrite.grammar.parse.Tokenizer.Token;
import com.rewrite.grammar.parse.Tokenizer.TokenStream;
import com.rewrite.grammar.parse.Tokenizer.Token.TokenType;

public class GenericParser {
	private static class GenericParserTuple {
		boolean repeated;
		private GenericParserTuple next;
		private GenericParser parser;

		public GenericParserTuple(GenericParser gp, boolean repeated) {
			this.repeated = repeated;
			this.parser = gp;
		}
	}

	private GenericParserTuple head = new GenericParserTuple(new NamedStubParser("head"), false);
	private GenericParserTuple current = head;

	public static GenericParser from(String name, TokenStream tokens) throws Exception {
		GenericParser ret = new GenericParser();
		Token t = tokens.getNext();
		while (t.getType() != TokenType.TT_PERIOD) {
			if (t.getType() == TokenType.TT_LCURLY) {
				// repeated field
				t = tokens.getNext();
				GenericParser gp = fromToken(t, tokens);
				GenericParserTuple tuple = new GenericParserTuple(gp, true);
				ret.current.next = tuple;
				t = tokens.getNext();
				if (t.getType() != TokenType.TT_RCURLY) {
					throw new Exception();
				}
			}

		}
		return ret;
	}

	private static GenericParser fromToken(Token t, TokenStream tokens) throws Exception {
		if (t.getType() == TokenType.TT_VAR) {
			return new NamedStubParser(t.getLit());
		} else if (t.getType() == TokenType.TT_SINGLE_QUOTE) {
			t = tokens.getNext();
			String quotedValue = "";
			while (t.getType() != TokenType.TT_SINGLE_QUOTE) {
				quotedValue += t.getLit();
			}
			return new SimpleStringParser(quotedValue);
		}
		throw new Exception();
	}

	public boolean accepts(String str) {
		return false; // TODO: do this
	}

	public SyntaxNode parse(String str) {
		return null;
	}

}
