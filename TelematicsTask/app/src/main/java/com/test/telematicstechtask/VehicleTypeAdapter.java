package com.test.telematicstechtask;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.test.telematicstechtask.ResponseModel.VehicleType;

import java.util.ArrayList;

import static androidx.recyclerview.widget.RecyclerView.*;

public class VehicleTypeAdapter extends Adapter {

    interface Callbacks {
        public void onClickLoadMore();
    }
    private Callbacks mCallbacks;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_FOOTER = 2;

    private boolean mWithHeader = false;
    private boolean mWithFooter = false;
    private Context context;
    private ArrayList<VehicleType> vehList;
    int count  = 0;
    public VehicleTypeAdapter(Context context, ArrayList<VehicleType> vehList) {
        super();
        this.context = context;
        this.vehList = vehList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemView = null;

        if (viewType == TYPE_FOOTER ) {

                itemView = View.inflate(context, R.layout.loadmore, null);
                return new LoadMoreViewHolder(itemView);

        }

        else {

            itemView = View.inflate(context, R.layout.list_items, null);
            return new VehViewHolder(itemView);
        }
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if(holder instanceof LoadMoreViewHolder) {
            LoadMoreViewHolder loadMoreViewHolder = (LoadMoreViewHolder) holder;

            loadMoreViewHolder.addMorelinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(mCallbacks!=null) {
                        count++;
                        mCallbacks.onClickLoadMore();
                    }
                    if(count%2!=0){
                        loadMoreViewHolder.addMorelinearLayout.setImageResource(R.drawable.ic_baseline_remove_24);
                        loadMoreViewHolder.type .setText( "Less");
                    }
                    else {
                        loadMoreViewHolder.addMorelinearLayout.setImageResource(R.drawable.ic_baseline_add_24);
                        loadMoreViewHolder.type .setText( "More");
                    }
                }
            });

        }
        else {
            VehViewHolder elementsViewHolder = (VehViewHolder) holder;

            VehicleType elements = vehList.get(position);

            elementsViewHolder.Name.setText(elements.getText());
        }

    }


    @Override
    public int getItemCount() {
        int itemCount = vehList.size();
        if (mWithHeader)
            itemCount++;
        if (mWithFooter)
            itemCount++;
        return itemCount;
    }

    @Override
    public int getItemViewType(int position) {
        if (mWithHeader && isPositionHeader(position))
            return TYPE_HEADER;
        if (mWithFooter && isPositionFooter(position))
            return TYPE_FOOTER;
        return TYPE_ITEM;
    }

    public boolean isPositionHeader(int position) {
        return position == 0 && mWithHeader;
    }

    public boolean isPositionFooter(int position) {
        return position == getItemCount() - 1 && mWithFooter;
    }

    public void setWithHeader(boolean value){
        mWithHeader = value;
    }

    public void setWithFooter(boolean value){
        mWithFooter = value;
    }

    public void setCallback(MainActivity callbacks){
        mCallbacks = callbacks;
    }



    static class VehViewHolder extends ViewHolder {
        TextView Name;


        VehViewHolder(@NonNull View itemView) {
            super(itemView);
           Name = itemView.findViewById(R.id.vehicle_name);

        }
    }

    public static class LoadMoreViewHolder  extends ViewHolder {
        ImageButton addMorelinearLayout;
        TextView  type;
        LoadMoreViewHolder(View itemView) {
            super(itemView);
             addMorelinearLayout = itemView.findViewById(R.id.img);
             type = itemView.findViewById(R.id.type);

        }
    }


}
