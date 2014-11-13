package org.test.hecjlucene.core07_advance_search;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.FieldCache;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.function.CustomScoreProvider;
import org.apache.lucene.search.function.CustomScoreQuery;
import org.apache.lucene.search.function.FieldScoreQuery;
import org.apache.lucene.search.function.FieldScoreQuery.Type;
import org.apache.lucene.search.function.ValueSourceQuery;

/**
 * @类功能说明：自定义评分规则 写自己的Query
 * @类修改者：
 * @修改日期：
 * @修改说明：
 * @作者：HECJ
 * @创建时间：2014年11月12日 上午10:13:23
 * @版本：V1.0
 */
public class MyScoreQuery {
	
	private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public void searchByScoreQuery(){
		
		try {
			IndexSearcher searcher = new IndexSearcher(IndexReader.open(LuceneIndexUtil.getDirectory()));
			Query query = new TermQuery(new Term("content","null"));
			//创建一个评分域
			FieldScoreQuery fieldScoreQuery = new FieldScoreQuery("score", Type.INT);
			//根据评分域和原有的Query创建自定义的Query对象
			MyCustomScoreQuery customScoreQuery = new MyCustomScoreQuery(query,fieldScoreQuery);
			//文件评分
			//查询
			TopDocs topDocs = searcher.search(customScoreQuery, 100);
			ScoreDoc[] scoreDocs = topDocs.scoreDocs;
			for(ScoreDoc sd : scoreDocs){
				Document doc = searcher.doc(sd.doc);
				System.out.println(sd.doc+" ==== score:("+sd.score+") ====    filename:"+doc.get("filename")+" ====   path:"+doc.get("path")+" ====  size:"+doc.get("size")+"   date:"+format.format(new Date(Long.valueOf(doc.get("date"))))+"======score:"+doc.get("score"));
			}
			searcher.close();
			
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void searchByFileNameScoreQuery(){
		
		try {
			IndexSearcher searcher = new IndexSearcher(IndexReader.open(LuceneIndexUtil.getDirectory()));
			Query query = new TermQuery(new Term("content","null"));
			//文件评分
			FileNameScoreQuery customScoreQuery = new FileNameScoreQuery(query);
			//查询
			TopDocs topDocs = searcher.search(customScoreQuery, 100);
			ScoreDoc[] scoreDocs = topDocs.scoreDocs;
			for(ScoreDoc sd : scoreDocs){
				Document doc = searcher.doc(sd.doc);
				System.out.println(sd.doc+" ==== score:("+sd.score+") ====    filename:"+doc.get("filename")+" ====   path:"+doc.get("path")+" ====  size:"+doc.get("size")+"   date:"+format.format(new Date(Long.valueOf(doc.get("date"))))+"======score:"+doc.get("score"));
			}
			searcher.close();
			
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @类功能说明：自定义评分
	 * @类修改者：
	 * @修改日期：
	 * @修改说明：
	 * @作者：HECJ
	 * @创建时间：2014年11月12日 上午11:06:33
	 * @版本：V1.0
	 */
	private class MyCustomScoreQuery extends CustomScoreQuery{

		public MyCustomScoreQuery(Query subQuery, ValueSourceQuery valSrcQuery) {
			super(subQuery, valSrcQuery);
		}
		
		@Override
		protected CustomScoreProvider getCustomScoreProvider(IndexReader reader)
				throws IOException {
			
			//默认实现的评分是通过原有的评分*传入的评分来确定
			//为了根据不同的需求进行评分，需要进行评分的设定
			/*
			 * 自定义评分的不再
			 * 创建一个继续CustomScoreProvider 覆盖customScore方法
			 */
			return new MyCustomScoreProvider(reader);
		}
	}
	
	/**
	 * @类功能说明：自定义评分
	 * @类修改者：
	 * @修改日期：
	 * @修改说明：
	 * @作者：HECJ
	 * @创建时间：2014年11月12日 上午11:15:30
	 * @版本：V1.0
	 */
	private class MyCustomScoreProvider extends CustomScoreProvider{

		public MyCustomScoreProvider(IndexReader reader) {
			super(reader);
		}
		
		/**
		 * subQueryScore : 默认文档的打分
		 * valSrcScore ：评分域的打分
		 */
		@Override
		public float customScore(int doc, float subQueryScore, float valSrcScore)
				throws IOException {

			return 1/(subQueryScore*valSrcScore);
		}
		
	}
	
	/**
	 * @类功能说明：文件排序
	 * @类修改者：
	 * @修改日期：
	 * @修改说明：
	 * @作者：HECJ
	 * @创建时间：2014年11月12日 下午10:14:08
	 * @版本：V1.0
	 */
	private class FileNameScoreQuery extends CustomScoreQuery{

		public FileNameScoreQuery(Query subQuery) {
			super(subQuery);
			
		}
		@Override
		protected CustomScoreProvider getCustomScoreProvider(IndexReader reader)
				throws IOException {
			return new FileNameScoreProvider(reader);
		}
	}
	/**
	 * @类功能说明：文件排序
	 * @类修改者：
	 * @修改日期：
	 * @修改说明：
	 * @作者：HECJ
	 * @创建时间：2014年11月12日 下午10:14:25
	 * @版本：V1.0
	 */
	private class FileNameScoreProvider extends CustomScoreProvider{
		String[] filenames = null ;
		public FileNameScoreProvider(IndexReader reader) {
			super(reader);
			try {
				filenames = FieldCache.DEFAULT.getStrings(reader, "filename");
			
			} catch (IOException e) {
				e.printStackTrace();
			}
					
		}
		@Override
		public float customScore(int doc, float subQueryScore, float valSrcScore)
				throws IOException {
			
			String filename = filenames[doc];
			if(filename.endsWith(".java")){
				return subQueryScore*1.5f;
			}
			return subQueryScore/1.5f;
		}
		
	}
	
	
	
	
	
	
	
	/**
	 * @类功能说明：根据文件名评分
	 * @类修改者：
	 * @修改日期：
	 * @修改说明：
	 * @作者：HECJ
	 * @创建时间：2014年11月12日 上午11:32:16
	 * @版本：V1.0
	 */
	private class FileNameScoreQuery extends CustomScoreQuery{

		public FileNameScoreQuery(Query subQuery) {
			super(subQuery);
		}
		@Override
		protected CustomScoreProvider getCustomScoreProvider(IndexReader reader)
				throws IOException {
			return new FileNameScoreProvider(reader);
		}
		
	}
	
	/**
	 * @类功能说明：根据文件名评分
	 * @类修改者：
	 * @修改日期：
	 * @修改说明：
	 * @作者：HECJ
	 * @创建时间：2014年11月12日 上午11:33:26
	 * @版本：V1.0
	 */
	private class FileNameScoreProvider extends CustomScoreProvider{

		public FileNameScoreProvider(IndexReader reader) {
			super(reader);
		}
		
		@Override
		public float customScore(int doc, float subQueryScore, float valSrcScore)
				throws IOException {
			
			//
			
			return super.customScore(doc, subQueryScore, valSrcScore);
		}
		
	}
	
	
}
