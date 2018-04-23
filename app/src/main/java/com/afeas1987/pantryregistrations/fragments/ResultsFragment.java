package com.afeas1987.pantryregistrations.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afeas1987.pantryregistrations.R;
import com.afeas1987.pantryregistrations.activities.QuestionActivity;
import com.afeas1987.pantryregistrations.adapters.SimpleListAdapter;
import com.afeas1987.pantryregistrations.utilities.PantryGuest;
import com.afeas1987.pantryregistrations.utilities.SimpleListElement;

import java.util.LinkedList;
import java.util.List;

import static com.afeas1987.pantryregistrations.utilities.Constants.QUESTIONS;


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
        Log.d("DEBUG", "~~~~~~~~~~~~~~~ s = " + s);
        PantryGuest guest = activity.realm.where(PantryGuest.class).equalTo("email", s).findFirst();
        Log.d("DEBUG", "~~~~~~~~~~~~~~~ guest = " + guest);
//        activity.realm.executeTransaction(realm -> guest.deleteFromRealm());
//                realm.where(PantryGuest.class).equalTo("email", guest.getEmail()).findAll().deleteAllFromRealm());
        RecyclerView recyclerView = activity.findViewById(R.id.list_results);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setAdapter(new SimpleListAdapter(toList(guest), item -> {
            // Do nothing
        }));
        activity.findViewById(R.id.btn_results).setOnClickListener(v -> {
            activity.realm.executeTransactionAsync(realm -> realm.insert(guest));
            activity.finish();
        });
    }

    private List<SimpleListElement> toList(PantryGuest g) {
        LinkedList<SimpleListElement> list = new LinkedList<>();
        list.addLast(new SimpleListElement(g.getEmail(), "Email"));
        list.add(new SimpleListElement(g.getLast() + ", " + g.getFirst(), "Name"));
        list.addLast(new SimpleListElement(g.getAddress(), QUESTIONS[3].title));
        list.addLast(new SimpleListElement(g.getNccID(), QUESTIONS[4].title));
        list.addLast(new SimpleListElement(g.getGender(), QUESTIONS[5].title));
        list.addLast(new SimpleListElement(g.getAge(), QUESTIONS[6].title));
        list.addLast(new SimpleListElement(Integer.toString(g.getHouseholdSize()), QUESTIONS[7].title));
        list.addLast(new SimpleListElement(Integer.toString(g.getIncome()), QUESTIONS[8].title));
        list.addLast(new SimpleListElement(g.getFoodStamps(), QUESTIONS[9].title));
        list.addLast(new SimpleListElement(g.getFoodPrograms(), QUESTIONS[10].title));
        list.addLast(new SimpleListElement(g.getStatusEmploy(), QUESTIONS[11].title));
        list.addLast(new SimpleListElement(g.getStatusHealth(), QUESTIONS[12].title));
        list.addLast(new SimpleListElement(g.getStatusHousing(), QUESTIONS[13].title));
        list.addLast(new SimpleListElement(g.getStatusChild(), QUESTIONS[14].title));
        list.addLast(new SimpleListElement(Integer.toString(g.getChildUnder1()), QUESTIONS[15].title));
        list.addLast(new SimpleListElement(Integer.toString(g.getChild1to5()), QUESTIONS[16].title));
        list.addLast(new SimpleListElement(Integer.toString(g.getChild6to12()), QUESTIONS[17].title));
        list.addLast(new SimpleListElement(Integer.toString(g.getChild13to18()), QUESTIONS[18].title));
        list.addLast(new SimpleListElement(g.getDietNeeds(), QUESTIONS[19].title));
        list.addLast(new SimpleListElement(g.getFoundFrom(), QUESTIONS[20].title));
        list.addLast(new SimpleListElement(g.getComments(), QUESTIONS[21].title));
        list.addLast(new SimpleListElement(g.getHelpedBy(), QUESTIONS[22].title));
        return list;
    }
}
