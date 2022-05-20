package com.striim.BasicFileAdapters.filters;

import com.striim.BasicFileAdapters.database.*;

public class GreaterThanFilter extends Filter{

    public GreaterThanFilter(){
    }

    public GreaterThanFilter(String columnName,String compareWithValue){
        this.columnName=columnName;
        this.compareWithValue=compareWithValue;
    }

    public Filter getInstance(String columnName,String compareWithValue){
        return new GreaterThanFilter(columnName,compareWithValue);
    }

    public boolean doesSatisfies(DataRecord dataRecord){
        return getComparisonResult(dataRecord.getRecord()) > 0;
    }

}
