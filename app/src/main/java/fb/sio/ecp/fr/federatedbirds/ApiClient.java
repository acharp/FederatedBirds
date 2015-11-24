package fb.sio.ecp.fr.federatedbirds;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import fb.sio.ecp.fr.federatedbirds.model.Message;
import fb.sio.ecp.fr.federatedbirds.model.User;

/**
 * Created by charpi on 24/11/15.
 */
public class ApiClient {

    private static final String API_BASE="http://10.0.0.2:8080";

    private static ApiClient mInstance;

    public static synchronized ApiClient getInstance() {
        if (mInstance == null) {
            mInstance = new ApiClient();
        }
        return mInstance;
    }

    private ApiClient(){

    }
    // On rend ce constructeur privé pour compléter le pattern singleton et obliger les utilisateurs à utiliser getInstance
    // au lieu qu'ils apellent new ApiClient() qui briserait notre singleton...

    private String getUserToken(){
        return "azetyiu";
    }

    private <T> T get(String path, Type type) throws IOException{
        String url = API_BASE + path;
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        String token = getUserToken();
        if (token != null) {
            connection.addRequestProperty("Authorization", "Bearer " + token);
        }
        Reader reader = new InputStreamReader(connection.getInputStream());
        try{
            return new Gson().fromJson(reader, type);
        } finally {
            reader.close();
        }
    }

    public List<Message> getMessages() throws IOException {
        TypeToken<List<Message>> type = new TypeToken<List<Message>>(){};
        return get("messages", type.getType());
    }

    public User getUser(long id) throws IOException {
        return get("users/" + id, User.class);
    }

}
