package org.test.hecjlucene.core08_filter;

import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.util.Version;
import org.test.hecjlucene.core05.LuceneIndexUtil;
import org.test.hecjlucene.core07_advance_search.CustomParser;

/**
 * @类功能说明：自定义Filter查询
 * @类修改者：
 * @修改日期：
 * @修改说明：
 * @作者：HECJ
 * @创建时间：2014年11月16日 下午6:46:16
 * @版本：V1.0
 */
public class CustomFilterQuery {

	/**
	 * @函数功能说明 自定义Filter查询
	 * @修改作者名字 HECJ  
	 * @修改时间 2014年11月16日
	 * @修改内容
	 * @参数： @param queryStr    
	 * @return void   
	 * @throws
	 */
	public void searcherByFilterQuery(String queryStr) {

		IndexSearcher searcher = new LuceneIndexUtil().getSearcher();
		CustomParser parser = new CustomParser(Version.LUCENE_35, "content",
				new StandardAnalyzer(Version.LUCENE_35));
		try {
			// 相当于创建一个查询语句
			Query query = parser.parse(queryStr);
			// 执行查询
			TopDocs topDocs = null;
			//使用自定义的Filter
			topDocs = searcher.search(query,new MyIdFilter(new FilterAccessorImp()), 100);
//			topDocs = searcher.search(query, 100);
			System.out.println(query);
			ScoreDoc[] scoreDocs = topDocs.scoreDocs;
			for (ScoreDoc sd : scoreDocs) {
				Document doc = searcher.doc(sd.doc);
				System.out.println(sd.doc+"-->score:"+sd.score+"-->filename:"+doc.get("filename")+"-->path:"+doc.get("path")+"-->size:"+doc.get("size")+"-->Id:"+doc.get("id"));
			}

		} catch (ParseException e) {
			System.err.println(e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				searcher.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
	
	
}
