package com.gcl.news.activity;


import android.content.Context;
import android.content.Intent;
import android.graphics.Picture;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.gcl.news.R;
import com.gcl.news.utils.Properties;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFragment extends Fragment {

    public CategoryFragment() {
        // Required empty public constructor
    }

    private GridView gridView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        gridView = view.findViewById(R.id.category_grid_view);

        int[] images = {
                R.drawable.society, R.drawable.entertainment, R.drawable.military,
                R.drawable.tech, R.drawable.sport, R.drawable.ecnomic,
                R.drawable.world, R.drawable.history_news, R.drawable.health
        };
        final String[] titles = {
                "社会", "娱乐", "军事",
                "科技", "体育", "财经",
                "国际", "历史", "养生"
        };
        final String[] category = {
                "news_society", "news_entertainment", "news_military",
                "news_tech", "news_sports", "news_finance",
                "news_world", "news_history", "news_regimen"
        };

        ItemAdapter adapter = new ItemAdapter(getContext(), titles, images);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String titleStr = titles[position];
                Intent intent = new Intent(getContext(), WebActivity.class);
                intent.putExtra("title", titleStr);
                intent.putExtra("url", Properties.BASE_NEWS_URL + "/" + category[position] + "/1");
                startActivity(intent);
            }
        });
        return view;
    }

    class ItemAdapter extends BaseAdapter {

        private Context context;
        private String[] titles;
        private int[] images;

        public ItemAdapter(Context context, String[] titles, int[] images)
        {
            this.context = context;
            this.titles = titles;
            this.images = images;
        }

        @Override
        public int getCount()
        {
            return titles.length;
        }

        @Override
        public Object getItem(int position)
        {
            return titles[position];
        }

        @Override
        public long getItemId(int position)
        {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            ViewHolder viewHolder;
            if (convertView == null)
            {
                convertView = View.inflate(context, R.layout.category_item, null);
                viewHolder = new ViewHolder();
                viewHolder.title = convertView.findViewById(R.id.category_title);
                viewHolder.image = convertView.findViewById(R.id.category_image);
                convertView.setTag(viewHolder);
            } else
            {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.title.setText(titles[position]);
            viewHolder.image.setImageResource(images[position]);
            return convertView;
        }

    }

    class ViewHolder
    {
        public TextView title;
        public ImageView image;
    }
}
