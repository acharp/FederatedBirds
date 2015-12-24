package fb.sio.ecp.fr.federatedbirds.app;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import fb.sio.ecp.fr.federatedbirds.R;
import fb.sio.ecp.fr.federatedbirds.auth.TokenManager;
import fb.sio.ecp.fr.federatedbirds.model.Message;

/**
 * Created by charpi on 23/12/15.
 */
public class DetailUserActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Message>>{

    private static final int LOADER_MESSAGES = 0;
    private static final String USERID_KEY = "user_id";
    private static final String USERNAME_KEY = "user_name";
    private static final String USERAVATAR_KEY = "user_avatar";

    private MessagesAdapter mMessagesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detailuser);

        RecyclerView listView = (RecyclerView) findViewById(R.id.list);
        listView.setLayoutManager(new LinearLayoutManager(this));
        mMessagesAdapter = new MessagesAdapter();
        listView.setAdapter(mMessagesAdapter);

        TextView userNameView = (TextView) findViewById(R.id.username);
        userNameView.setText(getIntent().getExtras().getCharSequence(USERNAME_KEY));

        ImageView userAvatarView = (ImageView) findViewById(R.id.avatar);
        Picasso.with(userAvatarView.getContext())
                .load(getIntent().getExtras().getString(USERAVATAR_KEY))
                .into(userAvatarView);

        userAvatarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String auth_username = TokenManager.getUserLogin(v.getContext());
                if (auth_username.equals(getIntent().getExtras().getCharSequence(USERNAME_KEY))) {
                    Toast.makeText(v.getContext(),"OK OK", Toast.LENGTH_SHORT).show();
                    //TODO: Launch activity or fragment to upload profile picture from gallery
                }
                else {
                    Toast.makeText(v.getContext(), R.string.auth_restriction, Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        getSupportLoaderManager().initLoader(
                LOADER_MESSAGES,
                getIntent().getExtras(),
                this
        );
    }

    @Override
    public Loader<List<Message>> onCreateLoader(int id, Bundle args) {
        return new MessagesLoader(getApplicationContext(), null);
        //TODO: Replace null by args.getLong(USERID_KEY) when the backend method to get messages of a specific user will be implemented
    }

    @Override
    public void onLoadFinished(Loader<List<Message>> loader, List<Message> messages) {
        mMessagesAdapter.setMessages(messages);
    }

    @Override
    public void onLoaderReset(Loader<List<Message>> loader) {

    }
}
