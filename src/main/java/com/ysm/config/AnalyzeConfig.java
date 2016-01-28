package com.ysm.config;

import org.apache.lucene.analysis.ko.KoreanAnalyzer;

public class AnalyzeConfig {

	/**
	 * 색인용
	 * @return
	 */
	public KoreanAnalyzer index() {
		KoreanAnalyzer analyzer = new KoreanAnalyzer();
		
		analyzer.setHasOrigin(true);
		analyzer.setOriginCNoun(true);
		analyzer.setQueryMode(false);
		
		return analyzer;
	}
	
	/**
	 * 조회용
	 * @return
	 */
	public KoreanAnalyzer query() {
		KoreanAnalyzer analyzer = new KoreanAnalyzer();
		
		analyzer.setHasOrigin(false);
		analyzer.setOriginCNoun(false);
		analyzer.setQueryMode(true);
		
		return analyzer;
	}
	
}
