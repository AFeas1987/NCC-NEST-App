/*
 * Copyright 2017 Nafundi
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
import android.widget.TextView;

import com.example.nccnestapp.R;
import com.example.nccnestapp.utilities.SimpleListElement;

import java.util.List;

public class SimpleListAdapter extends RecyclerView.Adapter<SimpleListAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(SimpleListElement item);
    }

    private final OnItemClickListener listener;

    private List<SimpleListElement> simpleListElements;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView text1;
        TextView text2;
        View layout;

        ViewHolder(View v) {
            super(v);
            layout = v;
            text1 = v.findViewById(R.id.text1);
            text2 = v.findViewById(R.id.text2);
        }

        void bind(final SimpleListElement item, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }

    public SimpleListAdapter(List<SimpleListElement> simpleListElements, OnItemClickListener listener) {
        this.simpleListElements = simpleListElements;
        this.listener = listener;
    }

    @Override
    public SimpleListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.simple_list_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(simpleListElements.get(position), listener);
        holder.text1.setText(simpleListElements.get(position).getText1());
        holder.text2.setText(simpleListElements.get(position).getText2());
    }

    @Override
    public int getItemCount() {
        return simpleListElements.size();
    }
}
