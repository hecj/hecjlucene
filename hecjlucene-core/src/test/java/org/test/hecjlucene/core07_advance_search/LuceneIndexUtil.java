package org.test.hecjlucene.core07_advance_search;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.NumericField;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.util.Version;

/**
 * @类功能说明： Lucene实现分页功能
 * @类修改者：
 * @修改日期：
 * @修改说明：
 * @作者：JECJ
 * @创建时间：2014年11月2日 下午5:03:02
 * @版本：V1.0
 */
public class LuceneIndexUtil {

	// 存放索引路径
	private static String INDEX_DIRETORY = "E:\\lucene\\04";
	private static String FILE_DIRETORY = "E:\\lucene\\file";

	private IndexReader reader;

	public LuceneIndexUtil() {

	}
	
	/**
	 * @函数功能说明 索引目录
	 * @修改作者名字 HECJ  
	 * @修改时间 2014年11月11日
	 * @修改内容
	 * @参数： @return    
	 * @return Directory   
	 * @throws
	 */
	public static Directory getDirectory(){
		try {
			return FSDirectory.open( new File(INDEX_DIRETORY));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	

	/**
	 * @函数功能说明 获取查询的IndexSearcher
	 * @修改作者名字 JECJ
	 * @修改时间 2014年11月2日
	 * @修改内容
	 * @参数： @return
	 * @return IndexSearcher
	 * @throws
	 */
	public IndexSearcher getSearcher() {

		try {
			if (reader == null) {
				reader = IndexReader.open(getDirectory());
			}else{
				IndexReader newReader = IndexReader.openIfChanged(reader);
				if(newReader != null){
					reader.close();
					reader = newReader ;
				}
			}
		} catch (CorruptIndexException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new IndexSearcher(reader);
	}

	/**
	 * @函数功能说明 复制文件
	 * @修改作者名字 JECJ
	 * @修改时间 2014年11月2日
	 * @修改内容
	 * @参数：
	 * @return void
	 * @throws
	 */
	public void copyFile() {

		for (File f : new File(FILE_DIRETORY).listFiles()) {
			try {
				String newFileName = FilenameUtils.getFullPath(f
						.getAbsolutePath())
						+ FilenameUtils.getBaseName(f.getName()) + ".vvv";
				FileUtils.copyFile(f, new File(newFileName));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @函数功能说明 创建索引
	 * @修改作者名字 JECJ
	 * @修改时间 2014年11月2日
	 * @修改内容
	 * @参数：
	 * @return void
	 * @throws
	 */
	public void createIndex() {
		IndexWriter writer = null;
		try {
			writer = new IndexWriter(getDirectory(), new IndexWriterConfig(
					Version.LUCENE_35, new StandardAnalyzer(Version.LUCENE_35)));
			writer.deleteAll();
			File[] files = new File(FILE_DIRETORY).listFiles();
			Document doc = null;
			Random random = new Random();
			int index = 0;
			for (File f : files) {
				int score = random.nextInt(100);
				doc = new Document();
				doc.add(new Field("id", String.valueOf(index++),Field.Store.YES,Field.Index.NOT_ANALYZED));
				doc.add(new Field("content", new FileReader(f)));
				doc.add(new Field("filename", f.getName(), Field.Store.YES,Field.Index.NOT_ANALYZED));
				doc.add(new Field("path", f.getAbsolutePath(), Field.Store.YES,Field.Index.NOT_ANALYZED));
				doc.add(new NumericField("date", Field.Store.YES, true).setLongValue(f.lastModified()));
				doc.add(new NumericField("size", Field.Store.YES, true).setLongValue((int) (f.length())));
				//随机评分
				doc.add(new NumericField("score", Field.Store.YES, true).setIntValue(score));
				writer.addDocument(doc);
			}

		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (LockObtainFailedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				writer.close();
			} catch (CorruptIndexException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @函数功能说明 查询不分页
	 * @修改作者名字 JECJ
	 * @修改时间 2014年11月2日
	 * @修改内容
	 * @参数： @param query
	 * @参数： @param indexPage
	 * @参数： @param pageSize
	 * @return void
	 * @throws
	 */
	public void queryByQuery(String queryStr) {
		
		IndexSearcher searcher = getSearcher();
		//创建QueryParser
		QueryParser parser = new QueryParser(Version.LUCENE_35, "content", new StandardAnalyzer(Version.LUCENE_35));
		try {
			//得到Query
			Query q = parser.parse(queryStr);
			//查询
			TopDocs topDocs = searcher.search(q, 500);
			//得到ScoreDoc[]
			ScoreDoc[] scoreDocs = topDocs.scoreDocs;
			//遍历
			for(int i=0 ; i< scoreDocs.length ;i++){
				ScoreDoc sd = scoreDocs[i];
				//获取Document
				Document doc = searcher.doc(sd.doc);
				System.out.println(i+"-->filename:"+doc.get("filename")+",path:"+doc.get("path"));
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
	 * @函数功能说明 分页查询(第一种分页方式)
	 * @修改作者名字 JECJ
	 * @修改时间 2014年11月2日
	 * @修改内容
	 * @参数： @param query
	 * @参数： @param indexPage
	 * @参数： @param pageSize
	 * @return void
	 * @throws
	 */
	public void queryByQuery(String queryStr, int indexPage, int pageSize) {
		
		IndexSearcher searcher = getSearcher();
		//创建QueryParser
		QueryParser parser = new QueryParser(Version.LUCENE_35, "content", new StandardAnalyzer(Version.LUCENE_35));
		try {
			//得到Query
			Query q = parser.parse(queryStr);
			//查询
			int queryMaxNun = indexPage*pageSize ;
			TopDocs topDocs = searcher.search(q, queryMaxNun);
			//得到ScoreDoc[]
			ScoreDoc[] scoreDocs = topDocs.scoreDocs;
			int start = (indexPage - 1)*pageSize ;
			//遍历
			for(int i= start; i< scoreDocs.length ;i++){
				ScoreDoc sd = scoreDocs[i];
				//获取Document
				Document doc = searcher.doc(sd.doc);
				System.out.println(i+"-->filename:"+doc.get("filename")+",path:"+doc.get("path"));
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
	 * @函数功能说明 分页查询(第二种分页方式)
	 * @修改作者名字 JECJ
	 * @修改时间 2014年11月2日
	 * @修改内容
	 * @参数： @param query
	 * @参数： @param indexPage
	 * @参数： @param pageSize
	 * @return void
	 * @throws
	 */
	public void queryPageByAfter(String queryStr, int indexPage, int pageSize) {
		
		IndexSearcher searcher = getSearcher();
		//创建QueryParser
		QueryParser parser = new QueryParser(Version.LUCENE_35, "content", new StandardAnalyzer(Version.LUCENE_35));
		try {
			//得到Query
			Query q = parser.parse(queryStr);
			//查询：计算最多查询条数
			int queryMaxNun = indexPage*pageSize ;
			TopDocs topDocs = searcher.search(q, queryMaxNun);
			//得到ScoreDoc[]
			ScoreDoc[] scoreDocs = topDocs.scoreDocs;
			//遍历
			for(int i = (indexPage-1)*pageSize; i< scoreDocs.length;i++){
				ScoreDoc sd = scoreDocs[i];
				//获取Document
				Document doc = searcher.doc(sd.doc);
				System.out.println(i+"-->filename:"+doc.get("filename")+",path:"+doc.get("path"));
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
