package com.drop_box_clone.dto.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FileMetadataResponse {
    @JsonProperty("file_id")
    private String fileId;
    @JsonProperty("file_name")
    private String fileName;
    @JsonProperty("file_type")
    private String fileType;
    @JsonProperty("created_at")
    private String createdAt;
}
