package com.chanjet.chanapp.qa.iFramework.common;

/**
 * Created by haijia on 6/27/17.
 */
public interface IRemoteStarter {
    String run(String dburl,
               String uid,
               String pwd,
               String dn,
               String productName,
               String dns,
               String path);

    String getSummary(String productname, String rundate);

    String getDirectoriesFiles(String productname) throws Exception;

    String showXml(String path);

    String getHistoryReportList(String productname, String date);

    String getReport(String reportID);

    boolean editXml(String path, String content);
}
