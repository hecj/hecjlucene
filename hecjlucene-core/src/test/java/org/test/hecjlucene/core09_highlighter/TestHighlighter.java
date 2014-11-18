package org.test.hecjlucene.core09_highlighter;

import org.junit.Test;

public class TestHighlighter {
	
	@Test
	public void test01(){
		HighlighterSearch highlighterSearch = new HighlighterSearch();
		highlighterSearch.highlighter01();
	}
	
	@Test
	public void test02(){
		HighlighterSearch highlighterSearch = new HighlighterSearch();
		highlighterSearch.highlighterByFileContent("null");
	}
}
