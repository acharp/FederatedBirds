package fb.sio.ecp.fr.federatedbirds.app;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.os.AsyncTaskCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.util.HashMap;

import fb.sio.ecp.fr.federatedbirds.ApiClient;
import fb.sio.ecp.fr.federatedbirds.R;
import fb.sio.ecp.fr.federatedbirds.utils.ValidationUtils;

/**
 * Created by charpi on 21/12/15.
 */
public class SignUpFragment extends Fragment {

    private EditText mLoginText;
    private EditText mPasswordText;
    private EditText mEmailText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.signup_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        mLoginText = (EditText) view.findViewById(R.id.signup_username);
        mPasswordText = (EditText) view.findViewById(R.id.signup_password);
        mEmailText = (EditText) view.findViewById(R.id.signup_email);

        view.findViewById(R.id.signup_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String login = mLoginText.getText().toString();
                String password = mPasswordText.getText().toString();
                String email = mEmailText.getText().toString();

                if (!(ValidationUtils.validateLogin(login))){
                    mLoginText.setError(getString(R.string.invalid_format));
                    mLoginText.requestFocus();
                    return;
                }

                if (!(ValidationUtils.validatePassword(password))) {
                    mPasswordText.setError(getString(R.string.invalid_format));
                    mPasswordText.requestFocus();
                    return;
                }

                if (!(ValidationUtils.validateEmail(email))) {
                    mEmailText.setError(getString(R.string.invalid_format));
                    mEmailText.requestFocus();
                    return;
                }

                HashMap<String, String> task_params = new HashMap<>();
                task_params.put("login", login);
                task_params.put("password", password);
                task_params.put("email", email);

                SignUpTaskFragment taskFragment = new SignUpTaskFragment();
                taskFragment.setArguments(task_params);
                taskFragment.setTargetFragment(
                        getTargetFragment(),
                        getTargetRequestCode()
                );
                taskFragment.show(getFragmentManager(), "post_progress");
            }
        });

        view.findViewById(R.id.cancel_action).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeFragment();
            }
        });

    }

    private void closeFragment() {
        getFragmentManager().beginTransaction().remove(this).commit();
    }

        public static class SignUpTaskFragment extends DialogFragment {

        private static final String ARG_LOGIN = "login";
        private static final String ARG_PASSWORD = "password";
        private static final String ARG_EMAIL = "email";

        public void setArguments(HashMap<String, String> user_infos) {
            Bundle args = new Bundle();
            args.putString(SignUpTaskFragment.ARG_LOGIN, user_infos.get(ARG_LOGIN));
            args.putString(SignUpTaskFragment.ARG_PASSWORD, user_infos.get(ARG_PASSWORD));
            args.putString(SignUpTaskFragment.ARG_EMAIL, user_infos.get(ARG_EMAIL));
            setArguments(args);
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setRetainInstance(true);
            AsyncTaskCompat.executeParallel(
                    new SignUpTask()
            );
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            ProgressDialog dialog = new ProgressDialog(getContext());
            dialog.setIndeterminate(true);
            dialog.setMessage(getString(R.string.progress));
            return dialog;
        }

        private class SignUpTask extends AsyncTask<Void, Void, String> {

            @Override
            protected String doInBackground(Void... params) {
                try {
                    String login = getArguments().getString("login");
                    String password = getArguments().getString("password");
                    String email = getArguments().getString("email");
                    return ApiClient.getInstance(getContext()).suscribeUser(login, password, email);
                } catch (IOException e) {
                    Log.e(SignUpTask.class.getSimpleName(), "User registration failed", e);
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String user) {
                if (user != null) {
                    Toast.makeText(getContext(), R.string.registration_success, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), R.string.registration_failed, Toast.LENGTH_SHORT).show();
                }
                dismiss();
            }
        }
    }
}
