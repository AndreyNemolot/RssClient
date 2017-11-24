package com.example.nemol.bottom;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by nemol on 03.09.2017.
 */

public class LinksFragment extends Fragment {

    private final String DB_NAME = "RSStable";
    private ListView listRssLinks;
    private ArrayAdapter<RssLink> linksAdapter;
    private RssLink selectLink;
    private GetLink getLinkListener;
    private DBHelper dbHelper;
    private ArrayList<RssLink> rssLinks;
    private SQLiteDatabase db;
    private int deleteId;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            getLinkListener = (GetLink) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frg_links_list, null);
        listRssLinks = v.findViewById(R.id.linksList);

        listRssLinks.setAdapter(linksAdapter);
        listRssLinks.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                RssLink deleteItem = (RssLink) listRssLinks.getItemAtPosition(i);
                deleteId = deleteItem.getId();
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(R.string.dialog_delete_link)
                        .setCancelable(false)
                        .setPositiveButton(R.string.Yes,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        listRssLinks.getItemAtPosition(id);
                                        db.delete(DB_NAME, "id = ?", new String[]{String.valueOf(deleteId)});
                                        dbRead();
                                        dialog.cancel();
                                    }
                                })
                        .setNegativeButton(R.string.No, null);
                AlertDialog alert = builder.create();
                alert.show();
                return false;
            }
        });
        dbHelper = new DBHelper(getActivity());
        dbRead();

        listRssLinks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectLink = (RssLink) adapterView.getItemAtPosition(i);
                getLinkListener.getLink(selectLink);
            }
        });
        return v;
    }

    void dbRead() {
        db = dbHelper.getWritableDatabase();
        Cursor c = db.query(DB_NAME, null, null, null, null, null, null);
        rssLinks = new ArrayList<>();
        if (c.moveToFirst()) {
            int idColIndex = c.getColumnIndex("id");
            int titleColIndex = c.getColumnIndex("title");
            int linkColIndex = c.getColumnIndex("link");
            do {
                rssLinks.add(new RssLink(c.getInt(idColIndex), c.getString(linkColIndex), c.getString(titleColIndex)));
            } while (c.moveToNext());
            setList();
        } else
            c.close();
    }

    public void setList() {
        linksAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, rssLinks);
        listRssLinks.setAdapter(linksAdapter);
    }

}
