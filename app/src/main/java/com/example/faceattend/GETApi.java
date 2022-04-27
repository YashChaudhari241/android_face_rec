package com.example.faceattend;

import org.json.JSONObject;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GETApi {
    @Multipart
    @POST("checkface")
    Call<ResponseBody> checkface(
            @Query("name") String name,
            @Query("uniqueId") String uId,
            @Part MultipartBody.Part file
            //@Part ("description") RequestBody description
    );
    @Multipart
    @POST("saveface")
        //Call<RequestBody> uploadImage();
    Call<ResponseBody> saveFace(@Query("name") String name,
                                @Query("uniqueId") String uId,
                                @Part MultipartBody.Part file);
//    @GET("hello")
//    Call<DataModel> getHello();
    @Multipart
    @POST("inituser")
        //on below line we are creating a method to post our data. changed priv to FOrmdata
    //Call<InitUserModel> verifyToken(@Header("Authorization") String token,@Body JsonObj priv);
    Call<InitUserModel> verifyToken(@Header("Authorization") String token,@Part("priv") int priv);

    @Multipart
    @POST("calibrate")
    Call<InitUserModel> calibrateFace(@Header("Authorization") String token,@Part("pic1") MultipartBody.Part file);

    @Multipart
    @POST("initorg")
    Call<InitOrgModel> initOrg(@Header("Authorization") String token,
                               @Part("orgName") String orgName,
                               @Part("markExit") boolean markExit,
                               @Part("allowMissedExit") boolean allowMissedExit,
                               @Part("defMissedInterval") int defMissedInterval,
                               @Part("joinPass") String joinPass,
                               @Part("defStart") String defStart,
                               @Part("defEnd") String defEnd,
                               @Part("locEnabled") boolean locEnabled,
                               @Part("locations") String locations[],
                               @Part("locationsRadius") String locationsRadius[]);

    @GET("join/{org_str}")
    Call<GetOrgModel> getOrgDetails(@Path("org_str") String uniqueStr,@Query("p") String pass);

    @POST("join/{org_str}")
    Call<InitUserModel> joinOrg(@Header("Authorization") String token,@Path("org_str") String uniqueStr,
                               @Query("p") String pass
                               );

    @Multipart
    @POST("markattendance")
    Call<MarkAttendModel> joinOrg(@Header("Authorization") String token,
                               @Part("pic") MultipartBody.Part file,
                               @Part("locx") String locx,
                               @Part("locy") String locy,
                               @Part("entryExit") boolean entryExit);

    @POST("userdetails")
    Call<UserDetailsModel> userDetails(@Header("Authorization") String token);

    @Multipart
    @POST("newleave")
    Call<InitUserModel> newLeave(@Header("Authorization") String token,
                                 @Part("startDate") String startDate,   //"yyyy-mm-dd"
                                 @Part("endDate") String endDate,       //"yyyy-mm-dd"
                                 @Part("message") String message);

    @Multipart
    @POST("approveLeave")
    Call<InitUserModel> approveLeave(@Header("Authorization") String token,
                                    @Part("pubID") String pubID);

    @POST("getLeaves")
    Call<GetLeavesModel> getLeavesAsOwner(@Header("Authorization") String token);

    @POST("myleaves")
    Call<GetLeavesModel> getMyLeaves(@Header("Authorization") String token);

}
