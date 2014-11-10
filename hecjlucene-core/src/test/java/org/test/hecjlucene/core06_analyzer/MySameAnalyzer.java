package org.test.hecjlucene.core06_analyzer;

import java.io.Reader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;

import com.chenlb.mmseg4j.Dictionary;
import com.chenlb.mmseg4j.MaxWordSeg;
import com.chenlb.mmseg4j.Seg;
import com.chenlb.mmseg4j.analysis.MMSegTokenizer;
/**
 * @类功能说明：同义词
 * @类修改者：
 * @修改日期：
 * @修改说明：
 * @作者：HECJ
 * @创建时间：2014年11月10日 下午2:43:45
 * @版本：V1.0
 */
public class MySameAnalyzer extends Analyzer {
	
	private Dictionary dic = null ;
	@Override
	public TokenStream tokenStream(String fieldName, Reader reader) {
		dic = Dictionary.getInstance("src/main/resources/mmseg4j/data");
		return new MySameTokenFilter(new MMSegTokenizer(new MaxWordSeg(dic),reader));
	}

}
