package com.afeas1987.pantryregistrations.utilities;

public class SurveyQuestion {
    public String title, prompt, hint;
    public String msgRequired, msgConstraint;
    public Integer min, max;
    public String[] options;
    public int type;
    public int[] constraints;
    public boolean required;

    public SurveyQuestion (int type, final boolean required, String title, String prompt, int... constraints){
        this.type = type;
        this.required = required;
        this.title = title;
        this.prompt = prompt;
        this.constraints = constraints;
    }

    public SurveyQuestion setHint(String hint){
        this.hint = hint;
        return this;
    }

    public SurveyQuestion setRequiredMessage(String msg){
        this.msgRequired = msg;
        return this;
    }

    public SurveyQuestion setConstraintMessage(String msg){
        this.msgConstraint = msg;
        return this;
    }

    public SurveyQuestion setOptions(String[] options){
        this.options = options;
        return this;
    }

    public SurveyQuestion setRange(int min, int max){
        this.min = min;
        this.max = max;
        return this;
    }

}
