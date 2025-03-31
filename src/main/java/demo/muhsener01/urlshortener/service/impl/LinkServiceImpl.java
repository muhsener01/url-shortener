package demo.muhsener01.urlshortener.service.impl;

import demo.muhsener01.urlshortener.ApplicationProperties;
import demo.muhsener01.urlshortener.command.ShortenCommand;
import demo.muhsener01.urlshortener.command.UpdateLinkCommand;
import demo.muhsener01.urlshortener.command.response.ShorteningResponse;
import demo.muhsener01.urlshortener.domain.entity.ShortURL;
import demo.muhsener01.urlshortener.domain.entity.Text;
import demo.muhsener01.urlshortener.domain.entity.expiration.ExpirationPolicy;
import demo.muhsener01.urlshortener.domain.entity.expiration.SingleUsePolicy;
import demo.muhsener01.urlshortener.domain.factory.ExpirationPolicyFactory;
import demo.muhsener01.urlshortener.exception.NoPermissionException;
import demo.muhsener01.urlshortener.exception.URLNotFoundException;
import demo.muhsener01.urlshortener.exception.UrlExpiredException;
import demo.muhsener01.urlshortener.mapper.LinkMapper;
import demo.muhsener01.urlshortener.repository.TextRepository;
import demo.muhsener01.urlshortener.repository.UrlRepository;
import demo.muhsener01.urlshortener.repository.jpa.UrlJpaRepository;
import demo.muhsener01.urlshortener.security.UserPrincipal;
import demo.muhsener01.urlshortener.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LinkServiceImpl implements LinkService {

    private final UrlJpaRepository urlRepo;
    private final UrlRepository urlRepository;
    private final SecurityOperations securityOperations;
    private final CacheService<String, ShortURL> urlCacheService;
    private final LinkMapper linkMapper;
    private final UrlValidator urlValidator;
    private final ApplicationProperties applicationProperties;
    private final Engine hashingEngine;
    private final TextRepository textRepository;


    public ShortURL findById(String code) {
        UUID userId = securityOperations.getAuthenticatedUserId();


        ShortURL shortURL = urlRepository.findById(code).orElseThrow(
                () -> new URLNotFoundException("id", code));

        if (!userId.equals(shortURL.getUserId())) {
            throw new NoPermissionException("User has no permission to see link");
        }

        return shortURL;

    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.id")
    public List<ShortURL> findAllByUserId(UUID userId, int page, int limit) {
        assert page >= 0;
        assert limit >= 0;

        Pageable pageable = PageRequest.of(page, limit);

        return urlRepo.findAllByUserId(userId, pageable);

    }

    @Override
    @Transactional
    public ShortURL deleteById(String urlCode) {
        UserPrincipal userPrincipal = securityOperations.getAuthenticatedPrincipal();
        ShortURL shortURL = urlRepository.findById(urlCode).orElseThrow(() -> new URLNotFoundException("id", urlCode));

        if (!shortURL.getUserId().equals(userPrincipal.getId()) && !userPrincipal.isAdmin()) {
            throw new NoPermissionException("No permission to delete link with ID: " + urlCode);
        }

        shortURL.remove();

        urlCacheService.delete("urls:" + shortURL.getId());

        return urlRepository.update(shortURL);
    }

    @Transactional
    public ShortURL update(UpdateLinkCommand command, String code) {
        UserPrincipal userPrincipal = securityOperations.getAuthenticatedPrincipal();

        ShortURL shortURL = urlRepository.findById(code).orElseThrow(() -> new URLNotFoundException("id", code));

        if (!userPrincipal.getId().equals(shortURL.getUserId()) && userPrincipal.isAdmin()) {
            throw new NoPermissionException("No permission to update link with code: " + code);
        }


        linkMapper.merge(command, shortURL);
        urlRepository.update(shortURL);
        urlCacheService.delete("urls:" + code);

        return shortURL;
    }

    @Override
    @Transactional
    public ShorteningResponse shortenUrl(ShortenCommand command) {
        UUID userId = securityOperations.getAuthenticatedUserId();

        String longUrl = command.getInput().trim();
        longUrl = urlValidator.validateAndFormatUrl(longUrl);

        ExpirationPolicy expirationPolicy = ExpirationPolicyFactory.create(command.getExpirationPolicy(), command.getAfterHours());

        ShortURL shortURL = new ShortURL(userId, longUrl, expirationPolicy);

        ShortURL savedUrl = urlRepository.generateUniqueKeyAndSave(shortURL);

        return new ShorteningResponse(applicationProperties.getBaseDomain() + "/" + savedUrl.getId());
    }

    @Override
    @Transactional
    public ShorteningResponse shortenText(ShortenCommand command) {
        UUID userId = securityOperations.getAuthenticatedUserId();
        String text = command.getInput();
        String uniqueKey = hashingEngine.generateUniqueKey(text);
        ExpirationPolicy expirationPolicy = ExpirationPolicyFactory.create(command.getExpirationPolicy(), command.getAfterHours());

        String originalUrl = applicationProperties.getBaseDomain() + "/text" + "/" + uniqueKey;
        ShortURL shortURL = new ShortURL(uniqueKey, userId, originalUrl, expirationPolicy);

        String shortLink = applicationProperties.getBaseDomain() + "/" + uniqueKey;

        urlRepository.save(shortURL);
        textRepository.save(new Text(uniqueKey, text));

        return new ShorteningResponse(shortLink);
    }

    @Transactional(noRollbackFor = UrlExpiredException.class)
    public String resolve(String shortenCode) {
        String cacheKey = "urls:" + shortenCode;

        ShortURL url = urlCacheService.get(cacheKey);

        if (url == null) {
            url = urlRepository.findById(shortenCode).orElseThrow(() -> new URLNotFoundException("id", shortenCode));
            urlCacheService.set(cacheKey, url, 60);
        }

        try {
            url.resolve();
            if (url.getExpirationPolicy() instanceof SingleUsePolicy singleUsePolicy) {
                urlCacheService.delete(cacheKey);
            }
        } catch (UrlExpiredException e) {
            urlRepository.update(url);
            urlCacheService.delete(cacheKey);
            throw e;
        }

        return url.getOriginalUrl();
    }
}
