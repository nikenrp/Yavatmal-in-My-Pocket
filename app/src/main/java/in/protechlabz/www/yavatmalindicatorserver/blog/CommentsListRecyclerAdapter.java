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
 * Created by Nikesh on 26/03/2017.
 */

public class CommentsListRecyclerAdapter extends RecyclerView.Adapter<CommentsListRecyclerAdapter.RecyclerViewHolder> {

    private ArrayList<String> listUserNames;
    private ArrayList<String> listProfilePics;
    private ArrayList<String> listUserComments;
    private ArrayList<String> listCommentTimings;
    private Context mContext;

    public CommentsListRecyclerAdapter(ArrayList<String> listUserNames, ArrayList<String> listProfilePics,
                                       ArrayList<String> listUserComments, ArrayList<String> listCommentTimings, Context context) {
        this.listUserNames = listUserNames;
        this.listProfilePics = listProfilePics;
        this.listUserComments = listUserComments;
        this.listCommentTimings = listCommentTimings;
        mContext = context;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.commentlist_row, parent, false);
        RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(view);
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        holder.userName.setText(listUserNames.get(position));
        /*if(!(listProfilePics.get(position).equals("No Image"))) {
            Picasso.with(mContext).load(listProfilePics.get(position)).into(holder.profilePic);
        }*/
        holder.userComment.setText(listUserComments.get(position));
        holder.userCommentTime.setText(listCommentTimings.get(position));
    }

    @Override
    public int getItemCount() {
        return listUserNames.size();
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {

        private TextView userName;
        private ImageView profilePic;
        private TextView userComment;
        private TextView userCommentTime;
        public RecyclerViewHolder(View itemView) {
            super(itemView);
            userName = (TextView) itemView.findViewById(R.id.commentlist_username);
            //profilePic = (ImageView) itemView.findViewById(R.id.commentlist_profile_image);
            userComment = (TextView) itemView.findViewById(R.id.commentlist_user_comment);
            userCommentTime = (TextView) itemView.findViewById(R.id.commentlist_comment_time);
        }
    }
}
