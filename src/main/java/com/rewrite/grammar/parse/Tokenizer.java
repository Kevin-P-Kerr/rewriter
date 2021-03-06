package com.rewrite.grammar.parse;

import java.util.List;

import com.google.common.collect.Lists;
import com.rewrite.grammar.parse.Tokenizer.Token.TokenType;

public class Tokenizer {
	public static class Token {
		public enum TokenType {
			TT_TILDE, TT_SHEFFER, TT_LCURLY, TT_RCURLY, TT_LBRAK, TT_RBRAK, TT_LPAREN, TT_RPAREN, TT_VAR, TT_PERIOD, TT_EQUALS, TT_SINGLE_QUOTE, TT_DASH;
		}

		private final TokenType type;
		private final String literal;

		public Token(TokenType type, String lit) {
			this.type = type;
			this.literal = lit;
		}

		public TokenType getType() {
			return type;
		}

		public String getLit() {
			return literal;
		}

		@Override
		public String toString() {
			return type + " : " + literal;
		}
	}

	public static boolean isWhite(char c) {
		return c == ' ' || c == '\t' || c == '\n';
	}

	public static TokenStream tokenize(String ln) {

		List<Token> tokens = Lists.newArrayList();
		for (int i = 0, ii = ln.length(); i < ii; i++) {
			char c = ln.charAt(i);
			if (isWhite(c)) {
				continue;
			}
			if (c == '#') { // it's a comment--move to new line
				while (c != '\n' && i < ii) {
					i++;
					c = ln.charAt(i);
				}
				continue;
			}
			String lit = Character.toString(c);
			TokenType type;
			if (c == '{') {
				type = TokenType.TT_LCURLY;
			} else if (c == '}') {
				type = TokenType.TT_RCURLY;
			} else if (c == '(') {
				type = TokenType.TT_LPAREN;
			} else if (c == ')') {
				type = TokenType.TT_RPAREN;
			} else if (c == '[') {
				type = TokenType.TT_LBRAK;
			} else if (c == ']') {
				type = TokenType.TT_RBRAK;
			} else if (c == '.') {
				type = TokenType.TT_PERIOD;
			} else if (c == '=') {
				type = TokenType.TT_EQUALS;
			} else if (c == '\'') {
				type = TokenType.TT_SINGLE_QUOTE;
			} else if (c == '|') {
				type = TokenType.TT_SHEFFER;
			} else if (c == '-') {
				type = TokenType.TT_DASH;
			} else if (c == '~') {
				type = TokenType.TT_TILDE;
			} else {
				type = TokenType.TT_VAR;
				i++;
				while (i < ii && !isWhite((c = ln.charAt(i)))) {
					if (isReserved(c)) {
						i--;
						break;
					}
					lit += Character.toString(c);
					i++;
				}
			}
			Token t = new Token(type, lit);
			tokens.add(t);

		}
		return new TokenStream(tokens);

	}

	private static boolean isReserved(char c) {
		return c == '-' || c == ':' || c == '+' || c == '*' || c == '(' || c == ')' || c == '.' || c == '{' || c == '}'
				|| c == '[' || c == ']' || c == '|' || c == '\'';
	}

	public static class TokenStream {
		private final List<Token> tokens;
		private int ptr = 0;

		public TokenStream(List<Token> tokens) {
			this.tokens = tokens;
		}

		public boolean hasToken() {
			return tokens.size() > ptr;
		}

		public Token getNext() {
			Token t = tokens.get(ptr);
			ptr++;
			return t;
		}

		/**
		 * get the next token without incrementing the token counter
		 */
		public Token peek() {
			return tokens.get(ptr);
		}

		@Override
		public String toString() {
			String ret = "";
			for (Token t : tokens) {
				ret += t.toString();
				ret += ", ";
			}
			return ret;
		}

		public boolean lookAhead(int i) {
			return tokens.size() > (ptr + i);
		}

		public TokenStream copy() {
			return new TokenStream(tokens);
		}
	}

	public static boolean isAlpha(char c) {
		return ('a' <= c && c <= 'z') || ('A' <= c && c <= 'Z');
	}

}
