package ca.ulaval.ima.mp.ui.dashboard;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import ca.ulaval.ima.mp.R;
import ca.ulaval.ima.mp.ui.dashboard.ResaurantListFragment.OnListFragmentInteractionListener;
import ca.ulaval.ima.mp.dummy.DummyContent.DummyItem;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyResaurantListRecyclerViewAdapter extends RecyclerView.Adapter<MyResaurantListRecyclerViewAdapter.ViewHolder> {

    private final List<DummyItem> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyResaurantListRecyclerViewAdapter(List<DummyItem> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_resaurant_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.name.setText(mValues.get(position).name);
        holder.distance.setText(mValues.get(position).distance);
        holder.reviewCount.setText(mValues.get(position).reviewCount);
        holder.reviewAverage.setRating(mValues.get(position).reviewAverage);
        Picasso.get().load(mValues.get(position).image).into(holder.image);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView name;
        public final TextView distance;
        public final TextView reviewCount;
        public final ImageView image;
        public final RatingBar reviewAverage;
        public DummyItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            name = view.findViewById(R.id.name);
            distance = view.findViewById(R.id.distance);
            reviewCount = view.findViewById(R.id.reviewCount);
            reviewAverage = view.findViewById(R.id.ratingBar);
            image = view.findViewById(R.id.image);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + name.getText() + "'";
        }
    }
}
