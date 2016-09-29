package com.microsoft.xuetang.internalrpc.response;

import org.codehaus.jackson.annotate.JsonProperty;

import java.util.List;
import java.util.Map;

/**
 * Created by jiash on 7/29/2016.
 */
public class BingPaperProfileResponse {
    private String errorCode;
    private String error;
    private MessageTypePart MessageType;
    private ResultPart Result;

    public String getErrorCode() {
        return errorCode;
    }

    @JsonProperty("error_code")
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public MessageTypePart getMessageType() {
        return MessageType;
    }

    @JsonProperty("MessageType")
    public void setMessageType(MessageTypePart messageType) {
        MessageType = messageType;
    }

    public ResultPart getResult() {
        return Result;
    }

    @JsonProperty("Result")
    public void setResult(ResultPart result) {
        Result = result;
    }

    public static class MessageTypePart {
        private int StatusCode;
        private String Description;

        public int getStatusCode() {
            return StatusCode;
        }

        public void setStatusCode(int statusCode) {
            StatusCode = statusCode;
        }

        public String getDescription() {
            return Description;
        }

        public void setDescription(String description) {
            Description = description;
        }
    }

    public static class ResultPart {
        private List<Map<String, Object>> DownloadUrls;
        private String Keywords;
        private String PreviewUrl;
        private List<Map<String, String>> SourceUrls;
        private String ExtensionData;
        private String Title;
        private String PublishDate;
        private String Year;
        private String OriginalVenue;
        private String DOI;
        private String Volume;
        private String Issue;
        private String FirstPage;
        private String LastPage;
        private String ISBN;
        private String Abstract;
        private int ReferenceCount;
        private int CitationCount;
        private String Publisher;
        private List<Map<String, Object>> Authors;
        private int ArticleType;
        private Map<String, Object> Publication;
        private List<Map<String, Object>> FieldsOfStudy;
        private long Id;
        private String Name;
        private String ImageUrl;
        private String Homepage;
        private String Description;
        private int Type;
        private int FollowerCount;
        private int FollowingCount;
        private boolean IsFollowed;

        public List<Map<String, Object>> getDownloadUrls() {
            return DownloadUrls;
        }

        @JsonProperty("DownloadUrls")
        public void setDownloadUrls(List<Map<String, Object>> downloadUrls) {
            DownloadUrls = downloadUrls;
        }

        public String getKeywords() {
            return Keywords;
        }

        @JsonProperty("Keywords")
        public void setKeywords(String keywords) {
            Keywords = keywords;
        }

        public String getPreviewUrl() {
            return PreviewUrl;
        }

        @JsonProperty("PreviewUrl")
        public void setPreviewUrl(String previewUrl) {
            PreviewUrl = previewUrl;
        }

        public List<Map<String, String>> getSourceUrls() {
            return SourceUrls;
        }

        @JsonProperty("SourceUrls")
        public void setSourceUrls(List<Map<String, String>> sourceUrls) {
            SourceUrls = sourceUrls;
        }

        public String getExtensionData() {
            return ExtensionData;
        }

        @JsonProperty("ExtensionData")
        public void setExtensionData(String extensionData) {
            ExtensionData = extensionData;
        }

        public String getTitle() {
            return Title;
        }

        @JsonProperty("Title")
        public void setTitle(String title) {
            Title = title;
        }

        public String getPublishDate() {
            return PublishDate;
        }

        @JsonProperty("PublishDate")
        public void setPublishDate(String publishDate) {
            PublishDate = publishDate;
        }

        public String getYear() {
            return Year;
        }

        @JsonProperty("Year")
        public void setYear(String year) {
            Year = year;
        }

        public String getOriginalVenue() {
            return OriginalVenue;
        }

        @JsonProperty("OriginalVenue")
        public void setOriginalVenue(String originalVenue) {
            OriginalVenue = originalVenue;
        }

        public String getDOI() {
            return DOI;
        }

        @JsonProperty("DOI")
        public void setDOI(String DOI) {
            this.DOI = DOI;
        }

