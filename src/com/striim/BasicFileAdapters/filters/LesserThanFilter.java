package com.striim.BasicFileAdapters.filters;

import com.striim.BasicFileAdapters.database.*;

public class LesserThanFilter extends Filter{

    public LesserThanFilter(){
    }

    public LesserThanFilter(String columnName,String compareWithValue){
        this.columnName=columnName;
        this.compareWithValue=compareWithValue;
    }

    public Filter getInstance(String columnName,String compareWithValue){
        return new LesserThanFilter(columnName,compareWithValue);
    }

    public boolean doesSatisfies(DataRecord dataRecord){
        return getComparisonResult(dataRecord.getRecord()) < 0;
    }

}
