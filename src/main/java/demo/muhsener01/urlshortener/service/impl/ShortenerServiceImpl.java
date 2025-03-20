package demo.muhsener01.urlshortener.service.impl;

import demo.muhsener01.urlshortener.ApplicationProperties;
import demo.muhsener01.urlshortener.command.ShortenCommand;
import demo.muhsener01.urlshortener.command.response.ShorteningResponse;
import demo.muhsener01.urlshortener.domain.entity.ShortenedUrl;
import demo.muhsener01.urlshortener.domain.entity.Text;
import demo.muhsener01.urlshortener.domain.entity.expiration.ExpirationPolicy;
import demo.muhsener01.urlshortener.domain.factory.ExpirationPolicyFactory;
import demo.muhsener01.urlshortener.exception.NoSuchUrlFoundException;
import demo.muhsener01.urlshortener.exception.UrlExpiredException;
import demo.muhsener01.urlshortener.repository.ShortenedUrlRepository;
import demo.muhsener01.urlshortener.repository.TextRepository;
import demo.muhsener01.urlshortener.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service("shortenerService")
@RequiredArgsConstructor
public class ShortenerServiceImpl implements ShortenerService {

    private final ShortenedUrlRepository urlRepository;
    private final SecurityOperations securityOperations;
    private final Engine hashingEngine;
    private final ApplicationProperties applicationProperties;
    private final UrlValidatorService urlValidatorService;
    private final TextRepository textRepository;
    private final CacheService<String, ShortenedUrl> cacheService;

    @Override
    @Transactional
    public ShorteningResponse shortenUrl(ShortenCommand command) {
        UUID userId = securityOperations.getAuthenticatedUserId();

        String longUrl = command.getInput().trim();

        longUrl = urlValidatorService.validateAndFormatUrl(longUrl);

        String code = hashingEngine.generateUniqueKey(longUrl);


        ExpirationPolicy expirationPolicy = ExpirationPolicyFactory.create(command.getExpirationPolicy(), command.getAfterHours());

        ShortenedUrl shortenedUrl = new ShortenedUrl(userId, longUrl, code, expirationPolicy);
        urlRepository.save(shortenedUrl);

        return new ShorteningResponse(shortenedUrl.getId(), applicationProperties.getBaseDomain() + "/" + code);
    }

    @Override
    @Transactional
    public ShorteningResponse shortenText(ShortenCommand command) {
        UUID userId = securityOperations.getAuthenticatedUserId();
        String text = command.getInput();

        String code = hashingEngine.generateUniqueKey(text);

        ExpirationPolicy expirationPolicy = ExpirationPolicyFactory.create(command.getExpirationPolicy(), command.getAfterHours());

        String shortLink = applicationProperties.getBaseDomain() + "/" + code;
        String originalUrl = applicationProperties.getBaseDomain() + "/text" + "/" + code;
        ShortenedUrl shortenedUrl = new ShortenedUrl(userId, originalUrl, code, expirationPolicy);
        urlRepository.save(shortenedUrl);
        textRepository.save(new Text(code, text));

        return new ShorteningResponse(shortenedUrl.getId(), shortLink);
    }


    @Override
    @Transactional(noRollbackFor = UrlExpiredException.class)
    public String resolve(String shortenCode) {
        String cacheKey = "urls:" + shortenCode;

        ShortenedUrl url = cacheService.get(cacheKey);

        if (url == null) {
            url = urlRepository.findByShortenCode(shortenCode);
            cacheService.set(cacheKey, url, 60);
        }


        if (url == null)
            throw new NoSuchUrlFoundException("shortenCode", shortenCode);

        try {
            url.resolve();
        } catch (UrlExpiredException e) {
            urlRepository.save(url);
            cacheService.delete(cacheKey);
            throw e;
        }

        return url.getOriginalUrl();
    }


}
