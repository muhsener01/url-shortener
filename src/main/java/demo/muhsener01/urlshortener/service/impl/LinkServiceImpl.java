package demo.muhsener01.urlshortener.service.impl;

import demo.muhsener01.urlshortener.domain.entity.ShortenedUrl;
import demo.muhsener01.urlshortener.exception.NoPermissionException;
import demo.muhsener01.urlshortener.exception.NoSuchUrlFoundException;
import demo.muhsener01.urlshortener.repository.ShortenedUrlRepository;
import demo.muhsener01.urlshortener.security.UserPrincipal;
import demo.muhsener01.urlshortener.service.CacheService;
import demo.muhsener01.urlshortener.service.LinkService;
import demo.muhsener01.urlshortener.service.SecurityOperations;
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

    private final ShortenedUrlRepository urlRepo;
    private final SecurityOperations securityOperations;
    private final CacheService<String, ShortenedUrl> urlCacheService;


    public ShortenedUrl findById(UUID linkId) {
        UUID userId = securityOperations.getAuthenticatedUserId();

        ShortenedUrl shortenedUrl = urlRepo.findById(linkId).orElseThrow(
                () -> new NoSuchUrlFoundException("id", linkId));

        if (!userId.equals(shortenedUrl.getUserId())) {
            throw new NoPermissionException("User has no permission to see link");
        }

        return shortenedUrl;

    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.id")
    public List<ShortenedUrl> findAllByUserId(UUID userId, int page, int limit) {
        assert page >= 0;
        assert limit >= 0;

        Pageable pageable = PageRequest.of(page, limit);

        return urlRepo.findAllByUserId(userId, pageable);

    }

    @Override
    @Transactional
    public ShortenedUrl deleteById(UUID linkId) {
        UserPrincipal userPrincipal = securityOperations.getAuthenticatedPrincipal();
        ShortenedUrl shortenedUrl = urlRepo.findById(linkId).orElseThrow(() -> new NoSuchUrlFoundException("id", linkId));

        if (!shortenedUrl.getUserId().equals(userPrincipal.getId()) && !userPrincipal.isAdmin()) {
            throw new NoPermissionException("No permission to delete link with ID: " + linkId);
        }

        shortenedUrl.remove();

        urlCacheService.delete("urls:" + shortenedUrl.getShortenCode());

        return urlRepo.save(shortenedUrl);
    }
}
