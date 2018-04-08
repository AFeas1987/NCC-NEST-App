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
package com.example.nccnestapp.utilities;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;


/**
 * Item class representing a guest of the pantry.  For use with ODK Collect and Realm.
 */
public class PantryGuest extends RealmObject{
    @PrimaryKey
    @Required
    private String email;

    @Required
    private Integer pin;

    private String first, last, phone, street, city, state, gender, age;
    private String statusEmploy, statusHealth, statusHousing, statusChild;
    private String dietNeeds, foundFrom, helpedBy;
    private int zip;
    private String nccID, comments;
    int householdSize, income, childUnder1, child1to5, child6to12, child13to18;
    String foodStamps, foodPrograms;


    /** Initializes an empty {@link PantryGuest} **/
    public PantryGuest(){super();}


    /**
     * Initializes a new {@link PantryGuest} with email and pin.
     *
     * @param email
     * @param pin
     */
    public PantryGuest (String email, String pin){
        this.email = email;
        setPin(pin);
    }

    /** Setters **/
    public PantryGuest setFirstName(Object first) {this.first = (String)first; return this;}
    public PantryGuest setLastName(Object last) {this.last = (String)last; return this;}
    public PantryGuest setEmail(Object email) {this.email = (String)email; return this;}
    public PantryGuest setPin(Object pin) {this.pin = Integer.parseInt((String)pin); return this;}
    public PantryGuest setPhone(Object phone) {this.phone = (String)phone; return this;}
    public PantryGuest setSchoolID(Object nccID) {this.nccID = (String)nccID; return this;}
    public PantryGuest setStreet(Object street) {this.street = (String)street; return this;}
    public PantryGuest setCity(Object city) {this.city = (String)city; return this;}
    public PantryGuest setState(Object state) {this.state = (String)state; return this;}
    public PantryGuest setZip(Object zip) {this.zip = Integer.parseInt((String)zip); return this;}
    public PantryGuest setGender(Object gender) {this.gender = (String)gender; return this;}
    public PantryGuest setAge(Object age) {this.age = (String)age; return this;}
    public PantryGuest setStatusEmploy(Object statusEmploy) {this.statusEmploy = (String)statusEmploy; return this;}
    public PantryGuest setStatusHealth(Object statusHealth) {this.statusHealth = (String)statusHealth; return this;}
    public PantryGuest setStatusHousing(Object statusHousing) {this.statusHousing = (String)statusHousing; return this;}
    public PantryGuest setStatusChild(Object statusChild) {this.statusChild = (String)statusChild; return this;}
    public PantryGuest setDietNeeds(Object dietNeeds) {this.dietNeeds = (String)dietNeeds; return this;}
    public PantryGuest setFoundFrom(Object foundFrom) {this.foundFrom = (String)foundFrom; return this;}
    public PantryGuest setComments(Object comments) {this.comments = (String)comments; return this;}
    public PantryGuest setHelpedBy(Object helpedBy) {this.helpedBy = (String)helpedBy; return this;}

    /** Household Setters **/
    public PantryGuest setHouseholdSize(Object householdSize) {
        this.householdSize = Integer.parseInt((String)householdSize);  return this;}
    public PantryGuest setIncome(Object income) {this.income = Integer.parseInt((String)income); return this;}
    public PantryGuest setChildUnder1(Object childUnder1) {this.childUnder1 = Integer.parseInt((String)childUnder1);  return this;}
    public PantryGuest setChild1to5(Object child1to5) {this.child1to5 = Integer.parseInt((String)child1to5); return this;}
    public PantryGuest setChild6to12(Object child6to12) {this.child6to12 = Integer.parseInt((String)child6to12); return this;}
    public PantryGuest setChild13to18(Object child13to18) {this.child13to18 = Integer.parseInt((String)child13to18); return this;}
    public PantryGuest setFoodStamps(Object foodStamps) {this.foodStamps = (String)foodStamps; return this;}
    public PantryGuest setFoodPrograms(Object foodPrograms) {this.foodPrograms = (String)foodPrograms; return this;}

    /** Getters **/
    public String getFirst() {return first;}
    public String getLast() {return last;}
    public String getEmail() {return email;}
    public String getPhone() {return phone;}
    public String getNccID() {return nccID;}
    public String getStreet() {return street;}
    public String getCity() {return city;}
    public String getState() {return state;}
    public String getGender() {return gender;}
    public String getStatusEmploy() {return statusEmploy;}
    public String getStatusHealth() {return statusHealth;}
    public String getStatusHousing() {return statusHousing;}
    public String getStatusChild() {return statusChild;}
    public String getDietNeeds() {return dietNeeds;}
    public int getZip() {return zip;}
    public String getAge() {return age;}
    public String getFoundFrom() {return foundFrom;}
    public String getHelpedBy() {return helpedBy;}
    public String getComments() {return comments;}

    /** Household Getters **/
    public int getHouseholdSize() {return householdSize; }
    public int getIncome() {return income;}
    public int getChildUnder1() {return childUnder1;}
    public int getChild1to5() {return child1to5;}
    public int getChild6to12() {return child6to12;}
    public int getChild13to18() {return child13to18;}
    public String getFoodStamps() {return foodStamps;}
    public String getFoodPrograms() {return foodPrograms;}


    /** Verify pin input **/
    public boolean checkPin(int input) {return pin == String.valueOf(input).hashCode();}


    @Override
    public String toString(){
        return String.format("%20s  %13s, %-12s", String.format("[%s]", email), last, first);
    }

    public String getAddress() {
        return String.format("%s%n%s, %s  %d", street, city, state, zip);
    }
}
