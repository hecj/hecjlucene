package org.test.hecjlucene.core07_advance_search;

import org.junit.Before;
import org.junit.Test;

public class TestSearcherSort {
	
	private SearcherSort searcherSort = null;
	private LuceneIndexUtil luceneIndexUtil = null;
	
	@Before
	public void init(){
		luceneIndexUtil = new LuceneIndexUtil();
		searcherSort = new SearcherSort();
	}
	
	@Test
	public void createIndex(){
		luceneIndexUtil.createIndex();
	}
	
	@Test
	public void test01(){
		
		searcherSort.searcher("int", null);
	}
}
