package com.rewrite.lang;

import java.util.Map;

import com.google.common.collect.Maps;
import com.rewrite.grammar.parse.GenericParser;
import com.rewrite.grammar.parse.SyntaxNode;

public class Interpreter {
	private static class Environment {
		private Map<String, SyntaxNode> valueMap = Maps.newHashMap();
		private Map<String, GenericParser> parseMap = Maps.newHashMap();
		private final Environment upper;

		public Environment(Map<String, GenericParser> namedParsers) {
			this.parseMap = namedParsers;
			this.upper = null;
		}

		public Environment(Environment e) {
			this.upper = e;
		}

		public GenericParser getParser(String k) {
			GenericParser p = parseMap.get(k);
			if (p == null) {
				if (upper == null) {
					return null;
				}
				return upper.getParser(k);
			}
			return p;
		}

		public SyntaxNode getSyntax(String k) {
			SyntaxNode s = valueMap.get(k);
			if (s == null) {
				if (upper == null) {
					return null;
				}
				return upper.getSyntax(k);
			}
			return s;
		}

		public void put(String k, SyntaxNode n) {
			valueMap.put(k, n);
		}
	}

	private static void eval(SyntaxNode s, Environment e) {

	}

	public static void eval(SyntaxNode s, Map<String, GenericParser> namedParsers) {
		Environment e = new Environment(namedParsers);
		eval(s, e);
	}
}
