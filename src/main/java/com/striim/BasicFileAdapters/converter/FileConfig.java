package com.striim.BasicFileAdapters.converter;

import com.striim.BasicFileAdapters.database.DataRecord;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.function.Predicate;

public class FileConfig {

    private @Getter
    @Setter String filePath;
    private @Getter
    @Setter String fileType;
    private @Getter
    @Setter String delimiter;
    private @Getter String type;
    private @Getter Predicate<DataRecord> query;
    private @Getter
    @Setter ArrayList<String> fetchColumns;

    public void setType(String type) {
        this.type = type.toUpperCase();
    }

    public void setQuery(Predicate<DataRecord> query) {
        if (this.query == null)
            this.query = query;
        else
            this.query = this.query.and(query);
    }
}
