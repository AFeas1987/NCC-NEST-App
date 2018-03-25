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
import android.text.TextUtils;
import android.widget.TextView;

import com.example.nccnestapp.R;
import com.example.nccnestapp.utilities.PantryGuest;
import com.example.nccnestapp.utilities.SheetsTaskListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.nccnestapp.utilities.Constants.SHEET_ID;
import static com.example.nccnestapp.utilities.Constants.SHEET_RANGE;

public class SheetsActivity extends AbstractActivity {

    static List<PantryGuest> mResults;


    /**
     *
     *
     * @param savedInstanceState
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sheets);
        mResults = new ArrayList<>();
        makeApiCall();
    }

    @Override
    protected void makeApiCall() {
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
                ((TextView)findViewById(R.id.admin_list)).setText(TextUtils.join("\n", mResults));
            }

            @Override
            public void processData(List row) {
                PantryGuest guest = new PantryGuest()
                        .setEmail(row.get(1)).setPin(row.get(2))
                        .setLastName(row.get(3)).setFirstName(row.get(4))
                        .setPhone(row.get(5)).setStreet(row.get(6))
                        .setCity(row.get(7)).setState(row.get(8))
                        .setZip(row.get(9)).setSchoolID(row.get(10))
                        .setGender(row.get(11)).setAge(row.get(12))
                        .setSize(row.get(13)).setIncome(row.get(14))
                        .setFoodStamps(row.get(15)).setFoodPrograms(row.get(16))
                        .setStatusEmploy(row.get(17)).setStatusHealth(row.get(18))
                        .setStatusHousing(row.get(19)).setStatusChild(row.get(20))
                        .setChildUnder1(row.get(21)).setChild1to5(row.get(22))
                        .setChild6to12(row.get(23)).setChild13to18(row.get(24))
                        .setDietNeeds(row.get(25)).setFoundFrom(row.get(26) + " " + row.get(27))
                        .setComments(row.get(28)).setHelpedBy(row.get(29));
                mResults.add(guest);
            }
        });
    }


}