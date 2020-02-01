package pt.coloryou;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import pt.coloryou.enums.FragmentsEnum;
import pt.coloryou.fragments.CameraFragment;
import pt.coloryou.fragments.informations.ColorAddFragment;
import pt.coloryou.fragments.informations.ColorBlindFragment;
import pt.coloryou.fragments.informations.FathersInformationFragment;
import pt.coloryou.fragments.informations.TeachersInformationFragment;
import pt.coloryou.fragments.TestsFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        if (savedInstanceState == null)
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new ColorAddFragment(), FragmentsEnum.COLOR_ADD_FRAGMENT.getValor()).commit();


        // START - Drawer Navigation Config
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        // END - Drawer Navigation Config

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(FragmentsEnum.COLOR_FRAGMENT.getValor());

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (fragment != null && fragment.isAdded()) {
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            fragment = getSupportFragmentManager().findFragmentByTag(FragmentsEnum.COLOR_PICKER_FRAGMENT.getValor());
            getSupportFragmentManager().beginTransaction().show(fragment).commit();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        System.out.println("Settings Item " + item.getTitle());
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        System.out.println("Item Selected : " + item.getTitle());

        int id = item.getItemId();

        switch (id) {
            case (R.id.nav_home):
                break;
            case (R.id.nav_color_picker):
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new CameraFragment(), FragmentsEnum.COLOR_PICKER_FRAGMENT.getValor()).commit();
                //vf.setDisplayedChild(ErrorEnum.CAMERA.getValor());
                break;
            case (R.id.nav_color_add):
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ColorAddFragment(), FragmentsEnum.COLOR_ADD_FRAGMENT.getValor()).commit();
                break;
            case (R.id.nav_tests):
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new TestsFragment(), FragmentsEnum.TESTS_FRAGMENT.getValor()).commit();
                break;
            case (R.id.nav_fathers_info):
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new FathersInformationFragment(), FragmentsEnum.FATHERS_INFO_FRAGMENT.getValor()).commit();
                break;
            case (R.id.nav_teachers_info):
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new TeachersInformationFragment(), FragmentsEnum.TEACHERS_INFO_FRAGMENT.getValor()).commit();
                break;case (R.id.nav_color_blind):
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ColorBlindFragment(), FragmentsEnum.COLOR_BLIND_FRAGMENT.getValor()).commit();
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /* PRIVATE Methods */

    public Fragment getVisibleFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                if (fragment != null && fragment.isVisible())
                    return fragment;
            }
        }
        return null;
    }
}
