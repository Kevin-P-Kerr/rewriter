package com.rewrite.grammar.parse;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.rewrite.grammar.parse.Tokenizer.Token;
import com.rewrite.grammar.parse.Tokenizer.Token.TokenType;
import com.rewrite.grammar.parse.Tokenizer.TokenStream;

public class EBNFParser {
	private final TokenStream tokens;

	public EBNFParser(TokenStream t) {
		this.tokens = t;
	}

	public List<GenericParser> parse() throws Exception {
		List<GenericParser> ret = Lists.newArrayList();
		Map<String, GenericParser> namedParsers = Maps.newHashMap();
		while (tokens.hasToken()) {
			Token t = tokens.getNext();
			if (t.getType() != TokenType.TT_VAR) {
				throw new Exception();
			}
			String name = t.getLit();
			t = tokens.getNext();
			if (t.getType() != TokenType.TT_EQUALS) {
				throw new Exception();
			}
			GenericParser gp = GenericParser.from(tokens);
			gp.setName(name);
			namedParsers.put(name, gp);
			ret.add(gp);
		}
		for (GenericParser gp : ret) {
			gp.unStub(namedParsers);
		}
		return ret;
	}
}
