package com.qa.iFramework.common.Util;

import com.opencsv.CSVReader;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by haijia on 6/19/17.
 */
public class ExcelHelper {

    public static List<Map<String, String>> readCSV(String path) throws Exception {
        List<Map<String, String>> results = new ArrayList<Map<String,String>>();
        File file = new File(path);
        FileReader fReader = new FileReader(file);
        CSVReader csvReader = new CSVReader(fReader);
        String[] headers = csvReader.readNext();

        List<String[]> list = csvReader.readAll();
        for(String[] ss : list){
            Map<String, String> rowMap = new HashMap<String, String>();
            for(int i = 0; i < ss.length; i++){
//                if(null != ss[i] && !ss[i].equals("")){
//                    rowMap.put(headers[i], ss[i]);
//                }
                if(null != ss[i]){
                    rowMap.put(headers[i], ss[i]);
                }
            }
            results.add(rowMap);
        }
        csvReader.close();

        return results;
    }

}
