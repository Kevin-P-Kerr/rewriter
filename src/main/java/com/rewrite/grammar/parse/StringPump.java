package com.rewrite.grammar.parse;

public class StringPump {
	private final String str;
	private int i;
	private final int l;

	public StringPump(String s) {
		this.str = s;
		this.l = s.length();
		this.i = 0;
	}

	public char getChar() throws IndexOutOfBoundsException {
		if (i >= l) {
			throw new IndexOutOfBoundsException();
		}
		int n = i;
		i++;
		return str.charAt(n);
	}

	public char peekChar() throws IndexOutOfBoundsException {
		if (i >= l) {
			throw new IndexOutOfBoundsException();
		}
		int n = i;

		char c = str.charAt(i);
		i = n;
		return c;
	}

	public boolean hasChar() {
		return i < l;
	}

	public int getIndex() {
		return i;
	}

	public void setIndex(int i) {
		this.i = i;
	}

	@Override
	public String toString() {
		return str;
	}

}
