package demo.muhsener01.urlshortener.service.impl;

import demo.muhsener01.urlshortener.ApplicationProperties;
import demo.muhsener01.urlshortener.command.ShortenCommand;
import demo.muhsener01.urlshortener.command.UpdateLinkCommand;
import demo.muhsener01.urlshortener.command.response.ShorteningResponse;
import demo.muhsener01.urlshortener.domain.entity.ShortURL;
import demo.muhsener01.urlshortener.domain.entity.Text;
import demo.muhsener01.urlshortener.domain.entity.expiration.ExpirationPolicy;
import demo.muhsener01.urlshortener.domain.entity.expiration.SingleUsePolicy;
import demo.muhsener01.urlshortener.domain.enums.LinkStatus;
import demo.muhsener01.urlshortener.domain.enums.LinkType;
import demo.muhsener01.urlshortener.domain.factory.ExpirationPolicyFactory;
import demo.muhsener01.urlshortener.exception.NoPermissionException;
import demo.muhsener01.urlshortener.exception.TextNotFoundException;
import demo.muhsener01.urlshortener.exception.URLNotFoundException;
import demo.muhsener01.urlshortener.exception.UrlExpiredException;
import demo.muhsener01.urlshortener.io.response.ResolveResponse;
import demo.muhsener01.urlshortener.mapper.LinkMapper;
import demo.muhsener01.urlshortener.repository.TextRepository;
import demo.muhsener01.urlshortener.repository.UrlRepository;
import demo.muhsener01.urlshortener.repository.jpa.TextJpaRepository;
import demo.muhsener01.urlshortener.repository.jpa.UrlJpaRepository;
import demo.muhsener01.urlshortener.security.UserPrincipal;
import demo.muhsener01.urlshortener.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Slf4j
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
    private final TextJpaRepository textJpaRepository;
    private final MinioService minioService;


    public ShortURL findById(String code) {
        UUID userId = securityOperations.getAuthenticatedUserId();


        ShortURL shortURL = urlRepository.findById(code).orElseThrow(() -> new URLNotFoundException("id", code));

        if (!userId.equals(shortURL.getUserId())) {
            throw new NoPermissionException("User has no permission to see link");
        }

        return shortURL;

    }

    @Override
    @Transactional(readOnly = true)
//    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.id")
    public List<ShortURL> findAllByUserId(int page, int limit) {

        UUID authenticatedUserId = securityOperations.getAuthenticatedUserId();


        assert page >= 0;
        assert limit >= 0;

        Pageable pageable = PageRequest.of(page, limit);

        return urlRepo.findAllByUserId(authenticatedUserId, pageable).stream().filter(shortURL -> !shortURL.isRemoved()).toList();

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

        ShortURL shortURL = new ShortURL(userId, longUrl, expirationPolicy, LinkType.URL);
        ShortURL savedUrl = urlRepository.generateUniqueKeyAndSave(shortURL);


//        String shortLink = applicationProperties.getBaseDomain() + "/" + savedUrl.getId();
        String shortLink = command.getBaseDomain() + "/" + savedUrl.getId();

        log.info("New link created: id: {} longUrl: {}  by User: {}", savedUrl.getId(), savedUrl.getOriginalUrl(), userId);
        return new ShorteningResponse(shortLink);
    }

    @Override
    @Transactional
    public ShorteningResponse shortenText(ShortenCommand command) {
        UUID userId = securityOperations.getAuthenticatedUserId();
        String textInput = command.getInput();
        String uniqueKey = hashingEngine.generateUniqueKey(textInput);
        ExpirationPolicy expirationPolicy = ExpirationPolicyFactory.create(command.getExpirationPolicy(), command.getAfterHours());

//        String originalUrl = applicationProperties.getBaseDomain() + "/text" + "/" + uniqueKey;
//        String originalUrl = command.getBaseDomain() + "/" + uniqueKey;
        ShortURL shortURL = new ShortURL(uniqueKey, userId, "", expirationPolicy, LinkType.TEXT);


        Text text = new Text(uniqueKey, textInput);

        urlRepository.save(shortURL);
        textRepository.save(text);

        String shortLink = command.getBaseDomain() + "/" + uniqueKey;
        return new ShorteningResponse(shortLink);
    }

    @Override
    public ShorteningResponse shortenImage(ShortenCommand command, MultipartFile multipartFile) {
        UUID userId = securityOperations.getAuthenticatedUserId();

        String uniqueKey = hashingEngine.generateUniqueKey(multipartFile.getOriginalFilename());

        String publicUrl = minioService.putObject(multipartFile);

        log.info("New image with code: {} has been shortened by User: {}", uniqueKey, userId);

        ExpirationPolicy expirationPolicy = ExpirationPolicyFactory.create(command.getExpirationPolicy(), command.getAfterHours());

        ShortURL shortURL = new ShortURL(uniqueKey, userId, publicUrl, expirationPolicy, LinkType.IMAGE);

        ShortURL save = urlRepository.save(shortURL);

        return new ShorteningResponse(command.getBaseDomain() + "/" + uniqueKey);


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

    @Transactional(noRollbackFor = UrlExpiredException.class)
    public ResolveResponse resolve2(String shortenCode) {


        String cacheKey = "urls:" + shortenCode;
        ShortURL url = urlCacheService.get(cacheKey);

        if (url == null) {
            url = urlRepository.findById(shortenCode).orElseThrow(() -> new URLNotFoundException("id", shortenCode));
            urlCacheService.set(cacheKey, url, 60);
        }

        try {
            LinkStatus statusBeforeResolving = url.getStatus();
            url.resolve();
            if (url.getExpirationPolicy() instanceof SingleUsePolicy singleUsePolicy) {
                urlCacheService.delete(cacheKey);
            }

            if (url.getLinkType().equals(LinkType.URL) || url.getLinkType().equals(LinkType.IMAGE)) {
                return new ResolveResponse(url.getLinkType().name(), url.getOriginalUrl(), "", statusBeforeResolving.name());
            } else {
                ShortURL finalUrl = url;
                Text text = textJpaRepository.findById(url.getId()).orElseThrow(
                        () -> new TextNotFoundException("id", finalUrl.getId())
                );

                return new ResolveResponse(LinkType.TEXT.name(), "", text.getText(), statusBeforeResolving.name());
            }


        } catch (UrlExpiredException e) {
            urlRepository.update(url);
            urlCacheService.delete(cacheKey);
            throw e;
        }

//        return url.getOriginalUrl();
    }
}
