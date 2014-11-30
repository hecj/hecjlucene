package org.hecjlucene.solr;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.hecjlucene.bean.Message;
import org.junit.Before;
import org.junit.Test;

public class TestSolr {
	
	private static String URL = "http://localhost:8080/solr";
	
	private CommonsHttpSolrServer server = null ;
	@Before
	public void beforeInit(){
		
		try {
			server = new CommonsHttpSolrServer(URL);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
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
			document.addField("msg_title", "这是我的第");
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
	
	/**
	 * @函数功能说明 集合添加document
	 * @修改作者名字 HECJ  
	 * @修改时间 2014年11月30日
	 * @修改内容
	 * @参数：     
	 * @return void   
	 * @throws
	 */
	@Test
	public void addListDocument(){
		
		List<SolrInputDocument> list = new ArrayList<SolrInputDocument>();
		SolrInputDocument doc = new SolrInputDocument();
		doc.addField("id", "7");
		doc.addField("msg_title", "你好我是7，我可以工作了");
		doc.addField("msg_content", "你好，我可以工作了,哈哈，我针的可以使用了！");
		list.add(doc);
		doc = new SolrInputDocument();
		doc.addField("id", "4");
		doc.addField("msg_title", "你好我是4，我可以工作了");
		doc.addField("msg_content", "你好我是4，我可以工作了,哈哈，我针的可以使用了！");
		list.add(doc);
		
		try {
			server.add(list);
			server.commit();
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * @函数功能说明 bean数据添加
	 * @修改作者名字 HECJ  
	 * @修改时间 2014年11月30日
	 * @修改内容
	 * @参数：     
	 * @return void   
	 * @throws 
	 */
	@Test
	public void addBeanDocument(){
		
		List<Message> list = new ArrayList<Message>();
		list.add(new Message("5","测试一下bean",new String[]{"我的bean的附件一","我的bean的附件 二"}));
		list.add(new Message("6","测试一下bean",new String[]{"我的bean的附件一","我的bean的附件 二"}));
		try {
			server.addBeans(list);
			server.commit();
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
}
