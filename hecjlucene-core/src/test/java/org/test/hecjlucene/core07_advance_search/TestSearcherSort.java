package org.test.hecjlucene.core07_advance_search;

import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.function.ShortFieldSource;
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
		/**
		 * 设置了排序后就没有评分了
		 */
		searcherSort.searcher("int", null);
		System.out.println("============================================");
		//Id记性排序
		searcherSort.searcher("int", Sort.INDEXORDER);
		System.out.println("============================================");
		//使用默认评分排序
		searcherSort.searcher("int", Sort.RELEVANCE);
		System.out.println("============================================");
		//对size排序
		searcherSort.searcher("int", new Sort(new SortField("size",SortField.INT)));
	}
}
