package demo.muhsener01.urlshortener.io.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OperationResponse {

    private String operation;
    private boolean status;
}
