package org.hecjlucene.bean;

import org.apache.solr.client.solrj.beans.Field;

public class Message {
	
	private String id;
	private String title;
	private String[] content;
	public Message(String id, String title, String[] content) {
		super();
		this.id = id;
		this.title = title;
		this.content = content;
	}
	public Message() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getId() {
		return id;
	}
	@Field("id")
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	@Field("msg_title")
	public void setTitle(String title) {
		this.title = title;
	}
	public String[] getContent() {
		return content;
	}
	@Field("msg_content")
	public void setContent(String[] content) {
		this.content = content;
	}
	
	
}
