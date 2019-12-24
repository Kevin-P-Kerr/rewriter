package com.rewrite.grammar.parse;

import com.rewrite.grammar.parse.Tokenizer.TokenStream;

public class EBNFParser {
	private final TokenStream tokens;

	public EBNFParser(TokenStream t) {
		this.tokens = t;
	}
}
