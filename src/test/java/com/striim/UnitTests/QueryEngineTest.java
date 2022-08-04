package com.striim.UnitTests;
import com.striim.BasicFileAdapters.query.QueryEngine;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;


import java.util.ArrayList;
import java.util.Arrays;

public class QueryEngineTest{
    @ParameterizedTest
    @CsvFileSource(resources="/constraints.csv",numLinesToSkip=1)
    public void validateConstraint(String constraint,String expected){
        Assertions.assertEquals(Boolean.parseBoolean(expected),QueryEngine.isProperConstraint(constraint,new ArrayList<String>(Arrays.asList("age","education","marital"))));
    }

}