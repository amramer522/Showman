package showman0.amoor.showman.helper.Notifications;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiService
{
    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAARV8_R9w:APA91bGF6rVmNB0E7dn8wDJBvcCoPa-l3j4JyBEpxdLRf6l1P7OhSqnGhWiAKXnJmSHAGSFlslDvIqwt2qyU2vNdQVkLaTgwiud7f4iMuVAjyVlJyX9HvNj-_jVSPOYG5ULXwCdmm_EJ"
    })

 @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);

}
