package org.test.hecjlucene.core10;

import org.junit.Test;

public class TestIndexUtils {
	
	@Test
	public void createIndex(){
		IndexUtils indexUtils = new IndexUtils();
		indexUtils.createIndex();
	}
	
	@Test
	public void query(){
		IndexUtils indexUtils = new IndexUtils();
		indexUtils.query();
	}
	
	@Test
	public void delete(){
		IndexUtils indexUtils = new IndexUtils();
		indexUtils.deleteByFileName("BeanToBakBean.java");
	}
	
	@Test
	public void search(){
		IndexUtils indexUtils = new IndexUtils();
		for(int i=0;i<5;i++){
			indexUtils.queryByQuery("String");
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("\n----------------------------------------------------------");
		}
		
	}
	
	
}
