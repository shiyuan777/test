package com.jixunkeji.result;

import java.util.List;

public class ConditionAndParamsDto {

    private String condition;
    private List<Object> params;

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public List<Object> getParams() {
        return params;
    }

    public void setParams(List<Object> params) {
        this.params = params;
    }

}
