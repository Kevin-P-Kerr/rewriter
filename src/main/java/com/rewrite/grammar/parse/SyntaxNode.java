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
			sb.append(sn.toString());
		}
		sb.append(")");
		return sb.toString();
	}

	public void rollUp() {
		List<SyntaxNode> newChildren = Lists.newArrayList();
		List<SyntaxNode> toBeRemoved = Lists.newArrayList();
		for (SyntaxNode c : children) {
			c.rollUp();
			if (c.name == null && c.value == null) {
				newChildren.addAll(c.children);
				toBeRemoved.add(c);
			}
		}
		children.addAll(newChildren);
		children.removeAll(toBeRemoved);
	}

}
