package software_masters.planner_networking;

import java.io.Serializable;

public class Comment implements Serializable{
	private static final long serialVersionUID = 1L;
	private String username;
	private String content;
	private int id;
	
	public Comment(String username,String content,int id)
	{
		this.username=username;
		this.content=content;
		this.id=id;
	}
	public String getUsername()
	{
		return username;
	}
	public int getID()
	{
		return id;
	}
	public String getContent()
	{
		return content;
	}

}
