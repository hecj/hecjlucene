package org.test.hecjlucene.core09_highlighter;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.search.highlight.SimpleSpanFragmenter;
import org.apache.lucene.util.Version;
import org.test.hecjlucene.core07_advance_search.LuceneIndexUtil;

import com.chenlb.mmseg4j.analysis.MMSegAnalyzer;
/**
 * @类功能说明：高亮显示
 * @类修改者：
 * @修改日期：
 * @修改说明：
 * @作者：Administrator
 * @创建时间：2014年11月18日 下午9:02:25
 * @版本：V1.0
 */
public class HighlighterSearch {
	
	/**
	 * @函数功能说明 简单高亮显示
	 * @修改作者名字 Administrator  
	 * @修改时间 2014年11月18日
	 * @修改内容
	 * @参数：     
	 * @return void   
	 * @throws
	 */
	public void highlighter01(){
		
		String txt = "我是哈哈，你这是干啥呀，哈哈";
		
		QueryParser parser = new QueryParser(Version.LUCENE_35, "f", new MMSegAnalyzer());
		try {
			Query query = parser.parse("哈哈");
			QueryScorer queryScorer = new QueryScorer(query);
			Fragmenter fragmenter = new SimpleSpanFragmenter(queryScorer);
			//自定义高亮标签
			Formatter formatter = new SimpleHTMLFormatter("<span style='color:red'>", "</span>");
			Highlighter highlighter = new Highlighter(formatter, queryScorer);
			highlighter.setTextFragmenter(fragmenter);
			String str = highlighter.getBestFragment(new MMSegAnalyzer(), "f", txt);
			System.out.println(str);
			
			
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidTokenOffsetsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * @函数功能说明 对文件内容高亮显示
	 * @修改作者名字 Administrator  
	 * @修改时间 2014年11月18日
	 * @修改内容
	 * @参数： @param name    
	 * @return void   
	 * @throws
	 */
	public void highlighterByFileContent(String name){
		IndexSearcher searcher = null ;
		try {
			Analyzer analyzer = new MMSegAnalyzer();
			searcher = new IndexSearcher(IndexReader.open(LuceneIndexUtil.getDirectory()));
			MultiFieldQueryParser parser = new MultiFieldQueryParser(Version.LUCENE_35, new String[]{"filename","content"},analyzer);
			Query query = parser.parse(name);
			TopDocs topDocs = searcher.search(query, 100);
			ScoreDoc[] scoreDocs = topDocs.scoreDocs;
			for(ScoreDoc sd : scoreDocs){
				Document doc = searcher.doc(sd.doc);
				String path = doc.get("path");
				String content = FileUtils.readFileToString(new File(path));
				String str = highlighterContet(analyzer, query, content, "title");
				System.out.println(str);
			}
			
			
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (InvalidTokenOffsetsException e) {
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
	 * @函数功能说明 高亮显示
	 * @修改作者名字 Administrator  
	 * @修改时间 2014年11月18日
	 * @修改内容 
	 * @参数： @param analyzer
	 * @参数： @param query
	 * @参数： @param content
	 * @参数： @param filename
	 * @参数： @return
	 * @参数： @throws IOException
	 * @参数： @throws InvalidTokenOffsetsException    
	 * @return String   
	 * @throws
	 */
	private String highlighterContet(Analyzer analyzer,Query query,String content,String filename) throws IOException, InvalidTokenOffsetsException{
		
		QueryScorer queryScorer = new QueryScorer(query);
		Fragmenter fragmenter = new SimpleSpanFragmenter(queryScorer);
		Formatter formatter = new SimpleHTMLFormatter("<b>","</b>");
		Highlighter highlighter = new Highlighter(formatter,queryScorer);
		highlighter.setTextFragmenter(fragmenter);
		String str = highlighter.getBestFragment(analyzer, filename, content);
		if(str == null){
			return content;
		}
		return str ;
	}
}
