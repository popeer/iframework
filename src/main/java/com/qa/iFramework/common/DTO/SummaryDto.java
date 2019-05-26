package com.qa.iFramework.common.DTO;

import java.io.Serializable;

/**
 * Created by haijia on 9/22/17.
 */
public class SummaryDto implements Serializable {

    public SummaryDto(){

    }

    public SummaryDto(Integer pass, Integer fail, Integer skip, String rundate, String productname){
        this.pass = pass;
        this.fail = fail;
        this.skip = skip;
        this.rundate = rundate;
        this.productname = productname;
    }

    long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Integer getPass() {
        return pass;
    }

    public void setPass(Integer pass) {
        this.pass = pass;
    }

    Integer pass;

    public Integer getFail() {
        return fail;
    }

    public void setFail(Integer fail) {
        this.fail = fail;
    }

    Integer fail;

    public Integer getSkip() {
        return skip;
    }

    public void setSkip(Integer skip) {
        this.skip = skip;
    }

    Integer skip;

    public String getRundate() {
        return rundate;
    }

    public void setRundate(String rundate) {
        this.rundate = rundate;
    }

    String rundate;

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    String productname;


}
