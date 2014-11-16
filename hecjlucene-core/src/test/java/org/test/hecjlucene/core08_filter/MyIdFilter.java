package org.test.hecjlucene.core08_filter;

import java.io.IOException;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermDocs;
import org.apache.lucene.search.DocIdSet;
import org.apache.lucene.search.Filter;
import org.apache.lucene.util.OpenBitSet;

public class MyIdFilter extends Filter {

	private FilterAccessor filterAccessor;

	public MyIdFilter(FilterAccessor filterAccessor) {
		this.filterAccessor = filterAccessor;
	}

	@Override
	public DocIdSet getDocIdSet(IndexReader reader) throws IOException {

		OpenBitSet obs = new OpenBitSet(reader.maxDoc());
		if(filterAccessor.set()){
			set(reader, obs);
		}else{
			clear(reader, obs);
		}
		return obs;
	}

	private void set(IndexReader reader, OpenBitSet obs) {
		int[] docs = new int[1];
		int[] freqs = new int[1];
		try {
			// 遍历
			for (String delId : filterAccessor.getValues()) {
				// 获取TermDocs
				TermDocs termDocs = reader.termDocs(new Term(filterAccessor.getField(), delId));
				// 将查询出来的对象存放到docs中，出现的频率存放到freqs中
				int count = termDocs.read(docs, freqs);
				if (count == 1) {
					obs.set(docs[0]);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void clear(IndexReader reader, OpenBitSet obs) {
		// 填充所有的Id
		obs.set(0, reader.maxDoc());
		int[] docs = new int[1];
		int[] freqs = new int[1];
		try {
			// 遍历
			for (String delId : filterAccessor.getValues()) {
				// 获取TermDocs
				TermDocs termDocs = reader.termDocs(new Term(filterAccessor.getField(), delId));
				// 将查询出来的对象存放到docs中，出现的频率存放到freqs中
				int count = termDocs.read(docs, freqs);
				if (count == 1) {
					obs.clear(docs[0]);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
