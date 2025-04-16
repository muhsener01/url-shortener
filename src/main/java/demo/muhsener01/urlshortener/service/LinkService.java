package demo.muhsener01.urlshortener.service;

import demo.muhsener01.urlshortener.command.ShortenCommand;
import demo.muhsener01.urlshortener.command.UpdateLinkCommand;
import demo.muhsener01.urlshortener.command.response.ShorteningResponse;
import demo.muhsener01.urlshortener.domain.entity.Link;
import demo.muhsener01.urlshortener.shared.dto.GetAllLinkResponse;
import demo.muhsener01.urlshortener.shared.dto.ResolveResponse;
import org.springframework.web.multipart.MultipartFile;

public interface LinkService {


    GetAllLinkResponse findAllOfAuthenticatedUser(int page, int limit);

    Link deleteById(String urlCode);

    Link update(UpdateLinkCommand command, String code);


    ShorteningResponse shortenImage(ShortenCommand command, MultipartFile multipartFile);

    ShorteningResponse shorten(String shortenType, ShortenCommand shortenCommand);


    ResolveResponse resolve(String shortenCode);
}
