package com.contactsearch;

public class Contacts
{
	private String	name;
	private String	address;
	private String	contact_no;

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getAddress()
	{
		return address;
	}

	public void setAddress(String address)
	{
		this.address = address;
	}

	public String getContact_no()
	{
		return contact_no;
	}

	public void setContact_no(String contact_no)
	{
		this.contact_no = contact_no.replace('(', ' ').replace(')', ' ').replace(" ", "");
	}
}
