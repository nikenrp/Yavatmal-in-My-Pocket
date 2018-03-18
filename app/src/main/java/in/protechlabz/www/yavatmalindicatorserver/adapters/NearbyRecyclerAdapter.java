package in.protechlabz.www.yavatmalindicatorserver.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import in.protechlabz.www.yavatmalindicatorserver.R;
import in.protechlabz.www.yavatmalindicatorserver.nearby.NearbyMainActivity;

/**
 * Created by Nikesh on 13/01/2017.
 */

public class NearbyRecyclerAdapter extends RecyclerView.Adapter<NearbyRecyclerAdapter.RecyclerViewHolder> {
    String[] listData;
    Context mContext;
    public NearbyRecyclerAdapter(Context context, String[] listData) {
        this.mContext = context;
        this.listData = listData;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_row_for_bus_and_nearby, parent, false);
        RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(view);
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        holder.textList.setText(listData[position]);
    }

    @Override
    public int getItemCount() {
        return listData.length;
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
            Intent i = new Intent(this.viewholderContext, NearbyMainActivity.class);
            i.putExtra("NearbyListKey",getAdapterPosition());
            Toast.makeText(v.getContext(), "Please Enable Location Services", Toast.LENGTH_LONG).show();
            viewholderContext.startActivity(i);
        }
    }
}
