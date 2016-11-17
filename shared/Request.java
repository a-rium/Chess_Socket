package shared;

import java.io.Serializable;

public class Request<DataType> implements Serializable
{
	private String request;
	private DataType data;

	public Request(String request)
	{
		this.request = request;
		this.data = null;
	}

	public Request(String request, DataType data)
	{
		this.request = request;
		this.data = data;
	}

	public void setRequest(String request)
	{
		this.request = request;
	}

	public String getRequest()
	{
		return request;
	}

	public void setData(DataType data)
	{
		this.data = data;
	}

	public DataType getData()
	{
		return data;
	}

}