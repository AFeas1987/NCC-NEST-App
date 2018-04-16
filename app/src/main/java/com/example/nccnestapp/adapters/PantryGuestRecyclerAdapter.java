/*
 * Copyright 2018 Realm Inc.
 * Modifications 2018 AFeas1987
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
package com.example.nccnestapp.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.nccnestapp.R;
import com.example.nccnestapp.utilities.PantryGuest;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

public class PantryGuestRecyclerAdapter extends
        RealmRecyclerViewAdapter<PantryGuest, PantryGuestRecyclerAdapter.MyViewHolder> {


    public PantryGuestRecyclerAdapter(OrderedRealmCollection<PantryGuest> data) {
        super(data, true);
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_layout_guest, parent, false);
        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final PantryGuest g = getItem(position);
        holder.setItem(g);
    }


    class MyViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        TextView emailView, nameView;
        CheckBox checkBox;
        PantryGuest mGuest;


        MyViewHolder(View itemView) {
            super(itemView);
            emailView = itemView.findViewById(R.id.rec_guest_email);
            nameView = itemView.findViewById(R.id.rec_guest_name);
            checkBox = itemView.findViewById(R.id.checkbox);
            checkBox.setOnClickListener(this);
        }


        void setItem(PantryGuest guest){
            this.mGuest = guest;
            this.emailView.setText(guest.getEmail());
            this.nameView.setText(
                    String.format("%s, %s", guest.getLast(), guest.getFirst()));
            this.checkBox.setChecked(false);
        }


        @Override
        public void onClick(View v) {
            checkBox.setChecked(checkBox.isChecked());
        }
    }
}
