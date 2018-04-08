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
package com.example.nccnestapp.utilities;

public class Constants {
//    //  For collectTester
//    public static final String FORMS_URI = "content://org.odk.collect.android.provider.odk.forms/forms";
//    public static final String INSTANCES_URI = "content://org.odk.collect.android.provider.odk.instances/instances";
//    public static final String DISPLAY_NAME = "displayName";
//    public static final String DISPLAY_SUBTEXT = "displaySubtext";
//    public static final String COLLECT_PACKAGE_NAME = "org.odk.collect.android";
//    //  For SheetsActivity
//    public static final int REQUEST_ACCOUNT_PICKER = 1000;
//    public static final int REQUEST_AUTHORIZATION = 1001;
//    public static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
//    public static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;
//    public static final String PREF_ACCOUNT_NAME = "accountName";
//    public static final String GOOGLE_APP_NAME = "NCC NEST Registrations";
//    public static final String[] SCOPES = { SheetsScopes.SPREADSHEETS_READONLY };
//    public static final String SHEET_ID = "1u2t-wl4h70he3nKCfsrycyNgW_uSHhbT2BgNNDQrBGA";
//    public static final String SHEET_RANGE = "Guest";
    //  For Realm
    private static final String INSTANCE_ADDRESS = "nest-registrations.us1a.cloud.realm.io";
    public static final String AUTH_URL = "https://" + INSTANCE_ADDRESS + "/auth";
    public static final String REALM_BASE_URL = "realms://" + INSTANCE_ADDRESS;
    //  For SurveyQuestion
    public final static class QuestionType
        {public final static int TEXT = 0, NUMBER_ENTRY = 1, SELECT_NUMBER = 2, SELECT_ONE = 3, SELECT_MULTI = 4, ADDRESS = 5;}
    public final static class Constraint
        {public final static int PHONE_NUMBER = 0, NCC_ID = 1, RANGE = 2;}
    //  Questions
    public static final SurveyQuestion[] QUESTIONS = {
            new SurveyQuestion(QuestionType.TEXT, true, "Last Name", "Enter your last name"),
            new SurveyQuestion(QuestionType.TEXT, true, "First Name", "Enter your first name"),
            new SurveyQuestion(QuestionType.TEXT, true, "Phone Number", "Enter your phone number", Constraint.PHONE_NUMBER),
            new SurveyQuestion(QuestionType.ADDRESS, true, "Address", "Where are you located?"),
            new SurveyQuestion(QuestionType.TEXT, false, "NCC ID", "Enter your NCC ID#", Constraint.NCC_ID),
            new SurveyQuestion(QuestionType.SELECT_ONE, true, "Gender", "Select your gender")
                    .setOptions(new String[]{"Male", "Female", "No response"}),
            new SurveyQuestion(QuestionType.SELECT_ONE, true, "Age", "Select your age")
                    .setOptions(new String[]{"Under 19", "19 to 64", "Over 64"}),
            new SurveyQuestion(QuestionType.SELECT_NUMBER, true, "Household size", "Select the number of people in your household")
                    .setRange(1, 25),
            new SurveyQuestion(QuestionType.NUMBER_ENTRY, true, "Household income", "Enter your household income", Constraint.RANGE)
                    .setRange(0, 250000),
            new SurveyQuestion(QuestionType.SELECT_ONE, true, "SNAP/Food Stamps", "Do you have SNAP/Food stamps?")
                    .setOptions(new String[]{"Yes", "No"}),
            new SurveyQuestion(QuestionType.SELECT_ONE, true, "Food Programs", "Aside from The NEST, do you know of other emergency food programs in the area?")
                    .setOptions(new String[]{"Yes", "No"}),
            new SurveyQuestion(QuestionType.SELECT_MULTI, true, "Employment Status", "What is your employment status?")
                    .setOptions(new String[]{"I am currently working", "I am not currently working", "I am looking for employment",
                    "I would like more education/training", "I would like help with my resume writing", "I would like help with immigration counseling",
                    "I choose not to answer"}),
            new SurveyQuestion(QuestionType.SELECT_MULTI, true, "Health Status", "What is your health status?")
                    .setOptions(new String[]{"I have health insurance", "I do not have health insurance", "I do not know how to get health insurance",
                    "I have a primary care physician", "I need a referral for physical or medical health treatment", "I need a referral for drug/alcohol treatment",
                    "I choose not to answer"}),
            new SurveyQuestion(QuestionType.SELECT_MULTI, true, "Housing Status", "What is your housing status?")
                    .setOptions(new String[]{"I have a safe and comfortable place to live", "I am currently homeless or in danger of becoming homeless",
                    "I need housing", "I choose not to answer"}),
            new SurveyQuestion(QuestionType.SELECT_MULTI, true, "Childcare Status", "What is your childcare status?")
                    .setOptions(new String[]{"I do not have children in my household", "I need diapers, baby food, and other baby supplies",
                    "I am interested in parenting workshops", "I choose not to answer"}),
            new SurveyQuestion(QuestionType.SELECT_NUMBER, true, "Children Under 1", "Select the number of children in your household under 12 months old")
                    .setRange(0, 25),
            new SurveyQuestion(QuestionType.SELECT_NUMBER, true, "Children 1 to 5", "Select the number of children in your household ages 1 to 5")
                    .setRange(0, 25),
            new SurveyQuestion(QuestionType.SELECT_NUMBER, true, "Children 6 to 12", "Select the number of children in your household ages 6 to 12")
                    .setRange(0, 25),
            new SurveyQuestion(QuestionType.SELECT_NUMBER, true, "Children 13 to 18", "Select the number of children in your household ages 13 to 18")
                    .setRange(0, 25),
            new SurveyQuestion(QuestionType.SELECT_MULTI, true, "Dietary Needs", "What are your dietary needs and preferences?")
                    .setOptions(new String[]{"None", "Diabetic", "Low salt", "Kosher", "Halal", "Gluten-free", "I choose not to answer"}),
            new SurveyQuestion(QuestionType.SELECT_MULTI, true, "", "How did you find us?")
                    .setOptions(new String[]{"Professor/campus staff", "Friend", "Website", "Facebook", "Twitter", "Instagram", "Internet Search", "Other"}),
            new SurveyQuestion(QuestionType.TEXT, false, "Comments", "Is there anything you would like us to know at this time?"),
            new SurveyQuestion(QuestionType.TEXT, true, "Volunteer", "What is the name of the Volunteer who helped you today?")
    };
}
