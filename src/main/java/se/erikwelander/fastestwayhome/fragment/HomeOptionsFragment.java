package se.erikwelander.fastestwayhome.fragment;

import android.app.ListFragment;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import se.erikwelander.fastestwayhome.R;
import se.erikwelander.fastestwayhome.adapter.OptionAdapter;
import se.erikwelander.fastestwayhome.model.OptionRow;

public class HomeOptionsFragment extends ListFragment
{
    private ListView listView;
    private ArrayList<OptionRow> items;
    private OptionAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.list_fragment, container, false);
        listView = (ListView) rootView.findViewById(android.R.id.list);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        Resources resources = getResources();
        items = new ArrayList<>();

        items.add(new OptionRow(resources.getDrawable(R.mipmap.icon_destination_home), "Hem "));
        items.add(new OptionRow(resources.getDrawable(R.mipmap.icon_destionation_work), "Till jobbet "));
        items.add(new OptionRow(resources.getDrawable(R.mipmap.icon_destionation_other), "Annat "));
        adapter = new OptionAdapter(getActivity(), android.R.id.list, items);
        listView.setAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // retrieve theListView item
        OptionRow item = items.get(position);

        // do something
        Toast.makeText(getActivity(), item.title, Toast.LENGTH_SHORT).show();
    }
}
