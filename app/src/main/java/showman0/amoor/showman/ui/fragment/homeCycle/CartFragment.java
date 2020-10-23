package showman0.amoor.showman.ui.fragment.homeCycle;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.amoor.showman.R;
import showman0.amoor.showman.adapter.CartAdapter;
import showman0.amoor.showman.data.model.Sales;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import showman0.amoor.showman.helper.HelperMethod;

public class CartFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    @BindView(R.id.Cart_Fragment_Tv_Date)
    TextView CartFragmentTvDate;
    @BindView(R.id.Cart_Fragment_Rv_recycler)
    RecyclerView CartFragmentRvRecycler;
    Unbinder unbinder;
    @BindView(R.id.Cart_Fragment_S_user_place)
    Spinner CartFragmentSUserPlace;
    @BindView(R.id.Cart_Fragment_Tv_Total)
    TextView CartFragmentTvTotal;
    @BindView(R.id.Cart_Fragment_Iv_Select_Date)
    ImageButton CartFragmentIvSelectDate;
    private CartAdapter adapter;
    private DatabaseReference reference;
    private ProgressDialog progressDialog;
    private SharedPreferences preferences;
    private String user_name;
    private String user_place;
    private ArrayList<Sales> salesList;
    private String user_privilege;


    public CartFragment()
    {

    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_cart, container, false);
        unbinder = ButterKnife.bind(this, root);
        CartFragmentSUserPlace.setOnItemSelectedListener(this);
        progressDialog = new ProgressDialog(getContext());
        reference = FirebaseDatabase.getInstance().getReference();
        salesList = new ArrayList<>();


        if (getContext() != null) {
            preferences = getContext().getSharedPreferences("UserData", Context.MODE_PRIVATE);
        }
        user_name = preferences.getString("user_name", "");
        user_privilege = preferences.getString("user_privilege", "");
        user_place = preferences.getString("user_place", "");
        if (user_privilege.equals("ادمن")) {
            CartFragmentSUserPlace.setVisibility(View.VISIBLE);
        }

        CartFragmentTvDate.setText(HelperMethod.getCurrentDate());
        CartFragmentRvRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CartAdapter(getContext());
        CartFragmentRvRecycler.setAdapter(adapter);

        return root;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        viewSales(CartFragmentTvDate.getText().toString());
    }

    private void viewSales(String date) {
        progressDialog.setMessage("انتظر قليلا يتم تحميل المبيعات المحجوزه ... ");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);

        reference.child("Sales").child(date).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressDialog.dismiss();
                salesList.clear();
                long total = 0L;
                for (DataSnapshot s : dataSnapshot.getChildren()) {
                    Sales sales = s.getValue(Sales.class);
                    if (sales != null) {
                        if (user_privilege.equals("مندوب مبيعات") && sales.getSalesManName().equals(user_name)) {
                            salesList.add(sales);
                            total += sales.getQuantity();

                        } else if (user_privilege.equals("ادمن") && user_place.equals("ادمن") && sales.getUser_place().equals(user_place)) {
                            salesList.add(sales);
                            total += sales.getQuantity();
                        } else if (user_privilege.equals("ادمن") && user_place.equals("طنطا") && sales.getUser_place().equals(user_place)) {
                            salesList.add(sales);
                            total += sales.getQuantity();
                        } else if (user_privilege.equals("ادمن") && user_place.equals("المحله") && sales.getUser_place().equals(user_place)) {
                            salesList.add(sales);
                            total += sales.getQuantity();
                        }
                    }
                }
                if (CartFragmentTvTotal != null) {
                    CartFragmentTvTotal.setText("الإجمالى : " + total + " متر");
                }

                adapter.setData(salesList);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.Cart_Fragment_Iv_Select_Date)
    public void onViewClicked() {
        HelperMethod.showDateDialog(getActivity(), CartFragmentTvDate);
//        viewSales(CartFragmentTvDate.getText().toString());
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        user_place = HelperMethod.getTextFromSpinner(CartFragmentSUserPlace);
        viewSales(HelperMethod.getCurrentDate());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}
