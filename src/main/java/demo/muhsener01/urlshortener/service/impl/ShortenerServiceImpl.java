package demo.muhsener01.urlshortener.service.impl;

import demo.muhsener01.urlshortener.ApplicationProperties;
import demo.muhsener01.urlshortener.command.ShortenCommand;
import demo.muhsener01.urlshortener.command.response.ShorteningResponse;
import demo.muhsener01.urlshortener.domain.entity.ShortURL;
import demo.muhsener01.urlshortener.exception.UrlExpiredException;
import demo.muhsener01.urlshortener.repository.jpa.UrlJpaRepository;
import demo.muhsener01.urlshortener.repository.jpa.TextJpaRepository;
import demo.muhsener01.urlshortener.repository.TextRepository;
import demo.muhsener01.urlshortener.repository.UrlRepository;
import demo.muhsener01.urlshortener.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("shortenerService")
@RequiredArgsConstructor
public class ShortenerServiceImpl implements ShortenerService {

    private final UrlJpaRepository urlRepository;
    private final SecurityOperations securityOperations;
    private final Engine hashingEngine;
    private final ApplicationProperties applicationProperties;
    private final UrlValidator urlValidator;
    private final TextJpaRepository textJpaRepository;
    private final CacheService<String, ShortURL> cacheService;
    private final TextRepository textRepository;

    // REFACTORING PART
    private final UrlRepository urlRepository2;


    @Override
    @Transactional
    public ShorteningResponse shortenUrl(ShortenCommand command) {
//        UUID userId = securityOperations.getAuthenticatedUserId();
//
//        String longUrl = command.getInput().trim();
//
//        longUrl = urlValidator.validateAndFormatUrl(longUrl);
//
////        String code = hashingEngine.generateUniqueKey(longUrl);
//
//
//        ExpirationPolicy expirationPolicy = ExpirationPolicyFactory.create(command.getExpirationPolicy(), command.getAfterHours());
//
////        ShortenedUrl shortenedUrl = new ShortenedUrl(userId, longUrl, code, expirationPolicy);
//        ShortenedUrl shortenedUrl = new ShortenedUrl(userId, longUrl, expirationPolicy);
//
//
////        urlRepository.save(shortenedUrl);
//        ShortenedUrl shortenedUrl1 = urlRepository2.generateUniqueKeyAndSave(shortenedUrl);
//        return new ShorteningResponse(applicationProperties.getBaseDomain() + "/" + shortenedUrl1.getId());
        return null;
    }

    @Override
    @Transactional
    public ShorteningResponse shortenText(ShortenCommand command) {
//        UUID userId = securityOperations.getAuthenticatedUserId();
//        String text = command.getInput();
//        String uniqueKey = hashingEngine.generateUniqueKey(text);
//        ExpirationPolicy expirationPolicy = ExpirationPolicyFactory.create(command.getExpirationPolicy(), command.getAfterHours());
//
//        String originalUrl = applicationProperties.getBaseDomain() + "/text" + "/" + uniqueKey;
//        ShortenedUrl shortenedUrl = new ShortenedUrl(uniqueKey, userId, originalUrl, expirationPolicy);
////        ShortenedUrl savedUrl = urlRepository2.generateUniqueKeyAndSave(shortenedUrl);
//
//
////        savedUrl.setOriginalUrl(originalUrl);
//        String shortLink = applicationProperties.getBaseDomain() + "/" + uniqueKey;
//
//        urlRepository2.save(shortenedUrl);
//        textRepository.save(new Text(uniqueKey, text));
//
//        return new ShorteningResponse(shortLink);
        return null;
    }


    @Override
    @Transactional(noRollbackFor = UrlExpiredException.class)
    public String resolve(String shortenCode) {
//        String cacheKey = "urls:" + shortenCode;
//
//        ShortenedUrl url = cacheService.get(cacheKey);
//
//        if (url == null) {
//            url = urlRepository2.findById(shortenCode).orElseThrow(() -> new NoSuchUrlFoundException("id", shortenCode));
////            url = urlRepository.findByShortenCode(shortenCode);
//            cacheService.set(cacheKey, url, 60);
//        }
////        if (url == null)
////            throw new NoSuchUrlFoundException("shortenCode", shortenCode);
//
//        try {
//            url.resolve();
//            if (url.getExpirationPolicy() instanceof SingleUsePolicy singleUsePolicy) {
//                cacheService.delete(cacheKey);
//            }
//        } catch (UrlExpiredException e) {
//            urlRepository2.update(url);
//            cacheService.delete(cacheKey);
//            throw e;
//        }
//
//        return url.getOriginalUrl();
        return null;
    }


}
