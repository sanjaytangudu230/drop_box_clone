package com.drop_box_clone.dto.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CreateFileResponse {
    @JsonProperty("file_id")
    private String fileId;
}
