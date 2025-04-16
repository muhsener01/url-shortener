package demo.muhsener01.urlshortener.service.impl;

import demo.muhsener01.urlshortener.command.ShortenCommand;
import demo.muhsener01.urlshortener.command.UpdateLinkCommand;
import demo.muhsener01.urlshortener.command.response.ShorteningResponse;
import demo.muhsener01.urlshortener.domain.entity.Link;
import demo.muhsener01.urlshortener.domain.entity.expiration.ExpirationPolicy;
import demo.muhsener01.urlshortener.domain.enums.LinkStatus;
import demo.muhsener01.urlshortener.domain.enums.LinkType;
import demo.muhsener01.urlshortener.domain.factory.ExpirationPolicyFactory;
import demo.muhsener01.urlshortener.exception.LinkNotFoundException;
import demo.muhsener01.urlshortener.exception.NoPermissionException;
import demo.muhsener01.urlshortener.shared.dto.GetAllLinkResponse;
import demo.muhsener01.urlshortener.shared.dto.ResolveResponse;
import demo.muhsener01.urlshortener.mapper.LinkMapper;
import demo.muhsener01.urlshortener.repository.UrlRepository;
import demo.muhsener01.urlshortener.security.UserPrincipal;
import demo.muhsener01.urlshortener.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class LinkServiceImpl implements LinkService {
    private final UrlRepository urlRepository;
    private final SecurityOperations securityOperations;
    private final CacheService<String, Link> urlCacheService;
    private final LinkMapper linkMapper;
    private final UrlValidator urlValidator;
    private final Engine hashingEngine;
    private final MinioService minioService;
    private final ApplicationEventPublisher applicationEventPublisher;


    @Override
    @Transactional(readOnly = true)
    public GetAllLinkResponse findAllOfAuthenticatedUser(int page, int limit) {

        UUID authenticatedUserId = securityOperations.getAuthenticatedUserId();


        return urlRepository.findAllByUserIdIfNotRemoved(authenticatedUserId, page, limit);


    }

    @Override
    @Transactional
    public Link deleteById(String urlCode) {


        Link link = urlRepository.findByIdIfNotRemoved(urlCode)
                .orElseThrow(() -> new LinkNotFoundException("id", urlCode));

        if (!securityOperations.isUrlOwnerOrAdmin(link)) {
            throw new NoPermissionException("No permission to delete link with ID: " + urlCode);
        }

        link.remove();

        return urlRepository.update(link);
    }

    @Transactional
    public Link update(UpdateLinkCommand command, String code) {

        Link link = urlRepository.findByIdIfNotRemoved(code)
                .orElseThrow(() -> new LinkNotFoundException("id", code));

        if (!securityOperations.isUrlOwnerOrAdmin(link)) {
            throw new NoPermissionException("No permission to update link with code: " + code);
        }


        linkMapper.merge(command, link);
        urlRepository.update(link);
        return link;
    }


    @Override
    @Transactional
    public ShorteningResponse shortenImage(ShortenCommand command, MultipartFile multipartFile) {
        UserPrincipal userPrincipal = securityOperations.getAuthenticatedPrincipal();

        String uniqueKey = hashingEngine.generateUniqueKey(multipartFile.getOriginalFilename());

        minioService.putObject(uniqueKey, multipartFile);

        ExpirationPolicy expirationPolicy = ExpirationPolicyFactory.create(command.getExpirationPolicy(), command.getAfterHours());

        Link link = new Link(uniqueKey, userPrincipal.getId(), userPrincipal.getEmail(), multipartFile.getOriginalFilename(), expirationPolicy, LinkType.IMAGE);

        urlRepository.save(link);

        return new ShorteningResponse(command.getBaseDomain() + "/" + uniqueKey);

    }

    @Override
    public ShorteningResponse shorten(String shortenType, ShortenCommand command) {
        UserPrincipal userPrincipal = securityOperations.getAuthenticatedPrincipal();

        String content = "";
        LinkType linkType = null;
        if (shortenType.equalsIgnoreCase("url")) {
            content = urlValidator.validateAndFormatUrl(command.getInput());
            linkType = LinkType.URL;
        } else if (shortenType.equalsIgnoreCase("text")) {
            content = command.getInput();
            linkType = LinkType.TEXT;
        } else {
            throw new RuntimeException("Unknown shorten type: " + shortenType);
        }

        ExpirationPolicy expirationPolicy = ExpirationPolicyFactory.create(command.getExpirationPolicy(), command.getAfterHours());
        Link link = new Link(userPrincipal.getId(), userPrincipal.getEmail(), content, expirationPolicy, linkType);
        Link savedUrl = urlRepository.generateUniqueKeyAndSave(link);
        String shortLink = command.getBaseDomain() + "/" + savedUrl.getId();
        return new ShorteningResponse(shortLink);
    }


    @Transactional
    public ResolveResponse resolve(String shortenCode) {
        String cacheKey = "urls:" + shortenCode;
        Link url = urlCacheService.get(cacheKey);

        if (url == null) {
            url = urlRepository.findById(shortenCode).orElseThrow(() -> new LinkNotFoundException("id", shortenCode));
            urlCacheService.set(cacheKey, url, 60);
        }

        boolean resolved = url.resolve();


        String currentStatus = "";
        String content = "";
        if (resolved) {
            currentStatus = LinkStatus.ACTIVE.name();

            if (url.isImage())
                content = minioService.getPreSignedUrl(url.getId());
            else
                content = url.getContent();


        } else {
            currentStatus = url.getStatus().name();
        }

        ResolveResponse response = new ResolveResponse(url.getLinkType().name(), content, currentStatus, url.getCreatorEmail());

        url.getEvents().forEach(applicationEventPublisher::publishEvent);
        url.clearEvents();

        return response;
    }
}
