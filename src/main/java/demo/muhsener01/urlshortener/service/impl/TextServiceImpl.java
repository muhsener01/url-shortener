package demo.muhsener01.urlshortener.service.impl;

import demo.muhsener01.urlshortener.domain.enums.LinkStatus;
import demo.muhsener01.urlshortener.domain.entity.ShortURL;
import demo.muhsener01.urlshortener.domain.entity.Text;
import demo.muhsener01.urlshortener.exception.TextNotFoundException;
import demo.muhsener01.urlshortener.repository.jpa.UrlJpaRepository;
import demo.muhsener01.urlshortener.repository.jpa.TextJpaRepository;
import demo.muhsener01.urlshortener.service.CacheService;
import demo.muhsener01.urlshortener.service.TextService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TextServiceImpl implements TextService {


    private final TextJpaRepository textJpaRepository;
    private final UrlJpaRepository urlRepository;
    private final CacheService<String, Text> textCacheService;
    private final CacheService<String, ShortURL> urlCacheService;


    @Override
    @Transactional
    public String getText(String code) {
        String urlCacheKey = "urls:" + code;
        ShortURL shortURL = urlCacheService.get(urlCacheKey);

        if (shortURL != null) {
            if (shortURL.isActive())
                return getTextFromCacheOrDatabase(code).getText();
            else
                throw new TextNotFoundException("No such active text found with provided code: " + code);

        } else {
            if (urlRepository.existsByIdAndStatus(code, LinkStatus.ACTIVE))
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

        text = textJpaRepository.findById(code).orElseThrow(() -> new TextNotFoundException("id", code));
        textCacheService.set(textCacheKey, text, 60);

        return text;
    }

}
