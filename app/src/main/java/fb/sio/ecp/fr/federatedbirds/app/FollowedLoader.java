package fb.sio.ecp.fr.federatedbirds.app;

import android.content.Context;

import java.io.IOException;
import java.util.List;

import fb.sio.ecp.fr.federatedbirds.ApiClient;
import fb.sio.ecp.fr.federatedbirds.model.User;

/**
 * Created by charpi on 13/12/15.
 */
public class FollowedLoader extends UsersLoader {

    public FollowedLoader(Context context, Long userId) {
        super(context, userId);
    }

    @Override
    protected List<User> getUsers(Long userId) throws IOException {
        return ApiClient.getInstance(getContext()).getUserFollowed(userId);
    }

}
