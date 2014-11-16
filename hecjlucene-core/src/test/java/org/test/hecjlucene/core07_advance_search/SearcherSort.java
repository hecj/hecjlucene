package org.test.hecjlucene.core07_advance_search;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.util.Version;

public class SearcherSort {
	
	
	private static IndexReader reader = null;
	private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	/**
	 * 静态代码块获取IndexReader
	 
	static{
		try {
			reader = IndexReader.open(LuceneIndexUtil.getDirectory());
			
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}*/
	
	/**
	 * @函数功能说明 得到IndexSearcher
	 * @修改作者名字 HECJ  
	 * @修改时间 2014年11月11日
	 * @修改内容
	 * @参数： @return    
	 * @return IndexSearcher   
	 * @throws
	 */
	public static IndexSearcher getSearcher(){

		try {
			if(reader == null){
					reader = IndexReader.open(LuceneIndexUtil.getDirectory());
			}else{
				IndexReader readTemp = IndexReader.openIfChanged(reader);
				if(readTemp != null ){
					reader.close();
					reader = readTemp ;
				}
			}
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new IndexSearcher(reader);
	}
	
	
	/**
	 * @函数功能说明 排序
	 * @修改作者名字 HECJ  
	 * @修改时间 2014年11月11日
	 * @修改内容
	 * @参数： @param queryStr
	 * @参数： @param sort    
	 * @return void   
	 * @throws
	 */
	public void searcher(String queryStr,Sort sort){
		
		IndexSearcher searcher = getSearcher();
		QueryParser parser = new QueryParser(Version.LUCENE_35, "content", new StandardAnalyzer(Version.LUCENE_35));
		try {
			//相当于创建一个查询语句
			Query query = parser.parse(queryStr);
			//执行查询
			TopDocs topDocs = null;
			if(sort != null){
				topDocs = searcher.search(query, 50, sort);
			}else{
				topDocs = searcher.search(query, 50);
			}
			
			ScoreDoc[] scoreDocs = topDocs.scoreDocs;
			for(ScoreDoc sd : scoreDocs){
				Document doc = searcher.doc(sd.doc);
				System.out.println(sd.doc+" ==== score:("+sd.score+") ====    filename:"+doc.get("filename")+" ====   path:"+doc.get("path")+" ====  size:"+doc.get("size")+"   date:"+format.format(new Date(Long.valueOf(doc.get("date")))));
			}
			
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			try {
				searcher.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	/**
	 * @函数功能说明 过滤
	 * @修改作者名字 HECJ  
	 * @修改时间 2014年11月11日
	 * @修改内容
	 * @参数： @param queryStr
	 * @参数： @param filter    
	 * @return void   
	 * @throws
	 */
	public void searcherFilter(String queryStr,Filter filter){
		
		IndexSearcher searcher = getSearcher();
		QueryParser parser = new QueryParser(Version.LUCENE_35, "content", new StandardAnalyzer(Version.LUCENE_35));
		try {
			//相当于创建一个查询语句
			Query query = parser.parse(queryStr);
			//执行查询
			TopDocs topDocs = null;
			if(filter != null){
				topDocs = searcher.search(query, filter, 50);
			}else{
				topDocs = searcher.search(query, 50);
			}
			
			ScoreDoc[] scoreDocs = topDocs.scoreDocs;
			for(ScoreDoc sd : scoreDocs){
				Document doc = searcher.doc(sd.doc);
				System.out.println(sd.doc+" ==== score:("+sd.score+") ====    filename:"+doc.get("filename")+" ====   path:"+doc.get("path")+" ====  size:"+doc.get("size")+"   date:"+format.format(new Date(Long.valueOf(doc.get("date"))))+" === myScore:"+doc.get("score"));
			}
			 
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			try {
				searcher.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	
}
