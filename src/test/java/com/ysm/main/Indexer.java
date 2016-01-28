package com.ysm.main;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.lucene.analysis.ko.KoreanAnalyzer;
//import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class Indexer {
	public static final String FILE_NAME = "small_set.txt";

	public static final String LOG_FILE = System.getProperty("user.home") + File.separator + FILE_NAME;

	public static void main(String[] args) throws Exception {

		String indexDir = System.getProperty("user.home") + File.separator + "MeetLucene";
		String file = LOG_FILE;

		long start = System.currentTimeMillis();
		Indexer indexer = new Indexer(indexDir);
		int numIndexed = 0;

		try {
			numIndexed = indexer.index(file);
		} catch(Exception e) {
			System.out.println( e.getMessage() );
		} finally {
			indexer.close();
		}

		long end = System.currentTimeMillis();

		System.out.println("Indexing " + numIndexed + " text took " + (end - start) + " milliseconds");
	}

	private IndexWriter writer;

	public Indexer(String indexDir) throws IOException {
		Path path = Paths.get(indexDir);
		Directory dir = FSDirectory.open(path);

		KoreanAnalyzer analyzer = new KoreanAnalyzer();
		analyzer.setHasOrigin(true);
		analyzer.setOriginCNoun(true);
		analyzer.setQueryMode(false);

		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		writer = new IndexWriter(dir, config);
	}

	public void close() throws IOException {
		writer.close();
	}

	public int index(String file) throws Exception {
		Path path = Paths.get(file);
		getDocument( path );

		System.out.println( writer.numDocs() );

		return writer.numDocs();
	}

	protected void getDocument(Path path) throws Exception {

		Files.readAllLines(path).forEach(value -> {

			try {
				String[] lines = value.split(",");

				if(lines.length == 4) {
					String keyword = lines[1];

					Document doc = new Document();
					doc.add(new TextField("keyword", keyword, Field.Store.YES));
					
					System.out.println( doc );
					writer.addDocument(doc);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}


		});

	}

}