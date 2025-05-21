package kr.co.pawong.pwbe.test.application.port.out;

public interface TestMessagePublisherPort {

    void publishMessage(String topic, String message);
}
