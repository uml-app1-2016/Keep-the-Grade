package edu.uml.android.keepthegrade;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

/**
 * Created by Ashley on 11/15/2016.
 */
public class ChartAdapter extends ArrayAdapter<Chart> {

    /**
     * Create a new {@link ChartAdapter} object.
     *
     * @param context is the current context (i.e. Activity) that the adapter is being created in.
     * @param charts is the list of {@link Chart}s to be displayed.
     */
    public ChartAdapter(Context context, ArrayList<Chart> charts) {
        super(context, 0, charts);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if an existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.item_list, parent, false);
        }

        // Get the {@link Chart} object located at this position in the list
        final Chart currentChart = getItem(position);

        // Based on https://developer.android.com/training/volley/request.html
        // Find the NetworkImageView in the list_item.xml layout with the ID image.
        NetworkImageView imageView =
                (NetworkImageView) listItemView.findViewById(R.id.image);
        // Check if an image is provided for this word or not
        if (currentChart.hasImage()) {
            // If an image is available, display the provided image based on the imageURI
            ImageLoader imageLoader = MySingleton.getInstance(getContext()).getImageLoader();
            imageView.setImageUrl(currentChart.getImageURI().toString(), imageLoader);
            // Make sure the view is visible
            imageView.setVisibility(View.VISIBLE);
        } else {
            // Otherwise hide the ImageView (set visibility to GONE)
            imageView.setVisibility(View.GONE);
        }

        // Return the whole list item layout (containing 2 TextViews) so that it can be shown in
        // the ListView.
        return listItemView;
    }
}
