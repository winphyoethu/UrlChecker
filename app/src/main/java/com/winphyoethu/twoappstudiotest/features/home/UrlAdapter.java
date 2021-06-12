package com.winphyoethu.twoappstudiotest.features.home;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.winphyoethu.twoappstudiotest.R;
import com.winphyoethu.twoappstudiotest.data.model.UrlModel;

import java.util.List;

public class UrlAdapter extends ListAdapter<UrlModel, UrlAdapter.UrlViewHolder> {

    private final RequestManager requestManager;
    private final ClickListener urlClickListener;

    public UrlAdapter(RequestManager requestManager, ClickListener urlClickListener) {
        super(new DiffUtil.ItemCallback<UrlModel>() {
            @Override
            public boolean areItemsTheSame(@NonNull UrlModel oldItem, @NonNull UrlModel newItem) {
                return oldItem.getUrl().equals(newItem.getUrl());
            }

            @Override
            public boolean areContentsTheSame(@NonNull UrlModel oldItem, @NonNull UrlModel newItem) {
                return oldItem.equals(newItem);
            }

            @Nullable
            @Override
            public Object getChangePayload(@NonNull UrlModel oldItem, @NonNull UrlModel newItem) {
                if (oldItem.getUrl().equals(newItem.getUrl())) {
                    if (oldItem.isSelected() == newItem.isSelected()) {
                        super.getChangePayload(oldItem, newItem);
                    } else {
                        return newItem.isSelected();
                    }
                }
                return super.getChangePayload(oldItem, newItem);
            }
        });

        this.urlClickListener = urlClickListener;
        this.requestManager = requestManager;
    }

    @Override
    public int getItemCount() {
        return getCurrentList().size();
    }

    @NonNull
    @Override
    public UrlViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UrlViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_url, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull UrlViewHolder holder, int position) {

    }

    @Override
    public void onBindViewHolder(@NonNull UrlViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);

        UrlModel url = getItem(position);

        if (payloads.isEmpty() || payloads.get(0) instanceof Boolean) {
            if (url.isSelected()) {
                holder.clUrl.setBackgroundColor(Color.parseColor("#E4E3E3"));
            } else {
                holder.clUrl.setBackgroundColor(Color.parseColor("#FFFFFF"));
            }
        } else {
            boolean isSelected = (boolean) payloads.get(0);
            if (isSelected) {
                holder.clUrl.setBackgroundColor(Color.parseColor("#E4E3E3"));
            } else {
                holder.clUrl.setBackgroundColor(Color.parseColor("#FFFFFF"));
            }
        }

        requestManager.load(url.getUrlImage())
                .circleCrop()
                .placeholder(R.drawable.ic_placeholder)
                .into(holder.ivUrlImage);

        holder.tvUrl.setText(url.getUrl());
        holder.tvUrlTitle.setText(url.getUrlName());
    }

    class UrlViewHolder extends RecyclerView.ViewHolder {

        ViewGroup clUrl;
        TextView tvUrlTitle, tvUrl;
        ImageView ivUrlImage;

        public UrlViewHolder(@NonNull View itemView) {
            super(itemView);

            clUrl = itemView.findViewById(R.id.cl_url);
            tvUrlTitle = itemView.findViewById(R.id.tv_url_title);
            tvUrl = itemView.findViewById(R.id.tv_url);
            ivUrlImage = itemView.findViewById(R.id.iv_url_image);

            itemView.setOnClickListener(v -> {
                urlClickListener.onUrlClick(getItem(getLayoutPosition()));
            });

            itemView.setOnLongClickListener(v -> {
                urlClickListener.onUrlLongClick(getItem(getLayoutPosition()));
                return true;
            });
        }

    }

}
