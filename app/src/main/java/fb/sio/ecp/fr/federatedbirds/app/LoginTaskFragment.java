package fb.sio.ecp.fr.federatedbirds.app;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.os.AsyncTaskCompat;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

import fb.sio.ecp.fr.federatedbirds.ApiClient;
import fb.sio.ecp.fr.federatedbirds.R;
import fb.sio.ecp.fr.federatedbirds.auth.TokenManager;

/**
 * Created by charpi on 30/11/15.
 */
public class LoginTaskFragment extends DialogFragment {

    public static final String ARG_LOGIN = "login";
    public static final String ARG_PASSWORD = "password";

    public void setArguments(String login, String password) {
        Bundle params = new Bundle();
        params.putString(LoginTaskFragment.ARG_LOGIN, login);
        params.putString(LoginTaskFragment.ARG_PASSWORD, password);
        setArguments(params);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        AsyncTaskCompat.executeParallel(
                new LoginTaskFragment.LoginTask()
        );
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        ProgressDialog dialog = new ProgressDialog(getContext());
        dialog.setIndeterminate(true);
        dialog.setMessage(getString(R.string.login_progress));
        return dialog;
    }


    private  class LoginTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            try {
                String login = getArguments().getString("login");
                String password = getArguments().getString("password");
                return ApiClient.getInstance(getContext()).login(login, password);
            } catch (IOException e) {
                Log.e(LoginActivity.class.getSimpleName(), "Login failed", e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(String token) {
            if (token != null){
                String login = getArguments().getString("login");
                TokenManager.setUserToken(getContext(), token, login);
                getActivity().finish(); // Pour terminer l'activité de login quand on a fini.
                // Pour que Précédent réagisse correctement : revienne à l'écran d'accueil du tel au lieu de revenir à l'écran de login.
                startActivity(MainActivity.newIntent(getContext()));
            }
            else {
                Toast.makeText(getContext(), R.string.login_failed, Toast.LENGTH_SHORT).show();
            }
            dismiss();
        }
    }
}
