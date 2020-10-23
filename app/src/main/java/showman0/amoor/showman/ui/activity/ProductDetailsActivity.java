package showman0.amoor.showman.ui.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.amoor.showman.R;
import showman0.amoor.showman.data.model.Product;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProductDetailsActivity extends AppCompatActivity {

    @BindView(R.id.Product_Details_Activity_Tv_Company_Name)
    TextView ProductDetailsActivityTvCompanyName;
    @BindView(R.id.Product_Details_Activity_Tv_Product_Size)
    TextView ProductDetailsActivityTvProductSize;
    @BindView(R.id.Product_Details_Activity_Tv_Product_Code)
    TextView ProductDetailsActivityTvProductCode;
    @BindView(R.id.Product_Details_Activity_Tv_Product_Name)
    TextView ProductDetailsActivityTvProductName;
    @BindView(R.id.Product_Details_Activity_Tv_Size_In_Meters)
    TextView ProductDetailsActivityTvSizeInMeters;
    @BindView(R.id.Product_Details_Activity_Tv_Ton)
    TextView ProductDetailsActivityTvTon;
    @BindView(R.id.Product_Details_Activity_Tv_Product_Quality)
    TextView ProductDetailsActivityTvProductQuality;
    @BindView(R.id.Product_Details_Activity_Tv_Product_Quantity)
    TextView ProductDetailsActivityTvProductQuantity;
    @BindView(R.id.Product_Details_Activity_Tv_Product_Type)
    TextView ProductDetailsActivityTvProductType;
    private DatabaseReference productsReference;
    private ProgressDialog progressDialog;
    private String type;
    private String company;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        ButterKnife.bind(this);
        progressDialog = new ProgressDialog(this);
        SharedPreferences preferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
        type = preferences.getString("type", "");
        company = preferences.getString("company", "");

        productsReference = FirebaseDatabase.getInstance().getReference("products").child(type).child(company);
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("product_code"))
        {
            String product_code = intent.getExtras().getString("product_code", "");
            getDetails(product_code);
        }


    }

    private void getDetails(final String product_code)
    {
        progressDialog.setMessage("انتظر قليلا يتم تحميل تفاصيل المنتج ... ");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);

        productsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> products = dataSnapshot.getChildren();
                for (DataSnapshot product : products) {
                    Product productDetails = product.getValue(Product.class);
                    if (String.valueOf(productDetails.getProductCode()).equals(product_code)) {
                        setDetails(productDetails);
                        break;
                    }

                }

                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });

    }

    private void setDetails(Product productDetails) {
        ProductDetailsActivityTvCompanyName.setText(ProductDetailsActivityTvCompanyName.getText() + "  " + productDetails.getCompanyName());
        ProductDetailsActivityTvProductType.setText(ProductDetailsActivityTvProductType.getText() + "  " + productDetails.getProductType());
        ProductDetailsActivityTvProductSize.setText(ProductDetailsActivityTvProductSize.getText() + "  " + productDetails.getProductSize());
        ProductDetailsActivityTvProductCode.setText(ProductDetailsActivityTvProductCode.getText() + "  " + productDetails.getProductCode());
        ProductDetailsActivityTvProductName.setText(ProductDetailsActivityTvProductName.getText() + "  " + productDetails.getProductName());
        ProductDetailsActivityTvSizeInMeters.setText(ProductDetailsActivityTvSizeInMeters.getText() + "  " + productDetails.getPackageSizeInMeter());
        ProductDetailsActivityTvTon.setText(ProductDetailsActivityTvTon.getText() + "  " + productDetails.getTonC());
        ProductDetailsActivityTvProductQuantity.setText(ProductDetailsActivityTvProductQuantity.getText() + "  " + productDetails.getProductQuantity());

        ProductDetailsActivityTvProductQuality.setText(" فرز "+productDetails.getProductQuality());

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), ProductsActivity.class));
        finish();
    }


    @OnClick(R.id.Product_Details_Activity_Iv_Back)
    public void onViewClicked() {
        startActivity(new Intent(getApplicationContext(), ProductsActivity.class));
        finish();
    }
}
