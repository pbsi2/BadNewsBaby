package com.pbsi2.badnewsbaby;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyViewHolder> {
    private ArrayList<BadNews> mNews;


    // Provide a suitable constructor (depends on the kind of dataset)
    public NewsAdapter(ArrayList<BadNews> news) {
        mNews = news;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public NewsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
        // create a new view
        LinearLayout ll = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_single_recycler, parent, false);
        return new MyViewHolder(ll);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        LinearLayout linearLayout = holder.mLinearLayout;

        TextView stitle = linearLayout.findViewById(R.id.newsid);
        stitle.setText(mNews.get(position).getTitle());
        TextView ssection = linearLayout.findViewById(R.id.section);
        ssection.setText(mNews.get(position).getSection());
        TextView sauthor = linearLayout.findViewById(R.id.first_name);
        sauthor.setText(mNews.get(position).getauthor());
        TextView slink = linearLayout.findViewById(R.id.urltxt);
        slink.setText(mNews.get(position).getlink());
        TextView sdescription = linearLayout.findViewById(R.id.description);
        sdescription.setText(mNews.get(position).getsummary());
        TextView sfirstname = linearLayout.findViewById(R.id.first_name);
        sfirstname.setText(mNews.get(position).getdate());
        TextView slastname = linearLayout.findViewById(R.id.last_name);
        slastname.setText("XXXXXXXXXXXXXXXX");


        slink.setOnClickListener(new View.OnClickListener() {
            // The code in this method will be executed when the numbers category is clicked on.
            @Override
            public void onClick(View view) {

            }

        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mNews.size();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public LinearLayout mLinearLayout;

        public MyViewHolder(LinearLayout v) {
            super(v);
            mLinearLayout = v;
        }
    }
}