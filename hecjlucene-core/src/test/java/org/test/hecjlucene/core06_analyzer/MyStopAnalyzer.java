package org.test.hecjlucene.core06_analyzer;

import java.io.Reader;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.LetterTokenizer;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.util.Version;

/**
 * @类功能说明：自定义分词器
 * @类修改者：
 * @修改日期：
 * @修改说明：
 * @作者：HECJ
 * @创建时间：2014年11月6日 下午9:43:41
 * @版本：V1.0
 */
public class MyStopAnalyzer extends Analyzer {

	private Set stops = null;
	
	/**
	 * 添加停用词
	 *@类名：MyStopAnalyzer.java
	 *@描述：{todo}
	 * @param str
	 */
	public MyStopAnalyzer(String[] str) {
		stops = StopFilter.makeStopSet(str, true);
	}

	@Override
	public TokenStream tokenStream(String fieldName, Reader reader) {

		return new StopFilter(Version.LUCENE_35, new LowerCaseFilter(
				Version.LUCENE_35, new LetterTokenizer(Version.LUCENE_35,
						reader)), stops);
	}

}
