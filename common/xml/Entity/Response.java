package com.chanjet.chanapp.qa.iFramework.common.xml.Entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by haijia on 11/23/16.
 */
public class Response {

    List<ResponseParameter> parameters = new ArrayList<ResponseParameter>();

    public void setParameters(List<ResponseParameter> parameters) {
        this.parameters = parameters;
    }

    public List<ResponseParameter> getParameters(){
        return parameters;
    }

    public void addParameters(ResponseParameter parameter){
        parameters.add(parameter);
    }

    @Override
    public String toString() {
        return "Response{" +
                "parameters=" + parameters +
                '}';
    }
}
