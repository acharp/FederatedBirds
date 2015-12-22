package fb.sio.ecp.fr.federatedbirds.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import fb.sio.ecp.fr.federatedbirds.R;
import fb.sio.ecp.fr.federatedbirds.auth.TokenManager;

public class MainActivity extends AppCompatActivity  {

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }

    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkUserLogin();

        setContentView(R.layout.activity_main);

        final NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        Fragment homeFragment = new HomeFragment();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.main_container, homeFragment)
                                .commit();
                        ((DrawerLayout) findViewById(R.id.drawer)).closeDrawer(navigationView);
                        return true;
                    case R.id.followed:
                        RelationshipFragment followedFragment = new RelationshipFragment();
                        followedFragment.setArguments("followed");
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.main_container, followedFragment)
                                .commit();
                        ((DrawerLayout) findViewById(R.id.drawer)).closeDrawer(navigationView);
                        return true;
                    case R.id.followers:
                        RelationshipFragment followersFragment = new RelationshipFragment();
                        followersFragment.setArguments("followers");
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.main_container, followersFragment)
                                .commit();
                        ((DrawerLayout) findViewById(R.id.drawer)).closeDrawer(navigationView);
                        return true;
                    case R.id.settings:
                        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                        startActivity(intent);
                        ((DrawerLayout) findViewById(R.id.drawer)).closeDrawer(navigationView);
                        return true;
                }
                return false;
            }
        });

        if (savedInstanceState == null){
            HomeFragment fragment = new HomeFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_container, fragment)
                    .commit();
        }

    }

    @Override
    public void setSupportActionBar(Toolbar toolbar) {
        super.setSupportActionBar(toolbar);
        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                (DrawerLayout) findViewById(R.id.drawer),
                toolbar,
                R.string.open_menu,
                R.string.close_menu
        );
        mDrawerToggle.syncState();
    }

    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        mDrawerToggle.syncState();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        checkUserLogin();
    }

    protected void checkUserLogin(){
        if (TokenManager.getUserToken(this) == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }

}
