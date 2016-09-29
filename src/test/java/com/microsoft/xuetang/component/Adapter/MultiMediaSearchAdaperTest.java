package com.microsoft.xuetang.component.Adapter;

import com.microsoft.xuetang.bean.internal.response.QueryKeyword;
import com.microsoft.xuetang.schema.request.search.SearchApiRequest;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.*;

public class MultiMediaSearchAdaperTest {

    @Test
    public void testParserQuery() throws IOException {
        SearchApiRequest request = new SearchApiRequest();
        request.setQuery("data mining decision trees");
        List<QueryKeyword> keywordList = new ArrayList<>();
        keywordList.add(new QueryKeyword("data mining", 0, 11));
        keywordList.add(new QueryKeyword("decision tree", 12, 25));
        request.setKeywords(keywordList);

        List<MultiMediaSearchAdaper.RequestAdaper.QueryTerm> termList = new MultiMediaSearchAdaper.RequestAdaper("", "").parseQueryTerm(request);

        assertEquals(termList.size(), 2);
        assertEquals(termList.get(0).term, "data mining");
        assertEquals(termList.get(0).slop, 1);
        assertEquals(termList.get(0).startOffset, 0);
        assertEquals(termList.get(0).endOffset, 11);

        assertEquals(termList.get(1).term, "decision tree");
        assertEquals(termList.get(1).slop, 1);
        assertEquals(termList.get(1).startOffset, 12);
        assertEquals(termList.get(1).endOffset, 25);


        request = new SearchApiRequest();
        request.setQuery("如何学习机器学习数据挖掘方法abc");
        keywordList = new ArrayList<>();
        keywordList.add(new QueryKeyword("何", 1, 2));
        keywordList.add(new QueryKeyword("数据", 4, 6));
        keywordList.add(new QueryKeyword("数据挖掘", 4, 8));
        keywordList.add(new QueryKeyword("掘学", 7, 9));
        keywordList.add(new QueryKeyword("机器学习", 8, 12));
        keywordList.add(new QueryKeyword("学习方法", 10, 14));
        request.setKeywords(keywordList);

        termList = new MultiMediaSearchAdaper.RequestAdaper("", "").parseQueryTerm(request);

        assertEquals(termList.size(), 10);
        assertEquals(termList.get(0).term, "如");
        assertEquals(termList.get(0).slop, -1);
        assertEquals(termList.get(0).startOffset, 0);
        assertEquals(termList.get(0).endOffset, 1);

        assertEquals(termList.get(1).term, "何");
        assertEquals(termList.get(1).slop, 1);
        assertEquals(termList.get(1).startOffset, 1);
        assertEquals(termList.get(1).endOffset, 2);

        assertEquals(termList.get(2).term, "学");
        assertEquals(termList.get(2).slop, -1);
        assertEquals(termList.get(2).startOffset, 2);
        assertEquals(termList.get(2).endOffset, 3);

        assertEquals(termList.get(3).term, "习");
        assertEquals(termList.get(3).slop, -1);
        assertEquals(termList.get(3).startOffset, 3);
        assertEquals(termList.get(3).endOffset, 4);

        assertEquals(termList.get(4).term, "数据");
        assertEquals(termList.get(4).slop, 1);
        assertEquals(termList.get(4).startOffset, 4);
        assertEquals(termList.get(4).endOffset, 6);

        assertEquals(termList.get(5).term, "数据挖掘");
        assertEquals(termList.get(5).slop, 1);
        assertEquals(termList.get(5).startOffset, 4);
        assertEquals(termList.get(5).endOffset, 8);

        assertEquals(termList.get(6).term, "掘学");
        assertEquals(termList.get(6).slop, 1);
        assertEquals(termList.get(6).startOffset, 7);
        assertEquals(termList.get(6).endOffset, 9);

        assertEquals(termList.get(7).term, "机器学习");
        assertEquals(termList.get(7).slop, 1);
        assertEquals(termList.get(7).startOffset, 8);
        assertEquals(termList.get(7).endOffset, 12);

        assertEquals(termList.get(8).term, "学习方法");
        assertEquals(termList.get(8).slop, 1);
        assertEquals(termList.get(8).startOffset, 10);
        assertEquals(termList.get(8).endOffset, 14);

        assertEquals(termList.get(9).term, "abc");
        assertEquals(termList.get(9).slop, -1);
        assertEquals(termList.get(9).startOffset, 14);
        assertEquals(termList.get(9).endOffset, 17);
    }

}