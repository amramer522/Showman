package showman0.amoor.showman.ui.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.amoor.showman.R;
import showman0.amoor.showman.adapter.SwipeRecyclerViewAdapter;
import showman0.amoor.showman.data.model.Product;
import com.daimajia.swipe.util.Attributes;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProductsActivity extends AppCompatActivity {

    @BindView(R.id.Product_Activity_Sv_search)
    SearchView ProductActivitySvSearch;
    @BindView(R.id.Product_Activity_Rv_recycler)
    RecyclerView ProductActivityRvRecycler;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.Product_Activity_Tv_company_and_type)
    TextView ProductActivityTvCompanyAndType;
    private String user_privilege;
    private DatabaseReference reference;
    private ProgressDialog progressDialog;
    private SwipeRecyclerViewAdapter adapter;
    private ArrayList<Product> productsList;
    private String type;
    private String company;

    private static final int TOTAL_ITEMS_TO_LOAD = 10;
    private int mCurrentPage = 1;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        ButterKnife.bind(this);
        productsList = new ArrayList<>();
        SharedPreferences preferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
        type = preferences.getString("type", "");
        company = preferences.getString("company", "");
        user_privilege = preferences.getString("user_privilege", "");
        reference = FirebaseDatabase.getInstance().getReference();
        progressDialog = new ProgressDialog(this);
        ProductActivityTvCompanyAndType.setText(company+" : "+type);


//        viewProducts();
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        ProductActivityRvRecycler.setLayoutManager(layoutManager);
        adapter = new SwipeRecyclerViewAdapter(this);
        adapter.setMode(Attributes.Mode.Single);
        ProductActivityRvRecycler.setAdapter(adapter);


        ProductActivityRvRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
//                Toast.makeText(getApplicationContext(), "RecyclerView onScrollStateChanged", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (!user_privilege.equals("مندوب مبيعات")) {
                    if (dy > 0) {
                        fab.hide();
                    } else {
                        fab.show();
                    }
                }
//                Toast.makeText(ProductsActivity.this, ""+productsList.size(), Toast.LENGTH_SHORT).show();
////                mCurrentPage++;
//////                    productsList.clear();
////                viewProducts();
                super.onScrolled(recyclerView, dx, dy);
            }
        });

    }


    private void searchInProducts() {
//        ProductActivitySvSearch.setQueryHint("اكتب هنا للبحث");
        ProductActivitySvSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                ProductActivitySvSearch.setIconified(false);
                search(query);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!TextUtils.isEmpty(newText.trim())) {
                    search(newText);
                } else {
                    viewProducts();
                }
                return false;
            }
        });

    }

    private void search(String newText) {
        final List<Product> searchedProductsList = new ArrayList<>();
        for (Product p : productsList) {
            if (String.valueOf(p.getProductCode()).startsWith(newText) || p.getProductName().startsWith(newText) || p.getProductName().contains(newText) || p.getProductSize().contains(newText)) {
                // ابقى اعمل السيرش ب الفرز كمان بس مش دلوقتى
                searchedProductsList.add(p);
            }
        }
        adapter.setData(searchedProductsList);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onStart() {
        super.onStart();
        viewProducts();
        searchInProducts();
    }


    private void viewProducts() {

        progressDialog.setMessage("انتظر قليلا يتم تحميل المنتجات ... ");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        DatabaseReference productsRef = reference.child("products").child(type).child(company);
//        Query mProducts = productsRef.limitToFirst(mCurrentPage * TOTAL_ITEMS_TO_LOAD);

        if (productsList.size() == 0 && !user_privilege.equals("مندوب مبيعات"))
        {
            fab.show();
        }
        productsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productsList.clear();
                for (DataSnapshot product : dataSnapshot.getChildren()) {
                    Product p = product.getValue(Product.class);
                    productsList.add(p);
                }
                progressDialog.dismiss();
                adapter.setData(productsList);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });


    }

    @OnClick({R.id.Products_Activity_Ib_back, R.id.fab, R.id.Products_Activity_Ib_Home, R.id.Products_Activity_Ib_Qr_Search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.Products_Activity_Ib_back:
                startActivity(new Intent(getApplicationContext(), TypesActivity.class));
                finish();
                break;
            case R.id.fab:
                startActivity(new Intent(getApplicationContext(), AddProductActivity.class));
                finish();
                break;
            case R.id.Products_Activity_Ib_Home:
                startActivity(new Intent(getApplicationContext(), HomeCycleActivity.class));
                finish();
                break;
            case R.id.Products_Activity_Ib_Qr_Search:
                qrSearch();
                break;
        }
    }

    private void qrSearch() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        //integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scan");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(false);
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "You Cancelled the scanning", Toast.LENGTH_SHORT).show();
            } else {
                String contents = result.getContents();
                ProductActivitySvSearch.setQuery(contents, true);
//                    search(contents);

            }
        } else {
            Toast.makeText(this, "result null", Toast.LENGTH_SHORT).show();
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onBackPressed() {
        if (!ProductActivitySvSearch.isIconified()) {
            ProductActivitySvSearch.setIconified(true);
        } else {
            startActivity(new Intent(getApplicationContext(), TypesActivity.class));
            finish();
        }

    }
}
