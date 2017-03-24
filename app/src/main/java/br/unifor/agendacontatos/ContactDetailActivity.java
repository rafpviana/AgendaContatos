package br.unifor.agendacontatos;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactDetailActivity extends AppCompatActivity {

    static final int ACTIVITY_REQUEST_CODE_NEW_CONTACT = 1;
    static final int ACTIVITY_REQUEST_CODE_EDIT_CONTACT = 2;
    private EditText editTextName;
    private EditText editTextPhone;
    private EditText editTextEmail;
    private EditText editTextObservation;
    private Button buttonEditContact;
    private Button buttonSaveContact;
    private Button buttonDeleteContact;
    private String idContact;
    private Contact contactSelected;
    private int requestCodeIntent;
    private ContactRealmService contactRealmService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);

        editTextName = (EditText) findViewById(R.id.editTextNameId);
        editTextPhone = (EditText) findViewById(R.id.editTextPhoneId);
        editTextEmail = (EditText) findViewById(R.id.editTextEmailId);
        editTextObservation = (EditText) findViewById(R.id.editTextObservationId);
        buttonEditContact = (Button) findViewById(R.id.buttonEditContactId);
        buttonSaveContact = (Button) findViewById(R.id.buttonSaveContactId);
        buttonDeleteContact = (Button) findViewById(R.id.buttonDeleteContactId);
        idContact = getIntent().getStringExtra("idContact");
        requestCodeIntent = getIntent().getIntExtra("requestCode", 0);

        contactRealmService = new ContactRealmService(this);

        if(requestCodeIntent == ACTIVITY_REQUEST_CODE_NEW_CONTACT){
            editTextName.setText("");
            editTextPhone.setText("");
            editTextEmail.setText("");
            editTextObservation.setText("");
            buttonEditContact.setEnabled(false);
            buttonDeleteContact.setEnabled(false);
        }
        else if(requestCodeIntent == ACTIVITY_REQUEST_CODE_EDIT_CONTACT){
            contactSelected = contactRealmService.queryContactById(idContact);
            editTextName.setText(contactSelected.getName());
            editTextPhone.setText(contactSelected.getPhone());
            editTextEmail.setText(contactSelected.getEmail());
            editTextObservation.setText(contactSelected.getObservation());
            editTextName.setFocusable(false);
            editTextPhone.setFocusable(false);
            editTextEmail.setFocusable(false);
            editTextObservation.setFocusable(false);
            buttonSaveContact.setEnabled(false);
        }
        buttonEditContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextName.setFocusableInTouchMode(true);
                editTextName.setFocusable(true);
                editTextPhone.setFocusableInTouchMode(true);
                editTextPhone.setFocusable(true);
                editTextEmail.setFocusableInTouchMode(true);
                editTextEmail.setFocusable(true);
                editTextObservation.setFocusableInTouchMode(true);
                editTextObservation.setFocusable(true);
                buttonEditContact.setEnabled(false);
                buttonDeleteContact.setEnabled(false);
                buttonSaveContact.setEnabled(true);
            }
        });
        buttonSaveContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editTextName.length() != 0 && editTextPhone.length() != 0){
                    if(requestCodeIntent == ACTIVITY_REQUEST_CODE_NEW_CONTACT){
                        createContact(new Contact(editTextName.getText().toString(), editTextPhone.getText().toString(), editTextEmail.getText().toString(), editTextObservation.getText().toString()));
                    }
                    else if(requestCodeIntent == ACTIVITY_REQUEST_CODE_EDIT_CONTACT){
                        updateContact(idContact, new Contact(editTextName.getText().toString(), editTextPhone.getText().toString(), editTextEmail.getText().toString(), editTextObservation.getText().toString()));
                    }
                    editTextName.setFocusable(false);
                    editTextPhone.setFocusable(false);
                    buttonEditContact.setEnabled(true);
                    buttonDeleteContact.setEnabled(true);
                    buttonSaveContact.setEnabled(false);
                    Toast.makeText(ContactDetailActivity.this, "Contato salvo com sucesso", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(ContactDetailActivity.this, "Dados inválidos", Toast.LENGTH_SHORT).show();
                }
            }
        });
        buttonDeleteContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteContact(idContact);
                Toast.makeText(ContactDetailActivity.this, "Contato deletado com sucesso", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    public void createContact(Contact contact) {
        ContactRetrofitService contactService = ContactRetrofitService.retrofit.create(ContactRetrofitService.class);

        Call<Contact> callContact = contactService.createContactRetrofit(contact);

        callContact.enqueue(new Callback<Contact>() {
            @Override
            public void onResponse(Call<Contact> call, Response<Contact> response) {
                String jsonContactCreated = new Gson().toJson(response.body());
                Log.i("onResponse Retrofit", " ");
                Log.i("Contact Created", jsonContactCreated);
            }

            @Override
            public void onFailure(Call<Contact> call, Throwable t) {
                Log.i("onFailure Retrofit", t.getMessage());
                Log.i("Contact Created", " ");
            }
        });
    }

    public void updateContact(String id, Contact contact){
        ContactRetrofitService contactService = ContactRetrofitService.retrofit.create(ContactRetrofitService.class);

        Call<Contact> callContact = contactService.updateContactRetrofit(id, contact);

        callContact.enqueue(new Callback<Contact>() {
            @Override
            public void onResponse(Call<Contact> call, Response<Contact> response) {
                String jsonContactUpdated = new Gson().toJson(response.body());
                Log.i("onResponse Retrofit", " ");
                Log.i("Contact Updated", jsonContactUpdated);
            }

            @Override
            public void onFailure(Call<Contact> call, Throwable t) {
                Log.i("onFailure Retrofit", t.getMessage());
                Log.i("Contact Updated", " ");
            }
        });
    }

    public void deleteContact(String id){
        ContactRetrofitService contactService = ContactRetrofitService.retrofit.create(ContactRetrofitService.class);

        Call<Contact> callContact = contactService.deleteContactRetrofit(id);

        callContact.enqueue(new Callback<Contact>() {
            @Override
            public void onResponse(Call<Contact> call, Response<Contact> response) {
                String jsonContactUpdated = new Gson().toJson(response.body());

                Log.i("onResponse Retrofit", " ");
                Log.i("Contact Deleted", jsonContactUpdated);
            }

            @Override
            public void onFailure(Call<Contact> call, Throwable t) {
                Log.i("onFailure Retrofit", t.getMessage());
                Log.i("Contact Deleted", " ");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        contactRealmService.closeRealm();
    }
}
