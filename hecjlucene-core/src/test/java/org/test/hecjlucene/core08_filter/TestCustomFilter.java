package org.test.hecjlucene.core08_filter;

import org.junit.Test;

/**
 * @类功能说明：自定义Filter测试
 * @类修改者：
 * @修改日期：
 * @修改说明：
 * @作者：HECJ
 * @创建时间：2014年11月16日 下午6:54:44
 * @版本：V1.0
 */
public class TestCustomFilter {
	
	@Test
	public void test01(){
		
		CustomFilterQuery customFilterQuery = new CustomFilterQuery();
		customFilterQuery.searcherByFilterQuery("null");
	}
}
