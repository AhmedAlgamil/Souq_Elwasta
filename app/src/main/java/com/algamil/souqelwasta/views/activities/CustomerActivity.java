package com.algamil.souqelwasta.views.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.algamil.souqelwasta.R;
import com.algamil.souqelwasta.data.local.TinyDB;
import com.algamil.souqelwasta.helper.HelperMethod;
import com.algamil.souqelwasta.services.MusicService;
import com.algamil.souqelwasta.views.activities.ui.gallery.CategoryFragment;
import com.algamil.souqelwasta.views.activities.ui.slideshow.ContactUsFragment;
import com.algamil.souqelwasta.views.fragments.AdsFragment;
import com.algamil.souqelwasta.views.fragments.DeliveryFragment;
import com.algamil.souqelwasta.views.fragments.DiscountFragment;
import com.algamil.souqelwasta.views.fragments.DoctorsFragment;
import com.algamil.souqelwasta.views.fragments.HomePageFragment;
import com.algamil.souqelwasta.views.fragments.JobsFragment;
import com.algamil.souqelwasta.views.fragments.RealEstateFragment;
import com.algamil.souqelwasta.views.fragments.UsedFragment;
import com.google.android.material.navigation.NavigationView;
import com.infideap.drawerbehavior.AdvanceDrawerLayout;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.algamil.souqelwasta.data.local.SharedPreferencesManger.DISTINATION;
import static com.algamil.souqelwasta.helper.Constant.FRAME_LOC;
import static com.algamil.souqelwasta.helper.Constant.FROM_DISTINATION;
import static com.algamil.souqelwasta.helper.Constant.PREVIOU_DESTINATION;
import static com.algamil.souqelwasta.helper.HelperMethod.getStringFromXML;
import static com.algamil.souqelwasta.helper.HelperMethod.showCustomDialog2;

public class CustomerActivity extends BaseActivity {

