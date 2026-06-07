package com.creatorscatalog.dto;

// DTO для реєстрації / входу Creator
public class CreatorDto {

    public static class CreateRequest {
        public int id;
        public String username;
        public String email;
        public String password;
    }

    public static class LoginRequest {
        public String email;
        public String password;
    }

    public static class VerifyRequest {
        public String code;
    }

    public static class CreatorResponse {
        public int id;
        public String username;
        public String email;
        public boolean verified;
        public String status;
        public int portfolioWorksCount;

        public CreatorResponse(int id, String username, String email,
                               boolean verified, String status, int portfolioWorksCount) {
            this.id = id;
            this.username = username;
            this.email = email;
            this.verified = verified;
            this.status = status;
            this.portfolioWorksCount = portfolioWorksCount;
        }
    }
}
