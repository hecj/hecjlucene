package org.hecjlucene.solr;

import java.io.IOException;
import java.net.MalformedURLException;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

public class TestSolr {
	
	private static String URL = "http://localhost:8080/solr";
	/**
	 * @函数功能说明 删除所有
	 * @修改作者名字 HECJ  
	 * @修改时间 2014年11月30日
	 * @修改内容
	 * @参数：     
	 * @return void   
	 * @throws
	 */
	@Test
	public void deleteAllIndex(){
		
		//1.创建SolrServer对象 CommonsHttpSolrServer EmbededSolrServer
		try {
			CommonsHttpSolrServer server = new CommonsHttpSolrServer(URL);
			//删除所有
			server.deleteByQuery("*:*");
			server.commit();
		
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @函数功能说明 添加索引
	 * @修改作者名字 HECJ  
	 * @修改时间 2014年11月30日
	 * @修改内容
	 * @参数：     
	 * @return void   
	 * @throws
	 */
	@Test
	public void testAddDocument(){
		
		//1.创建SolrServer对象 CommonsHttpSolrServer EmbededSolrServer
		try {
			CommonsHttpSolrServer server = new CommonsHttpSolrServer(URL);

			SolrInputDocument document = new SolrInputDocument();
			//id是唯一的主键，相同的id会覆盖前面的索引
			document.addField("id", "2");
			document.addField("title", "这是我的第");
			document.addField("msg_content", "这是我的第一个solrj程序");
			server.add(document);
			server.commit();
		
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @函数功能说明 根据Field删除索引
	 * @修改作者名字 HECJ  
	 * @修改时间 2014年11月30日
	 * @修改内容
	 * @参数：     
	 * @return void   
	 * @throws
	 */
	@Test
	public void deleteByField(){
		
		try {
			CommonsHttpSolrServer server = new CommonsHttpSolrServer(URL);
			server.deleteByQuery("id:2");
			server.commit();
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
