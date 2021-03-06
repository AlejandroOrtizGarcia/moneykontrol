package com.example.wittig.mymoney;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

public class Home extends AppCompatActivity {

    public static BackendlessUser user;

    private TabLayout tabLayout;
    private ViewPager viewPager;

    private Gestion gestion;
    private GraficaTest graficaTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Iniciamos APP //
        String appVersion = "v1";
        Backendless.initApp(getBaseContext(), "020DB3E3-BAB6-6786-FFD6-2BC187171600", "DA9A0E08-B631-AC2C-FF80-1CFB69047300", appVersion);

        // Hacemos el login //
        Backendless.UserService.login("a@a.a", "a", new AsyncCallback<BackendlessUser>() {
            @Override
            public void handleResponse(BackendlessUser response) {
                user = response;

                gestion = new Gestion();
                graficaTest = new GraficaTest();

                tabLayout = (TabLayout) findViewById(R.id.tabs);
                viewPager = (ViewPager)findViewById(R.id.viewpager);
                viewPager.setAdapter(new CustomAdapter(getSupportFragmentManager(), getApplicationContext()));
                tabLayout.setupWithViewPager(viewPager);
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Log.d("FAULT: ", fault.getMessage());
            }
        });


        // Gestionamos las Pestanias //



    }

    // Clase adaptador para las pestanias //
    private class CustomAdapter extends FragmentPagerAdapter {

        private String names [] = {"Gestion", "GraficaTest", "", "", ""};

        public CustomAdapter(FragmentManager supportFragmentManager, Context applicationContext) {
            super(supportFragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return gestion;

                case 1:
                    return graficaTest ;

                default:
                    return null;
            }
        }
        @Override
        public int getCount() {
            return 2;
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return names[position];
        }
    }
}
