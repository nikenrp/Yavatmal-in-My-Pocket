package in.protechlabz.www.yavatmalindicatorserver.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

import in.protechlabz.www.yavatmalindicatorserver.R;
import in.protechlabz.www.yavatmalindicatorserver.ui.BusActivity;

/**
 * Created by Nikesh on 13/01/2017.
 */

public class BusRecyclerAdapter extends RecyclerView.Adapter<BusRecyclerAdapter.RecyclerViewHolder> implements Filterable{
    String[] listData;
    Context mContext;
    public String[] tempListData;

    public BusRecyclerAdapter(Context context, String[] listData) {
        this.mContext = context;
        this.listData = listData;
        this.tempListData = listData;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_row_for_bus_and_nearby, parent, false);
        RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(view);
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        //one possible change here
        holder.textList.setText(tempListData[position]);
        //holder.textList.setText(listData[position]);
    }

    @Override
    public int getItemCount() {
        return tempListData.length;
        //return listData.length;
    }

    //Extra part related to searchview
    @Override
    public Filter getFilter() {
        return new Filter() {
            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                tempListData = (String[]) results.values;
                BusRecyclerAdapter.this.notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String[] filteredResults = null;
                if (constraint.length() == 0) {
                    filteredResults = listData;
                } else {
                    filteredResults = getFilteredResults(constraint.toString().toLowerCase());
                }

                FilterResults results = new FilterResults();
                results.values = filteredResults;
                return results;
            }
        };
    }

    protected String[] getFilteredResults(String constraint) {
        String[] results = new String[]{};
        ArrayList<String> arrayListToBeConvertedToStringArray = new ArrayList<>();
        for (String item : listData) {
            if (item.toLowerCase().contains(constraint)) {
                arrayListToBeConvertedToStringArray.add(item);
            }
        }
        results = arrayListToBeConvertedToStringArray.toArray(new String[0]);
        return results;
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView textList;
        private final Context viewholderContext;
        public RecyclerViewHolder(View itemView) {
            super(itemView);
            viewholderContext = itemView.getContext();
            itemView.setOnClickListener(this);
            textList = (TextView) itemView.findViewById(R.id.text_bus_nearby);
        }

        @Override
        public void onClick(View v) {

            Log.d("Recycle textintextview",String.valueOf(textList.getText()));
            //Log.d("Recycle adapterposition",String.valueOf(getAdapterPosition()));
            Intent i = new Intent(this.viewholderContext, BusActivity.class);
            i.putExtra("BusListKey",String.valueOf(textList.getText()));
            viewholderContext.startActivity(i);
        }
    }
}
