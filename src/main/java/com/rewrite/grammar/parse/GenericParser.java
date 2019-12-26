package com.rewrite.grammar.parse;

import com.rewrite.grammar.parse.Tokenizer.Token;
import com.rewrite.grammar.parse.Tokenizer.TokenStream;
import com.rewrite.grammar.parse.Tokenizer.Token.TokenType;

public class GenericParser {
	private static enum PARSER_TYPE {
		PT_GROUP, PT_REPEATING, PT_OPTIONAL
	}

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

	public boolean accepts(String str) {
		return false; // TODO: do this
	}

	public SyntaxNode parse(String str) {
		return null;
	}

}
