package com.rewrite;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import com.rewrite.grammar.parse.EBNFParser;
import com.rewrite.grammar.parse.GenericParser;
import com.rewrite.grammar.parse.Tokenizer;
import com.rewrite.grammar.parse.Tokenizer.TokenStream;

public class Rewrite {

	public static void main(String args[]) {
		String fn;
		if (args.length == 0) {
			fn = null;
		} else {
			fn = args[0];
		}
		if (fn == null) {
			fn = "test.ebnf";
		}
		BufferedReader bf = null;
		try {
			File file = new File(fn);
			FileReader r = new FileReader(file);
			bf = new BufferedReader(r);
			StringBuilder sb = new StringBuilder();
			String s;
			while ((s = bf.readLine()) != null) {
				sb.append(s + " \n");
			}
			String input = sb.toString();
			TokenStream tokens = Tokenizer.tokenize(input);
			EBNFParser p = new EBNFParser(tokens);
			List<GenericParser> genericParsers = p.parse();
			System.out.println(genericParsers.size());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				bf.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.exit(0);
		}
	}

}
