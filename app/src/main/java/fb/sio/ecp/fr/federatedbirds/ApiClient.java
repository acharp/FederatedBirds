package fb.sio.ecp.fr.federatedbirds;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import fb.sio.ecp.fr.federatedbirds.auth.TokenManager;
import fb.sio.ecp.fr.federatedbirds.model.Message;
import fb.sio.ecp.fr.federatedbirds.model.User;

/**
 * Created by charpi on 24/11/15.
 */
public class ApiClient {

    //private static final String API_BASE="http://10.0.2.2:8080/";
    private static final String API_BASE="https://jablog-1158.appspot.com/";

    private static ApiClient mInstance;

    public static synchronized ApiClient getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new ApiClient(context);
        }
        return mInstance;
    }

    private Context mContext;

    private ApiClient(Context context){
        mContext = context.getApplicationContext();
        // On ne stocke pas directement le context (au cas où on nous passerait par exemple une activité) mais l'ApplicationContext
    }

    private <T> T get(String path, Type type) throws IOException{
        return method("GET", path, null, type);
    }

    private <T> T post(String path, Object body, Type type) throws IOException{
        return method("POST", path, body, type);
    }

    private <T> T method(String method, String path, Object body, Type type) throws IOException{
        String url = API_BASE + path;
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod(method);
        String token = TokenManager.getUserToken(mContext);
        if (token != null) {
            connection.addRequestProperty("Authorization", "Bearer " + token);
        }
        if (body != null) {
            Writer writer = new OutputStreamWriter(connection.getOutputStream());
            try {
                new Gson().toJson(body, writer);
            } finally {
                writer.close();
            }
        }
        Reader reader = new InputStreamReader(connection.getInputStream());
        try{
            return new Gson().fromJson(reader, type);
        } finally {
            reader.close();
        }
    }

    public List<Message> getMessages(Long userId) throws IOException {
        TypeToken<List<Message>> type = new TypeToken<List<Message>>(){};
        String path = userId == null ? "messages" : "users/" + userId + "/messages";
        return get(path, type.getType());
    }

    public User getUser(long id) throws IOException {
        return get("users" + id, User.class);
    }

    public List<User> getUserFollowed(Long userId) throws IOException {
        String id = userId != null ? Long.toString(userId) : "me";
        TypeToken<List<User>> type = new TypeToken<List<User>>() {};
        return get("users/" + id + "/followed", type.getType());
    }

    public List<User> getUserFollowers(Long userId) throws IOException {
        String id = userId != null ? Long.toString(userId) : "me";
        TypeToken<List<User>> type = new TypeToken<List<User>>() {};
        return get("users/" + id + "/followers", type.getType());
    }

    public String login(String login, String password) throws IOException {
        JsonObject body = new JsonObject();
        body.addProperty("login", login);
        body.addProperty("password", password);
        return post("auth/token", body, String.class);
    }

    public Message postMessage(String text) throws IOException {
        Message message = new Message();
        message.text = text;
        return post("messages", message, Message.class);
    }

    public String suscribeUser(String login, String password, String email) throws IOException {
        JsonObject body = new JsonObject();
        body.addProperty("login", login);
        body.addProperty("password", password);
        body.addProperty("email", email);
        return post("users", body, String.class);
    }

    public User getAuthUser() throws IOException {
        return get("users/me", User.class);
    }

    public User setFollowing(Long following_id, boolean follow) throws IOException {
        JsonObject body = new JsonObject();
        if (follow) {
            body.addProperty("followed", "true");
        } else {
            body.addProperty("followed", "false");
        }
        return post("users/" + following_id.toString(), body, User.class);
    }

    public String uploadAvatar(String imgString) throws IOException {
        HttpURLConnection connection = null;
        DataOutputStream outputStream = null;
        String pathToOurFile = imgString;
        String urlServer = "https://jablog-1158.appspot.com/users/me/avatar";
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary =  "*****";

        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1*1024*1024;

        FileInputStream fileInputStream = new FileInputStream(new File(pathToOurFile) );

        URL url = new URL(urlServer);
        connection = (HttpURLConnection) url.openConnection();

        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setUseCaches(false);

        connection.setRequestMethod("POST");
        String token = TokenManager.getUserToken(mContext);
        if (token != null) {
            connection.addRequestProperty("Authorization", "Bearer " + token);
        }

        connection.setRequestProperty("Connection", "Keep-Alive");
        connection.setRequestProperty("ENCTYPE", "multipart/form-data");
        connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

        outputStream = new DataOutputStream( connection.getOutputStream() );
        outputStream.writeBytes(twoHyphens + boundary + lineEnd);
        outputStream.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + pathToOurFile +"\"" + lineEnd);
        outputStream.writeBytes(lineEnd);

        bytesAvailable = fileInputStream.available();
        bufferSize = Math.min(bytesAvailable, maxBufferSize);
        buffer = new byte[bufferSize];

        // Read file
        bytesRead = fileInputStream.read(buffer, 0, bufferSize);

        while (bytesRead > 0)
        {
            outputStream.write(buffer, 0, bufferSize);
            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
        }

        outputStream.writeBytes(lineEnd);
        outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

        int serverResponseCode = connection.getResponseCode();
        String serverResponseMessage = connection.getResponseMessage();

        fileInputStream.close();
        outputStream.flush();
        outputStream.close();

        return serverResponseMessage;

        /*
        Reader reader = new InputStreamReader(connection.getInputStream());
        try{
            return new Gson().fromJson(reader, String.class);
        } finally {
            reader.close();
        }
        */

    }
}
