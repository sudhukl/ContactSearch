package com.contactsearch;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ListView;

import com.contactsearch.util.CSConstants;

public class SearchActivity extends Activity implements OnClickListener
{
	private final String		CLEAR_SEARCH	= "Reset";

	private ListView			searchListView	= null;
	private ContactsAdapter		adapter			= null;
	private ArrayList<Contacts>	all_contacts	= null;
	private ArrayList<Contacts>	search_results	= new ArrayList<Contacts>();
	private EditText			searchBox		= null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		init();
	}

	@Override
	protected void onResume()
	{
		super.onResume();
	}

	//can be handled in a loader if its large data set
	private void init()
	{

		try
		{
			String sourceStr = "";
			InputStream is = getAssets().open(CSConstants.ASSET_DUMMY_CONTACTS);
			InputStreamReader isr = new InputStreamReader(is);
			int charRead = 0;
			char[] inputBuffer = new char[2000];
			while ((charRead = isr.read(inputBuffer)) > 0)
			{
				// ---convert the chars to a String---
				String readString = String.copyValueOf(inputBuffer, 0, charRead);
				sourceStr += readString;
				inputBuffer = new char[2000];
			}

			is.close();

			JSONObject jsonSource = new JSONObject(sourceStr);
			parseJsonArray(jsonSource);
		}
		catch (Exception e)
		{
			Log.e(getClass().getName(), "Error reading file", e);
		}

		(searchBox = (EditText) findViewById(R.id.searchBox)).setOnClickListener(this);
		findViewById(R.id.searchButton).setOnClickListener(this);
		search_results.addAll(all_contacts);
		adapter = new ContactsAdapter(this, R.layout.search_item, search_results);
		searchListView = (ListView) findViewById(R.id.search_results);
		searchListView.setAdapter(adapter);
		searchListView.setOnItemClickListener(adapter);

	}

	private void parseJsonArray(JSONObject jsonSource) throws Exception
	{
		if (jsonSource != null)
		{
			JSONArray array = jsonSource.optJSONArray(CSConstants.KEY_CONTACTS);
			if (array == null)
				throw new Exception("Field not found : " + CSConstants.KEY_CONTACTS);
			JSONObject jObj = null;
			all_contacts = new ArrayList<Contacts>(array.length());
			for (int i = 0; i < array.length(); i++)
			{
				jObj = array.optJSONObject(i);
				Contacts contacts = new Contacts();
				contacts.setName(jObj.optString(CSConstants.KEY_NAME, "name"));
				contacts.setAddress(jObj.optString(CSConstants.KEY_ADDRESS, "address"));
				contacts.setContact_no(jObj.optString(CSConstants.KEY_PHONE_NO, "(816) 486-3673"));
				all_contacts.add(contacts);
			}
		}
		else
		{
			throw new Exception("No data to parse ");
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		menu.add(CLEAR_SEARCH);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle item selection
		reset();
		return true;
	}

	private void reset()
	{
		search_results.clear();
		refresh();
	}

	@Override
	public void onClick(View view)
	{
		if (view.getId() == R.id.searchButton)
		{
			search();
		}
	}

	private void search()
	{
		search_results.clear();
		String searchStr = searchBox.getText().toString().toLowerCase();
		if (searchStr.length() <= 0)
			return;
		if (searchStr.replace(" ", "").length() <= 0)
			return;
		for (Contacts c : all_contacts)
		{
			if (c.getName().toLowerCase().contains(searchStr) || c.getContact_no().toLowerCase().contains(searchStr) || c.getAddress().toLowerCase().contains(searchStr))
				search_results.add(c);
		}
		//listview notify dataset changed
		refresh();
	}

	private void refresh()
	{
		runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				adapter.notifyDataSetChanged();
			}
		});
	}

}
