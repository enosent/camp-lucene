package com.ysm.main;

import java.io.IOException;
import java.io.StringReader;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;

import com.ysm.config.AnalyzeConfig;

public class AnalyzerMain {

	@SuppressWarnings("resource")
	public static void main(String[] args) throws Exception {

		// keyword
		String text = "Super Definition Crayon-Edge Brown 컨투어링 키트(뷰티스테이션 방송상품) 컨투어링 (JUMBC900-A-110BS)";

		// WhitespaceAnalyzer
		System.out.println(":: # WhitespaceAnalyzer ::");
		showToken( new WhitespaceAnalyzer().tokenStream("test", new StringReader(text)) );

		// SimpleAnalyzer
		System.out.println("\n:: # SimpleAnalyzer ::");
		showToken( new SimpleAnalyzer().tokenStream("test", new StringReader(text)) );

		// StopAnalyzer
		System.out.println("\n:: # StopAnalyzer ::");
		showToken( new StopAnalyzer().tokenStream("test", new StringReader(text)) );

		// StandardAnalyzer
		System.out.println("\n:: # StandardAnalyzer ::");
		showToken( new StandardAnalyzer().tokenStream("test", new StringReader(text)) );

		// KeywordAnalyzer
		System.out.println("\n:: # KeywordAnalyzer ::");
		showToken( new KeywordAnalyzer().tokenStream("test", new StringReader(text)) );

		// KoreanAnalyzer
		System.out.println("\n:: # KoreanAnalyzer ::");
		showToken( new AnalyzeConfig().index().tokenStream("test", new StringReader(text)) );
		
	}

	private static void showToken(TokenStream tokenStream) throws IOException {
		CharTermAttribute terms = tokenStream.addAttribute(CharTermAttribute.class);
		TypeAttribute type = tokenStream.addAttribute(TypeAttribute.class);
		
		tokenStream.reset();

		while(tokenStream.incrementToken()) {
			String term = terms.toString();  
			System.out.println(term + " (" + type.type() + ")");  
		}
	}

}