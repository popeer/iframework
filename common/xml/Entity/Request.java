package com.chanjet.chanapp.qa.iFramework.common.xml.Entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by haijia on 11/23/16.
 */
public class Request {

    List<RequestParameter> parameters = new ArrayList<RequestParameter>();

    public void setParameters(List<RequestParameter> parameters) {
        this.parameters = parameters;
    }

    public List<RequestParameter> getParameters(){
        return parameters;
    }

    public void addParameters(RequestParameter parameter){
        parameters.add(parameter);
    }

    private int id;
    private String title;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Request{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", RequestParameters=" + parameters +
                '}';
    }
}
