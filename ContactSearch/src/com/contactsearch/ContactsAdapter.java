package com.contactsearch;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ContactsAdapter extends ArrayAdapter<Contacts> implements OnItemClickListener
{
	ArrayList<Contacts>	searchResults	= null;
	int					layout_resource;
	Dialog				dialog			= null;

	public ContactsAdapter(Context context, int resource, ArrayList<Contacts> objects)
	{
		super(context, resource, objects);
		searchResults = objects;
		layout_resource = resource;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent)
	{
		if (view == null)
		{
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(layout_resource, null);
		}
		((TextView) view.findViewById(R.id.name)).setText(searchResults.get(position).getName());
		((TextView) view.findViewById(R.id.contact)).setText(searchResults.get(position).getContact_no());
		return view;
	}

	@Override
	public void onItemClick(AdapterView<?> adapter, View view, int position, long arg3)
	{
		if (dialog == null)
		{
			dialog = new Dialog(adapter.getContext(), R.style.dialogTheme);
			dialog.setContentView(R.layout.contact_details);
			dialog.findViewById(R.id.close).setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					dialog.dismiss();
				}
			});
			dialog.setCanceledOnTouchOutside(true);
		}
		Contacts c = searchResults.get(position);
		((TextView)dialog.findViewById(R.id.name)).setText(c.getName());
		((TextView)dialog.findViewById(R.id.address)).setText(c.getAddress());
		((TextView)dialog.findViewById(R.id.phone)).setText(c.getContact_no());
		((TextView)dialog.findViewById(R.id.title)).setText(c.getName());
		
		dialog.show();
	}
}
