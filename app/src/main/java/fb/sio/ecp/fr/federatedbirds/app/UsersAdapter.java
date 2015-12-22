package fb.sio.ecp.fr.federatedbirds.app;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import fb.sio.ecp.fr.federatedbirds.R;
import fb.sio.ecp.fr.federatedbirds.model.User;

/**
 * Created by charpi on 13/12/15.
 */
public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.MessageViewHolder> {

    private List<User> mUsers;

    public void setUsers(List<User> users) {
        mUsers = users;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mUsers != null ? mUsers.size() : 0;
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        return new MessageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        User user = mUsers.get(position);

        Picasso.with(holder.mAvatarView.getContext())
                .load(user.avatar)
                .into(holder.mAvatarView);

        holder.mUsernameView.setText(user.login);

        holder.mButtonFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: implement following
            }
        });

        holder.mButtonUnfollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: implement unfollowing
            }
        });

    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {

        private ImageView mAvatarView;
        private TextView mUsernameView;
        private Button mButtonFollow;
        private Button mButtonUnfollow;

        public MessageViewHolder(View itemView) {
            super(itemView);
            mAvatarView = (ImageView) itemView.findViewById(R.id.avatar);
            mUsernameView = (TextView) itemView.findViewById(R.id.username);
            mButtonFollow = (Button) itemView.findViewById(R.id.follow);
            mButtonUnfollow = (Button) itemView.findViewById(R.id.unfollow);
        }

    }

}
