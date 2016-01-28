package com.ysm.main;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import org.apache.lucene.analysis.ko.KoreanAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;

public class Searcher {

	public static void main(String[] args) throws Exception {

		String indexDir = System.getProperty("user.home") + File.separator + "MeetLucene";
		String q = "양말";

		search(indexDir, q);
	}

	public static void search(String indexDir, String q) throws IOException, ParseException {
		IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(indexDir)));
		IndexSearcher searcher = new IndexSearcher(reader);
		
		KoreanAnalyzer analyzer = new KoreanAnalyzer();

	    analyzer.setHasOrigin(false);
	    analyzer.setOriginCNoun(false);
	    analyzer.setQueryMode(true);
	    
	    QueryParser parser = new QueryParser("dealname", analyzer);
		Query query = parser.parse(q);
		
		System.out.println( query.toString() );

		long start = System.currentTimeMillis();
		TopDocs hits = searcher.search(query, 10);
		long end = System.currentTimeMillis();

		System.err.println("Found " + hits.totalHits + " document(s) (in " + (end - start) + " milliseconds) that matched query '" + q + "':");

		for(ScoreDoc scoreDoc : hits.scoreDocs) {
			Document doc = searcher.doc(scoreDoc.doc);
			
			System.out.println(doc.get("dealname"));
		}

		reader.close();
	}
}