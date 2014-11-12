package org.test.hecjlucene.core07_advance_search;

import org.junit.Test;
/**
 * @类功能说明：自定义评分测试
 * @类修改者：
 * @修改日期：
 * @修改说明：
 * @作者：HECJ
 * @创建时间：2014年11月12日 上午11:11:30
 * @版本：V1.0
 */
public class TestMyScoreQuery {
	
	@Test
	public void test01(){
		
		MyScoreQuery myScoreQuery = new MyScoreQuery();
		myScoreQuery.searchByScoreQuery();
	}
	@Test
	public void test02(){
		
		MyScoreQuery myScoreQuery = new MyScoreQuery();
		myScoreQuery.searchByFileNameScoreQuery();
	}
}
