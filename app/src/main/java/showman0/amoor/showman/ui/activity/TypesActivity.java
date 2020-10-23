package showman0.amoor.showman.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.amoor.showman.R;
import showman0.amoor.showman.adapter.TypesAdapter;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TypesActivity extends AppCompatActivity {

    @BindView(R.id.Types_Activity_Rv_recycler)
    RecyclerView TypesActivityRvRecycler;
    @BindView(R.id.Types_Activity_Tb_toolbar)
    Toolbar TypesActivityTbToolbar;
    private DatabaseReference reference;
    private ProgressDialog progressDialog;
    private TypesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_types);
        ButterKnife.bind(this);
        reference = FirebaseDatabase.getInstance().getReference();
        progressDialog = new ProgressDialog(this);


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        TypesActivityRvRecycler.setLayoutManager(layoutManager);
        adapter = new TypesAdapter(this);
        TypesActivityRvRecycler.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), HomeCycleActivity.class));
        finish();
    }


    @OnClick(R.id.Types_Activity_Ib_back)
    public void onViewClicked()
    {
        startActivity(new Intent(getApplicationContext(), HomeCycleActivity.class));
        finish();
    }
}
