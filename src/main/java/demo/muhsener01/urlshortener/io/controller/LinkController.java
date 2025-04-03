package demo.muhsener01.urlshortener.io.controller;

import demo.muhsener01.urlshortener.command.ShortenCommand;
import demo.muhsener01.urlshortener.command.UpdateLinkCommand;
import demo.muhsener01.urlshortener.command.response.ShorteningResponse;
import demo.muhsener01.urlshortener.domain.entity.ShortURL;
import demo.muhsener01.urlshortener.io.response.LinkResponse;
import demo.muhsener01.urlshortener.io.response.ResolveResponse;
import demo.muhsener01.urlshortener.mapper.LinkMapper;
import demo.muhsener01.urlshortener.service.LinkService;
import demo.muhsener01.urlshortener.service.SecurityOperations;
import demo.muhsener01.urlshortener.utils.JsonUtils;
import io.minio.MinioClient;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/links")
@RequiredArgsConstructor
public class LinkController {

    private final LinkService linkService;
    private final LinkMapper linkMapper;
    private final SecurityOperations securityOperations;
    private MinioClient minioClient;


//    @PostMapping(value = "/test",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public void test(@RequestParam("file") MultipartFile multipartFile){
//        System.out.println("Name: " + multipartFile.getOriginalFilename());
//        System.out.println("Size: " + multipartFile.getSize());
//        System.out.println("Type: " + multipartFile.getContentType());
//
//
//    }


    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ShorteningResponse> shorten(@RequestParam("type") String inputType, @RequestParam("body") String bodyJson,
                                                      @RequestParam(value = "file", required = false) MultipartFile multipartFile) {

        ShortenCommand command = JsonUtils.convertToObject(bodyJson, ShortenCommand.class);


        ShorteningResponse response = null;
        if (inputType.equalsIgnoreCase("url")) {
            response = linkService.shortenUrl(command);
        } else if (inputType.equalsIgnoreCase("text")) {
            response = linkService.shortenText(command);
        } else if (inputType.equalsIgnoreCase("image")) {
            response = linkService.shortenImage(command, multipartFile);
        } else {
            throw new IllegalArgumentException("Invalid operation type: " + inputType);
        }


        return ResponseEntity.ok(response);
    }


//    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<ShorteningResponse> shorten(@RequestParam("type") String inputType, @RequestBody ShortenCommand command, @RequestParam(value = "file", required = false) MultipartFile multipartFile) {
//        ShorteningResponse response = null;
//        if (inputType.equalsIgnoreCase("url")) {
//            response = linkService.shortenUrl(command);
//        } else if (inputType.equalsIgnoreCase("text")) {
//            response = linkService.shortenText(command);
//        } else if (inputType.equalsIgnoreCase("image")) {
//            String originalFilename = multipartFile.getOriginalFilename();
//            String contentType = multipartFile.getContentType();
//            long fileSize = multipartFile.getSize();
//
//            System.out.println("OriginalFileName: " + originalFilename);
//            System.out.println("contentType: " + contentType);
//            System.out.println("fileSize: " + fileSize);
//
//
//        } else {
//            throw new IllegalArgumentException("Invalid operation type: " + inputType);
//        }
//
//
//        return ResponseEntity.ok(response);
//    }


    @GetMapping("/{linkId}")
    public ResponseEntity<LinkResponse> findById(@PathVariable(name = "linkId") String code) {


        ShortURL url = linkService.findById(code);

        LinkResponse response = linkMapper.toResponse(url);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{linkId}/resolve")
    public ResponseEntity<ResolveResponse> resolve(@PathVariable(name = "linkId") String code) {


        ResolveResponse resolveResponse = linkService.resolve2(code);

        return ResponseEntity.ok(resolveResponse);
    }

    @PutMapping("/{code}")
    public ResponseEntity<LinkResponse> update(@PathVariable(name = "code") String code, @RequestBody UpdateLinkCommand command) {


        ShortURL url = linkService.update(command, code);
        LinkResponse response = linkMapper.toResponse(url);

        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/{linkId}")
    public ResponseEntity<LinkResponse> deleteById(@PathVariable("linkId") String urlCode) {
        ShortURL deleted = linkService.deleteById(urlCode);

        return ResponseEntity.ok().body(linkMapper.toResponse(deleted));
    }


    @GetMapping
    public ResponseEntity<List<LinkResponse>> findAllByUserId(@PositiveOrZero @RequestParam(name = "page", defaultValue = "0") int page, @PositiveOrZero @RequestParam(name = "limit", defaultValue = "10") int limit) {


        List<ShortURL> allByUserId = linkService.findAllByUserId(page, limit);


        return ResponseEntity.ok().body(linkMapper.toResponse(allByUserId));
    }


}
