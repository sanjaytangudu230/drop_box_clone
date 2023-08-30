package com.drop_box_clone.constants;

public interface APIMappings {
    String BASE_URL = "/dropBoxClone";

    interface UserEndpoints {
        String USER = "/user";
        String SIGNUP = "/signup";
        String LOGIN = "/login";

    }

    interface FileEndpoints {
        String HEALTH_CHECK = "/healthCheck";
        String FILES = "/files";
        String UPLOAD = "/upload";
        String FILE_ID = "/{fileId}";
    }
}
