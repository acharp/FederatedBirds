package fb.sio.ecp.fr.federatedbirds.app;

import android.content.Context;

import java.io.IOException;
import java.util.List;

import fb.sio.ecp.fr.federatedbirds.ApiClient;
import fb.sio.ecp.fr.federatedbirds.model.User;

/**
 * Created by charpi on 13/12/15.
 */
public class RelationshipLoader extends UsersLoader {

    private String mRelation;

    public RelationshipLoader(Context context, Long userId, String relation) {
        super(context, userId);
        mRelation = relation;
    }

    @Override
    protected List<User> getUsers(Long userId) throws IOException {
        if (mRelation.equals("followed")) {
            return ApiClient.getInstance(getContext()).getUserFollowed(userId);
        }
        else if (mRelation.equals("followers")) {
            return ApiClient.getInstance(getContext()).getUserFollowers(userId);
        }
        else return null;
    }

}
