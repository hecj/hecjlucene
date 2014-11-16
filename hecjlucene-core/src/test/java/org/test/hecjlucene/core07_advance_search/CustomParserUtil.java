package org.test.hecjlucene.core07_advance_search;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.util.Version;
import org.test.hecjlucene.core05.LuceneIndexUtil;

public class CustomParserUtil {
	
	private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	/**
	 * @函数功能说明 自定义QueryParser
	 * @修改作者名字 HECJ  
	 * @修改时间 2014年11月16日
	 * @修改内容
	 * @参数： @param queryStr
	 * @参数： @param filter    
	 * @return void   
	 * @throws
	 */
	public void searcherByCustomQuery(String queryStr){
		
		IndexSearcher searcher = new LuceneIndexUtil().getSearcher();
		CustomParser parser = new CustomParser(Version.LUCENE_35, "content", new StandardAnalyzer(Version.LUCENE_35));
		try {
			//相当于创建一个查询语句
			Query query = parser.parse(queryStr);
			//执行查询
			TopDocs topDocs = null;
			topDocs = searcher.search(query, 50);
			System.out.println(query);
			ScoreDoc[] scoreDocs = topDocs.scoreDocs;
			for(ScoreDoc sd : scoreDocs){
				Document doc = searcher.doc(sd.doc);
				System.out.println(sd.doc+" ==== score:("+sd.score+") ====    filename:"+doc.get("filename")+" ====   path:"+doc.get("path")+" ====  size:"+doc.get("size")+"   date:"+format.format(new Date(Long.valueOf(doc.get("date"))))+" === myScore:"+doc.get("score"));
			}
			 
		} catch (ParseException e) {
			System.err.println(e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				searcher.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
}
