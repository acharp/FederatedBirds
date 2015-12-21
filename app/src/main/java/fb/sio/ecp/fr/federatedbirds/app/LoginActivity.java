package fb.sio.ecp.fr.federatedbirds.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import fb.sio.ecp.fr.federatedbirds.R;
import fb.sio.ecp.fr.federatedbirds.utils.ValidationUtils;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        findViewById(R.id.signup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });
    }

    private void login(){
        // Get form views
        EditText usernameText = (EditText) findViewById(R.id.username);
        EditText passwordText = (EditText) findViewById(R.id.password);

        String login = usernameText.getText().toString();
        String password =passwordText.getText().toString();

        if (!ValidationUtils.validatePassword(password)){
            passwordText.setError(getString(R.string.invalid_format));
            passwordText.requestFocus();
            return;
        }

        if (!ValidationUtils.validateLogin(login)){
            usernameText.setError(getString(R.string.invalid_format));
            usernameText.requestFocus();
            // Replace l'utilisateur sur le champ, pour qu'il réécrive son username correctement
            return;
        }

        LoginTaskFragment taskFragment = new LoginTaskFragment();
        taskFragment.setArguments(login, password);

        taskFragment.show(getSupportFragmentManager(), "login_task");

    }

    private void signup(){
        SignUpFragment signUpFragment = new SignUpFragment();
        getSupportFragmentManager().beginTransaction()
                .add(signUpFragment, "signup_task")
                .commit();
    }

}
