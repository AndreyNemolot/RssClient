package com.example.nemol.bottom.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.nemol.bottom.R;

import org.w3c.dom.Text;

/**
 * Created by nemol on 11.09.2017.
 */

public class ArticleFragment extends Fragment {

    private TextView tvTitle;
    private TextView tvDescription;
    private TextView tvTime;
    private String title;
    private String description;
    private String time;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frg_article, null);
        tvTitle = (TextView) v.findViewById(R.id.tvTitle);
        tvDescription = (TextView) v.findViewById(R.id.tvDescription);
        tvTime = (TextView) v.findViewById(R.id.tvTime);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            title = bundle.getString("title");
            description = bundle.getString("description");
            time = bundle.getString("time");
        }
        tvTitle.setText(title);
        tvDescription.setText(description);
        tvTime.setText(time);

        return v;
    }

}
