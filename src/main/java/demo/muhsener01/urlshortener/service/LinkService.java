package demo.muhsener01.urlshortener.service;

import demo.muhsener01.urlshortener.command.ShortenCommand;
import demo.muhsener01.urlshortener.command.UpdateLinkCommand;
import demo.muhsener01.urlshortener.command.response.ShorteningResponse;
import demo.muhsener01.urlshortener.domain.entity.ShortURL;
import demo.muhsener01.urlshortener.io.response.ResolveResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface LinkService {

    ShortURL findById(String code);

    List<ShortURL> findAllByUserId(int page, int limit);

    ShortURL deleteById(String urlCode);

    ShortURL update(UpdateLinkCommand command, String code);

    ShorteningResponse shortenUrl(ShortenCommand command);

    ShorteningResponse shortenText(ShortenCommand command);

    ShorteningResponse shortenImage(ShortenCommand command , MultipartFile multipartFile);


    String resolve(String shortenCode);

    ResolveResponse resolve2(String shortenCode);
}
