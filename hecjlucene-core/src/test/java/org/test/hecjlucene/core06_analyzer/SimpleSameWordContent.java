package org.test.hecjlucene.core06_analyzer;

import java.util.HashMap;
import java.util.Map;

/**
 * @类功能说明：简单同义词实现
 * @类修改者：
 * @修改日期：
 * @修改说明：
 * @作者：HECJ
 * @创建时间：2014年11月10日 下午5:25:05
 * @版本：V1.0
 */
public class SimpleSameWordContent implements SameWordContent {
	
	Map<String,String[]> map = new HashMap<String,String[]>();
	
	public SimpleSameWordContent() {
		
		map.put("我", new String[]{"咱","俺"});
		map.put("中国", new String[]{"天朝","大陆"});
	}

	public String[] getSameWords(String name) {
		return map.get(name);
	}

}
