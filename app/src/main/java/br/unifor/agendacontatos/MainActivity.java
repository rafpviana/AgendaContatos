package br.unifor.agendacontatos;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements ContactScheduleAdapter.OnDataSelectedInterface {

    private RecyclerView recyclerViewContactSchedule;
    private RecyclerView.Adapter adapterContactSchedule;
    private RecyclerView.LayoutManager layoutManagerContactSchedule;
    private ArrayList<Contact> contactSchedule;
    private Button buttonAddContact;
    static final int ACTIVITY_REQUEST_CODE_NEW_CONTACT = 1;
    static final int ACTIVITY_REQUEST_CODE_EDIT_CONTACT = 2;
    private ContactRealmService contactRealmService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            prepareToGetContactsRetrofit();
        } catch (IOException e) {
            e.printStackTrace();
        }

        buttonAddContact = (Button) findViewById(R.id.buttonAddContactId);
        buttonAddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentGoToContactDetailActivity = new Intent(MainActivity.this, ContactDetailActivity.class);
                intentGoToContactDetailActivity.putExtra("requestCode", ACTIVITY_REQUEST_CODE_NEW_CONTACT);
                startActivity(intentGoToContactDetailActivity);
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        contactRealmService.closeRealm();
    }


    @Override
    public void onDataSelectedMethod(View view, int position) {
        Toast.makeText(this, "Contact Name: " + contactSchedule.get(position).getName()+" Contact Id: "+contactSchedule.get(position).getId(), Toast.LENGTH_SHORT).show();
        Intent intentGoToContactDetailActivity = new Intent(MainActivity.this, ContactDetailActivity.class);
        intentGoToContactDetailActivity.putExtra("idContact", contactSchedule.get(position).getId());
        intentGoToContactDetailActivity.putExtra("requestCode", ACTIVITY_REQUEST_CODE_EDIT_CONTACT);
        startActivity(intentGoToContactDetailActivity);
    }


     @Override
    protected void onRestart() {
        super.onRestart();

         try {
             prepareToGetContactsRetrofit();
         } catch (IOException e) {
             e.printStackTrace();
         }

//         ArrayList<Contact> temporaryContactSchedule = contactSchedule;
//         ContactScheduleAdapter temporaryContactScheduleAdapter = (ContactScheduleAdapter) adapterContactSchedule;
//         temporaryContactScheduleAdapter.setList(temporaryContactSchedule);
//         adapterContactSchedule.notifyDataSetChanged();
//
//         contactRealmService.deleteAllContacts();
//         contactRealmService.createAllContactsFromList(contactSchedule);
    }


    public void prepareToGetContactsRetrofit() throws IOException {

        ContactRetrofitService contactRetrofitService = ContactRetrofitService.retrofit.create(ContactRetrofitService.class);

        Call<ArrayList<Contact>> callContacts = contactRetrofitService.getContactsRetrofit();

        callContacts.enqueue(new Callback<ArrayList<Contact>>() {
            @Override
            public void onResponse(Call<ArrayList<Contact>> call, Response<ArrayList<Contact>> response) {
                String jsonContacts = new Gson().toJson(response.body());
                Log.i("onResponse Retrofit", " ");
                Log.i("Contacts List", jsonContacts);
                setContactsByRetrofit(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<Contact>> call, Throwable t) {
                Log.i("onFailure Retrofit", t.getMessage());
            }
        });
    }

    public void setContactsByRetrofit(ArrayList<Contact> contactScheduleFromRetrofit){
        this.contactSchedule = contactScheduleFromRetrofit;

//        Log.i("Retrofit Contact", this.contactSchedule.get(0).getName());

        contactRealmService = new ContactRealmService(this);
        contactRealmService.deleteAllContacts();
        contactRealmService.createAllContactsFromList(contactSchedule);

//        Log.i("Realm Contact: ", contactRealmService.getContactSchedule().get(0).getName());

        recyclerViewContactSchedule = (RecyclerView) findViewById(R.id.recyclerViewContactScheduleId);
//        recyclerViewContactSchedule.setHasFixedSize(true);
        layoutManagerContactSchedule = new LinearLayoutManager(this);
        recyclerViewContactSchedule.setLayoutManager(layoutManagerContactSchedule);
        adapterContactSchedule = new ContactScheduleAdapter(contactSchedule, this, this);
        recyclerViewContactSchedule.setAdapter(adapterContactSchedule);

    }

}



