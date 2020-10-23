package showman0.amoor.showman.helper.Notifications;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseIdService extends FirebaseInstanceIdService
{
    @Override
    public void onTokenRefresh()
    {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String reference_token = FirebaseInstanceId.getInstance().getToken();

        if (currentUser!=null)
        {
            updateToken(reference_token);
        }
    }

    private void updateToken(String reference_token)
    {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token = new Token(reference_token);
        reference.child(currentUser.getUid()).setValue(token);
    }
}
