package com.microsoft.xuetang.component.Adapter;

import com.microsoft.xuetang.bean.SearchList;
import com.microsoft.xuetang.bean.internal.response.QueryKeyword;
import com.microsoft.xuetang.bean.schema.response.search.SearchElementData;
import com.microsoft.xuetang.component.ElasticSearchComponent;
import com.microsoft.xuetang.schema.request.search.SearchApiRequest;
import com.microsoft.xuetang.util.CommonUtils;
import com.microsoft.xuetang.util.Constants;
import org.apache.logging.log4j.core.appender.SyslogAppender;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.CharTermAttributeImpl;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by jiash on 8/8/2016.
 */
public class MultiMediaSearchAdaper {
    public static class RequestAdaper extends ElasticSearchComponent.SearchRequestAdapter<SearchApiRequest> {

        private static final Logger logger = LoggerFactory.getLogger(RequestAdaper.class);
        private static int RERANK_NUMBER = 100;
        private static FromSize RERANK_FROM_SIZE = new FromSize(0, RERANK_NUMBER);
        private static StandardAnalyzer analyzer = new StandardAnalyzer();

        public RequestAdaper(String index, String type) {
            super(index, type);
        }

        @Override
        public SearchRequestBuilder build(SearchRequestBuilder newBuilder, SearchApiRequest searchApiRequest) throws Exception {

            FromSize fromSize = calFromSize(searchApiRequest);
            newBuilder.setTypes(getType()).setFrom(fromSize.from).setSize(fromSize.size);
            String query = searchApiRequest.getQuery();

            List<QueryKeyword> keywords = searchApiRequest.getKeywords();
            if(keywords == null || keywords.size() == 0) {
                newBuilder.setQuery(QueryBuilders.disMaxQuery()
                        .add(QueryBuilders.matchQuery("NormalizedTitle", query).minimumShouldMatch(calTitleMatchThreshold(searchApiRequest)))
                        .add(QueryBuilders.matchQuery("Keywords", query).boost(2))
                );
            } else {

                BoolQueryBuilder keywordsBuilder = QueryBuilders.boolQuery();
                for (QueryKeyword ele : keywords) {
                    keywordsBuilder.should(QueryBuilders.matchQuery("Keywords", ele.getTerm()));
                }
                keywordsBuilder.boost(2);

                List<QueryTerm> queryTerms = null;
                try {
                    queryTerms = parseQueryTerm(searchApiRequest);
                } catch (Throwable t) {
                    logger.error("Parser Query to term error. Query: {}, Keywords: {}. Reason: {}", query, keywords, t.getMessage());
                }
                if(queryTerms == null) {
                    newBuilder.setQuery(
                            QueryBuilders.boolQuery()
                                    .should(QueryBuilders.matchQuery("NormalizedTitle", query).minimumShouldMatch(calTitleMatchThreshold(searchApiRequest)))
                                    .should(keywordsBuilder)
                    );
                } else {
                    BoolQueryBuilder queryTermBuilder = QueryBuilders.boolQuery();
                    BoolQueryBuilder noEntityTermBuilder = QueryBuilders.boolQuery();
                    for (QueryTerm term : queryTerms) {
                        if (term.slop < 0) {
                            noEntityTermBuilder.should(QueryBuilders.termQuery("NormalizedTitle", term.term));
                        } else {
                            queryTermBuilder.should(QueryBuilders.matchPhraseQuery("NormalizedTitle", term.term).slop(term.slop).boost(4));
                        }
                    }
                    newBuilder.setQuery(
                            QueryBuilders.boolQuery()
                                    .should(noEntityTermBuilder)
                                    .should(queryTermBuilder.boost(2))
                                    .should(keywordsBuilder)
                    );
                }
            }
            logger.info(newBuilder.toString());
            return newBuilder;

        }

