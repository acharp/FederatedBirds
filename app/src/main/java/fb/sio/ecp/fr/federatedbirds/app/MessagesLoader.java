package fb.sio.ecp.fr.federatedbirds.app;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import java.io.IOException;
import java.util.List;

import fb.sio.ecp.fr.federatedbirds.ApiClient;
import fb.sio.ecp.fr.federatedbirds.model.Message;

/**
 * Created by charpi on 24/11/15.
 */
public class MessagesLoader extends AsyncTaskLoader<List<Message>>{

    private List<Message> mResult;
    private Long mUserId;

    public MessagesLoader(Context context, Long UserId){
        super(context);
        mUserId = UserId;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        if (mResult != null) {
            deliverResult(mResult);
        }else {
            forceLoad();
        }
    }

    @Override
    public List<Message> loadInBackground() {
        try {
            return ApiClient.getInstance(getContext()).getMessages(mUserId);
        } catch (IOException e){
            Log.e("MessagesLoader", "Failed to load messages", e);
            return null;
        }
    }

    @Override
    public void deliverResult(List<Message> data) {
        mResult = data;
        super.deliverResult(data);
    }

}
