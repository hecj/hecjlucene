package org.test.hecjlucene.core05;

import org.junit.Test;

public class LuceneIndexUtilTest {
	
	@Test
	public void testCopyFile(){
		LuceneIndexUtil luceneIndexUtil = new LuceneIndexUtil();
		luceneIndexUtil.copyFile();
	}
	@Test
	public void testCreateIndex(){
		LuceneIndexUtil luceneIndexUtil = new LuceneIndexUtil();
		luceneIndexUtil.createIndex();
	}
	@Test
	public void testQueryByQueryNoPage(){
		LuceneIndexUtil luceneIndexUtil = new LuceneIndexUtil();
		luceneIndexUtil.queryByQuery("int");
	}
	@Test
	public void testQueryByQuery(){
		LuceneIndexUtil luceneIndexUtil = new LuceneIndexUtil();
		luceneIndexUtil.queryByQuery("int",4,5);
	}
}
