package com.rewrite.grammar.parse;

import java.util.Map;
import com.rewrite.grammar.parse.Tokenizer.Token;
import com.rewrite.grammar.parse.Tokenizer.TokenStream;
import com.rewrite.grammar.parse.Tokenizer.Token.TokenType;

public class GenericParser {
	private static enum PARSER_TYPE {
		PT_RANGE, PT_DISJ, PT_GROUP, PT_REPEATING, PT_OPTIONAL, PT_LITERAL, PT_NAMED;
	}

	private TokenStream src;

	private static class GenericParserTuple {
		PARSER_TYPE type;
		private GenericParserTuple next;
		private GenericParser parser;
		private String name;

		public GenericParserTuple(GenericParser gp, PARSER_TYPE type) {
			this.type = type;
			this.parser = gp;
		}
	}

	private String name = null;

	public void setName(String n) {
		this.name = n;
	}

	private GenericParserTuple head;
	private GenericParserTuple current = head;

	public static GenericParser from(TokenStream tokens) throws Exception {
		GenericParser ret = new GenericParser();
		ret.src = tokens;
		ret.head = new GenericParserTuple(new NamedStubParser("head"), null);
		ret.current = ret.head;
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
				GenericParser n;
				if (name.equals("WhiteSpace")) {
					n = new WhiteSpaceParser();
				} else {
					n = new NamedStubParser(name);
				}
				GenericParserTuple tuple = new GenericParserTuple(n, PARSER_TYPE.PT_NAMED);
				tuple.name = name;
				ret.current.next = tuple;
				ret.current = ret.current.next;
			} else if (t.getType() == TokenType.TT_SINGLE_QUOTE) {
				t = tokens.getNext();

				String l = t.getLit();
				t = tokens.getNext();
				while (t.getType() != TokenType.TT_SINGLE_QUOTE) {
					l += t.getLit();
					t = tokens.getNext();
				}
				GenericParser n = new LiteralParser(l);
				GenericParserTuple tuple = new GenericParserTuple(n, PARSER_TYPE.PT_LITERAL);
				ret.current.next = tuple;
				ret.current = ret.current.next;
			} else if (t.getType() == TokenType.TT_SHEFFER) {
				GenericParser gp = from(tokens);
				GenericParser ggp = from(tokens);
				DisjunctiveParser dp = new DisjunctiveParser(gp, ggp);
				GenericParserTuple tuple = new GenericParserTuple(dp, PARSER_TYPE.PT_DISJ);
				ret.current.next = tuple;
				ret.current = ret.current.next;
			} else if (t.getType() == TokenType.TT_DASH) {
				GenericParser gp = from(tokens).head.parser;
				GenericParser ggp = from(tokens).head.parser;
				if (!((gp instanceof LiteralParser) && (ggp instanceof LiteralParser))) {
					throw new Exception();
				}
				LiteralParser start = (LiteralParser) gp;
				LiteralParser finish = (LiteralParser) ggp;
				if (!start.isChar() || !finish.isChar()) {
					throw new Exception();
				}
				RangedParser rp = new RangedParser(start.getFirstChar(), finish.getFirstChar());
				GenericParserTuple tuple = new GenericParserTuple(rp, PARSER_TYPE.PT_RANGE);
				ret.current.next = tuple;
				ret.current = ret.current.next;
			} else {
				throw new Exception(t.getType().toString());
			}
			t = tokens.getNext();
		}
		ret.head = ret.head.next;
		return ret;
	}

	public boolean accepts(StringPump pump) {
		GenericParserTuple gpt = head;
		while (gpt != null) {
			int n = pump.getIndex();
			if (!gpt.parser.accepts(pump)) {
				if (gpt.type != PARSER_TYPE.PT_OPTIONAL) {
					return false;
				} else {
					pump.setIndex(n);
				}
			} else if (gpt.type == PARSER_TYPE.PT_REPEATING) {
				n = pump.getIndex();
				while (pump.hasChar()) {
					if (gpt.parser.accepts(pump)) {
						n = pump.getIndex();
					} else {
						pump.setIndex(n);
						break;
					}
				}
			}
			gpt = gpt.next;
		}
		return true;
	}

	public SyntaxNode parse(StringPump pump) throws Exception {

		SyntaxNode sn = new SyntaxNode(name);

		GenericParserTuple gpt = head;
		while (gpt != null) {
			int n = pump.getIndex();
			SyntaxNode node = gpt.parser.parse(pump);
			if (node == null) {
				if (gpt.type != PARSER_TYPE.PT_OPTIONAL) {
					return null;
				} else {
					pump.setIndex(n);
				}
			} else {
				sn.addChild(node);
				if (gpt.type == PARSER_TYPE.PT_REPEATING) {
					n = pump.getIndex();
					while (pump.hasChar()) {
						node = gpt.parser.parse(pump);
						if (node != null) {
							n = pump.getIndex();
							sn.addChild(node);
						} else {
							pump.setIndex(n);
							break;
						}
					}
				}

			}
			gpt = gpt.next;
		}
		return sn;
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
					|| gpt.type == PARSER_TYPE.PT_GROUP || gpt.type == PARSER_TYPE.PT_DISJ
					|| gpt.type == PARSER_TYPE.PT_RANGE) {
				gpt.parser.unStub(namedParsers);
			}
			gpt = gpt.next;
		}
	}
}
