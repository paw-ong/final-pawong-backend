package kr.co.pawong.pwbe.infrastructure.messaging.application.port.out;

public interface MessagePublishPort {

    void publishMessage(String topic, Object message);
}
