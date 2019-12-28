package com.rewrite.lang;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.rewrite.grammar.parse.GenericParser;
import com.rewrite.grammar.parse.StringPump;
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

	private static List<SyntaxNode> collectArguments(SyntaxNode args, Environment e) throws Exception {
		List<SyntaxNode> ret = Lists.newArrayList();
		for (SyntaxNode c : args.getChildren()) {
			if (!c.getName().equals("Arg")) {
				throw new Exception();
			}
			SyntaxNode evaluated = evalExpr(c, e);
			ret.add(evaluated);
		}
		return ret;
	}

	private static String collectStringFromString(SyntaxNode str) {
		return str.getValue();
	}

	private static void assertName(SyntaxNode s, String n) throws Exception {
		if (!s.getName().equals(n)) {
			throw new Exception();
		}
	}

	private static SyntaxNode evalRewrite(SyntaxNode body, SyntaxNode s, Environment e) throws Exception {
		for (SyntaxNode c : body.getChildren()) {
			assertName(c, "BodyPair");
			String template = collectStringFromString(c.getChildren().get(0));
			// String toBeReplaced = collectStringFromString(c.getChi)
		}
		return s;
	}

	private static SyntaxNode evalInvoke(SyntaxNode s, Environment e) throws Exception {
		SyntaxNode funcName = s.getChildren().get(0);
		assertName(funcName, "FuncName");
		SyntaxNode varName = funcName.getChildren().get(0);
		assertName(varName, "VarName");
		String k = collectStringFromVarName(varName);
		SyntaxNode args = s.getChildren().get(2);
		if (!args.getName().equals("Args")) {
			throw new Exception();
		}
		List<SyntaxNode> arguments = collectArguments(args, e);
		GenericParser gp = e.getParser(k);
		if (gp != null) {
			if (arguments.size() > 1) {
				throw new Exception();
			}
			SyntaxNode str = arguments.get(0);
			assertName(str, "String");
			String toBeParsed = collectStringFromString(str);
			return gp.parse(new StringPump(toBeParsed));
		}
		SyntaxNode found = e.getSyntax(k);
		return evalRewrite(found, s, e);
	}

	private static SyntaxNode evalExpr(SyntaxNode s, Environment e) throws Exception {
		assertName(s, "Expr");
		s = s.getChildren().get(0);
		switch (s.getName()) {
		case "AssignExpr":
			return evalAssign(s, e);
		case "InvExpr":
			return evalInvoke(s, e);

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
		assertName(s, "Program");
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
