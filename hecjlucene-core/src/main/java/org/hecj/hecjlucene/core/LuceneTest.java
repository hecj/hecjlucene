package org.hecj.hecjlucene.core;


import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
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

public class LuceneTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		System.out.println("测试一下");
		
		new LuceneTest().index();
		new LuceneTest().seach();
	}

	/**
	 * 创建索引
	 */
	
	public void index(){
		
		//1创建Directory
		Directory directory = null ;
//		Directory directory = new RAMDirectory();
		//2创建IndexWriter
		IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LUCENE_35, new StandardAnalyzer(Version.LUCENE_35));
		IndexWriter indexWriter = null ;
		try {
			directory = FSDirectory.open(new File("e:/lucene/01"));
			indexWriter = new IndexWriter(directory, indexWriterConfig);
			//3创建document
			Document doc = null;
			//4添加Field
			File f = new File("E:/lucene/file");
			for(File file : f.listFiles()){
				doc = new Document();
				doc.add(new Field("content", new FileReader(file)));
				doc.add(new Field("filename", file.getName(),Field.Store.YES,Field.Index.NOT_ANALYZED));
				doc.add(new Field("path",file.getAbsolutePath(),Field.Store.YES,Field.Index.NOT_ANALYZED));
				//5添加索引
				indexWriter.addDocument(doc);
			}
			
			//
			
			
		} catch (CorruptIndexException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LockObtainFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			if(indexWriter != null){
				try {
					indexWriter.close();
				} catch (CorruptIndexException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}
	
	
	/**
	 * 搜索
	 */
	
	public void seach(){
		
		//1创建Dir
		Directory directory = null;
		IndexReader reader = null ;
		try {
			directory = FSDirectory.open(new File("e:/lucene/01"));
			//2.创建IndexReader
			reader =  IndexReader.open(directory);
			//3根据IndexReader创建IndexSeacher
			IndexSearcher search = new IndexSearcher(reader);
			//4.创建搜索的Query
			QueryParser parse = new QueryParser(Version.LUCENE_35, "content", new StandardAnalyzer(Version.LUCENE_35));
			Query query = parse.parse("classd");
			//5.根据search搜索并返回TopDocs
			TopDocs tds = search.search(query, 10);
			//6.根据TopDocs获取SocreDoc对象
			ScoreDoc[] sds = tds.scoreDocs;
			for(ScoreDoc sd : sds){
				//7.根据seacher和ScordDoc对象获取具体的Document对象
				Document d = search.doc(sd.doc);
				//8.根据Document对象获取需要的值
				System.out.println(d.get("filename")+","+d.get("path"));
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			
			try {
				reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		
	}
	
}
