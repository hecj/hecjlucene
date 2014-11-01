package org.test.hecjlucene.core04;

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
}
