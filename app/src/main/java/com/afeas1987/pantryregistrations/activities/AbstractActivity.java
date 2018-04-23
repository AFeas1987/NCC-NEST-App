/*
 * Copyright 2018 AFeas1987
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.afeas1987.pantryregistrations.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.afeas1987.pantryregistrations.utilities.PantryGuest;
import com.afeas1987.pantryregistrations.utilities.PantryVolunteer;

import io.realm.ObjectServerError;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.SyncConfiguration;
import io.realm.SyncCredentials;
import io.realm.SyncUser;

import static com.afeas1987.pantryregistrations.utilities.Constants.AUTH_URL;
import static com.afeas1987.pantryregistrations.utilities.Constants.REALM_BASE_URL;


public abstract class AbstractActivity extends AppCompatActivity {

    public Realm realm;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    public void loginToRealmAsync(String realmPath) {
        SyncUser.logInAsync(
                SyncCredentials.nickname("NESTAdmin", true), AUTH_URL,
                new SyncUser.Callback<SyncUser>() {

                    @Override
                    public void onSuccess(@NonNull SyncUser result) {
                        SyncConfiguration configuration = new SyncConfiguration.Builder(
                                SyncUser.current(), REALM_BASE_URL + "/" + realmPath).build();
                        realm = Realm.getInstance(configuration);
                        updateOfflineRealm(realmPath);
                    }

                    @Override
                    public void onError(@NonNull ObjectServerError error) {
                        realm = Realm.getInstance(SyncConfiguration.automatic());
                    }

                });
    }


    private void updateOfflineRealm(String realmPath) {
        switch (realmPath) {
            case "guests":
                final RealmResults<PantryGuest> guestRes =
                        realm.where(PantryGuest.class).findAll();
                Realm.getInstance(SyncConfiguration.automatic())
                        .executeTransaction(r -> r.copyToRealmOrUpdate(guestRes));
                break;
            case "personnel":
                final RealmResults<PantryVolunteer> volRes =
                        realm.where(PantryVolunteer.class).findAll();
                Realm.getInstance(SyncConfiguration.automatic())
                        .executeTransaction(r -> r.copyToRealmOrUpdate(volRes));
                break;
        }
        Realm.getInstance(SyncConfiguration.automatic()).close();
    }


    public void loginToRealm(String realmPath) {
        SyncUser.logIn(SyncCredentials.nickname("NESTAdmin", true), AUTH_URL);
        SyncConfiguration configuration = new SyncConfiguration.Builder(
                SyncUser.current(), REALM_BASE_URL + "/" + realmPath).build();
        realm = Realm.getInstance(configuration);
        updateOfflineRealm(realmPath);
    }
}
