package com.striim.BasicFileAdapters.query;

import com.striim.BasicFileAdapters.database.DataRecord;
import java.util.function.Predicate;

public class FilterFactory {

    public static Predicate<DataRecord> getFilter(String coloumnName, String operation, String referenceValue) {
        switch (operation) {
            case ">": {
                return dataRecord -> getComparisonResult(dataRecord.get(coloumnName), referenceValue) > 0;
            }
            case "=": {
                return dataRecord -> getComparisonResult(dataRecord.get(coloumnName),referenceValue) == 0;
            }
            case "<": {
                return dataRecord -> getComparisonResult(dataRecord.get(coloumnName),referenceValue ) < 0;
            }
            default:
                return null;

        }
    }

    public static Predicate<DataRecord> getFilter(String constraint) {
        String[] temp = constraint.split(" ");
        return getFilter(temp[0],temp[1],temp[2]);
    }

    private static int getComparisonResult(String columnValue,String referenceValue){
        return columnValue.compareTo(referenceValue);
    }
}
