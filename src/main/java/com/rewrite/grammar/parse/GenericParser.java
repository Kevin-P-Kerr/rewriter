package com.rewrite.grammar.parse;

import java.util.Map;

import javax.naming.OperationNotSupportedException;

import com.rewrite.grammar.parse.Tokenizer.Token;
import com.rewrite.grammar.parse.Tokenizer.TokenStream;
import com.rewrite.grammar.parse.Tokenizer.Token.TokenType;

public class GenericParser {
	private static enum PARSER_TYPE {
		PT_GROUP, PT_REPEATING, PT_OPTIONAL, PT_LITERAL, PT_NAMED;
	}

	private static class GenericParserTuple {
		PARSER_TYPE type;
		private GenericParserTuple next;
		private GenericParser parser;
		private String name = null;

		public GenericParserTuple(GenericParser gp, PARSER_TYPE type) {
			this.type = type;
			this.parser = gp;
		}
	}

	private GenericParserTuple head = new GenericParserTuple(new NamedStubParser("head"), null);
	private GenericParserTuple current = head;

	public static GenericParser from(TokenStream tokens) throws Exception {
		GenericParser ret = new GenericParser();
		Token t = tokens.getNext();
		while (t.getType() != TokenType.TT_PERIOD) {
			if (t.getType() == TokenType.TT_LCURLY) {
				// repeated field
				GenericParser gp = from(tokens);
				GenericParserTuple tuple = new GenericParserTuple(gp, PARSER_TYPE.PT_REPEATING);
				ret.current.next = tuple;
				ret.current = ret.current.next;
				t = tokens.getNext();
				if (t.getType() != TokenType.TT_RCURLY) {
					throw new Exception();
				}
			} else if (t.getType() == TokenType.TT_LBRAK) {
				// repeated field
				GenericParser gp = from(tokens);
				GenericParserTuple tuple = new GenericParserTuple(gp, PARSER_TYPE.PT_OPTIONAL);
				ret.current.next = tuple;
				ret.current = ret.current.next;
				t = tokens.getNext();
				if (t.getType() != TokenType.TT_RBRAK) {
					throw new Exception();
				}
			} else if (t.getType() == TokenType.TT_LPAREN) {
				// repeated field
				GenericParser gp = from(tokens);
				GenericParserTuple tuple = new GenericParserTuple(gp, PARSER_TYPE.PT_GROUP);
				ret.current.next = tuple;
				ret.current = ret.current.next;
				t = tokens.getNext();
				if (t.getType() != TokenType.TT_RPAREN) {
					throw new Exception();
				}
			} else if (t.getType() == TokenType.TT_VAR) {
				String name = t.getLit();
				GenericParser n = new NamedStubParser(name);
				GenericParserTuple tuple = new GenericParserTuple(n, PARSER_TYPE.PT_NAMED);
				tuple.name = name;
				ret.current.next = tuple;
				ret.current = ret.current.next;
			} else {
				throw new Exception("wtf");
			}
		}
		ret.head = ret.head.next;
		return ret;
	}

	public boolean accepts(String str) {
		return false; // TODO: do this
	}

	public SyntaxNode parse(String str) throws OperationNotSupportedException {
		return null;
	}

	public void unStub(Map<String, GenericParser> namedParsers) throws Exception {
		GenericParserTuple gpt = head;
		while (gpt != null) {
			if (gpt.parser instanceof NamedStubParser) {
				NamedStubParser nsp = (NamedStubParser) gpt.parser;
				String name = nsp.getName();
				GenericParser gp = namedParsers.get(name);
				if (gp == null) {
					throw new Exception();
				}
				gpt.parser = gp;
			} else if (gpt.type == PARSER_TYPE.PT_OPTIONAL || gpt.type == PARSER_TYPE.PT_REPEATING
					|| gpt.type == PARSER_TYPE.PT_GROUP) {
				gpt.parser.unStub(namedParsers);
			}
			gpt = gpt.next;
		}
	}

}
