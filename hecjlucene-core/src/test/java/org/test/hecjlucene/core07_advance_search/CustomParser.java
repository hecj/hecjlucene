package org.test.hecjlucene.core07_advance_search;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.util.Version;

public class CustomParser extends QueryParser {

	public CustomParser(Version matchVersion, String f, Analyzer a) {
		super(matchVersion, f, a);
	}
	
	@Override
	protected org.apache.lucene.search.Query getWildcardQuery(String field,
			String termStr) throws ParseException {
		throw new ParseException("由于性能原因，已经禁用了通配符查询，请输入精确的查询条件！");
	}
	
	@Override
	protected org.apache.lucene.search.Query getFuzzyQuery(String field,
			String termStr, float minSimilarity) throws ParseException {
		throw new ParseException("由于性能原因，已经禁用了模糊查询，请输入精确的查询条件！");
	}
	
}
