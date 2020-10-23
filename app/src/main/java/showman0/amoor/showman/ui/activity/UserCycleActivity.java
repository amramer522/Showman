package showman0.amoor.showman.ui.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Toast;

import com.amoor.showman.R;
import showman0.amoor.showman.data.model.User;
import showman0.amoor.showman.helper.HelperMethod;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserCycleActivity extends AppCompatActivity {

    @BindView(R.id.Login_Activity_Til_User_Name)
    TextInputLayout LoginActivityTilUserName;
    @BindView(R.id.Login_Activity_Til_Password)
    TextInputLayout LoginActivityTilPassword;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference reference;
    private SharedPreferences preferences;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_cycle);
        ButterKnife.bind(this);
        firebaseAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference();
        preferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
        progressDialog = new ProgressDialog(this);


    }

    @OnClick(R.id.Login_Activity_Btn_Login)
    public void onViewClicked()
    {

        final String user_name = HelperMethod.getTextFromTil(LoginActivityTilUserName);
        String user_password = HelperMethod.getTextFromTil(LoginActivityTilPassword);

        if (!TextUtils.isEmpty(user_name)&&!TextUtils.isEmpty(user_password))
        {
            progressDialog.setMessage("انتظر قليلا يتم تسجيل الدخول ... ");
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(false);

            firebaseAuth.signInWithEmailAndPassword(user_name + "@showman.com", user_password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    reference.child("Users").child(authResult.getUser().getUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                        {
                            progressDialog.dismiss();
                            HelperMethod.setTilEmpty(LoginActivityTilUserName,LoginActivityTilPassword);
                            User userDetails = dataSnapshot.getValue(User.class);
                            if (userDetails!=null) {
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("user_name", userDetails.getName());
                                editor.putString("user_privilege", userDetails.getPrivilege());
                                if (userDetails.getPrivilege().equals("مندوب مبيعات")) {
                                    editor.putString("user_place", userDetails.getUser_place());

                                } else {
                                    editor.putString("user_place", "ادمن");
                                }
                                editor.apply();
                            }
                            Intent intent = new Intent(getApplicationContext(), HomeCycleActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            progressDialog.dismiss();
                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(UserCycleActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }
        else
        {
            Toast.makeText(this, "يجب كتابة اليوزر والباسورد", Toast.LENGTH_SHORT).show();
        }

    }
}
