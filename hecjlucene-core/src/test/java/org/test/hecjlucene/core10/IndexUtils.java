package org.test.hecjlucene.core10;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.Executors;

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
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.NRTManager;
import org.apache.lucene.search.NRTManagerReopenThread;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.SearcherManager;
import org.apache.lucene.search.SearcherWarmer;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.util.Version;

/**
 * @类功能说明： Lucene实现分页功能
 * 1、如果使用SearcherManager，索引提交后才能实现近实时搜索
 * 2、或者使用NrtManager实现近实时搜索
 * @类修改者：
 * @修改日期：
 * @修改说明：
 * @作者：JECJ
 * @创建时间：2014年11月2日 下午5:03:02
 * @版本：V1.0
 */
public class IndexUtils {

	// 存放索引路径
	private static String INDEX_DIRETORY = "E:\\lucene\\04";
	private static String FILE_DIRETORY = "E:\\lucene\\file";

	private Directory directory = null;
	private SearcherManager searcherManager = null ; 
	
	private IndexWriter  writer = null ;
	private NRTManager nrtManager = null ;
	
	public IndexUtils() {
		try {
			directory = FSDirectory.open(new File(INDEX_DIRETORY));
			writer = new IndexWriter(directory, new IndexWriterConfig(Version.LUCENE_35, new StandardAnalyzer(Version.LUCENE_35)));
			nrtManager = new NRTManager(writer, new SearcherWarmer() {
				public void warm(IndexSearcher s) throws IOException {
					System.out.println("nrtManager ——> warm");
				}
			});
			NRTManagerReopenThread  reopenThread = new NRTManagerReopenThread(nrtManager, 5.0, 0.025);
			reopenThread.setDaemon(true);
			reopenThread.setName("NRTManagerReopenThread  -->run ");
			reopenThread.start();
			//通过NrtManager获取SearcherManager
			searcherManager = nrtManager.getSearcherManager(true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private SearcherManager getSearcherManager(){
		try {
			if(searcherManager == null){
				searcherManager = new SearcherManager(directory, new SearcherWarmer() {
					public void warm(IndexSearcher s) throws IOException {
						System.out.println("索引更新了...");
					}
				}, Executors.newCachedThreadPool());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return searcherManager ;
	}
	
	public void commit(){
		try {
			writer.commit();
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
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
		IndexWriter writer = this.writer;
		try {
			File[] files = new File(FILE_DIRETORY).listFiles();
			Document doc = null;
			writer.deleteAll();
			int i = 0;
			for (File f : files) {
				doc = new Document();
				doc.add(new Field("id",(i++)+"",Field.Store.YES,Field.Index.NOT_ANALYZED_NO_NORMS));
				doc.add(new Field("content", new FileReader(f)));
				doc.add(new Field("filename", f.getName(), Field.Store.YES,
						Field.Index.NOT_ANALYZED));
				doc.add(new Field("path", f.getAbsolutePath(), Field.Store.YES,
						Field.Index.NOT_ANALYZED));
				doc.add(new NumericField("date", Field.Store.YES, true)
						.setLongValue(f.lastModified()));
				doc.add(new NumericField("size", Field.Store.YES, true)
						.setIntValue((int) (f.length())));
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
	
	public void update(){
		try {
			Document doc = new Document();
			doc.add(new Field("path", String.valueOf(System.currentTimeMillis()), Field.Store.YES,
					Field.Index.NOT_ANALYZED));
			doc.add(new Field("id","11",Field.Store.YES,Field.Index.NOT_ANALYZED_NO_NORMS));
			nrtManager.updateDocument(new Term("id","1"), doc);
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void deleteByFileName(String fileName) {
		try {
			nrtManager.deleteDocuments(new Term("filename",fileName));
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (LockObtainFailedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
		}
	}
	
	public void query() {
		try {
			IndexReader reader = IndexReader.open(directory);
			System.out.println("numDocs:"+reader.numDocs());
			System.out.println("maxDocs:"+reader.maxDoc());
			System.out.println("deleteDocs:"+reader.numDeletedDocs());
			reader.close();
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @函数功能说明 查询
	 * @修改作者名字 HECJ  
	 * @修改时间 2014年11月25日
	 * @修改内容
	 * @参数： @param queryStr    
	 * @return void   
	 * @throws
	 */
	public void queryByQuery(String queryStr) {
		
		IndexSearcher searcher = getSearcherManager().acquire();
		try {
			//判断是否需要重新打开一个searcher
			getSearcherManager().maybeReopen();
			//创建QueryParser
			QueryParser parser = new QueryParser(Version.LUCENE_35, "content", new StandardAnalyzer(Version.LUCENE_35));
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
				System.out.println(doc.get("id")+"-->filename:"+doc.get("filename")+",path:"+doc.get("path"));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			try {
				getSearcherManager().release(searcher);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
