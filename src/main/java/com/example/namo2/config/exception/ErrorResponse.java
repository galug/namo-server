package capstone.bapool.config.error;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ErrorResponse {
    private String message;
    private int code;

    @Builder
    public ErrorResponse(String message, int code) {
        this.message = message;
        this.code = code;
    }
}
