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
	public void commit(){
		IndexUtils indexUtils = new IndexUtils();
		indexUtils.commit();
	}
	
	
	@Test
	public void search(){
		IndexUtils indexUtils = new IndexUtils();
		for(int i=0;i<50;i++){
			indexUtils.queryByQuery("String");
			System.out.println("\n------deleteByFileName----------------------------------------------------");
			if(i == 0)
			indexUtils.deleteByFileName("BeanToBakBean.java");
			if(i == 2)
				indexUtils.update();
			
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	
}
