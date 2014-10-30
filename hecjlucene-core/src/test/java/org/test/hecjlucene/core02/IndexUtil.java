package org.test.hecjlucene.core02;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.util.Version;

public class IndexUtil {

	
	private String[] ids = {"1","2","3","4","5","6"};
	
	private String[] emails = {"fdks@dffd","fsdf@df.ff","ggf@org.itat","jvcbv@org.itat","gvcxf@org.zttc","jjhbv@fdff.mm"};

	private String[] content = {
			"ffjfklj fljsdk I love ioj lvcjbkvcj kljfdksl fjksdl",
			"ffjfgfdgytjjnvnfl n love fvdffjksdl",
			"ffjfknfgdfgdgdfg love gdfg3tggdg44g l",
			"ffjfklj fljs love dnfngfgf7kj6767love 5664jbkvcj kljfdksl fjksdl",
			"ffjfklj fljsdkret love hgfgf love jbkvcj kljfdksl fjksdl",
			"ffjfklj fljsdklvbjdkfvjdioj love lvcjbkvcj kljfdksl fjksdl"
	};
	
	
	private int[] attachs = {3,34,43,54,2,4};
	
	private String[] names = {"zhangsna","lisi","zhangjie","xiena","hedhoajie","ding"};

	private Directory directory = null ;
	
	private Map<String,Float> scores = new HashMap();
	
	public IndexUtil(){
		try {
			
			scores.put("org.itat", 2.0f);
			scores.put("org.zttc", 1.5f);
			
			directory = FSDirectory.open(new File("E:/lucene/02"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 创建索引
	 */
	public void index(){
		
		IndexWriter writer = null ;
		
		try {
			writer = new IndexWriter(directory, new IndexWriterConfig(Version.LUCENE_35, new StandardAnalyzer(Version.LUCENE_35)));
		
			Document doc = null ;
			for(int i= 0 ;i<ids.length;i++){
				doc = new Document();
				doc.add(new Field("id",ids[i],Field.Store.YES,Field.Index.NOT_ANALYZED_NO_NORMS));
				doc.add(new Field("email",emails[i],Field.Store.YES,Field.Index.NOT_ANALYZED));
				doc.add(new Field("content",content[i],Field.Store.NO,Field.Index.ANALYZED));
				doc.add(new Field("name",names[i],Field.Store.YES,Field.Index.NOT_ANALYZED_NO_NORMS));
			
				String et = emails[i].substring(emails[i].lastIndexOf("@")+1);
				if(scores.containsKey(et)){
					doc.setBoost(scores.get(et));
				}else{
					doc.setBoost(0.5f);
				}
				writer.addDocument(doc);
			}
			
		
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
			try {
				writer.close();
			} catch (CorruptIndexException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
	}
	
	/**
	 * 查询
	 */
	public void query(){
		
		IndexReader reader = null;
		
		try {
			reader = IndexReader.open(directory);
			
			System.out.println(reader.numDocs());
			System.out.println(reader.maxDoc());
			System.out.println(reader.numDeletedDocs());
			
			
			
		} catch (CorruptIndexException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			try {
				reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		
		
	}
	
	/**
	 * 删除索引
	 */
	public void deleteIndex(){
		
		IndexWriter writer = null ;
		try {
			writer = new IndexWriter(directory, new IndexWriterConfig(Version.LUCENE_35, new StandardAnalyzer(Version.LUCENE_35)));
			//此时删除的文档不会完全删除，放到了回收站，可以恢复
			writer.deleteDocuments(new Term("id","1"));
		
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
			
			try {
				writer.close();
			} catch (CorruptIndexException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	/**
	 * 清空回收站
	 */
	public void forceDeleteIndex(){
		
		IndexWriter writer = null ;
		try {
			writer = new IndexWriter(directory, new IndexWriterConfig(Version.LUCENE_35, new StandardAnalyzer(Version.LUCENE_35)));
			writer.forceMergeDeletes();
			
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
			
			try {
				writer.close();
			} catch (CorruptIndexException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	/**
	 * 清空回收站
	 */
	public void forceMerge(){
		
		IndexWriter writer = null ;
		try {
			writer = new IndexWriter(directory, new IndexWriterConfig(Version.LUCENE_35, new StandardAnalyzer(Version.LUCENE_35)));
			//会将索引合并为2段，这2段中被删除的数据会被清空
			writer.forceMerge(2);
			
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
			
			try {
				writer.close();
			} catch (CorruptIndexException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	/**
	 * 恢复索引
	 */
	public void unDelete(){
		
		IndexReader reader = null;
		try {
			//设置可写
			reader = IndexReader.open(directory,false);
			//恢复时必须把IndexReader readonly设置为false
			reader.undeleteAll();
			
			
		} catch (CorruptIndexException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			try {
				reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
	}
	
	/**
	 * 更新索引
	 */
	public void update(){
		
		IndexWriter writer = null ;
		try {
			writer = new IndexWriter(directory, new IndexWriterConfig(Version.LUCENE_35, new StandardAnalyzer(Version.LUCENE_35)));
			
			Document doc=  new Document();
			doc.add(new Field("id","11",Field.Store.YES,Field.Index.NOT_ANALYZED_NO_NORMS));
			doc.add(new Field("email",emails[0],Field.Store.YES,Field.Index.NOT_ANALYZED));
			doc.add(new Field("content",content[0],Field.Store.NO,Field.Index.ANALYZED));
			doc.add(new Field("name",names[0],Field.Store.YES,Field.Index.NOT_ANALYZED_NO_NORMS));
			
			writer.updateDocument(new Term("id", "1"),doc);
			
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
			
			try {
				writer.close();
			} catch (CorruptIndexException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	/**
	 * 查询
	 * @param args
	 */
	
	public void search(){
		IndexReader reader = null;
		try {
			reader = IndexReader.open(directory);
			
			IndexSearcher search = new IndexSearcher(reader);
			
			TermQuery query = new TermQuery(new Term("content","love"));
			TopDocs tds= search.search(query, 10);
			for(ScoreDoc sd :tds.scoreDocs){
				Document doc = search.doc(sd.doc);
				System.out.println(+sd.doc+","+doc.get("name")+","+doc.get("id")+"-----"+doc.get("content"));
			}
			
			
			
			
		} catch (CorruptIndexException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
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
	
	
	public static void main(String[] args) {
		
		new IndexUtil().index();
//		new IndexUtil().query();
//		new IndexUtil().deleteIndex();
//		new IndexUtil().unDelete();
//		new IndexUtil().forceDeleteIndex();
//		new IndexUtil().forceMerge();
//		new IndexUtil().update();
		new IndexUtil().query();
		new IndexUtil().search();
	}
	
	
}	
