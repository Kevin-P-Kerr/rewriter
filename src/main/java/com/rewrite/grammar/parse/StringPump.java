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
		while (Tokenizer.isWhite(str.charAt(i))) {
			i++;
			if (i >= l) {
				return;
			}
		}
	}

	public char getChar() throws IndexOutOfBoundsException {
		if (i >= l) {
			throw new IndexOutOfBoundsException();
		}
		int n = i;
		i++;
		killWhite();
		return str.charAt(n);
	}

	public char peekChar() throws IndexOutOfBoundsException {
		if (i >= l) {
			throw new IndexOutOfBoundsException();
		}
		int n = i;
		return str.charAt(n);
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
}
