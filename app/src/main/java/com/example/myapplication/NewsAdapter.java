package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.util.List;

/**
 * Represents the News Adapter Controller which controls the news and the list of articles to display
 */
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyViewHolder> {
    /**
     * List of articles to display
     */
    private List<Article> articles;
    /**
     * The current context of the application
     */
    private Context context;

    /**
     * Constructor to create new NewsAdapter
     * @param articles List of articles to display
     * @param context The current context of the application
     */
    public NewsAdapter(List<Article> articles, Context context) {
        this.articles = articles;
        this.context = context;
    }
    /**
     * Creates a view holder in the parent ViewGroup with the specified viewType
     * @param parent The ViewGroup into which the new View will be added after it is bound to an adapter position.
     * @param viewType The view type of the new View.
     * @return ViewHolder
     */
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.newsitem, parent, false);
        return new MyViewHolder(view);
    }
    /**
     * Displays the view at the specified position
     * @param holders The view holder whose contents should be updated
     * @param position The position of the holder with respect to this adapter
     */
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holders, int position) {
        final MyViewHolder holder = holders;
        Article model = articles.get(position);
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(Utils.getRandomDrawbleColor());
        requestOptions.error(Utils.getRandomDrawbleColor());
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
        requestOptions.centerCrop();

        Glide.with(context)
                .load(model.getUrlToImage())
                .apply(requestOptions)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                }).transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.newsImage);

        holder.title.setText(model.getTitle());
        if (model.getDescription().length()<=100)
        {
            holder.desc.setText(model.getDescription());
        }
        else
        {
            holder.desc.setText(model.getDescription().substring(0,100) + "...");
        }
        holder.source.setText(model.getSource().getName());
        holder.date.setText(model.getPublishedAt().substring(0,10));
    }
    /**
     * Get the size of the list of articles
     * @return integer to indicate size of the list of articles
     */
    @Override
    public int getItemCount() {
        return articles.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView title, desc,  date, source;
        ImageView newsImage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tvTitle);
            desc = itemView.findViewById(R.id.tvDescription);
            source = itemView.findViewById(R.id.tvSource);
            date = itemView.findViewById(R.id.tvDate);

            newsImage = itemView.findViewById(R.id.newsImage);
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {

                    Article article = articles.get(getAdapterPosition());
                    String url = article.getUrl();

                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    context.startActivity(i);
                }
            });
        }
    }
}
