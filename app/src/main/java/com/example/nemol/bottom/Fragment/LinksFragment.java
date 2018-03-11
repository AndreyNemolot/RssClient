package com.example.nemol.bottom.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.example.nemol.bottom.DBHelper;
import com.example.nemol.bottom.Interface.GetLink;
import com.example.nemol.bottom.Model.RssLink;
import com.example.nemol.bottom.R;


/**
 * Created by nemol on 03.09.2017.
 */

public class LinksFragment extends Fragment {

    private final String DB_NAME = "RSStable";
    private ListView listRssLinks;
    private SQLiteDatabase db;
    private GetLink getLinkListener;
    private CursorAdapter linksAdapter;
    private Cursor cursor;

    @Override
    public void onDestroy() {
        cursor.close();
        db.close();
        super.onDestroy();
    }

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

        DBHelper dbHelper = new DBHelper(getActivity());
        db = dbHelper.getWritableDatabase();
        setList(getCursor());

        listRssLinks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(i);
                getLinkListener.getLink(cursor.getString(2));
            }
        });

        listRssLinks.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final long deleteId = l;
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(R.string.dialog_delete_link)
                        .setCancelable(false)
                        .setPositiveButton(R.string.Yes,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        db.delete(DB_NAME, "_id = ?", new String[]{String.valueOf(deleteId)});
                                        changeCursor();
                                        dialog.cancel();
                                    }
                                })
                        .setNegativeButton(R.string.No, null);
                AlertDialog alert = builder.create();
                alert.show();
                return true;
            }
        });
        return v;
    }

    public Cursor getCursor() {
        cursor = db.query(DB_NAME, null, null, null, null, null, null);
        return cursor;
    }

    public void changeCursor() {
        linksAdapter.changeCursor(getCursor());
    }

    public void setList(Cursor cursor) {
        linksAdapter = new SimpleCursorAdapter(getActivity(), android.R.layout.simple_list_item_1,
                cursor, new String[]{"title"}, new int[]{android.R.id.text1}, 0);
        listRssLinks.setAdapter(linksAdapter);


    }

}
