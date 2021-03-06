package fb.sio.ecp.fr.federatedbirds.app;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.os.AsyncTaskCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

import fb.sio.ecp.fr.federatedbirds.ApiClient;
import fb.sio.ecp.fr.federatedbirds.R;
import fb.sio.ecp.fr.federatedbirds.model.User;

/**
 * Created by charpi on 13/12/15.
 */
public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.MessageViewHolder> {

    private static final String USERID_KEY = "user_id";
    private static final String USERNAME_KEY = "user_name";
    private static final String USERAVATAR_KEY = "user_avatar";

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

        final long user_id = user.id;
        final String username = user.login;
        final String user_avatar = user.avatar;

        Picasso.with(holder.mAvatarView.getContext())
                .load(user.avatar)
                .into(holder.mAvatarView);

        holder.mUsernameView.setText(user.login);

        holder.mButtonFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncTaskCompat.executeParallel(
                        new SetFollowingTask(v.getContext(), user_id, true)
                );
                Toast.makeText(v.getContext(), R.string.following_success, Toast.LENGTH_SHORT).show();
            }
        });

        holder.mButtonUnfollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncTaskCompat.executeParallel(
                        new SetFollowingTask(v.getContext(), user_id, false)
                );
                Toast.makeText(v.getContext(), R.string.unfollowing_success, Toast.LENGTH_SHORT).show();
            }
        });

        holder.mAvatarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),DetailUserActivity.class);
                Bundle bundle = new Bundle();
                bundle.putLong(USERID_KEY, user_id);
                bundle.putCharSequence(USERNAME_KEY, username);
                bundle.putString(USERAVATAR_KEY, user_avatar);
                intent.putExtras(bundle);
                v.getContext().startActivity(intent);
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


    private class SetFollowingTask extends AsyncTask<Void, Void, User> {

        private Context mContext;
        private long mFollowingId;
        private boolean mFollow;

        public SetFollowingTask(Context context, long following_id, boolean follow){
            mContext = context;
            mFollowingId = following_id;
            mFollow = follow;
        }

        @Override
        protected User doInBackground(Void... params) {
            try {
                return ApiClient.getInstance(mContext).setFollowing(mFollowingId, mFollow);
            } catch (IOException e) {
                Log.e(UsersAdapter.class.getSimpleName(), "Set following failed", e);
                return null;
            }
        }
    }

}
