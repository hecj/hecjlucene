package org.test.hecjlucene.core06_analyzer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.util.AttributeSource;
/**
 * @类功能说明：同义词过滤器
 * @类修改者：
 * @修改日期：
 * @修改说明：
 * @作者：HECJ
 * @创建时间：2014年11月10日 下午2:46:51
 * @版本：V1.0
 */
public class MySameTokenFilter extends TokenFilter {
	
	private CharTermAttribute cta;
	private PositionIncrementAttribute pia;
	private AttributeSource.State currState ;
	private Stack<String> sames ;

	protected MySameTokenFilter(TokenStream input) {
		super(input);
		cta = this.addAttribute(CharTermAttribute.class);
		pia = this.addAttribute(PositionIncrementAttribute.class);
		sames = new Stack<String>();
	}

	@Override
	public boolean incrementToken() throws IOException {
		
		while(sames.size()>0){
			//将元素出桟，并获取元素
			String str = sames.pop();
			//还原状态
			restoreState(currState);
			cta.setEmpty();
			cta.append(str);
			//设置位置0
			pia.setPositionIncrement(0);
			return true;
		}
		if(!input.incrementToken()){
			return false;
		}
		if(getSameWords(cta.toString())){
			//如果有同义词保存当前状态
			currState = captureState();
		}
		return true;
	}
	
	/**
	 * @函数功能说明 获取同义词
	 * @修改作者名字 HECJ  
	 * @修改时间 2014年11月10日
	 * @修改内容
	 * @参数： @param name    
	 * @return void   
	 * @throws
	 */
	private boolean getSameWords(String name){
		Map<String,String[]> map = new HashMap<String,String[]>();
		map.put("我", new String[]{"咱","俺"});
		map.put("中国", new String[]{"天朝","大陆"});
		
		String[] sws = map.get(name);
		if(sws != null){
			for(String s:sws){
				sames.push(s);
			}
			return true;
		}
		return false;
	}

}
