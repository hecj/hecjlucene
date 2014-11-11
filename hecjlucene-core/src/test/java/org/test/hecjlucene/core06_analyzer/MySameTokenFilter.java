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
	private AttributeSource.State currState;
	private Stack<String> sames;
	private SameWordContent sameWordContent;

	protected MySameTokenFilter(TokenStream input,
			SameWordContent sameWordContent) {
		super(input);
		this.cta = this.addAttribute(CharTermAttribute.class);
		this.pia = this.addAttribute(PositionIncrementAttribute.class);
		this.sames = new Stack<String>();
		this.sameWordContent = sameWordContent;
	}

	@Override
	public boolean incrementToken() throws IOException {
//		System.out.print("[" + cta.toString() + ":"+ pia.getPositionIncrement() + "]------TokenFilter\n");
		while (sames.size() > 0) {
			// 将元素出桟，并获取元素
			String str = sames.pop();
			// 还原状态
			restoreState(currState);
			cta.setEmpty();
			cta.append(str);
			// 设置位置0
			pia.setPositionIncrement(0);
//			System.out.println("currState:" + currState+cta.toString()+"~"+str);
			return true;
		}
		if (!input.incrementToken()) {
			return false;
		}
		if (addSames(cta.toString())) {
			// 如果有同义词保存当前状态
			currState = captureState();
//			System.out.println("currState:" + currState+cta.toString());
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
	private boolean addSames(String name) {
		String[] sws = sameWordContent.getSameWords(name);
		if (sws != null) {
			for (String s : sws) {
				sames.push(s);
			}
			return true;
		}
		return false;
	}

}
