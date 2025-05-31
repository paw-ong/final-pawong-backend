package kr.co.pawong.pwbe.adoption.adapter.out.proxy;

import kr.co.pawong.pwbe.adoption.application.port.out.ProxyUrlPort;
import kr.co.pawong.pwbe.infrastructure.proxy.wsrv.application.port.in.ProxyUrlUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProxyUrlAdapter implements ProxyUrlPort {

    private final ProxyUrlUseCase proxyUrlUseCase;

    @Override
    public String generateProxyUrl(String url) {
        return proxyUrlUseCase.generateProxyUrl(url);
    }
}
