package qa.guru.rococo.model.allure;

import com.fasterxml.jackson.annotation.JsonProperty;

public record EncodedAllureResult(@JsonProperty("content_base64") String contentBase64,
                                  @JsonProperty("file_name") String fileName) {
}