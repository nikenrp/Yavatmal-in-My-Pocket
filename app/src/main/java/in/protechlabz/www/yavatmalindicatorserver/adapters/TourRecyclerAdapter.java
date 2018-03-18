package in.protechlabz.www.yavatmalindicatorserver.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import in.protechlabz.www.yavatmalindicatorserver.R;
import in.protechlabz.www.yavatmalindicatorserver.tour.TourBorActivity;
import in.protechlabz.www.yavatmalindicatorserver.tour.TourChikhaldaraActivity;
import in.protechlabz.www.yavatmalindicatorserver.tour.TourMahurActivity;
import in.protechlabz.www.yavatmalindicatorserver.tour.TourMelghatActivity;
import in.protechlabz.www.yavatmalindicatorserver.tour.TourPaingangaActivity;
import in.protechlabz.www.yavatmalindicatorserver.tour.TourPenchActivity;
import in.protechlabz.www.yavatmalindicatorserver.tour.TourSahasrakundActivity;
import in.protechlabz.www.yavatmalindicatorserver.tour.TourTadobaActivity;
import in.protechlabz.www.yavatmalindicatorserver.tour.TourTipeshwarActivity;

/**
 * Created by Nikesh on 25/02/2017.
 */

public class TourRecyclerAdapter extends RecyclerView.Adapter<TourRecyclerAdapter.RecyclerViewHolder> {

    String[] listData;
    Context mContext;
    public TourRecyclerAdapter(Context context, String[] listData) {
        this.mContext = context;
        this.listData = listData;
    }

    @Override
    public TourRecyclerAdapter.RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_row_for_bus_and_nearby, parent, false);
        TourRecyclerAdapter.RecyclerViewHolder recyclerViewHolder = new TourRecyclerAdapter.RecyclerViewHolder(view);
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(TourRecyclerAdapter.RecyclerViewHolder holder, int position) {
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

            switch(getAdapterPosition()) {
                case 0:
                    viewholderContext.startActivity(new Intent(this.viewholderContext, TourBorActivity.class));
                    break;
                case 1:
                    viewholderContext.startActivity(new Intent(this.viewholderContext, TourChikhaldaraActivity.class));
                    break;
                case 2:
                    viewholderContext.startActivity(new Intent(this.viewholderContext, TourMahurActivity.class));
                    break;
                case 3:
                    viewholderContext.startActivity(new Intent(this.viewholderContext, TourMelghatActivity.class));
                    break;
                case 4:
                    viewholderContext.startActivity(new Intent(this.viewholderContext, TourPaingangaActivity.class));
                    break;
                case 5:
                    viewholderContext.startActivity(new Intent(this.viewholderContext, TourPenchActivity.class));
                    break;
                case 6:
                    viewholderContext.startActivity(new Intent(this.viewholderContext, TourSahasrakundActivity.class));
                    break;
                case 7:
                    viewholderContext.startActivity(new Intent(this.viewholderContext, TourTadobaActivity.class));
                    break;
                case 8:
                    viewholderContext.startActivity(new Intent(this.viewholderContext, TourTipeshwarActivity.class));
                    break;
            }
        }
    }

}
