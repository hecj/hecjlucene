package org.test.hecjlucene.core04;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.queryParser.QueryParser.Operator;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.Version;
import org.junit.Test;

public class IndexSearcherTest {
	
	@Test
	public void testCreateIndex(){
		
		IndexSearcherUtil indexSearcher = new IndexSearcherUtil();
		indexSearcher.createIndex();
	}
	@Test
	public void testSearchByTerm(){
		
		IndexSearcherUtil indexSearcher = new IndexSearcherUtil();
		indexSearcher.searchByTerm("content","i", 10);
	}
	@Test
	public void testSearchByTermRange(){
		
		IndexSearcherUtil indexSearcher = new IndexSearcherUtil();
		indexSearcher.searchByTermRange("name", "m", "i", 10);
	}
	@Test
	public void testSearchByTermNumricRange(){
		
		IndexSearcherUtil indexSearcher = new IndexSearcherUtil();
		indexSearcher.searchByTermNumricRange("attach", 1, 4, 10);
	}
	@Test
	public void testSearchByPrefixQuery(){
		
		IndexSearcherUtil indexSearcher = new IndexSearcherUtil();
		indexSearcher.searchByPrefixQuery("name","j", 10);
	}
	@Test
	public void testSearchByWildcardQuery(){
		
		IndexSearcherUtil indexSearcher = new IndexSearcherUtil();
		indexSearcher.searchByWildcardQuery("email","*@itat*", 10);
	}
	@Test
	public void testSearchByBooleanQuery(){
		
		IndexSearcherUtil indexSearcher = new IndexSearcherUtil();
		indexSearcher.searchByBooleanQuery(10);
	}
	@Test
	public void testSearchByPhraseQuery(){
		
		IndexSearcherUtil indexSearcher = new IndexSearcherUtil();
		indexSearcher.searchByPhraseQuery(10);
	}
	@Test
	public void testSearchByFuzzyQuery(){
		
		IndexSearcherUtil indexSearcher = new IndexSearcherUtil();
		indexSearcher.searchByFuzzyQuery("content","pinapeng",10);
	}
	@Test
	public void testSearchByQueryPhrase(){
		
		try {
			IndexSearcherUtil indexSearcher = new IndexSearcherUtil();
			QueryParser queryParser = new QueryParser(Version.LUCENE_35, "content", new StandardAnalyzer(Version.LUCENE_35));
			//改变空格的默认操作符 （默认是ALL）
			queryParser.setDefaultOperator(Operator.AND);
			Query query = queryParser.parse("like");
			query = queryParser.parse("I AND football movie");
			//改变默认搜索域
			query = queryParser.parse("name:hechaojie");
			query = queryParser.parse("name:j*");
			//通配符默认不能放在第一位
//			query = queryParser.parse("name:*@itat"); 
			//匹配name中没有mike但是必须有football的,+和-要放置到域说明前面
			query = queryParser.parse("- name:mike + football");
			//id是1到3的(TO必须是大写)
			query = queryParser.parse("id:[1 TO 3]");
			//还有一些匹配就不列举了...
			indexSearcher.searchByQueryPhrase(query,10);
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}
