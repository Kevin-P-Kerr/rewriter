package com.rewrite.grammar.parse;

public class StringPump {
	private final String str;
	private int i;
	private final int l;

	public StringPump(String s) {
		this.str = s;
		this.l = s.length();
		this.i = 0;
		killWhite();
	}

	private void killWhite() {
		while (i < l && Tokenizer.isWhite(str.charAt(i))) {
			i++;
		}
	}

	public char getChar() throws IndexOutOfBoundsException {
		if (i >= l) {
			throw new IndexOutOfBoundsException();
		}
		killWhite();
		int n = i;
		i++;
		return str.charAt(n);
	}

	public char peekChar() throws IndexOutOfBoundsException {
		if (i >= l) {
			throw new IndexOutOfBoundsException();
		}
		int n = i;
		killWhite();
		char c = str.charAt(i);
		i = n;
		return c;
	}

	public boolean hasChar() {
		killWhite();
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