        public String getVolume() {
            return Volume;
        }

        @JsonProperty("Volume")
        public void setVolume(String volume) {
            Volume = volume;
        }

        public String getIssue() {
            return Issue;
        }

        @JsonProperty("Issue")
        public void setIssue(String issue) {
            Issue = issue;
        }

        public String getFirstPage() {
            return FirstPage;
        }

        @JsonProperty("FirstPage")
        public void setFirstPage(String firstPage) {
            FirstPage = firstPage;
        }

        public String getLastPage() {
            return LastPage;
        }

        @JsonProperty("LastPage")
        public void setLastPage(String lastPage) {
            LastPage = lastPage;
        }

        public String getISBN() {
            return ISBN;
        }

        @JsonProperty("ISBN")
        public void setISBN(String ISBN) {
            this.ISBN = ISBN;
        }

        public String getAbstract() {
            return Abstract;
        }

        @JsonProperty("Abstract")
        public void setAbstract(String anAbstract) {
            Abstract = anAbstract;
        }

        public int getReferenceCount() {
            return ReferenceCount;
        }

        @JsonProperty("ReferenceCount")
        public void setReferenceCount(int referenceCount) {
            ReferenceCount = referenceCount;
        }

        public int getCitationCount() {
            return CitationCount;
        }

        @JsonProperty("CitationCount")
        public void setCitationCount(int citationCount) {
            CitationCount = citationCount;
        }

        public String getPublisher() {
            return Publisher;
        }

        @JsonProperty("Publisher")
        public void setPublisher(String publisher) {
            Publisher = publisher;
        }

        public List<Map<String, Object>> getAuthors() {
            return Authors;
        }

        @JsonProperty("Authors")
        public void setAuthors(List<Map<String, Object>> authors) {
            Authors = authors;
        }

        public int getArticleType() {
            return ArticleType;
        }

        @JsonProperty("ArticleType")
        public void setArticleType(int articleType) {
            ArticleType = articleType;
        }

        public Map<String, Object> getPublication() {
            return Publication;
        }

        @JsonProperty("Publication")
        public void setPublication(Map<String, Object> publication) {
            Publication = publication;
        }

        public List<Map<String, Object>> getFieldsOfStudy() {
            return FieldsOfStudy;
        }

        @JsonProperty("FieldsOfStudy")
        public void setFieldsOfStudy(List<Map<String, Object>> fieldsOfStudy) {
            FieldsOfStudy = fieldsOfStudy;
        }

        public long getId() {
            return Id;
        }

        @JsonProperty("Id")
        public void setId(long id) {
            Id = id;
        }

        public String getName() {
            return Name;
        }

        @JsonProperty("Name")
        public void setName(String name) {
            Name = name;
        }

        public String getImageUrl() {
            return ImageUrl;
        }

        @JsonProperty("ImageUrl")
        public void setImageUrl(String imageUrl) {
            ImageUrl = imageUrl;
        }

        public String getHomepage() {
            return Homepage;
        }

        @JsonProperty("Homepage")
        public void setHomepage(String homepage) {
            Homepage = homepage;
        }

        public String getDescription() {
            return Description;
        }

        @JsonProperty("Description")
        public void setDescription(String description) {
            Description = description;
        }

        public int getType() {
            return Type;
        }

        @JsonProperty("Type")
        public void setType(int type) {
            Type = type;
        }

        public int getFollowerCount() {
            return FollowerCount;
        }

        @JsonProperty("FollowerCount")
        public void setFollowerCount(int followerCount) {
            FollowerCount = followerCount;
        }

        public int getFollowingCount() {
            return FollowingCount;
        }

        @JsonProperty("FollowingCount")
        public void setFollowingCount(int followingCount) {
            FollowingCount = followingCount;
        }

        public boolean isFollowed() {
            return IsFollowed;
        }

        @JsonProperty("Followed")
        public void setFollowed(boolean followed) {
            IsFollowed = followed;
        }
    }
}
