package com.microsoft.xuetang.service;

import com.microsoft.xuetang.schema.request.search.SearchApiRequest;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class SearchServiceTest {

    public void testAcademicSearch() throws Exception {
        SearchService searchService = new SearchService();
        SearchApiRequest request = new SearchApiRequest();
        request.setQuery("数据挖掘");

        searchService.academicSearch(request);
    }
}