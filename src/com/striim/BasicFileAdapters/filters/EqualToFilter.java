package com.striim.BasicFileAdapters.filters;

import com.striim.BasicFileAdapters.database.*;

public class EqualToFilter extends Filter{

    public EqualToFilter(){
    }

    public EqualToFilter(String columnName,String compareWithValue){
        this.columnName=columnName;
        this.compareWithValue=compareWithValue;
    }

    public Filter getInstance(String columnName,String compareWithValue){
        return new EqualToFilter(columnName,compareWithValue);
    }

    public boolean doesSatisfies(DataRecord dataRecord){
        return getComparisonResult(dataRecord.getRecord()) == 0;
    }

}
