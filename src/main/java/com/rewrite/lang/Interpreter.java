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

	private static char getCharFromChar(SyntaxNode sn) {
		return sn.getChildren().get(1).getValue().charAt(0);
	}

	private static String collectStringFromVarName(SyntaxNode var) throws Exception {
		StringBuilder sb = new StringBuilder();
		for (SyntaxNode c : var.getChildren()) {
			if (!c.getName().equals("Char")) {
				throw new Exception();
			}
			char ch = getCharFromChar(c);
			sb.append(ch);
		}
		return sb.toString();
	}

	private static SyntaxNode evalAssign(SyntaxNode s, Environment e) throws Exception {
		SyntaxNode varName = s.getChildren().get(0);
		if (!varName.getName().equals("VarName")) {
			throw new Exception();
		}
		SyntaxNode v = evalExpr(s.getChildren().get(1), e);
		String k = collectStringFromVarName(varName);
		e.put(k, v);
		return v;
	}

	private static SyntaxNode evalExpr(SyntaxNode s, Environment e) throws Exception {
		if (!s.getName().equals("Expr")) {
			throw new Exception();
		}
		s = s.getChildren().get(0);
		switch (s.getName()) {
		case "AssignExpr":
			return evalAssign(s, e);
		case "InvExpr":
			break;
		case "DefExpr":
			break;
		case "ValExpr":
			break;
		default:
			throw new Exception();

		}
		return null;
	}

	private static void eval(SyntaxNode s, Environment e) throws Exception {
		if (!s.getName().equals("Program")) {
			throw new Exception();
		}
		for (SyntaxNode c : s.getChildren()) {
			SyntaxNode g = evalExpr(c, e);
			System.out.println(g.print());
		}
	}

	public static void eval(SyntaxNode s, Map<String, GenericParser> namedParsers) throws Exception {
		Environment e = new Environment(namedParsers);
		eval(s, e);
	}
}
