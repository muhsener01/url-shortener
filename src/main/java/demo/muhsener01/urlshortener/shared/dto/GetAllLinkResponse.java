package demo.muhsener01.urlshortener.shared.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Data
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class GetAllLinkResponse {

    private List<LinkResponse> data;
    private long totalRecords;
    private int page;
    private int limit;
    private int totalPages;
}
