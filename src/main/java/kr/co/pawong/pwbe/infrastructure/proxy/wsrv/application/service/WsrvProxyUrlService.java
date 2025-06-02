package kr.co.pawong.pwbe.infrastructure.proxy.wsrv.application.service;

import kr.co.pawong.pwbe.infrastructure.proxy.wsrv.application.port.in.ProxyUrlUseCase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class WsrvProxyUrlService implements ProxyUrlUseCase {

    @Value("${app.image.proxy.enabled:true}")
    private boolean proxyEnabled;

    @Value("${app.image.proxy.base-url}")
    private String proxyBaseUrl;

    public String generateProxyUrl(String originalUrl) {
        if (!proxyEnabled || originalUrl == null || originalUrl.isEmpty()) {
            return originalUrl;
        }

        // HTTP URL이 아니면 그대로 반환
        if (!originalUrl.startsWith("http://")) {
            return originalUrl;
        }

        // wsrv.nl을 통한 프록시 URL 생성
        return UriComponentsBuilder.fromHttpUrl(proxyBaseUrl)
                .queryParam("url", originalUrl)
                .queryParam("default", originalUrl) // 오류시 원본 이미지 표시
                .queryParam("l", 9) // PNG 최대 압축
                .queryParam("af", "") // PNG 파일 크기 감소
                .queryParam("il", "") // JPEG/GIF 최적화
                .queryParam("n", -1) // WebP/GIF 최적화
                .build()
                .toUriString();
    }
}