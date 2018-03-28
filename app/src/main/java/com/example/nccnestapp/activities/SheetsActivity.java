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
package com.example.nccnestapp.activities;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import com.example.nccnestapp.R;
import com.example.nccnestapp.adapters.PantryGuestRecyclerAdapter;
import com.example.nccnestapp.utilities.PantryGuest;
import com.example.nccnestapp.utilities.SheetsTaskListener;

import java.util.ArrayList;
import java.util.List;

import io.realm.ObjectServerError;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.SyncConfiguration;
import io.realm.SyncCredentials;
import io.realm.SyncUser;

import static com.example.nccnestapp.utilities.Constants.AUTH_URL;
import static com.example.nccnestapp.utilities.Constants.REALM_BASE_URL;
import static com.example.nccnestapp.utilities.Constants.SHEET_ID;
import static com.example.nccnestapp.utilities.Constants.SHEET_RANGE;

public class SheetsActivity extends AbstractActivity {

    static List<PantryGuest> mResults;
    static RealmResults<PantryGuest> guestResults;


    /**
     *
     *
     * @param savedInstanceState
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sheets);
        mResults = new ArrayList<>();
        SyncUser.logInAsync(SyncCredentials.nickname("NESTAdmin", true), AUTH_URL, new SyncUser.Callback<SyncUser>() {

            @Override
            public void onSuccess(SyncUser result) {
                guestResults = setUpRealm();
                Log.d("DEBUG", "~~~~~~~~~~~~~~~  Realm path: " + ((realm != null) ? realm.getPath() : null));
                displayRealmResults();
                //makeSheetsApiCall();
            }

            @Override
            public void onError(ObjectServerError error) {
                realm = Realm.getDefaultInstance();
                Log.d("DEBUG", "~~~~~~~~~~~~~~~  Realm path: " + ((realm != null) ? realm.getPath() : null));
                //makeSheetsApiCall();
            }

        });
    }


    private RealmResults<PantryGuest> setUpRealm() {
        SyncConfiguration configuration = new SyncConfiguration.Builder(
                SyncUser.current(), REALM_BASE_URL + "/default").build();
        realm = Realm.getInstance(configuration);
        return realm.where(PantryGuest.class).findAll();
    }


    private void displayRealmResults(){
//        ((TextView)findViewById(R.id.sheets_list)).setText(TextUtils.join("\n", guestResults.toArray()));



        final PantryGuestRecyclerAdapter recyclerAdapter = new PantryGuestRecyclerAdapter(guestResults);
        RecyclerView recyclerView = findViewById(R.id.sheets_list);
        LinearLayoutManager mgr = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mgr);
        ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) recyclerView.getLayoutParams();
        lp.height = 768;
        recyclerView.setLayoutParams(lp);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);

//        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
//
//            @Override
//            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
//                return false;
//            }
//
//            @Override
//            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
//                int position = viewHolder.getAdapterPosition();
//                String email = recyclerAdapter.getItem(position).getEmail();
//                realm.executeTransactionAsync(realm -> {
//                    PantryGuest g = realm.where(PantryGuest.class)
//                            .equalTo("email", email)
//                            .findFirst();
//                    if (g != null) {
//                        g.deleteFromRealm();
//                    }
//                });
//            }
//        };
//
//        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
//        itemTouchHelper.attachToRecyclerView(recyclerView);
    }


    @Override
    protected void makeSheetsApiCall() {
        //  For Guest Sheet:
        getResultsFromApi(new SheetsTaskListener() {

            @Override
            public String getSheetId() {
                return SHEET_ID;
            }

            @Override
            public String getSheetRange() {
                return SHEET_RANGE;
            }


            @Override
            public void onTaskCompleted() {
                ((TextView)findViewById(R.id.sheets_list)).setText(TextUtils.join("\n", mResults));
            }


            @Override
            public void processData(List<List<Object>> values) {
                if (values != null) {
                    values.remove(0);

                    for (List row : values) {
                        PantryGuest guest = new PantryGuest(((String)row.get(1)), ((String)row.get(2)))
                       //         .setEmail(row.get(1)).setPin(row.get(2))
                                .setLastName(row.get(3)).setFirstName(row.get(4))
                                .setPhone(row.get(5)).setStreet(row.get(6))
                                .setCity(row.get(7)).setState(row.get(8))
                                .setZip(row.get(9)).setSchoolID(row.get(10))
                                .setGender(row.get(11)).setAge(row.get(12))
                                .setHouseholdSize(row.get(13)).setIncome(row.get(14))
                                .setFoodStamps(row.get(15)).setFoodPrograms(row.get(16))
                                .setStatusEmploy(row.get(17)).setStatusHealth(row.get(18))
                                .setStatusHousing(row.get(19)).setStatusChild(row.get(20))
                                .setChildUnder1(row.get(21)).setChild1to5(row.get(22))
                                .setChild6to12(row.get(23)).setChild13to18(row.get(24))
                                .setDietNeeds(row.get(25)).setFoundFrom(row.get(26) + " " + row.get(27))
                                .setComments(row.get(28)).setHelpedBy(row.get(29));
                        mResults.add(guest);
                    }
                }
            }
        });
    }


    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}