package com.rewrite.grammar.parse;

import java.util.List;

import com.google.common.collect.Lists;

public class SyntaxNode {

	private List<SyntaxNode> children = Lists.newArrayList();
	private String name;
	private String value;

	public SyntaxNode(String name) {
		this.name = name;
	}

	public SyntaxNode(String name, String literal) {
		this.name = name;
		this.value = literal;
	}

	public void addChild(SyntaxNode c) {
		children.add(c);
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (value != null) {
			sb.append("(" + value);
		} else {
			sb.append("(" + name);
		}
		for (SyntaxNode sn : children) {
			sb.append("\n");
			sb.append(sn.toString(1));
		}
		sb.append(")");
		return sb.toString();
	}

	private Object toString(int i) {
		int l = i;
		String tab = "";
		while (i > 0) {
			tab += ' ';
			i--;
		}
		StringBuilder sb = new StringBuilder();
		if (value != null) {
			sb.append(tab + "(" + value);
		} else {
			sb.append(tab + "(" + name);
		}
		for (SyntaxNode sn : children) {
			sb.append("\n");
			sb.append(sn.toString(l + 1));
		}
		sb.append(")");
		return sb.toString();
	}

	public void rollUp() {
		List<SyntaxNode> newChildren = Lists.newArrayList();
		for (SyntaxNode c : children) {
			c.rollUp();
			if (c.name == null && c.value == null) {
				newChildren.addAll(c.children);
			} else {
				newChildren.add(c);
			}
		}
		children = newChildren;
	}

	public String print() {
		StringBuilder sb = new StringBuilder();
		if (value != null) {
			sb.append(value);
		}
		for (SyntaxNode c : children) {
			sb.append(c.print());
		}
		return sb.toString();
	}

}
