package showman0.amoor.showman.ui.fragment.homeCycle;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.amoor.showman.R;
import showman0.amoor.showman.data.model.User;
import showman0.amoor.showman.helper.HelperMethod;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class UsersFragment extends Fragment {
    @BindView(R.id.Register_Activity_S_Access_Permissions)
    Spinner RegisterActivitySAccessPermissions;
    @BindView(R.id.Register_Activity_Til_User_Id)
    TextInputLayout RegisterActivityTilUserId;
    @BindView(R.id.Register_Activity_Til_Name)
    TextInputLayout RegisterActivityTilName;
    @BindView(R.id.Register_Activity_Til_Password)
    TextInputLayout RegisterActivityTilPassword;
    @BindView(R.id.Register_Activity_Til_Confirm_Password)
    TextInputLayout RegisterActivityTilConfirmPassword;
    Unbinder unbinder;
    @BindView(R.id.Register_Fragment_S_user_place)
    Spinner RegisterFragmentSUserPlace;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference reference;
    private ProgressDialog progressDialog;
    private User user;

    public UsersFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_users, container, false);
        unbinder = ButterKnife.bind(this, root);
        firebaseAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference();
        progressDialog = new ProgressDialog(getContext());

        RegisterActivitySAccessPermissions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                if (HelperMethod.getTextFromSpinner(RegisterActivitySAccessPermissions).equals("ادمن"))
                {
                    RegisterFragmentSUserPlace.setVisibility(View.INVISIBLE);

                }else
                {
                    RegisterFragmentSUserPlace.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.Register_Activity_Btn_Register)
    public void onViewClicked() {
        final String privilege = HelperMethod.getTextFromSpinner(RegisterActivitySAccessPermissions);
        final String user_place = HelperMethod.getTextFromSpinner(RegisterFragmentSUserPlace);
        final String user_id = HelperMethod.getTextFromTil(RegisterActivityTilUserId);
        final String user_name = HelperMethod.getTextFromTil(RegisterActivityTilName);
        final String user_password = HelperMethod.getTextFromTil(RegisterActivityTilPassword);
        String user_confirm_password = HelperMethod.getTextFromTil(RegisterActivityTilConfirmPassword);

        if (!TextUtils.isEmpty(user_id) && !TextUtils.isEmpty(user_name)
                && !TextUtils.isEmpty(user_password)
                && !TextUtils.isEmpty(user_place)
                && TextUtils.equals(user_password, user_confirm_password)
        ) {
            progressDialog.setMessage("انتظر قليلا يتم تسجيل الحساب ... ");
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(false);

            firebaseAuth.createUserWithEmailAndPassword(user_name + "@showman.com", user_password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {

                    if (HelperMethod.getTextFromSpinner(RegisterActivitySAccessPermissions).equals("ادمن"))
                    {
                        user = new User(user_name, user_id, privilege);

                    }else
                    {
                        user = new User(user_name, user_id, privilege, user_place);
                    }
                    reference.child("Users").child(authResult.getUser().getUid()).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "تم انشاء الحساب بنجاح", Toast.LENGTH_SHORT).show();
                            HelperMethod.setTilEmpty(RegisterActivityTilUserId, RegisterActivityTilName, RegisterActivityTilPassword, RegisterActivityTilConfirmPassword);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "real " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "auth " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getContext(), "يجب كتابة كل الخانات", Toast.LENGTH_SHORT).show();
        }
    }



}
