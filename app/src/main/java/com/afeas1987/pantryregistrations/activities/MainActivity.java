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

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.afeas1987.pantryregistrations.R;
import com.afeas1987.pantryregistrations.adapters.PantryGuestRecyclerAdapter;
import com.afeas1987.pantryregistrations.utilities.PantryGuest;

import io.realm.RealmResults;

public class MainActivity extends AbstractActivity {

    private RecyclerView recView;
    private Button mainBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar(findViewById(R.id.toolbar));
        mainBtn = findViewById(R.id.btn_guest_main);
        mainBtn.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), QuestionActivity.class));
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    private void displayRealmResults() {
        RealmResults<PantryGuest> guestResults = realm.where(PantryGuest.class).findAll();
        final PantryGuestRecyclerAdapter recyclerAdapter = new PantryGuestRecyclerAdapter(guestResults);
        LinearLayoutManager mgr = new LinearLayoutManager(this);
        recView.setLayoutManager(mgr);
//        ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) recView.getLayoutParams();
//        lp.height = 768;
//        recView.setLayoutParams(lp);
        recView.setAdapter(recyclerAdapter);
        recView.setHasFixedSize(true);
        recView.setNestedScrollingEnabled(false);

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int position = viewHolder.getAdapterPosition();
                String email = recyclerAdapter.getItem(position).getEmail();
                realm.executeTransactionAsync(realm -> {
                    PantryGuest g = realm.where(PantryGuest.class)
                            .equalTo("email", email)
                            .findFirst();
                    if (g != null) {
                        g.deleteFromRealm();
                    }
                });
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recView);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_admin:
                recView = findViewById(R.id.list_main);
                TextView mainTxt = findViewById(R.id.txt_title_main);
                int vis = recView.getVisibility();
                mainBtn.setVisibility(vis);
                if (vis == View.GONE) {
                    recView.setVisibility(View.VISIBLE);
                    mainTxt.setVisibility(View.VISIBLE);
                    displayRealmResults();
                }
                else {
                    recView.setVisibility(View.GONE);
                    mainTxt.setVisibility(View.GONE);
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
