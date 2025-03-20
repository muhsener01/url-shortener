package demo.muhsener01.urlshortener.service.impl;

import demo.muhsener01.urlshortener.repository.ShortenedUrlRepository;
import demo.muhsener01.urlshortener.service.Engine;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class HashingEngine implements Engine {


    private final ShortenedUrlRepository shortenedUrlRepository;


    @Override
    public String generateUniqueKey(String longUrl) {
        String hash = hash(longUrl + System.currentTimeMillis()).substring(0, 7);
        while (shortenedUrlRepository.existsByShortenCode(hash)) {
            hash = hash(hash + UUID.randomUUID()).substring(0, 7);
        }

        return hash;
    }


    private static String hash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(input.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

}
