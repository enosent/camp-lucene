package com.ysm.service;

import java.io.File;
import java.nio.file.Paths;
import java.util.Set;

import org.apache.lucene.analysis.ko.KoreanAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StoredField;
//import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.NIOFSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.google.common.collect.Sets;
import com.ysm.config.AnalyzeConfig;
import com.ysm.query.QueryProperties;

@Service
public class DealService {

	private static final int RESULT_SIZE = 10;

	private static final Logger logger = LoggerFactory.getLogger(DealService.class);

	private static final String INDEXED_OUTPUT = System.getProperty("user.dir") + File.separator + "indexed";
	
	@Autowired private JdbcTemplate jdbcTemplate;
	@Autowired private QueryProperties query;

	/**
	 * DB에서 조회된 데이터 색인
	 * 
	 * @return
	 */
	public int indexedDeals() {

		int numIndexed = 0;
		long start = System.currentTimeMillis();

		try ( 	KoreanAnalyzer analyzer = new AnalyzeConfig().index(); 
				IndexWriter writer = new IndexWriter(NIOFSDirectory.open(Paths.get(INDEXED_OUTPUT)), new IndexWriterConfig(analyzer).setOpenMode(OpenMode.CREATE)); ) {

			// # OpenMode.CREATE - 새로운 인덱스 파일 생성 ( 기존 인덱스 파일 삭제 )
			// # OpenMode.CREATE_OR_APPEND - 원래 있던 인덱스 파일에 문서 추가
			
			jdbcTemplate.query(query.getSelectDeals(), rs -> {
				long did = rs.getLong("DID");
				String dealname = rs.getString("DEALNAME");
				String hashtag = rs.getString("OSDIDS");

				Document doc = new Document();
				
				doc.add(new StoredField("did", did));
				doc.add(new TextField("dealname", dealname, Field.Store.YES));
//				doc.add(new StringField("dealname", dealname, Field.Store.YES));
				doc.add(new StoredField("hashtag", hashtag));

				try {
					writer.addDocument(doc);
				} catch (Exception e) {
					logger.error("# indexed fail - {}", e.getMessage());
				}
			});

			numIndexed = writer.numDocs();

			long end = System.currentTimeMillis();

			logger.info("Indexing {} text tokk {} ms", numIndexed, (end - start));

		} catch (Exception e) {
			logger.error("# exception - {}", e.getMessage());
		}

		return numIndexed;
	}

	/**
	 * Hashtag 조회
	 * 
	 * @param keyword
	 * @return
	 */
	public Set<String> searchHashtag(String keyword) {
		Set<String> list = Sets.newHashSet();
		
		try (	KoreanAnalyzer analyzer = new AnalyzeConfig().query();
				IndexReader reader = DirectoryReader.open(NIOFSDirectory.open(Paths.get(INDEXED_OUTPUT))); ){
			
			IndexSearcher searcher = new IndexSearcher(reader);

			QueryParser parser = new QueryParser("dealname", analyzer);
			Query q = parser.parse(keyword);
			
			logger.info("# {}", q);

			long start = System.currentTimeMillis();
			TopDocs hits = searcher.search(q, RESULT_SIZE);
			long end = System.currentTimeMillis();

			logger.info("Found {}  document(s) (in {}  ms) that matched query {}", hits.totalHits, (end - start), q.toString());

			for(ScoreDoc scoreDoc : hits.scoreDocs) {
				Document doc = searcher.doc(scoreDoc.doc);

				logger.info("# result - {}. {}", doc.get("did"), doc.get("dealname"));
				
				list.add(doc.get("hashtag"));
			}

		} catch (Exception e) {
			logger.error("# exception - {}", e.getMessage());
		}
		
		
		return list;
	}
	
}