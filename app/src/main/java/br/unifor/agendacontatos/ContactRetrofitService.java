package br.unifor.agendacontatos;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * ContactRealmService interface for connecting with our backend.
 *
 * Created by adam.mcneilly on 8/22/16.
 */
public interface ContactRetrofitService {

    @GET("contatos.json")
    Call<ArrayList<Contact>> getContactsRetrofit();

    @GET("contatos/{id}.json")
    Call<Contact> getContactByIdRetrofit(@Path("id") String id);

    @POST("contatos.json")
    Call<Contact> createContactRetrofit(@Body Object Contact);

    @PUT("contatos/{id}.json")
    Call<Contact> updateContactRetrofit(@Path("id") String id, @Body Object contact);

    @DELETE("contatos/{id}.json")
    Call<Contact> deleteContactRetrofit(@Path("id") String id);

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://172.18.9.240:3008/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();


}

