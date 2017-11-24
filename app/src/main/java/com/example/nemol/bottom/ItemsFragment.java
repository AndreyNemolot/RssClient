package com.example.nemol.bottom;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by nemol on 03.09.2017.
 */

public class ItemsFragment extends Fragment {

    private ListView listRssItems;
    private ArrayAdapter<RssItem> itemsAdapter;
    private ArrayList<RssItem> rssItems = new ArrayList<RssItem>();
    private GetArticle getArticle;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            getArticle = (GetArticle) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frg_items_list, null);
        listRssItems = (ListView) v.findViewById(R.id.itemsList);
        itemsAdapter = new ArrayAdapter<RssItem>(getActivity(), R.layout.list_item, rssItems);
        listRssItems.setAdapter(itemsAdapter);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            String link = bundle.getString("link");
            bundle.clear();
            rssItems.addAll(RssItem.getRssItems(link));
            itemsAdapter.notifyDataSetChanged();

        }

        listRssItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                getArticle.getArticle((RssItem) adapterView.getItemAtPosition(i));
            }
        });

        return v;
    }
    void cleanList(){
        rssItems.clear();
    }
}
