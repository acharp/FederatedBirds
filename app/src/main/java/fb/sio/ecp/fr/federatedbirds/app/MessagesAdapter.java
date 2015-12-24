package fb.sio.ecp.fr.federatedbirds.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import fb.sio.ecp.fr.federatedbirds.R;
import fb.sio.ecp.fr.federatedbirds.model.Message;

/**
 * Created by charpi on 24/11/15.
 */
public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MessageViewHolder> {

    private static final String USERID_KEY = "user_id";
    private static final String USERNAME_KEY = "user_name";
    private static final String USERAVATAR_KEY = "user_avatar";

    private List<Message> mMessages;

    public void setMessages(List<Message> messages) {
        mMessages = messages;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mMessages != null ? mMessages.size() : 0;
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item, parent, false);
        return new MessageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        Message message = mMessages.get(position);

        final long user_id = message.user.id;
        final String username = message.user.login;
        final String user_avatar = message.user.avatar;

        Picasso.with(holder.mUserAvatarView.getContext())
                .load(message.user.avatar)
                .into(holder.mUserAvatarView);

        holder.mTextView.setText(message.text);

        holder.mUserAvatarView.setOnClickListener(new View.OnClickListener() {
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

        private ImageView mUserAvatarView;
        private TextView mTextView;

        public MessageViewHolder(View itemView) {
            super(itemView);
            mUserAvatarView = (ImageView) itemView.findViewById(R.id.avatar);
            mTextView = (TextView) itemView.findViewById(R.id.text);
        }
    }
}
