package showman0.amoor.showman.ui.fragment.homeCycle;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.amoor.showman.R;
import showman0.amoor.showman.adapter.CompanyAdapter;
import showman0.amoor.showman.data.model.Company;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

public class CompanyFragment extends Fragment {

    @BindView(R.id.Company_Fragment_Rv_recycler)
    RecyclerView CompanyFragmentRvRecycler;
    Unbinder unbinder;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    private DatabaseReference reference;
    private CompanyAdapter adapter;
    private ArrayList<Company> companyList;
    private ProgressDialog progressDialog;
    private Dialog dialog;

    public CompanyFragment() {
    }


    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_company, container, false);
        unbinder = ButterKnife.bind(this, root);
        reference = FirebaseDatabase.getInstance().getReference();
        progressDialog = new ProgressDialog(getContext());
        if (getContext() != null) {
            SharedPreferences preferences = getContext().getSharedPreferences("UserData", Context.MODE_PRIVATE);
            String user_privilege = preferences.getString("user_privilege", "");

            if (user_privilege != null && !user_privilege.equals("مندوب مبيعات")) {
                fab.setVisibility(View.VISIBLE);
            }
        }

        companyList = new ArrayList<>();


//        viewCompanies();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        CompanyFragmentRvRecycler.setLayoutManager(layoutManager);
        adapter = new CompanyAdapter(getContext());
        CompanyFragmentRvRecycler.setAdapter(adapter);


        return root;
    }

    private void viewCompanies() {
        progressDialog.setMessage("انتظر قليلا يتم تحميل الشركات ... ");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        reference.child("Companies").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                companyList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Company company = snapshot.getValue(Company.class);
                    companyList.add(company);
                }
                adapter.setData(companyList);
                adapter.notifyDataSetChanged();
                progressDialog.dismiss();

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

    @OnClick(R.id.fab)
    public void onViewClicked() {
        if (getContext() != null) {
            dialog = new Dialog(getContext());
        }
        dialog.setContentView(R.layout.dialog_new_company);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

        Button doneBtn = dialog.findViewById(R.id.Dialog_New_Company_Btn_done);
        final TextInputLayout company_name = dialog.findViewById(R.id.Dialog_New_Company_Til_Company_name);
        final TextInputLayout company_stands = dialog.findViewById(R.id.Dialog_New_Company_Til_Company_stands);

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(HelperMethod.getTextFromTil(company_name)) && !TextUtils.isEmpty(HelperMethod.getTextFromTil(company_stands)))
                {
                    ArrayList<String> companies_names = new ArrayList<>();
                    for (int i = 0; i <companyList.size() ; i++)
                    {
                        companies_names.add(companyList.get(i).getCompany_name());
                    }
                    if (!companies_names.contains(HelperMethod.getTextFromTil(company_name)))
                    {
                        reference.child("Companies").push().setValue(new Company(HelperMethod.getTextFromTil(company_name), HelperMethod.getTextFromTil(company_stands))).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                dialog.dismiss();
                                Toast.makeText(getContext(), "تم اضافة الشركة", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                dialog.dismiss();
                                Toast.makeText(getContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Toast.makeText(getContext(), "اسم الشركة دى موجود قبل كده", Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Toast.makeText(getContext(), "يجب ادخال اسم الشركة وكود الشركة", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


    @Override
    public void onStart() {
        super.onStart();
        viewCompanies();
    }
}
