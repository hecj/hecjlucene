package org.test.hecjlucene.core07_advance_search;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.NumericRangeFilter;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.QueryWrapperFilter;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TermRangeFilter;
import org.apache.lucene.search.WildcardQuery;
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
	
	/**
	 * @函数功能说明 排序
	 * @修改作者名字 HECJ  
	 * @修改时间 2014年11月11日
	 * @修改内容
	 * @参数：     
	 * @return void   
	 * @throws
	 */
	@Test
	public void test01(){
		/**
		 * 设置了排序后就没有评分了
		 */
/*		searcherSort.searcher("int", null);
		System.out.println("============================================");
		//Id记性排序
		searcherSort.searcher("int", Sort.INDEXORDER);
		System.out.println("============================================");
		//使用默认评分排序
		searcherSort.searcher("int", Sort.RELEVANCE);
		System.out.println("============================================");
		//对size排序
		searcherSort.searcher("int", new Sort(new SortField("size",SortField.LONG)));
		System.out.println("============================================");
		//通过日期排序 ,true表示从大到小排序
		searcherSort.searcher("null", new Sort(new SortField("date",SortField.LONG,true)));
*/		//通过文件名排序,组合排序
		searcherSort.searcher("null", new Sort(new SortField("date",SortField.LONG,true),new SortField("filename",SortField.STRING,true)));
	}
	
	/**
	 * @函数功能说明 过滤
	 * @修改作者名字 HECJ  
	 * @修改时间 2014年11月11日
	 * @修改内容
	 * @参数：     
	 * @return void   
	 * @throws
	 */
	@Test
	public void test02(){
		
		//两个文件之间过滤
		Filter ft = new TermRangeFilter("filename", "ChangeImageActivity.aaa", "ChangeImageActivity.ddd", true, true);
		//文件大小过滤
		ft = NumericRangeFilter.newIntRange("size", 1486, 1488, true, true);
		//模糊过滤 
		ft = new QueryWrapperFilter(new WildcardQuery(new Term("filename", "*.java")));
		searcherSort.searcherFilter("null", ft);
		
		
	}
}
