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
package com.afeas1987.pantryregistrations.utilities;

import java.util.Locale;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

import static java.lang.String.format;


/**
 * Item class representing a guest of the pantry.  For use with Realm.
 */
public class PantryVolunteer extends RealmObject{
    @PrimaryKey
    @Required
    private String email;

    @Required
    private Integer pin;

    private String first, last, phone, street, city, state;
    private int zip;
    private String nccID, availability;
    private boolean isAdmin;


    /** Initializes an empty {@link PantryVolunteer} **/
    public PantryVolunteer(){ super(); }


    /**
     * Initializes a new {@link PantryVolunteer} with email and pin.
     *
     * @param email
     * @param pin
     */
    public PantryVolunteer(String email, String pin){
        this(email, pin, true);
    }

    public PantryVolunteer(String email, String pin, final boolean isAdmin){
        this.email = email;
        setPin(pin);
        this.isAdmin = isAdmin;
    }

    /** Setters **/
    public PantryVolunteer setFirstName(Object first) {
        this.first = (String)first; return this; }
    public PantryVolunteer setLastName(Object last) {
        this.last = (String)last; return this; }
    public PantryVolunteer setEmail(Object email) {
        this.email = (String)email; return this; }
    public PantryVolunteer setPin(Object pin) {
        this.pin = Integer.parseInt((String)pin); return this; }
    public PantryVolunteer setPhone(Object phone) {
        this.phone = (String)phone; return this; }
    public PantryVolunteer setSchoolID(Object nccID) {
        this.nccID = (String)nccID; return this; }
    public PantryVolunteer setStreet(Object street) {
        this.street = (String)street; return this; }
    public PantryVolunteer setCity(Object city) {
        this.city = (String)city; return this; }
    public PantryVolunteer setState(Object state) {
        this.state = (String)state; return this; }
    public PantryVolunteer setZip(Object zip) {
        this.zip = zip != null ? Integer.parseInt((String)zip) : 0; return this; }
    public PantryVolunteer setAvailability(Object availability) {
        this.availability = (String)availability; return this; }
    public void toggleAdmin(boolean admin) {isAdmin = !isAdmin;}

    /** Getters **/
    public String getFirst() {return first;}
    public String getLast() {return last;}
    public String getEmail() {return email;}
    public String getPhone() {return phone;}
    public String getNccID() {return nccID;}
    public String getStreet() {return street;}
    public String getCity() {return city;}
    public String getState() {return state;}
    public String getAvailability() {return availability;}
    public int getZip() {return zip;}
    public boolean isAdmin() {return isAdmin;}

    /** Verify pin input **/
    public boolean checkPin(int input) {
        return pin == String.valueOf(input).hashCode();
    }


    @Override
    public String toString(){
        return format("%20s  %13s, %-12s",
                format("[%s]", email), last, first);
    }

    public String getAddress() {
        return format(Locale.US, "%s%n%s, %s  %d", street, city, state, zip);
    }
}