    AdvanceDrawerLayout drawer;
    TinyDB tinyDB;
    static Toolbar toolbar;
    NavigationView navigationView;
    String first = "first";
    @BindView(R.id.frame_customer)
    FrameLayout frameCustomer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);
        ButterKnife.bind(this);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getStringFromXML(CustomerActivity.this, R.string.menu_home));
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        tinyDB = new TinyDB(CustomerActivity.this);
        HelperMethod.onPermission(CustomerActivity.this);
        toolbar.setTitle(getStringFromXML(CustomerActivity.this, R.string.menu_branshes));
        navigationView = findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_category);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setItemIconTintList(null);
        HelperMethod.replaceFragment(getSupportFragmentManager(), R.id.frame_customer, new CategoryFragment());
        tinyDB.putString(DISTINATION , "another");
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        if (navigationView.getCheckedItem().getItemId() == item.getItemId()) {

                        }
                        else
                        {
                            tinyDB.putString(FROM_DISTINATION, "from customer");
                            tinyDB.putString(PREVIOU_DESTINATION, "home");
                            HelperMethod.replaceFragment(getSupportFragmentManager(), R.id.frame_customer, new HomePageFragment());
                            toolbar.setTitle(getStringFromXML(CustomerActivity.this, R.string.menu_home));
                            item.setChecked(false);
                        }
                        break;

                    case R.id.nav_category:
                        if (navigationView.getCheckedItem().getItemId() == item.getItemId()) {

                        } else {
                            tinyDB.putString(FROM_DISTINATION, "from customer");
                            tinyDB.putString(FRAME_LOC, "categories");
                            HelperMethod.replaceFragment(getSupportFragmentManager(), R.id.frame_customer, new CategoryFragment());
                            toolbar.setTitle(getStringFromXML(CustomerActivity.this, R.string.menu_branshes));
                            item.setChecked(false);
                        }
                        break;

                    case R.id.nav_ads:
                        if (navigationView.getCheckedItem().getItemId() == item.getItemId()) {

                        } else {
                            tinyDB.putString(FROM_DISTINATION, "from customer");
                            HelperMethod.replaceFragment(getSupportFragmentManager(), R.id.frame_customer, new AdsFragment());
                            toolbar.setTitle(getStringFromXML(CustomerActivity.this, R.string.menu_ads));
                            item.setChecked(false);
                        }
                        break;

                    case R.id.nav_offers:
                        if (navigationView.getCheckedItem().getItemId() == item.getItemId()) {

                        } else {
                            tinyDB.putString(FROM_DISTINATION, "from customer");
                            HelperMethod.replaceFragment(getSupportFragmentManager(), R.id.frame_customer, new DiscountFragment());
                            toolbar.setTitle(getStringFromXML(CustomerActivity.this, R.string.menu_discounts));
                        }
                        break;

                    case R.id.nav_used:
                        if (navigationView.getCheckedItem().getItemId() == item.getItemId()) {

                        } else {
                            tinyDB.putString(FROM_DISTINATION, "from customer");
                            HelperMethod.replaceFragment(getSupportFragmentManager(), R.id.frame_customer, new UsedFragment());
                            toolbar.setTitle(getStringFromXML(CustomerActivity.this, R.string.menu_used));
                        }
                        break;

                    case R.id.nav_real_estates:
                        if (navigationView.getCheckedItem().getItemId() == item.getItemId()) {

                        } else {
                            tinyDB.putString(FRAME_LOC, "nav_view");
                            tinyDB.putString(FROM_DISTINATION, "from customer");
                            HelperMethod.replaceFragment(getSupportFragmentManager(), R.id.frame_customer, new RealEstateFragment());
                            toolbar.setTitle(getStringFromXML(CustomerActivity.this, R.string.menu_real_estates));
                        }
                        break;

                    case R.id.nav_doctors:
                        if (navigationView.getCheckedItem().getItemId() == item.getItemId()) {

                        } else {
                            tinyDB.putString(FROM_DISTINATION, "from customer");
                            tinyDB.putString(FRAME_LOC, "nav_view");
                            HelperMethod.replaceFragment(getSupportFragmentManager(), R.id.frame_customer, new DoctorsFragment());
                            toolbar.setTitle(getStringFromXML(CustomerActivity.this, R.string.menu_doctors));
                        }
                        break;

                    case R.id.nav_jobs:

                        if (navigationView.getCheckedItem().getItemId() == item.getItemId()) {

                        } else {
                            tinyDB.putString(FROM_DISTINATION, "from customer");
                            HelperMethod.replaceFragment(getSupportFragmentManager(), R.id.frame_customer, new JobsFragment());
                            toolbar.setTitle(getStringFromXML(CustomerActivity.this, R.string.menu_jobs));
                        }
                        break;

                    case R.id.nav_map:
                        String uri = String.format(Locale.ENGLISH, "geo:%f,%f", 29.342328, 31.207861);
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                        startActivity(intent);
                        break;

                    case R.id.nav_delivery:
                        if (navigationView.getCheckedItem().getItemId() == item.getItemId()) {

                        } else {
                            tinyDB.putString(FROM_DISTINATION, "from customer");
                            HelperMethod.replaceFragment(getSupportFragmentManager(), R.id.frame_customer, new DeliveryFragment());
                            toolbar.setTitle(getStringFromXML(CustomerActivity.this, R.string.menu_delivery));
                        }
                        break;

                    case R.id.nav_channel:
                        HelperMethod.openUrl(CustomerActivity.this, "https://www.youtube.com/channel/UCxF49QlQkgv31dO7xZPv5vw?fbclid=IwAR0ek864XNNLh6K3IIX1jnkZQ-yW3XTREezVnuxk4PVGdEjyWpmTnTk9D7g");
                        break;

                    case R.id.nav_contact_us:
                        if (navigationView.getCheckedItem().getItemId() == item.getItemId()) {

                        } else {
                            tinyDB.putString(FROM_DISTINATION, "from customer");
                            toolbar.setTitle(getStringFromXML(CustomerActivity.this, R.string.menu_contact_us));
                            HelperMethod.replaceFragment(getSupportFragmentManager(), R.id.frame_customer, new ContactUsFragment());
                        }
                        break;
                }
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(CustomerActivity.this , MusicService.class));
    }

    @Override
    public void onBackPressed() {
        if (toolbar.getTitle() == getStringFromXML(CustomerActivity.this, R.string.menu_home)) {
            finish();
        } else {
            super.onBackPressed();
            first = "first";
            toolbar.setTitle(getStringFromXML(CustomerActivity.this, R.string.menu_home));
            navigationView.setCheckedItem(R.id.nav_home);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.customer, menu);
        return true;
    }

    public static void setToolbarTitle(String title)
    {
        toolbar.setTitle(title);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawer = findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.END)) {
//                    drawer.openDrawer(drawer);
                    drawer.useCustomBehavior(GravityCompat.START);
                } else {

                }
                break;
            case R.id.action_search:
                Intent intent2 = new Intent(CustomerActivity.this , AnotherActivity.class);
                startActivity(intent2);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
               break;
            case R.id.action_settings:
                showCustomDialog2(CustomerActivity.this);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
