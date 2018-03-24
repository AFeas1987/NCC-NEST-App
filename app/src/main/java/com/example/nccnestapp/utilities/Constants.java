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

import com.google.api.services.sheets.v4.SheetsScopes;

public class Constants {
    public static final String FORMS_URI = "content://org.odk.collect.android.provider.odk.forms/forms";
    //public static final String INSTANCES_URI = "content://org.odk.collect.android.provider.odk.instances/instances";
    public static final String DISPLAY_NAME = "displayName";
    public static final String DISPLAY_SUBTEXT = "displaySubtext";
    public static final String COLLECT_PACKAGE_NAME = "org.odk.collect.android";
    public static final int REQUEST_ACCOUNT_PICKER = 1000;
    public static final int REQUEST_AUTHORIZATION = 1001;
    public static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    public static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;
    public static final String PREF_ACCOUNT_NAME = "accountName";
    public static final String[] SCOPES = { SheetsScopes.SPREADSHEETS_READONLY };
}
