package kr.co.pawong.pwbe.infrastructure.messaging.application.port.in;

public interface PublishMessageUseCase {

    void publishMessage(String topic, Object message);
}
