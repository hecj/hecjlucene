package org.test.hecjlucene.core08_filter;

public class FilterAccessorImp implements FilterAccessor {

	public String[] getValues() {
		//过滤文件名
		String[]  ids = {"MainActivity.vvv","MainActivity.bbb","MainActivity.aaa","MainActivity.ccc","MainActivity.ddd","MainActivity.eee"};
		//过滤ID
		//String[]  ids = {"26","27","28","29","30"};
		return ids;
	}

	public String getField() {
		return "filename";
//		return "id";
	}

	public boolean set() {
		return false;
	}

}
