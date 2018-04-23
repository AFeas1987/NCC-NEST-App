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

public class SurveyQuestion {
    public String title, prompt, hint;
    public String msgRequired, msgConstraint;
    public Integer min, max;
    public String[] options;
    public int type;
    public int[] constraints;
    public boolean required;

    public SurveyQuestion (int type, final boolean required, String title,
                           String prompt, int... constraints) {
        this.type = type;
        this.required = required;
        this.title = title;
        this.prompt = prompt;
        this.constraints = constraints;
    }

    public SurveyQuestion setHint(String hint) {
        this.hint = hint;
        return this;
    }

    public SurveyQuestion setRequiredMessage(String msg) {
        this.msgRequired = msg;
        return this;
    }

    public SurveyQuestion setConstraintMessage(String msg) {
        this.msgConstraint = msg;
        return this;
    }

    public SurveyQuestion setOptions(String[] options) {
        this.options = options;
        return this;
    }

    public SurveyQuestion setRange(int min, int max) {
        this.min = min;
        this.max = max;
        return this;
    }

}
