package com.example.rajztanfolyam;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TanfolyamItemAdapter
        extends RecyclerView.Adapter<TanfolyamItemAdapter.ViewHolder>
        implements Filterable {
    // Member variables.
    private ArrayList<TanfolyamItem> mShoppingData;
    private ArrayList<TanfolyamItem> mSoppingDataAll;
    private Context mContext;
    private int lastPosition = -1;

    TanfolyamItemAdapter(Context context, ArrayList<TanfolyamItem> itemsData) {
        this.mShoppingData = itemsData;
        this.mSoppingDataAll = itemsData;
        this.mContext = context;
    }

    @Override
    public TanfolyamItemAdapter.ViewHolder onCreateViewHolder(
            ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(TanfolyamItemAdapter.ViewHolder holder, int position) {
        // Get current sport.
        TanfolyamItem currentItem = mShoppingData.get(position);

        // Populate the textviews with data.
        holder.bindTo(currentItem);


        if(holder.getAdapterPosition() > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.slide);
            holder.itemView.startAnimation(animation);
            lastPosition = holder.getAdapterPosition();
        }
    }

    @Override
    public int getItemCount() {
        return mShoppingData.size();
    }


    /**
     * RecycleView filter
     * **/
    @Override
    public Filter getFilter() {
        return shoppingFilter;
    }

    private Filter shoppingFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<TanfolyamItem> filteredList = new ArrayList<>();
            FilterResults results = new FilterResults();

            if(charSequence == null || charSequence.length() == 0) {
                results.count = mSoppingDataAll.size();
                results.values = mSoppingDataAll;
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();
                for(TanfolyamItem item : mSoppingDataAll) {
                    if(item.getName().toLowerCase().contains(filterPattern)){
                        filteredList.add(item);
                    }
                }

                results.count = filteredList.size();
                results.values = filteredList;
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            mShoppingData = (ArrayList)filterResults.values;
            notifyDataSetChanged();
        }
    };

    class ViewHolder extends RecyclerView.ViewHolder {
        // Member Variables for the TextViews
        private TextView mName;
        private TextView mPrice;
        private TextView mDiff;
        private TextView mEvent;

        ViewHolder(View itemView) {
            super(itemView);

            // Initialize the views.
            mName = itemView.findViewById(R.id.name);
            mPrice = itemView.findViewById(R.id.price);
            mEvent = itemView.findViewById(R.id.event);
            mDiff = itemView.findViewById(R.id.diff);

            itemView.findViewById(R.id.sendOrder).setOnClickListener(view -> ((TanfolyamListActivity)mContext).subcribeTanfolyam());
        }

        void bindTo(TanfolyamItem currentItem){
            mName.setText(currentItem.getName());
            mPrice.setText(currentItem.getPrice());
            mEvent.setText(currentItem.getEventDb());
            mDiff.setText(currentItem.getDiff());

        }
    }
}
