package com.striim.BasicFileAdapters.filters;

import com.striim.BasicFileAdapters.database.*;
import java.util.*;

public abstract class Filter {

    protected String columnName;
    protected String compareWithValue;

    public abstract boolean doesSatisfies(DataRecord dataRecord);
    public abstract Filter getInstance(String columnName,String compareWithValue);

    int getComparisonResult(Map<String,String> record){
        String columnValue=record.get(columnName);
        return columnValue.compareTo(compareWithValue);
    }

}
