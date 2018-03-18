package in.protechlabz.www.yavatmalindicatorserver.blog;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import in.protechlabz.www.yavatmalindicatorserver.R;

/**
 * Created by Nikesh on 25/03/2017.
 */

public class LikesListRecyclerAdapter extends RecyclerView.Adapter<LikesListRecyclerAdapter.RecyclerViewHolder>{


    ArrayList<String> listUserNames;
    ArrayList<String> listProfilePics;
    Context mContext;

    public LikesListRecyclerAdapter(ArrayList<String> listUserNames, ArrayList<String> listProfilePics, Context context) {
        this.listUserNames = listUserNames;
        this.listProfilePics = listProfilePics;
        mContext = context;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.likeslist_row, parent, false);
        RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(view);
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        holder.userName.setText(listUserNames.get(position));
        /*if(!(listProfilePics.get(position).equals("No Image"))) {
            Picasso.with(mContext).load(listProfilePics.get(position)).into(holder.profilePic);
        }*/
    }

    @Override
    public int getItemCount() {
        return listUserNames.size();
    }


    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {

        private TextView userName;
        private ImageView profilePic;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            userName = (TextView) itemView.findViewById(R.id.likelist_username);
            //profilePic = (ImageView) itemView.findViewById(R.id.likelist_profile_image);
        }
    }
}
