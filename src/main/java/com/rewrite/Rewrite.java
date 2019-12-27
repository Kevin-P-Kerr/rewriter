package com.rewrite;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Scanner;

import com.rewrite.grammar.parse.EBNFParser;
import com.rewrite.grammar.parse.GenericParser;
import com.rewrite.grammar.parse.StringPump;
import com.rewrite.grammar.parse.SyntaxNode;
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
			fn = "test.rw";
		}
		BufferedReader bf = null;
		BufferedReader obf = null;
		try {
			InputStream languageSpec = Rewrite.class.getClassLoader().getResourceAsStream("lang.ebnf");

			Scanner langSpec = new Scanner(languageSpec);
			String langEBNF = "";
			while (langSpec.hasNextLine()) {
				langEBNF += langSpec.nextLine();
			}
			EBNFParser langParserParser = new EBNFParser(Tokenizer.tokenize(langEBNF));
			GenericParser langParser = langParserParser.parse().get(0);
			File file = new File(fn);
			FileReader r = new FileReader(file);
			obf = new BufferedReader(r);
			String grammarFile = obf.readLine();
			File gf = new File(grammarFile);
			FileReader rr = new FileReader(gf);
			bf = new BufferedReader(rr);

			StringBuilder sb = new StringBuilder();
			String s;
			while ((s = bf.readLine()) != null) {
				sb.append(s + " \n");
			}
			String input = sb.toString();
			TokenStream tokens = Tokenizer.tokenize(input);
			EBNFParser p = new EBNFParser(tokens);
			List<GenericParser> genericParsers = p.parse();
			GenericParser top = genericParsers.get(0);
			String program = "";
			s = "";
			while ((s = obf.readLine()) != null) {
				program += s + "\n";
			}
			SyntaxNode t = langParser.parse(new StringPump(program));
			t.rollUp();
			System.out.println(t);
			System.out.println(t.print());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				bf.close();
				obf.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.exit(0);
		}
	}

}
