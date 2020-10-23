package showman0.amoor.showman.ui.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.amoor.showman.R;
import showman0.amoor.showman.helper.Notifications.Token;
import showman0.amoor.showman.ui.fragment.homeCycle.CartFragment;
import showman0.amoor.showman.ui.fragment.homeCycle.CompanyFragment;
import showman0.amoor.showman.ui.fragment.homeCycle.UsersFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import showman0.amoor.showman.helper.HelperMethod;

public class HomeCycleActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.Content_Home_Cycle_Fl_Container)
    FrameLayout ContentHomeCycleFlContainer;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;

    private SharedPreferences preferences;
    private String user_privilege,user_name,user_place;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private TextView NameTv,UserPrivilege,UserPlace;
    private Dialog dialog;
    private FirebaseUser fuser;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_cycle);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        firebaseAuth = FirebaseAuth.getInstance();
        fuser = firebaseAuth.getCurrentUser();
        progressDialog = new ProgressDialog(this);


        preferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
        user_name = preferences.getString("user_name", "");
        user_place = preferences.getString("user_place", "");
        user_privilege = preferences.getString("user_privilege", "");
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        updateToken(FirebaseInstanceId.getInstance().getToken());



        if (!user_privilege.isEmpty()) {
            switch (user_privilege) {
                case "مندوب مبيعات":
                    navigationView.getMenu().findItem(R.id.nav_new_user).setVisible(false);
                    break;
                case "ادمن":
                    break;
                case "مدير المخزن":
                    navigationView.getMenu().findItem(R.id.nav_new_user).setVisible(false);
                    break;
            }
        }


        CompanyFragment companyFragment = new CompanyFragment();
        HelperMethod.replace(companyFragment, getSupportFragmentManager(), R.id.Content_Home_Cycle_Fl_Container);

        View headerView = navigationView.getHeaderView(0);
        NameTv = headerView.findViewById(R.id.Nav_Header_Home_Cycle_Tv_user_name);
        UserPrivilege = headerView.findViewById(R.id.Nav_Header_Home_Cycle_Tv_user_privilege);
        UserPlace = headerView.findViewById(R.id.Nav_Header_Home_Cycle_Tv_user_place);
        if (user_privilege.equals("ادمن"))
        {
            UserPlace.setVisibility(View.GONE);
        }else
        {
            UserPlace.setText(user_place);
        }
        UserPrivilege.setText(user_privilege);
        NameTv.setText(user_name);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else
            {
            dialog = new Dialog(this);
            dialog.setContentView(R.layout.dialog_close_app);
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(true);
            dialog.show();

            Button yesBtn = dialog.findViewById(R.id.Dialog_Close_App_Btn_yes);
            Button noBtn = dialog.findViewById(R.id.Dialog_Close_App_Btn_no);

            yesBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    finish();
                }
            });
            noBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_products:
                CompanyFragment companyFragment = new CompanyFragment();
                HelperMethod.replace(companyFragment, getSupportFragmentManager(), R.id.Content_Home_Cycle_Fl_Container);
                break;
            case R.id.nav_sales:
                CartFragment cartFragment = new CartFragment();
                HelperMethod.replace(cartFragment, getSupportFragmentManager(), R.id.Content_Home_Cycle_Fl_Container);
                break;
            case R.id.nav_new_user:
                UsersFragment usersFragment = new UsersFragment();
                HelperMethod.replace(usersFragment, getSupportFragmentManager(), R.id.Content_Home_Cycle_Fl_Container);
                break;
//            case R.id.nav_done_sales:
//                Toast.makeText(this, "soon", Toast.LENGTH_SHORT).show();
//                break;
            case R.id.nav_exit:
                firebaseAuth.signOut();
                startActivity(new Intent(getApplicationContext(), UserCycleActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @OnClick(R.id.Home_Cycle_Activity_Ib_Cart)
    public void onViewClicked()
    {
        CartFragment cartFragment = new CartFragment();
        HelperMethod.replace(cartFragment, getSupportFragmentManager(), R.id.Content_Home_Cycle_Fl_Container);
        navigationView.getMenu().getItem(1).setChecked(true);

    }

    private void updateToken(String token)
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1 = new Token(token);
        reference.child(fuser.getUid()).setValue(token1);
    }
}
