package org.test.hecjlucene.core04;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.NumericField;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.util.Version;

/**
 * @类功能说明：
 * @类修改者：
 * @修改日期：
 * @修改说明：
 * @作者：JECJ
 * @创建时间：2014年11月1日 下午5:53:28
 * @版本：V1.0
 */

public class IndexSearcherUtil {
	
	private String[] ids = {"1","2","3","4","5","6"};
	private String[] emails = {"aa@itat.org","bb@itat.org","cc@cc.org","dd@sina.org","ee@zttc.edu","ff@itat.org"};
	private String[] contents = {
			"welcome to visited the space,I like book",
			"hello boy, I like pingpeng ball",
			"my name is cc I like game",
			"I like football",
			"I like football and I like basketball too",
			"I like movie and swim"
	};
	private Date[] dates = null;
	private int[] attachs = {2,3,1,4,5,5};
	private String[] names = {"zhangsan","lisi","hechaojie","jetty","mike","jake"};
	private Map<String,Float> scores = new HashMap<String,Float>();
	
	private Directory directory ;
	
	private IndexReader indexReader ;
	
	public IndexSearcherUtil() {
		
		try {
			//索引地址
			directory = FSDirectory.open(new File("E:/lucene/03"));
			setDates();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 填充数据
	 * @函数功能说明
	 * @修改作者名字 JECJ  
	 * @修改时间 2014年11月1日
	 * @修改内容
	 * @参数：     
	 * @return void   
	 * @throws
	 */
	private void setDates() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			dates = new Date[ids.length];
			dates[0] = sdf.parse("2010-02-19");
			dates[1] = sdf.parse("2012-01-11");
			dates[2] = sdf.parse("2011-09-19");
			dates[3] = sdf.parse("2010-12-22");
			dates[4] = sdf.parse("2012-01-01");
			dates[5] = sdf.parse("2011-05-19");
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 创建索引
	 * @函数功能说明
	 * @修改作者名字 JECJ  
	 * @修改时间 2014年11月1日
	 * @修改内容
	 * @参数：     
	 * @return void   
	 * @throws
	 */
	public void createIndex(){
		IndexWriter indexWriter = null ;
		try {
			
			indexWriter = new IndexWriter(directory, new IndexWriterConfig(Version.LUCENE_35,new StandardAnalyzer(Version.LUCENE_35)));
			indexWriter.deleteAll();
			Document doc = null ;
			for(int i = 0 ;i<ids.length ; i++){
				doc = new Document();
				doc.add(new Field("id",ids[i],Store.YES,Index.NOT_ANALYZED_NO_NORMS));
				doc.add(new Field("email",emails[i],Field.Store.YES,Field.Index.NOT_ANALYZED));
				doc.add(new Field("content",contents[i],Field.Store.NO,Field.Index.ANALYZED));
				doc.add(new Field("name",names[i],Field.Store.YES,Field.Index.NOT_ANALYZED_NO_NORMS));
				doc.add(new NumericField("attach",Field.Store.YES,true).setIntValue(attachs[i]));
				doc.add(new NumericField("date",Field.Store.YES,true).setLongValue(dates[i].getTime()));
				//添加索引
				indexWriter.addDocument(doc);
			}
			
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (LockObtainFailedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			try {
				indexWriter.close();
			} catch (CorruptIndexException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	/**
	 * @函数功能说明 得到查询的IndexSearcher
	 * @修改作者名字 JECJ  
	 * @修改时间 2014年11月1日
	 * @修改内容
	 * @参数： @return    
	 * @return IndexSearcher   
	 * @throws
	 */
	public IndexSearcher getSearcher(){ 
		
		try {
			if(indexReader == null){
				indexReader = IndexReader.open(directory);
			}else{
				IndexReader indexReaderTemp = IndexReader.openIfChanged(indexReader);
				if(indexReaderTemp != null){
					indexReader.close();
					indexReader = indexReaderTemp ;
				}
			}
			return new IndexSearcher(indexReader);
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null ;
	}
	
	/**
	 * @函数功能说明 精确查询
	 * @修改作者名字 JECJ  
	 * @修改时间 2014年11月1日
	 * @修改内容
	 * @参数： @param name
	 * @参数： @param num    
	 * @return void   
	 * @throws
	 */
	public void searchByTerm(String field,String content,int num){
		
		IndexSearcher indexSearcher = getSearcher();
		try {
			Query query = new TermQuery(new Term(field,content));
			TopDocs topDocs = indexSearcher.search(query,num);
			System.out.println("一共查询了条数："+topDocs.totalHits);
			ScoreDoc[] scoreDoc= topDocs.scoreDocs;
			for(ScoreDoc sd :scoreDoc){
				Document doc = indexSearcher.doc(sd.doc);
				System.out.println(doc.get("id")+"---->"+
						doc.get("name")+"["+doc.get("email")+"]-->"+doc.get("id")+","+
						doc.get("attach")+","+doc.get("date"));
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			try {
				indexSearcher.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * @函数功能说明 模糊查询
	 * @修改作者名字 JECJ  
	 * @修改时间 2014年11月1日
	 * @修改内容
	 * @参数： @param field
	 * @参数： @param start
	 * @参数： @param end
	 * @参数： @param num    
	 * @return void   
	 * @throws
	 */
	public void searchByTermRange(String field,String start,String end,int num){
		IndexSearcher indexSearcher = getSearcher();
		try {
			Query query = new TermRangeQuery(field, start, end, true, true);
			TopDocs topDocs = indexSearcher.search(query,num);
			System.out.println("一共查询了条数："+topDocs.totalHits);
			ScoreDoc[] scoreDoc= topDocs.scoreDocs;
			for(ScoreDoc sd :scoreDoc){
				Document doc = indexSearcher.doc(sd.doc);
				System.out.println(doc.get("id")+"---->  "+
						doc.get("name")+"  ["+doc.get("email")+"]-->"+doc.get("id")+","+
						doc.get("attach")+","+doc.get("date"));
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			try {
				indexSearcher.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * @函数功能说明 数字模糊查询
	 * @修改作者名字 JECJ  
	 * @修改时间 2014年11月1日
	 * @修改内容
	 * @参数： @param field
	 * @参数： @param start
	 * @参数： @param end
	 * @参数： @param num    
	 * @return void   
	 * @throws
	 */
	public void searchByTermNumricRange(String field,int start,int end,int num){
		IndexSearcher indexSearcher = getSearcher();
		try {
			Query query = NumericRangeQuery.newIntRange(field, start, end, true, true);
			TopDocs topDocs = indexSearcher.search(query,num);
			System.out.println("一共查询了条数："+topDocs.totalHits);
			ScoreDoc[] scoreDoc= topDocs.scoreDocs;
			for(ScoreDoc sd :scoreDoc){
				Document doc = indexSearcher.doc(sd.doc);
				System.out.println(doc.get("id")+"---->  name："+
						doc.get("name")+"  [email："+doc.get("email")+"]-->id:"+doc.get("id")+",attach:"+
						doc.get("attach")+",date:"+doc.get("date"));
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			try {
				indexSearcher.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * @函数功能说明 前缀查询
	 * @修改作者名字 JECJ  
	 * @修改时间 2014年11月1日
	 * @修改内容
	 * @参数： @param field
	 * @参数： @param start
	 * @参数： @param end
	 * @参数： @param num    
	 * @return void   
	 * @throws
	 */
	public void searchByPrefixQuery(String field,String value,int num){
		IndexSearcher indexSearcher = getSearcher();
		try {
			Query query = new PrefixQuery(new Term(field, value));
			TopDocs topDocs = indexSearcher.search(query,num);
			System.out.println("一共查询了条数："+topDocs.totalHits);
			ScoreDoc[] scoreDoc= topDocs.scoreDocs;
			for(ScoreDoc sd :scoreDoc){
				Document doc = indexSearcher.doc(sd.doc);
				System.out.println(doc.get("id")+"---->  name："+
						doc.get("name")+"  [email："+doc.get("email")+"]-->id:"+doc.get("id")+",attach:"+
						doc.get("attach")+",date:"+doc.get("date"));
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			try {
				indexSearcher.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * @函数功能说明 通配符查询
	 * @修改作者名字 JECJ  
	 * @修改时间 2014年11月1日
	 * @修改内容
	 * @参数： @param field
	 * @参数： @param start
	 * @参数： @param end
	 * @参数： @param num    
	 * @return void   
	 * @throws
	 */
	public void searchByWildcardQuery(String field,String value,int num){
		IndexSearcher indexSearcher = getSearcher();
		try {
			Query query = new WildcardQuery(new Term(field, value));
			TopDocs topDocs = indexSearcher.search(query,num);
			System.out.println("一共查询了条数："+topDocs.totalHits);
			ScoreDoc[] scoreDoc= topDocs.scoreDocs;
			for(ScoreDoc sd :scoreDoc){
				Document doc = indexSearcher.doc(sd.doc);
				System.out.println(doc.get("id")+"---->  name："+
						doc.get("name")+"  [email："+doc.get("email")+"]-->id:"+doc.get("id")+",attach:"+
						doc.get("attach")+",date:"+doc.get("date"));
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			try {
				indexSearcher.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * @函数功能说明 BooleanQuery查询
	 * @修改作者名字 JECJ  
	 * @修改时间 2014年11月1日
	 * @修改内容
	 * @参数： @param field
	 * @参数： @param start
	 * @参数： @param end
	 * @参数： @param num    
	 * @return void   
	 * @throws
	 */
	public void searchByBooleanQuery(int num){
		IndexSearcher indexSearcher = getSearcher();
		try {
			BooleanQuery query = new BooleanQuery();
			/**
			 * Occuer.MUST 必须出现
			 * Occuer.MUST_NOT 必须没有
			 * Occuer.SHOULD 可以出现
			 */
			query.add(new TermQuery(new Term("name", "hechaojie")), Occur.MUST_NOT);
			query.add(new TermQuery(new Term("content", "like")), Occur.MUST);
			TopDocs topDocs = indexSearcher.search(query,num);
			System.out.println("一共查询了条数："+topDocs.totalHits);
			ScoreDoc[] scoreDoc= topDocs.scoreDocs;
			for(ScoreDoc sd :scoreDoc){
				Document doc = indexSearcher.doc(sd.doc);
				System.out.println(doc.get("id")+"---->  name："+
						doc.get("name")+"  [email："+doc.get("email")+"]-->id:"+doc.get("id")+",attach:"+
						doc.get("attach")+",date:"+doc.get("date"));
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			try {
				indexSearcher.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * @函数功能说明 短语查询
	 * @修改作者名字 JECJ  
	 * @修改时间 2014年11月1日
	 * @修改内容
	 * @参数： @param field
	 * @参数： @param start
	 * @参数： @param end
	 * @参数： @param num    
	 * @return void   
	 * @throws
	 */
	public void searchByPhraseQuery(int num){
		IndexSearcher indexSearcher = getSearcher();
		try {
			
			PhraseQuery phraseQuery = new PhraseQuery();
			phraseQuery.setSlop(2);
			phraseQuery.add(new Term("content", "i"));
			phraseQuery.add(new Term("content", "too"));
			TopDocs topDocs = indexSearcher.search(phraseQuery,num);
			System.out.println("一共查询了条数："+topDocs.totalHits);
			ScoreDoc[] scoreDoc= topDocs.scoreDocs;
			for(ScoreDoc sd :scoreDoc){
				Document doc = indexSearcher.doc(sd.doc);
				System.out.println(doc.get("id")+"---->  name："+
						doc.get("name")+"  [email："+doc.get("email")+"]-->id:"+doc.get("id")+",attach:"+
						doc.get("attach")+",date:"+doc.get("date"));
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			try {
				indexSearcher.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * @函数功能说明 模糊查询
	 * @修改作者名字 JECJ  
	 * @修改时间 2014年11月1日
	 * @修改内容
	 * @参数： @param field
	 * @参数： @param start
	 * @参数： @param end
	 * @参数： @param num    
	 * @return void   
	 * @throws
	 */
	public void searchByFuzzyQuery(String field,String value,int num){
		IndexSearcher indexSearcher = getSearcher();
		try {
			FuzzyQuery phraseQuery = new FuzzyQuery(new Term(field, value));
			TopDocs topDocs = indexSearcher.search(phraseQuery,num);
			System.out.println("一共查询了条数："+topDocs.totalHits);
			ScoreDoc[] scoreDoc= topDocs.scoreDocs;
			for(ScoreDoc sd :scoreDoc){
				Document doc = indexSearcher.doc(sd.doc);
				System.out.println(doc.get("id")+"---->  name："+
						doc.get("name")+"  [email："+doc.get("email")+"]-->id:"+doc.get("id")+",attach:"+
						doc.get("attach")+",date:"+doc.get("date"));
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			try {
				indexSearcher.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}
