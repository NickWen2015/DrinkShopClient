package drinkshop.cp102.drinkshopclient.amain.news;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import drinkshop.cp102.drinkshopclient.bean.News;
import drinkshop.cp102.drinkshopclient.R;

public class NewsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.news_fragment, container, false);
        List<News> newsList = new ArrayList<>();
        newsList.add(new News("Title 1", "Detail 1"));
        newsList.add(new News("Title 2", "Detail 2"));
        newsList.add(new News("Title 3", "Detail 3"));

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new NewsAdapter(inflater, newsList));
        return view;
    }

    private class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
        private LayoutInflater inflater;
        private List<News> newsList;
        private View visibleView;

        NewsAdapter(LayoutInflater inflater, List<News> newsList) {
            this.inflater = inflater;
            this.newsList = newsList;
        }

        @Override
        public int getItemCount() {
            return newsList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvTitle, tvDetail;

            ViewHolder(View itemView) {
                super(itemView);
                tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
                tvDetail = (TextView) itemView.findViewById(R.id.tvDetail);
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View itemView = inflater.inflate(R.layout.news_item, viewGroup, false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, int position) {
            News news = newsList.get(position);
            viewHolder.tvTitle.setText(news.getTitle());
            viewHolder.tvDetail.setText(news.getDetail());
            viewHolder.tvTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (viewHolder.tvDetail.getVisibility()) {
                        case View.VISIBLE:
                            viewHolder.tvDetail.setVisibility(View.GONE);
                            break;
                        case View.GONE:
                            if (visibleView != null) {
                                visibleView.setVisibility(View.GONE);
                            }
                            viewHolder.tvDetail.setVisibility(View.VISIBLE);
                            visibleView = viewHolder.tvDetail;
                            break;
                    }
                }
            });
        }
    }
}
