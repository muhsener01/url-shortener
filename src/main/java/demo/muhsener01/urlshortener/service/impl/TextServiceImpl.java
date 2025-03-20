package demo.muhsener01.urlshortener.service.impl;

import demo.muhsener01.urlshortener.domain.entity.LinkStatus;
import demo.muhsener01.urlshortener.domain.entity.ShortenedUrl;
import demo.muhsener01.urlshortener.domain.entity.Text;
import demo.muhsener01.urlshortener.exception.TextNotFoundException;
import demo.muhsener01.urlshortener.repository.ShortenedUrlRepository;
import demo.muhsener01.urlshortener.repository.TextRepository;
import demo.muhsener01.urlshortener.service.CacheService;
import demo.muhsener01.urlshortener.service.TextService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TextServiceImpl implements TextService {


    private final TextRepository textRepository;
    private final ShortenedUrlRepository urlRepository;
    private final CacheService<String, Text> textCacheService;
    private final CacheService<String, ShortenedUrl> urlCacheService;


    @Override
    @Transactional
    public String getText(String code) {
        String urlCacheKey = "urls:" + code;
        ShortenedUrl shortenedUrl = urlCacheService.get(urlCacheKey);

        if (shortenedUrl != null) {
            if (shortenedUrl.isActive())
                return getTextFromCacheOrDatabase(code).getText();
            else
                throw new TextNotFoundException("No such active text found with provided code: " + code);

        } else {
            if (urlRepository.existsByShortenCodeAndStatus(code, LinkStatus.ACTIVE))
                return getTextFromCacheOrDatabase(code).getText();
            else
                throw new TextNotFoundException("No such active text found with provided code: " + code);
        }

    }


    private Text getTextFromCacheOrDatabase(String code) {
        String textCacheKey = "texts:" + code;
        Text text = textCacheService.get(textCacheKey);
        if (text != null)
            return text;

        text = textRepository.findById(code).orElseThrow(() -> new TextNotFoundException("id", code));
        textCacheService.set(textCacheKey, text, 60);

        return text;
    }

}
