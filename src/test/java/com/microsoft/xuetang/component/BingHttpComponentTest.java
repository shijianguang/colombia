package com.microsoft.xuetang.component;

import com.microsoft.xuetang.internalrpc.response.BingAcademicSearchResponse;
import org.apache.commons.lang.StringUtils;
import org.testng.annotations.Test;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;

import static org.testng.Assert.*;

public class BingHttpComponentTest {

    @Test
    public void testString2BingAcademicSearchResponse() throws Exception {
        String jsonString = "{\"BdiGeneric_BingResponse_1_0\":{\"AppNS\":\"API\",\"Responses\":[{\"results\":[{\"Url\":\"http:\\/\\/web.engr.illinois.edu\\/~hanj\\/bk2\\/toc.pdf\",\"DisplayUrl\":\"web.engr.illinois.edu\\/~hanj\\/bk2\\/toc.pdf\",\"Title\":\"\uE000Data Mining\uE001: Concepts and Techniques - Website Services\",\"Snippet\":\"\uE000Data Mining\uE001: Concepts and Techniques Second Edition Jiawei Han and Micheline Kamber University of Illinois at Urbana-Champaign AMSTERDAM BOSTON HEIDELBERG LONDON\"},{\"Url\":\"http:\\/\\/www.cms.waikato.ac.nz\\/~ml\\/publications\\/2009\\/weka_update.pdf\",\"DisplayUrl\":\"www.cms.waikato.ac.nz\\/~ml\\/publications\\/2009\\/weka_update.pdf\",\"Title\":\"The WEKA \uE000Data Mining\uE001 Software: An Update\",\"Snippet\":\"The WEKA \uE000Data Mining\uE001 Software: An Update Mark Hall Eibe Frank, Geoffrey Holmes, Bernhard Pfahringer Peter Reutemann, Ian H. Witten Pentaho Corporation Department of ...\"}],\"Query\":\"data mining\",\"IsAdultQuery\":false,\"Total\":713,\"EstimatedMatches\":8640000,\"Latency\":0.173798,\"EventId\":\"07712885b8534b1a937d5bc119e60294\"},{\"PageNum\":0,\"PageSize\":0,\"Papers\":[{\"Id\":214019024111,\"Title\":{\"OriginalTitle\":\"\uE000Data Mining\uE001: Concepts and Techniques\",\"NormalizedTitle\":\"\uE000Data Mining\uE001: Concepts and Techniques\"},\"PublishYear\":2011,\"Authors\":[{\"Id\":2121939561,\"Name\":\"Jiawei Han\"},{\"Id\":1996151347,\"Name\":\"Micheline Kamber\"},{\"Id\":2126330539,\"Name\":\"Jian Pei\"}],\"Affiliations\":[{\"Id\":8098720,\"Name\":\"Department of Computer Science University of Illinois at Urbana Champaign\"}],\"FieldsOfStudies\":[{\"Id\":9476365,\"Name\":\"\uE000Data\uE001 model\"},{\"Id\":5655090,\"Name\":\"Relational database\"},{\"Id\":86256295,\"Name\":\"Social network\"},{\"Id\":136764020,\"Name\":\"World Wide Web\"},{\"Id\":124101348,\"Name\":\"\uE000Data mining\uE001\"}],\"Citation\":31035,\"Url\":\"http:\\/\\/web.engr.illinois.edu\\/~hanj\\/bk2\\/toc.pdf\",\"Abstract\":{\"Content\":\"The increasing volume of data in modern business and science calls for more complex and sophisticated tools. Although advances in data mining technology have made extensive data collection much easier, it's still always evolving and there is a constant need for new techniques and tools that can help us transform this data into useful information and knowledge. Since the previous edition's publication, great advances have been made in the field of data mining. Not only does the third of edition of Data Mining: Concepts and Techniques continue the tradition of equipping you with an understanding and application of the theory and practice of discovering patterns hidden in large data sets, it also focuses on new, important topics in the field: data warehouses and data cube technology, mining stream, mining social networks, and mining spatial, multimedia and other complex data. Each chapter is a stand-alone guide to a critical topic, presenting proven algorithms and sound implementations ready to be used directly or with strategic modification against live data. This is the resource you need if you want to apply today's most powerful data mining techniques to meet real business challenges. * Presents dozens of algorithms and implementation examples, all in pseudo-code and suitable for use in real-world, large-scale data mining projects. * Addresses advanced topics such as mining object-relational databases, spatial databases, multimedia databases, time-series databases, text databases, the World Wide Web, and applications in several fields. *Provides a comprehensive, practical look at the concepts and techniques you need to get the most out of real business data\"},\"Caption\":{\"Content\":\"The increasing volume of data in modern business and science calls for more complex and sophisticated tools. Although advances in data mining technology have made extensive data collection much easier, it's still always evolving and there is a constant need for new techniques and tools that can help us transform this data into useful information and knowledge. Since the previous edition's publication, great advances have been made in the field of data mining. Not only does the third of edition of Data Mining: Concepts and Techniques continue the tradition of equipping you with an understanding and application of the theory and practice of discovering patterns hidden in large data sets, it also focuses on new, important topics in the field: data warehouses and data cube technology, mining stream, mining social networks, and mining spatial, multimedia and other complex data. Each chapter is a stand-alone guide to a critical topic, presenting proven algorithms and sound implementations ready to be used directly or with strategic modification against live data. This is the resource you need if you want to apply today's most powerful data mining techniques to meet real business challenges. * Presents dozens of algorithms and implementation examples, all in pseudo-code and suitable for use in real-world, large-scale data mining projects. * Addresses advanced topics such as mining object-relational databases, spatial databases, multimedia databases, time-series databases, text databases, the World Wide Web, and applications in several fields. *Provides a comprehensive, practical look at the concepts and techniques you need to get the most out of real business data\"},\"Sources\":[{\"Url\":\"http:\\/\\/web.engr.illinois.edu\\/~hanj\\/bk2\\/toc.pdf\"}]},{\"Id\":2140190241,\"Title\":{\"OriginalTitle\":\"\uE000Data Mining\uE001: Concepts and Techniques\",\"NormalizedTitle\":\"\uE000Data Mining\uE001: Concepts and Techniques\"},\"PublishYear\":2011,\"Authors\":[{\"Id\":2121939561,\"Name\":\"Jiawei Han\"},{\"Id\":1996151347,\"Name\":\"Micheline Kamber\"},{\"Id\":2126330539,\"Name\":\"Jian Pei\"}],\"Affiliations\":[{\"Id\":8098720,\"Name\":\"Department of Computer Science University of Illinois at Urbana Champaign\"}],\"FieldsOfStudies\":[{\"Id\":9476365,\"Name\":\"\uE000Data\uE001 model\"},{\"Id\":5655090,\"Name\":\"Relational database\"},{\"Id\":86256295,\"Name\":\"Social network\"},{\"Id\":136764020,\"Name\":\"World Wide Web\"},{\"Id\":124101348,\"Name\":\"\uE000Data mining\uE001\"}],\"Citation\":31035,\"Url\":\"http:\\/\\/www3.cs.stonybrook.edu\\/~cse634\\/ch6book.pdf\",\"Abstract\":{\"Content\":\"The increasing volume of data in modern business and science calls for more complex and sophisticated tools. Although advances in data mining technology have made extensive data collection much easier, it's still always evolving and there is a constant need for new techniques and tools that can help us transform this data into useful information and knowledge. Since the previous edition's publication, great advances have been made in the field of data mining. Not only does the third of edition of Data Mining: Concepts and Techniques continue the tradition of equipping you with an understanding and application of the theory and practice of discovering patterns hidden in large data sets, it also focuses on new, important topics in the field: data warehouses and data cube technology, mining stream, mining social networks, and mining spatial, multimedia and other complex data. Each chapter is a stand-alone guide to a critical topic, presenting proven algorithms and sound implementations ready to be used directly or with strategic modification against live data. This is the resource you need if you want to apply today's most powerful data mining techniques to meet real business challenges. * Presents dozens of algorithms and implementation examples, all in pseudo-code and suitable for use in real-world, large-scale data mining projects. * Addresses advanced topics such as mining object-relational databases, spatial databases, multimedia databases, time-series databases, text databases, the World Wide Web, and applications in several fields. *Provides a comprehensive, practical look at the concepts and techniques you need to get the most out of real business data\"},\"Caption\":{\"Content\":\"The increasing volume of data in modern business and science calls for more complex and sophisticated tools. Although advances in data mining technology have made extensive data collection much easier, it's still always evolving and there is a constant need for new techniques and tools that can help us transform this data into useful information and knowledge. Since the previous edition's publication, great advances have been made in the field of data mining. Not only does the third of edition of Data Mining: Concepts and Techniques continue the tradition of equipping you with an understanding and application of the theory and practice of discovering patterns hidden in large data sets, it also focuses on new, important topics in the field: data warehouses and data cube technology, mining stream, mining social networks, and mining spatial, multimedia and other complex data. Each chapter is a stand-alone guide to a critical topic, presenting proven algorithms and sound implementations ready to be used directly or with strategic modification against live data. This is the resource you need if you want to apply today's most powerful data mining techniques to meet real business challenges. * Presents dozens of algorithms and implementation examples, all in pseudo-code and suitable for use in real-world, large-scale data mining projects. * Addresses advanced topics such as mining object-relational databases, spatial databases, multimedia databases, time-series databases, text databases, the World Wide Web, and applications in several fields. *Provides a comprehensive, practical look at the concepts and techniques you need to get the most out of real business data\"},\"Sources\":[{\"Url\":\"http:\\/\\/www3.cs.stonybrook.edu\\/~cse634\\/ch6book.pdf\"}]},{\"Id\":2140190241,\"Title\":{\"OriginalTitle\":\"\uE000Data Mining\uE001: Concepts and Techniques\",\"NormalizedTitle\":\"\uE000Data Mining\uE001: Concepts and Techniques\"},\"PublishYear\":2011,\"Authors\":[{\"Id\":2121939561,\"Name\":\"Jiawei Han\"},{\"Id\":1996151347,\"Name\":\"Micheline Kamber\"},{\"Id\":2126330539,\"Name\":\"Jian Pei\"}],\"Affiliations\":[{\"Id\":8098720,\"Name\":\"Department of Computer Science University of Illinois at Urbana Champaign\"}],\"FieldsOfStudies\":[{\"Id\":9476365,\"Name\":\"\uE000Data\uE001 model\"},{\"Id\":5655090,\"Name\":\"Relational database\"},{\"Id\":86256295,\"Name\":\"Social network\"},{\"Id\":136764020,\"Name\":\"World Wide Web\"},{\"Id\":124101348,\"Name\":\"\uE000Data mining\uE001\"}],\"Citation\":31035,\"Url\":\"http:\\/\\/www.ece.ucsb.edu\\/faculty\\/manjunath\\/courses\\/ece594s03\\/dm-03l2.pdf\",\"Abstract\":{\"Content\":\"The increasing volume of data in modern business and science calls for more complex and sophisticated tools. Although advances in data mining technology have made extensive data collection much easier, it's still always evolving and there is a constant need for new techniques and tools that can help us transform this data into useful information and knowledge. Since the previous edition's publication, great advances have been made in the field of data mining. Not only does the third of edition of Data Mining: Concepts and Techniques continue the tradition of equipping you with an understanding and application of the theory and practice of discovering patterns hidden in large data sets, it also focuses on new, important topics in the field: data warehouses and data cube technology, mining stream, mining social networks, and mining spatial, multimedia and other complex data. Each chapter is a stand-alone guide to a critical topic, presenting proven algorithms and sound implementations ready to be used directly or with strategic modification against live data. This is the resource you need if you want to apply today's most powerful data mining techniques to meet real business challenges. * Presents dozens of algorithms and implementation examples, all in pseudo-code and suitable for use in real-world, large-scale data mining projects. * Addresses advanced topics such as mining object-relational databases, spatial databases, multimedia databases, time-series databases, text databases, the World Wide Web, and applications in several fields. *Provides a comprehensive, practical look at the concepts and techniques you need to get the most out of real business data\"},\"Caption\":{\"Content\":\"The increasing volume of data in modern business and science calls for more complex and sophisticated tools. Although advances in data mining technology have made extensive data collection much easier, it's still always evolving and there is a constant need for new techniques and tools that can help us transform this data into useful information and knowledge. Since the previous edition's publication, great advances have been made in the field of data mining. Not only does the third of edition of Data Mining: Concepts and Techniques continue the tradition of equipping you with an understanding and application of the theory and practice of discovering patterns hidden in large data sets, it also focuses on new, important topics in the field: data warehouses and data cube technology, mining stream, mining social networks, and mining spatial, multimedia and other complex data. Each chapter is a stand-alone guide to a critical topic, presenting proven algorithms and sound implementations ready to be used directly or with strategic modification against live data. This is the resource you need if you want to apply today's most powerful data mining techniques to meet real business challenges. * Presents dozens of algorithms and implementation examples, all in pseudo-code and suitable for use in real-world, large-scale data mining projects. * Addresses advanced topics such as mining object-relational databases, spatial databases, multimedia databases, time-series databases, text databases, the World Wide Web, and applications in several fields. *Provides a comprehensive, practical look at the concepts and techniques you need to get the most out of real business data\"},\"Sources\":[{\"Url\":\"http:\\/\\/www.ece.ucsb.edu\\/faculty\\/manjunath\\/courses\\/ece594s03\\/dm-03l2.pdf\"}]},{\"Id\":2140190241,\"Title\":{\"OriginalTitle\":\"\uE000Data Mining\uE001: Concepts and Techniques\",\"NormalizedTitle\":\"\uE000Data Mining\uE001: Concepts and Techniques\"},\"PublishYear\":2011,\"Authors\":[{\"Id\":2121939561,\"Name\":\"Jiawei Han\"},{\"Id\":1996151347,\"Name\":\"Micheline Kamber\"},{\"Id\":2126330539,\"Name\":\"Jian Pei\"}],\"Affiliations\":[{\"Id\":8098720,\"Name\":\"Department of Computer Science University of Illinois at Urbana Champaign\"}],\"FieldsOfStudies\":[{\"Id\":9476365,\"Name\":\"\uE000Data\uE001 model\"},{\"Id\":5655090,\"Name\":\"Relational database\"},{\"Id\":86256295,\"Name\":\"Social network\"},{\"Id\":136764020,\"Name\":\"World Wide Web\"},{\"Id\":124101348,\"Name\":\"\uE000Data mining\uE001\"}],\"Citation\":31035,\"Url\":\"http:\\/\\/www.ida.liu.se\\/~732a02\\/material\\/fo-intro.pdf\",\"Abstract\":{\"Content\":\"The increasing volume of data in modern business and science calls for more complex and sophisticated tools. Although advances in data mining technology have made extensive data collection much easier, it's still always evolving and there is a constant need for new techniques and tools that can help us transform this data into useful information and knowledge. Since the previous edition's publication, great advances have been made in the field of data mining. Not only does the third of edition of Data Mining: Concepts and Techniques continue the tradition of equipping you with an understanding and application of the theory and practice of discovering patterns hidden in large data sets, it also focuses on new, important topics in the field: data warehouses and data cube technology, mining stream, mining social networks, and mining spatial, multimedia and other complex data. Each chapter is a stand-alone guide to a critical topic, presenting proven algorithms and sound implementations ready to be used directly or with strategic modification against live data. This is the resource you need if you want to apply today's most powerful data mining techniques to meet real business challenges. * Presents dozens of algorithms and implementation examples, all in pseudo-code and suitable for use in real-world, large-scale data mining projects. * Addresses advanced topics such as mining object-relational databases, spatial databases, multimedia databases, time-series databases, text databases, the World Wide Web, and applications in several fields. *Provides a comprehensive, practical look at the concepts and techniques you need to get the most out of real business data\"},\"Caption\":{\"Content\":\"The increasing volume of data in modern business and science calls for more complex and sophisticated tools. Although advances in data mining technology have made extensive data collection much easier, it's still always evolving and there is a constant need for new techniques and tools that can help us transform this data into useful information and knowledge. Since the previous edition's publication, great advances have been made in the field of data mining. Not only does the third of edition of Data Mining: Concepts and Techniques continue the tradition of equipping you with an understanding and application of the theory and practice of discovering patterns hidden in large data sets, it also focuses on new, important topics in the field: data warehouses and data cube technology, mining stream, mining social networks, and mining spatial, multimedia and other complex data. Each chapter is a stand-alone guide to a critical topic, presenting proven algorithms and sound implementations ready to be used directly or with strategic modification against live data. This is the resource you need if you want to apply today's most powerful data mining techniques to meet real business challenges. * Presents dozens of algorithms and implementation examples, all in pseudo-code and suitable for use in real-world, large-scale data mining projects. * Addresses advanced topics such as mining object-relational databases, spatial databases, multimedia databases, time-series databases, text databases, the World Wide Web, and applications in several fields. *Provides a comprehensive, practical look at the concepts and techniques you need to get the most out of real business data\"},\"Sources\":[{\"Url\":\"http:\\/\\/www.ida.liu.se\\/~732a02\\/material\\/fo-intro.pdf\"}]},{\"Id\":2140190241,\"Title\":{\"OriginalTitle\":\"\uE000Data Mining\uE001: Concepts and Techniques\",\"NormalizedTitle\":\"\uE000Data Mining\uE001: Concepts and Techniques\"},\"PublishYear\":2011,\"Authors\":[{\"Id\":2121939561,\"Name\":\"Jiawei Han\"},{\"Id\":1996151347,\"Name\":\"Micheline Kamber\"},{\"Id\":2126330539,\"Name\":\"Jian Pei\"}],\"Affiliations\":[{\"Id\":8098720,\"Name\":\"Department of Computer Science University of Illinois at Urbana Champaign\"}],\"FieldsOfStudies\":[{\"Id\":9476365,\"Name\":\"\uE000Data\uE001 model\"},{\"Id\":5655090,\"Name\":\"Relational database\"},{\"Id\":86256295,\"Name\":\"Social network\"},{\"Id\":136764020,\"Name\":\"World Wide Web\"},{\"Id\":124101348,\"Name\":\"\uE000Data mining\uE001\"}],\"Citation\":31035,\"Url\":\"http:\\/\\/www.sciencedirect.com\\/science\\/book\\/9780123814791\",\"Abstract\":{\"Content\":\"The increasing volume of data in modern business and science calls for more complex and sophisticated tools. Although advances in data mining technology have made extensive data collection much easier, it's still always evolving and there is a constant need for new techniques and tools that can help us transform this data into useful information and knowledge. Since the previous edition's publication, great advances have been made in the field of data mining. Not only does the third of edition of Data Mining: Concepts and Techniques continue the tradition of equipping you with an understanding and application of the theory and practice of discovering patterns hidden in large data sets, it also focuses on new, important topics in the field: data warehouses and data cube technology, mining stream, mining social networks, and mining spatial, multimedia and other complex data. Each chapter is a stand-alone guide to a critical topic, presenting proven algorithms and sound implementations ready to be used directly or with strategic modification against live data. This is the resource you need if you want to apply today's most powerful data mining techniques to meet real business challenges. * Presents dozens of algorithms and implementation examples, all in pseudo-code and suitable for use in real-world, large-scale data mining projects. * Addresses advanced topics such as mining object-relational databases, spatial databases, multimedia databases, time-series databases, text databases, the World Wide Web, and applications in several fields. *Provides a comprehensive, practical look at the concepts and techniques you need to get the most out of real business data\"},\"Caption\":{\"Content\":\"The increasing volume of data in modern business and science calls for more complex and sophisticated tools. Although advances in data mining technology have made extensive data collection much easier, it's still always evolving and there is a constant need for new techniques and tools that can help us transform this data into useful information and knowledge. Since the previous edition's publication, great advances have been made in the field of data mining. Not only does the third of edition of Data Mining: Concepts and Techniques continue the tradition of equipping you with an understanding and application of the theory and practice of discovering patterns hidden in large data sets, it also focuses on new, important topics in the field: data warehouses and data cube technology, mining stream, mining social networks, and mining spatial, multimedia and other complex data. Each chapter is a stand-alone guide to a critical topic, presenting proven algorithms and sound implementations ready to be used directly or with strategic modification against live data. This is the resource you need if you want to apply today's most powerful data mining techniques to meet real business challenges. * Presents dozens of algorithms and implementation examples, all in pseudo-code and suitable for use in real-world, large-scale data mining projects. * Addresses advanced topics such as mining object-relational databases, spatial databases, multimedia databases, time-series databases, text databases, the World Wide Web, and applications in several fields. *Provides a comprehensive, practical look at the concepts and techniques you need to get the most out of real business data\"},\"Sources\":[{\"Url\":\"http:\\/\\/www.sciencedirect.com\\/science\\/book\\/9780123814791\"}]},{\"Id\":2133990480,\"Title\":{\"OriginalTitle\":\"The WEKA \uE000data mining\uE001 software: an update\",\"NormalizedTitle\":\"The WEKA \uE000data mining\uE001 software: an update\"},\"PublishYear\":2009,\"Venue\":{\"OriginalVenue\":\"Sigkdd Explorations\",\"NormalizedVenue\":\"Sigkdd Explorations\",\"DOI\":\"1\"},\"Authors\":[{\"Id\":2126237948,\"Name\":\"Mark A Hall\"},{\"Id\":2165714491,\"Name\":\"Eibe Frank\"},{\"Id\":2189262995,\"Name\":\"Geoffrey Holmes\"},{\"Id\":1991131908,\"Name\":\"Bernhard Pfahringer\"},{\"Id\":707865762,\"Name\":\"Peter Reutemann\"},{\"Id\":2163446563,\"Name\":\"Ian H Witten\"}],\"Affiliations\":[{\"Id\":52179390,\"Name\":\"University of Waikato\"}],\"FieldsOfStudies\":[{\"Id\":124101348,\"Name\":\"\uE000Data mining\uE001\"}],\"Citation\":10830,\"Url\":\"http:\\/\\/www.cms.waikato.ac.nz\\/~ml\\/publications\\/2009\\/weka_update.pdf\",\"Abstract\":{\"Content\":\"More than twelve years have elapsed since the first public release of WEKA. In that time, the software has been rewritten entirely from scratch, evolved substantially and now accompanies a text on data mining [35]. These days, WEKA enjoys widespread acceptance in both academia and business, has an active community, and has been downloaded more than 1.4 million times since being placed on Source-Forge in April 2000. This paper provides an introduction to the WEKA workbench, reviews the history of the project, and, in light of the recent 3.6 stable release, briefly discusses what has been added since the last stable version (Weka 3.4) released in 2003.\"},\"Caption\":{\"Content\":\"More than twelve years have elapsed since the first public release of WEKA. In that time, the software has been rewritten entirely from scratch, evolved substantially and now accompanies a text on data mining [35]. These days, WEKA enjoys widespread acceptance in both academia and business, has an active community, and has been downloaded more than 1.4 million times since being placed on Source-Forge in April 2000. This paper provides an introduction to the WEKA workbench, reviews the history of the project, and, in light of the recent 3.6 stable release, briefly discusses what has been added since the last stable version (Weka 3.4) released in 2003.\"},\"Sources\":[{\"Url\":\"http:\\/\\/www.cms.waikato.ac.nz\\/~ml\\/publications\\/2009\\/weka_update.pdf\"}]}],\"TotalEstimatedMatches\":8640000,\"AnswerType\":\"WebRichCaption\"}]}}";
        BingAcademicSearchResponse response = BingHttpComponent.string2BingAcademicSearchResponse(jsonString);
        assertEquals(response.getQuery(), "data mining");
        assertEquals(response.getResult().get(1).getTitle(), "The WEKA data mining software: an update");
        assertEquals(response.getResult().get(1).getPublishYear(), "2009");
        assertEquals(response.getResult().get(1).getVenue(), "Sigkdd Explorations");
        assertEquals(response.getResult().get(1).getAuthors().get(5).getId(), "2163446563");
        assertEquals(response.getResult().get(1).getAuthors().get(5).getName(), "Ian H Witten");

    }

    @Test
    public void testString2BingAcademicSearchResponseWithLargeData() throws Exception {
        BufferedReader inputStream = new BufferedReader(new FileReader(BingHttpComponentTest.class.getResource(
                "/bing_academic_result.json").getFile()));
        String jsonString = inputStream.readLine();
        BingAcademicSearchResponse response = BingHttpComponent.string2BingAcademicSearchResponse(jsonString);
        assertEquals(response.getQuery(), "data mining");
        assertEquals(response.getResult().get(1).getTitle(), "The WEKA data mining software: an update");
        assertEquals(response.getResult().get(1).getPublishYear(), "2009");
        assertEquals(response.getResult().get(1).getVenue(), "Sigkdd Explorations");
        assertEquals(response.getResult().get(1).getAuthors().get(5).getId(), "2163446563");
        assertEquals(response.getResult().get(1).getAuthors().get(5).getName(), "Ian H Witten");

    }
}