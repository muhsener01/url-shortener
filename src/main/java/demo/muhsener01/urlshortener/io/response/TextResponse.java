package demo.muhsener01.urlshortener.io.response;

import lombok.Data;

@Data
public class TextResponse {

    private String code;
    private String text;

    public TextResponse(String code, String text) {
        this.code = code;
        this.text = text;
    }
}
