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
 *
 * On utilise AsyncTaskLoader plutôt que juste Loader car on va utiliser ces loaders pour faire des appels réseaux donc
 * on veut que ce soit asynchrone.
 */
public class MessagesLoader extends AsyncTaskLoader<List<Message>>{

    private List<Message> mResult;

    public MessagesLoader(Context context){
        super(context);
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
            return ApiClient.getInstance().getMessages();
        } catch (IOException e){
            Log.e("MessagesLoader", "Failed to load messages", e);
            return null;
        }
    }
}
