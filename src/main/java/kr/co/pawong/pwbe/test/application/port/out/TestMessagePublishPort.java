package kr.co.pawong.pwbe.test.application.port.out;

public interface TestMessagePublishPort {

    void publishMessage(String topic, String message);
}
