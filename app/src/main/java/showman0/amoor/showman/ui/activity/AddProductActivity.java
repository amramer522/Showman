package showman0.amoor.showman.ui.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.amoor.showman.R;
import showman0.amoor.showman.data.model.Product;
import showman0.amoor.showman.data.model.User;
import showman0.amoor.showman.helper.Notifications.ApiService;
import showman0.amoor.showman.helper.Notifications.Data;
import showman0.amoor.showman.helper.Notifications.MyResponse;
import showman0.amoor.showman.helper.Notifications.RetrofitClient;
import showman0.amoor.showman.helper.Notifications.Sender;
import showman0.amoor.showman.helper.Notifications.Token;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import showman0.amoor.showman.helper.HelperMethod;

public class AddProductActivity extends AppCompatActivity {

    @BindView(R.id.Add_Product_Fragment_Til_product_code)
    TextInputLayout AddProductFragmentTilProductCode;
    @BindView(R.id.Add_Product_Fragment_Til_product_name)
    TextInputLayout AddProductFragmentTilProductName;
    @BindView(R.id.Add_Product_Fragment_Til_size_in_meters)
    TextInputLayout AddProductFragmentTilSizeInMeters;
    @BindView(R.id.Add_Product_Fragment_Til_tonC)
    TextInputLayout AddProductFragmentTilTonC;
    @BindView(R.id.Add_Product_Fragment_Til_product_quantity)
    TextInputLayout AddProductFragmentTilProductQuantity;

    @BindView(R.id.Add_Product_Fragment_Btn_Add_Product)
    Button AddProductFragmentBtnAddProduct;
    @BindView(R.id.Add_Product_Fragment_Tv_product_type)
    TextView AddProductFragmentTvProductType;
    @BindView(R.id.Add_Product_Fragment_Tv_company_name)
    TextView AddProductFragmentTvCompanyName;
    @BindView(R.id.Add_Product_Fragment_S_product_quality)
    Spinner AddProductFragmentSProductQuality;
    @BindView(R.id.Add_Product_Fragment_Til_product_size)
    TextInputLayout AddProductFragmentTilProductSize;

    // Variables
    private DatabaseReference reference;
    private ProgressDialog progressDialog;
    private String product_type;
    private String product_company;
    private ApiService apiService;
    private FirebaseUser fuser;
    boolean notify = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        ButterKnife.bind(this);

        apiService = RetrofitClient.getClient().create(ApiService.class);

        reference = FirebaseDatabase.getInstance().getReference();
        progressDialog = new ProgressDialog(this);
        SharedPreferences preferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);


        product_company = preferences.getString("company", "");
        product_type = preferences.getString("type", "");
        AddProductFragmentTvProductType.setText("النوع : " + product_type);
        AddProductFragmentTvCompanyName.setText("اسم الشركة : " + product_company);


    }


    private void addProduct() {
        String productQuality = HelperMethod.getTextFromSpinner(AddProductFragmentSProductQuality);
        String productCode = HelperMethod.getTextFromTil(AddProductFragmentTilProductCode);
        String productName = HelperMethod.getTextFromTil(AddProductFragmentTilProductName);
        String packagesizeInMeters = HelperMethod.getTextFromTil(AddProductFragmentTilSizeInMeters);
        String tonC = HelperMethod.getTextFromTil(AddProductFragmentTilTonC);
        String productSize = HelperMethod.getTextFromTil(AddProductFragmentTilProductSize);
        String productQuantity = HelperMethod.getTextFromTil(AddProductFragmentTilProductQuantity);


        boolean validate = HelperMethod.isValidate(this, productCode, productName, packagesizeInMeters, tonC, productQuantity, productSize);
        if (validate) {

            double p_packagesizeInMeters = Double.parseDouble(packagesizeInMeters);
            String p_quality = productQuality.replace("فرز", "");

            long p_quantity = Integer.parseInt(productQuantity);
            Product product = new Product(product_company, productSize, productCode, productName, p_packagesizeInMeters, tonC, p_quantity, p_quality, product_type);
            progressDialog.setMessage("انتظر قليلا يتم اضافة المنتج ... ");
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(false);

            reference.child("products").child(product_type).child(product_company).child(productCode).setValue(product).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    notify = true;
                    progressDialog.dismiss();
                    Toast.makeText(AddProductActivity.this, "تم اضافة المنتج بنجاح", Toast.LENGTH_SHORT).show();
                    HelperMethod.setTilEmpty(
                            AddProductFragmentTilProductCode,
                            AddProductFragmentTilProductName,
                            AddProductFragmentTilSizeInMeters,
                            AddProductFragmentTilTonC,
                            AddProductFragmentTilProductQuantity,
                            AddProductFragmentTilProductSize
                    );

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(AddProductActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });


        }


        fuser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").child(fuser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (notify) {
                    assert user != null;
                    sendNotification(fuser.getUid(), user.getName());
                }
                notify = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void sendNotification(String receiver, final String username) {
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = tokens.orderByKey().equalTo(receiver);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Token token = snapshot.getValue(Token.class);
                    // اليوزر المكتوب ده بتاع اسلام ثابت دلوقتى ده اللى المفروض ابعتله
                    final String msg = "اشطا عليك ياباشاا";
                    Data data = new Data(fuser.getUid(), username + " : " + msg, "New Message", "uIVaBDzZ2Pbi7ss9FsNMbSVRGYZ2", R.mipmap.ic_launcher);
                    Sender sender = new Sender(data, token.getToken());

                    apiService.sendNotification(sender).enqueue(new Callback<MyResponse>() {
                        @Override
                        public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                            if (response.code() == 200) {
                                if (response.body().success != 1) {
                                    Toast.makeText(AddProductActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                                }
                            }

                        }

                        @Override
                        public void onFailure(Call<MyResponse> call, Throwable t) {

                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    @OnClick({R.id.Add_Product_Fragment_Iv_Back, R.id.Add_Product_Fragment_Btn_Add_Product})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.Add_Product_Fragment_Iv_Back:
                startActivity(new Intent(getApplicationContext(), ProductsActivity.class));
                finish();
                break;
            case R.id.Add_Product_Fragment_Btn_Add_Product:
                addProduct();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), ProductsActivity.class));
        finish();
    }
}
