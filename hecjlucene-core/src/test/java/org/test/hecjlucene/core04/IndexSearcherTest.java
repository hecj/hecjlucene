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
}