        public List<QueryTerm>
        parseQueryTerm(SearchApiRequest searchApiRequest) throws IOException {
            String query = searchApiRequest.getQuery();
            List<QueryTerm> queryTerms = new ArrayList<>();
            List<QueryKeyword> keywords = searchApiRequest.getKeywords();
            int keywordSize = keywords.size();
            int iter = 0;

            TokenStream tokenStream = analyzer.tokenStream("", query);
            tokenStream.reset();
            CharTermAttribute attribute = tokenStream.addAttribute(CharTermAttribute.class);
            OffsetAttribute offsetAttribute = tokenStream.addAttribute(OffsetAttribute.class);
            while(tokenStream.incrementToken()) {
                queryTerms.add(new QueryTerm(attribute.toString(), offsetAttribute.startOffset(), offsetAttribute.endOffset()));
            }
            tokenStream.close();

            List<QueryTerm> result = new ArrayList<>(queryTerms.size());
            QueryKeyword keyword = null;
            if(iter < keywordSize) {
                keyword = keywords.get(iter);
            }
            for(int i = 0 ; i < queryTerms.size() ; ) {
                QueryTerm queryTerm = queryTerms.get(i);
                if (keyword != null && keyword.getStartOffset() == queryTerm.startOffset) {
                    int offset = query.length();
                    for (int j = i; j < queryTerms.size(); ++j) {
                        //buffer.append(queryTerms.get(j).term);
                        if (queryTerms.get(j).endOffset >= keyword.getEndOffset()) {
                            result.add(new QueryTerm(keyword.getTerm(), keyword.getStartOffset(), keyword.getEndOffset(), 1));
                            ++iter;
                            if (iter >= keywordSize) {
                                offset = keyword.getEndOffset();
                                keyword = null;
                            } else {
                                QueryKeyword next = keywords.get(iter);
                                if(next.getStartOffset() < keyword.getEndOffset()) {
                                    offset = next.getStartOffset();
                                } else {
                                    offset = keyword.getEndOffset();
                                }
                                keyword = next;
                            }
                            while (i < queryTerms.size()) {
                                if(queryTerms.get(i).startOffset >= offset)
                                    break;
                                ++ i;
                            }
                            break;
                        }
                    }
                } else {
                    result.add(queryTerm);
                    ++ i;
                }
            }
            return result;
        }

        static class QueryTerm {
            String term;
            int startOffset;
            int endOffset;
            int slop;

            public QueryTerm(String term, int startOffset, int endOffset) {
                this(term, startOffset, endOffset, -1);
            }
            public QueryTerm(String term, int startOffset, int endOffset, int slop) {
                this.term = term;
                this.startOffset = startOffset;
                this.endOffset = endOffset;
                this.slop = slop;
            }
        }

        private static class FromSize {
            int from;
            int size;

            public FromSize(int from, int size) {
                this.from = from;
                this.size = size;
            }
        }
        private FromSize calFromSize(SearchApiRequest searchApiRequest) {
            if(searchApiRequest.getOffsetInt() < RERANK_NUMBER) {
                return RERANK_FROM_SIZE;
            } else {
                return new FromSize(searchApiRequest.getOffsetInt(), searchApiRequest.getCountInt());
            }
        }

        private String calTitleMatchThreshold(SearchApiRequest searchApiRequest) {
            int len = CommonUtils.calStringLength(searchApiRequest.getQuery());
            if(len <= 4) {
                return "100%";
            } else if (len > 4 && len <= 10) {
                return "80%";
            } else {
                return "75%";
            }
        }
    }

    public static class ResponseAdaper extends ElasticSearchComponent.SearchResponseAdapter<SearchList<SearchElementData>> {

        @Override
        public SearchList<SearchElementData> trasform(SearchResponse response) throws Exception {
            SearchList<SearchElementData> result = new SearchList<>();
            for (SearchHit hit : response.getHits()) {
                Map<String, Object> data = hit.getSource();
                SearchElementData entity = new SearchElementData();
                entity.setId(hit.getId());
                Object value = null;
                value = data.get("Publisher");
                entity.setSinglePublisher(value instanceof String ? (String)value : null);
                value = data.get("Title");
                entity.setTitle(value instanceof String ? (String)value : null);
                value = data.get("Abstract");
                if(value instanceof String) {
                    String abstractContent = CommonUtils.trimBefore((String) value, Constants.NEW_LINE_CHARACTER_ARRAY);
                    entity.setSnippet(abstractContent);
                } else {
                    entity.setSnippet(null);
                }
                value = data.get("ImageUrl");
                entity.setImageUrl(value instanceof String ? (String)value : null);
                value = data.get("Url");
                entity.setUrl(value instanceof String ? (String)value : null);
                value = data.get("ResourceType");
                entity.setType(value instanceof String ? (String)value : null);
                value = data.get("ClusterId");
                entity.setVenu(value instanceof String ? (String)value : null);

                result.add(entity);
            }

            return result;
        }
    }
}
