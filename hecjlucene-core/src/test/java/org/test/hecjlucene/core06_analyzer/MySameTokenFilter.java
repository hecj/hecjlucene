package org.test.hecjlucene.core06_analyzer;

import java.io.IOException;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
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

	protected MySameTokenFilter(TokenStream input) {
		super(input);
		cta = this.addAttribute(CharTermAttribute.class);
	}

	@Override
	public boolean incrementToken() throws IOException {
		if(!input.incrementToken()){
			return false;
		}
		System.out.println(cta);
		if(cta.toString().equals("安徽")){
			cta.setEmpty();
			cta.append("老家");
		}
		return true;
	}

}
