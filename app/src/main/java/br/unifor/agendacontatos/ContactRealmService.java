package br.unifor.agendacontatos;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

/**
 * Created by rafaelpinheiro on 24/02/17.
 */

public class ContactRealmService {

    private Realm realm;


    ContactRealmService(Context context){
        Realm.init(context);
        RealmConfiguration config = new RealmConfiguration
                .Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);
        realm = Realm.getDefaultInstance();
    }


    public void createContact(final Contact contact){
        if(contact == null){
            Log.i("ContactRealmService", "Null Contact");
        }
        else{
            realm.beginTransaction();
            realm.insertOrUpdate(contact);
            realm.commitTransaction();
        }
    }


    public Contact queryContactById(String contactId){
        Contact contact = realm.where(Contact.class).equalTo("id", contactId).findFirst();
        return contact;
    }


    public void updateContact(final Contact contact){
        if(contact == null){
            Log.i("ContactRealmService", "Null Contact");
        }
        else{
            final Contact contactToUpdate = queryContactById(contact.getId());

            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    contactToUpdate.setName(contact.getName());
                    contactToUpdate.setPhone(contact.getPhone());
                }
            });
        }
    }


    public void deleteContactById(final String contactId){
        realm.beginTransaction();
        queryContactById(contactId).deleteFromRealm();
        realm.commitTransaction();
    }


    public  void deleteAllContacts(){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.deleteAll();
            }
        });
    }


    public int getNumberOfContacts(){
        int numberOfContacts = 0;
        if(realm.where(Contact.class).findFirst() != null) {
            numberOfContacts = realm.where(Contact.class).findAll().size();
        }
        return numberOfContacts;
    }


    public ArrayList<Contact> getContactSchedule(){
        RealmResults<Contact> realmResults = realm.where(Contact.class).findAll();
        return new ArrayList(realmResults);
    }

    public void closeRealm(){
        realm.close();
    }

    public int getIdAvailable(){
        ArrayList<Contact> contactSchedule = getContactSchedule();
        int idAvailable = 0;
        boolean finishSearchForId = false;
        while(finishSearchForId == false){
            boolean idIsAvailable = true;
            for(int i=0;i<contactSchedule.size();i++){
                if(Integer.parseInt(contactSchedule.get(i).getId()) == idAvailable){
                    idIsAvailable = false;
                }
            }
            if(idIsAvailable == false){
                idAvailable++;
            }
            else{
                finishSearchForId = true;
            }
        }
        return idAvailable;
    }

    public void createAllContactsFromList(ArrayList<Contact> contactScheduleToAdd){
        for(int i=0;i<contactScheduleToAdd.size();i++){
            createContact(contactScheduleToAdd.get(i));
        }
    }
}
