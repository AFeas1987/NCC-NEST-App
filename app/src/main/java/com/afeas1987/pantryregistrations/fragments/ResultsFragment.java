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
package com.afeas1987.pantryregistrations.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afeas1987.pantryregistrations.R;
import com.afeas1987.pantryregistrations.activities.SurveyActivity;
import com.afeas1987.pantryregistrations.adapters.SimpleListAdapter;
import com.afeas1987.pantryregistrations.utilities.PantryGuest;
import com.afeas1987.pantryregistrations.utilities.PantryVolunteer;
import com.afeas1987.pantryregistrations.utilities.SimpleListElement;
import com.afeas1987.pantryregistrations.utilities.Constants.SurveyType;

import java.util.LinkedList;
import java.util.List;

import io.realm.Realm;
import io.realm.SyncConfiguration;

import static com.afeas1987.pantryregistrations.utilities.Constants.GUEST_QUESTIONS;
import static com.afeas1987.pantryregistrations.utilities.Constants.VOLUNTEER_QUESTIONS;

public class ResultsFragment extends Fragment {
    private SurveyActivity mActivity;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_results, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mActivity = (SurveyActivity) getActivity();
        switch (mActivity.surveyType){
            case SurveyType.GUEST:
                processGuest();
                break;
            case SurveyType.VOLUNTEER:
                processVolunteer();
                break;
        }
    }

    private void processVolunteer() {
        String s = getArguments().getString("EMAIL");
        PantryVolunteer volunteer = mActivity.realm
                .where(PantryVolunteer.class).equalTo("email", s).findFirst();
        Object tempVolunteer = mActivity.realm.copyFromRealm(volunteer);
        mActivity.realm.executeTransaction(r -> volunteer.deleteFromRealm());
        RecyclerView recyclerView = mActivity.findViewById(R.id.list_results);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        recyclerView.setAdapter(new SimpleListAdapter(toList((PantryVolunteer)tempVolunteer),
                item -> { }));
        mActivity.findViewById(R.id.btn_results).setOnClickListener(v -> {
            mActivity.realm
                    .executeTransactionAsync(realm ->
                            realm.insert((PantryVolunteer)tempVolunteer));
            Realm.getInstance(SyncConfiguration.automatic())
                    .executeTransactionAsync(r ->
                            r.insertOrUpdate((PantryVolunteer)tempVolunteer));
            mActivity.finish();
        });
    }

    private List<SimpleListElement> toList(PantryVolunteer v) {
        LinkedList<SimpleListElement> list = new LinkedList<>();
        list.addLast(new SimpleListElement(v.getEmail(), "Email"));
        list.add(new SimpleListElement(v.getLast() + ", " + v.getFirst(), "Name"));
        list.addLast(new SimpleListElement(v.getPhone(), VOLUNTEER_QUESTIONS[4].title));
        list.addLast(new SimpleListElement(v.getAddress(), VOLUNTEER_QUESTIONS[5].title));
        list.addLast(new SimpleListElement(v.getNccID(), VOLUNTEER_QUESTIONS[6].title));
        return list;
    }

    private void processGuest() {
        String s = getArguments().getString("EMAIL");
        PantryGuest guest = mActivity.realm
                .where(PantryGuest.class).equalTo("email", s).findFirst();
        Object tempGuest = mActivity.realm.copyFromRealm(guest);
        mActivity.realm.executeTransaction(r -> guest.deleteFromRealm());
        RecyclerView recyclerView = mActivity.findViewById(R.id.list_results);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        recyclerView.setAdapter(new SimpleListAdapter(toList((PantryGuest)tempGuest),
                item -> { }));
        mActivity.findViewById(R.id.btn_results).setOnClickListener(v -> {
            mActivity.realm
                    .executeTransactionAsync(realm ->
                            realm.insert((PantryGuest)tempGuest));
            Realm.getInstance(SyncConfiguration.automatic())
                    .executeTransactionAsync(r ->
                            r.insertOrUpdate((PantryGuest)tempGuest));
            mActivity.finish();
        });
    }


    private List<SimpleListElement> toList(PantryGuest g) {
        LinkedList<SimpleListElement> list = new LinkedList<>();
        list.addLast(new SimpleListElement(g.getEmail(), "Email"));
        list.add(new SimpleListElement(g.getLast() + ", " + g.getFirst(), "Name"));
        list.addLast(new SimpleListElement(g.getPhone(), GUEST_QUESTIONS[4].title));
        list.addLast(new SimpleListElement(g.getAddress(), GUEST_QUESTIONS[5].title));
        list.addLast(new SimpleListElement(g.getNccID(), GUEST_QUESTIONS[6].title));
        list.addLast(new SimpleListElement(g.getGender(), GUEST_QUESTIONS[7].title));
        list.addLast(new SimpleListElement(g.getAge(), GUEST_QUESTIONS[8].title));
        list.addLast(new SimpleListElement(
                Integer.toString(g.getHouseholdSize()), GUEST_QUESTIONS[9].title));
        list.addLast(new SimpleListElement(
                Integer.toString(g.getIncome()), GUEST_QUESTIONS[10].title));
        list.addLast(new SimpleListElement(g.getFoodStamps(), GUEST_QUESTIONS[11].title));
        list.addLast(new SimpleListElement(g.getFoodPrograms(), GUEST_QUESTIONS[12].title));
        list.addLast(new SimpleListElement(g.getStatusEmploy(), GUEST_QUESTIONS[13].title));
        list.addLast(new SimpleListElement(g.getStatusHealth(), GUEST_QUESTIONS[14].title));
        list.addLast(new SimpleListElement(g.getStatusHousing(), GUEST_QUESTIONS[15].title));
        list.addLast(new SimpleListElement(g.getStatusChild(), GUEST_QUESTIONS[16].title));
        list.addLast(new SimpleListElement(
                Integer.toString(g.getChildUnder1()), GUEST_QUESTIONS[17].title));
        list.addLast(new SimpleListElement(
                Integer.toString(g.getChild1to5()), GUEST_QUESTIONS[18].title));
        list.addLast(new SimpleListElement(
                Integer.toString(g.getChild6to12()), GUEST_QUESTIONS[19].title));
        list.addLast(new SimpleListElement(
                Integer.toString(g.getChild13to18()), GUEST_QUESTIONS[20].title));
        list.addLast(new SimpleListElement(g.getDietNeeds(), GUEST_QUESTIONS[21].title));
        list.addLast(new SimpleListElement(g.getFoundFrom(), GUEST_QUESTIONS[22].title));
        list.addLast(new SimpleListElement(g.getComments(), GUEST_QUESTIONS[23].title));
        list.addLast(new SimpleListElement(g.getHelpedBy(), GUEST_QUESTIONS[24].title));
        return list;
    }
}
