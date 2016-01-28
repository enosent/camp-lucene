package com.ysm

import org.bitbucket.eunjeon.seunjeon.Analyzer

object AnalyzerMain extends App {

	// 형태소 분석
	Analyzer.parse("아버지가방에들어가신다.").foreach(println)

	// 어절 분석
	Analyzer.parseEojeol("아버지가방에들어가신다.").foreach(println)
	// or
	Analyzer.parseEojeol(Analyzer.parse("아버지가방에들어가신다.")).foreach(println)

	/**
	 * 사용자 사전 추가
	 * surface,cost
	 *   surface: 단어
	 *   cost: 단어 출연 비용. 작을수록 출연할 확률이 높다.
	 */
	Analyzer.setUserDict(Seq("덕후", "버카충,-100", "낄끼빠빠").toIterator)
	Analyzer.parse("덕후냄새가 난다.").foreach(println)

	// 활용어 원형
	Analyzer.parse("슬픈").flatMap(_.deInflect()).foreach(println)

	// 복합명사 분해
	Analyzer.parse("삼성전자").flatMap(_.deCompound()).foreach(println)
	
	Analyzer.parse("Super Definition Crayon-Edge Brown 컨투어링 키트(뷰티스테이션 방송상품) 컨투어링 (JUMBC900-A-110BS)").foreach(println)

}