package org.test.hecjlucene.core09_highlighter;

import java.io.IOException;

import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleSpanFragmenter;
import org.apache.lucene.util.Version;

import com.chenlb.mmseg4j.analysis.MMSegAnalyzer;

public class HighlighterSearch {
	
	
	public void highlighter01(){
		
		String txt = "我是哈哈，你这是干啥呀，哈哈";
		
		QueryParser parser = new QueryParser(Version.LUCENE_35, "f", new MMSegAnalyzer());
		try {
			Query query = parser.parse("哈哈");
			QueryScorer queryScorer = new QueryScorer(query);
			Fragmenter fragmenter = new SimpleSpanFragmenter(queryScorer);
			Highlighter highlighter = new Highlighter(queryScorer);
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
}
