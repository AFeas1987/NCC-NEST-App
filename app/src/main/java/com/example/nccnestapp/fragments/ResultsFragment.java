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
package com.example.nccnestapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nccnestapp.R;
import com.example.nccnestapp.activities.QuestionActivity;
import com.example.nccnestapp.adapters.SimpleListAdapter;
import com.example.nccnestapp.utilities.PantryGuest;
import com.example.nccnestapp.utilities.SimpleListElement;

import java.util.LinkedList;
import java.util.List;

import io.realm.Realm;
import io.realm.SyncConfiguration;

import static com.example.nccnestapp.utilities.Constants.QUESTIONS;

public class ResultsFragment extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_results, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        QuestionActivity activity = (QuestionActivity) getActivity();
        String s = getArguments().getString("EMAIL");
        PantryGuest guest = activity.realm
                .where(PantryGuest.class).equalTo("email", s).findFirst();
        Object tempGuest = activity.realm.copyFromRealm(guest);
        activity.realm.executeTransaction(r -> guest.deleteFromRealm());
        RecyclerView recyclerView = activity.findViewById(R.id.list_results);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setAdapter(new SimpleListAdapter(toList((PantryGuest)tempGuest),
                item -> {}));
        activity.findViewById(R.id.btn_results).setOnClickListener(v -> {
            activity.realm
                    .executeTransactionAsync(realm ->
                            realm.insert((PantryGuest)tempGuest));
            Realm.getInstance(SyncConfiguration.automatic())
                    .executeTransactionAsync(r ->
                            r.insertOrUpdate((PantryGuest)tempGuest));
            activity.finish();
        });
    }


    private List<SimpleListElement> toList(PantryGuest g) {
        LinkedList<SimpleListElement> list = new LinkedList<>();
        list.addLast(new SimpleListElement(g.getEmail(), "Email"));
        list.add(new SimpleListElement(g.getLast() + ", " + g.getFirst(), "Name"));
        list.addLast(new SimpleListElement(g.getAddress(), QUESTIONS[5].title));
        list.addLast(new SimpleListElement(g.getNccID(), QUESTIONS[6].title));
        list.addLast(new SimpleListElement(g.getGender(), QUESTIONS[7].title));
        list.addLast(new SimpleListElement(g.getAge(), QUESTIONS[8].title));
        list.addLast(new SimpleListElement(
                Integer.toString(g.getHouseholdSize()), QUESTIONS[9].title));
        list.addLast(new SimpleListElement(
                Integer.toString(g.getIncome()), QUESTIONS[10].title));
        list.addLast(new SimpleListElement(g.getFoodStamps(), QUESTIONS[11].title));
        list.addLast(new SimpleListElement(g.getFoodPrograms(), QUESTIONS[12].title));
        list.addLast(new SimpleListElement(g.getStatusEmploy(), QUESTIONS[13].title));
        list.addLast(new SimpleListElement(g.getStatusHealth(), QUESTIONS[14].title));
        list.addLast(new SimpleListElement(g.getStatusHousing(), QUESTIONS[15].title));
        list.addLast(new SimpleListElement(g.getStatusChild(), QUESTIONS[16].title));
        list.addLast(new SimpleListElement(
                Integer.toString(g.getChildUnder1()), QUESTIONS[17].title));
        list.addLast(new SimpleListElement(
                Integer.toString(g.getChild1to5()), QUESTIONS[18].title));
        list.addLast(new SimpleListElement(
                Integer.toString(g.getChild6to12()), QUESTIONS[19].title));
        list.addLast(new SimpleListElement(
                Integer.toString(g.getChild13to18()), QUESTIONS[20].title));
        list.addLast(new SimpleListElement(g.getDietNeeds(), QUESTIONS[21].title));
        list.addLast(new SimpleListElement(g.getFoundFrom(), QUESTIONS[22].title));
        list.addLast(new SimpleListElement(g.getComments(), QUESTIONS[23].title));
        list.addLast(new SimpleListElement(g.getHelpedBy(), QUESTIONS[24].title));
        return list;
    }
}
