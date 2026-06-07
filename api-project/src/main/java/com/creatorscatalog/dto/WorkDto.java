package com.creatorscatalog.dto;

import java.util.List;

public class WorkDto {

    public static class CreateRequest {
        public int workId;
        public String title;
        public String description;
        public String imageUrl;
        public List<String> tags;
    }

    public static class WorkResponse {
        public int workId;
        public String title;
        public String description;
        public String imageUrl;
        public List<String> tags;

        public WorkResponse(int workId, String title, String description,
                            String imageUrl, List<String> tags) {
            this.workId = workId;
            this.title = title;
            this.description = description;
            this.imageUrl = imageUrl;
            this.tags = tags;
        }
    }
}
