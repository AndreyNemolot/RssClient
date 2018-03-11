package com.example.nemol.bottom;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.example.nemol.bottom.Fragment.ArticleFragment;
import com.example.nemol.bottom.Fragment.ItemsFragment;
import com.example.nemol.bottom.Fragment.LinksFragment;
import com.example.nemol.bottom.Interface.GetArticle;
import com.example.nemol.bottom.Interface.GetLink;
import com.example.nemol.bottom.Model.RssItem;
import com.example.nemol.bottom.Model.RssLink;

public class MainActivity extends AppCompatActivity implements GetLink, GetArticle {

    final Context context = this;
    private LinksFragment linksFragment;
    private ItemsFragment itemsFragment;
    private ArticleFragment articleFragment;
    private BottomNavigationView bottomNavigationView;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DBHelper(this);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        linksFragment = new LinksFragment();
        itemsFragment = new ItemsFragment();
        articleFragment = new ArticleFragment();
        addFragment(R.id.linksListFrg, false);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFragment(R.id.linksListFrg, true);
                addNewLink();

            }
        });

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.linksListFrg:
                    addFragment(R.id.linksListFrg, false);
                    return true;
                case R.id.itemsListFrg:
                    addFragment(R.id.itemsListFrg, false);
                    return true;
                case R.id.itemArticle:
                    addFragment(R.id.itemArticle, false);
                    return true;
            }

            return false;
        }

    };

    void addFragment(int _view, boolean selectItem) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        switch (_view) {
            case R.id.linksListFrg:
                fragmentTransaction.replace(R.id.frgmCont, linksFragment);
                if (selectItem) {
                    bottomNavigationView.setSelectedItemId(R.id.linksListFrg);
                }
                break;
            case R.id.itemsListFrg:
                fragmentTransaction.replace(R.id.frgmCont, itemsFragment);
                if (selectItem) {
                    bottomNavigationView.setSelectedItemId(R.id.itemsListFrg);
                }
                break;
            case R.id.itemArticle:
                fragmentTransaction.replace(R.id.frgmCont, articleFragment);
                if (selectItem) {
                    bottomNavigationView.setSelectedItemId(R.id.itemArticle);
                }
                break;
        }
        fragmentTransaction.commit();
    }

    void addNewLink() {
        LayoutInflater li = LayoutInflater.from(context);
        View dialogView = li.inflate(R.layout.add_new_rss_dialog, null);
        AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(context);
        mDialogBuilder.setView(dialogView);
        final EditText titleInput = dialogView.findViewById(R.id.input_title);
        final EditText linkInput = dialogView.findViewById(R.id.input_link);

        mDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                SQLiteDatabase db = dbHelper.getWritableDatabase();
                                ContentValues cv = new ContentValues();
                                cv.put("title", titleInput.getText().toString());
                                cv.put("link", linkInput.getText().toString());
                                db.insert("RSStable", null, cv);
                                linksFragment.changeCursor();

                            }
                        })
                .setNegativeButton("Отмена",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        AlertDialog alertDialog = mDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void getLink(String link) {
        addFragment(R.id.itemsListFrg, true);
        itemsFragment.cleanList();
        Bundle args = new Bundle();
        args.putString("link", link);
        itemsFragment.setArguments(args);

    }

    @Override
    public void getArticle(RssItem item) {
        addFragment(R.id.itemArticle, true);
        Bundle args = new Bundle();
        args.putString("title", item.getTitle());
        args.putString("description", item.getDescription());
        args.putString("time", item.getPubDate().toString());
        articleFragment.setArguments(args);
    }
}
