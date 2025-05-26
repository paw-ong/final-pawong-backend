package kr.co.pawong.pwbe.infrastructure.messaging.adapter.out;

import kr.co.pawong.pwbe.Notification.application.service.dto.NotificationDto;
import kr.co.pawong.pwbe.global.error.errorcode.CustomErrorCode;
import kr.co.pawong.pwbe.global.error.exception.BaseException;
import kr.co.pawong.pwbe.infrastructure.messaging.application.port.out.MessagePublishPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaMessagePublisher implements MessagePublishPort {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    // 알림 메시지를 발행할 kafka 토픽 이름
    @Value("${kafka.topic.notification}")
    private String notificationTopic;

    @Override
    public void publishMessage(String topic, Object message) {
        kafkaTemplate.send(topic, message);
    }

    @Override
    public void publishFcmNotificationMessage(NotificationDto notificationDto) {
        log.debug("알림 메시지 발행 시작: title={}, adoptionId={}",
                notificationDto.getTitle(), notificationDto.getTargetId());

        try {
            // FCM 토큰을 키로 사용
            String key = notificationDto.getToken();

            // Kafka 토픽으로 메시지 발행 (비동기)
            kafkaTemplate.send(notificationTopic, key, notificationDto)
                    .whenComplete((message, ex) -> {
                        if (ex != null) {
                            SendResult<String, Object> sendResult = message;
                            log.info("메시지 전송 성공: topic={}, partition={}, offset={}, title={}",
                                    sendResult.getRecordMetadata().topic(),
                                    sendResult.getRecordMetadata().partition(),
                                    sendResult.getRecordMetadata().offset(),
                                    notificationDto.getTitle());
                        } else {
                            // 실패 시 에러 처리
                            log.error("메시지 전송 실패: title={}, error={}",
                                    notificationDto.getTitle(), ex.getMessage(), ex);
                            // 비동기 처리에서 예외 발생 시
                            throw new BaseException(CustomErrorCode.KAFKA_MESSAGE_PUBLISH_ERROR);
                        }
                    });
        } catch (Exception e) {
            // Kafka 연결 오류, 직렬화 오류 등 동기적 예외 처리
            log.error("알림 메시지 발행 중 예외 발생: title={}", notificationDto.getTitle(), e);
            throw new BaseException(CustomErrorCode.KAFKA_MESSAGE_PUBLISH_ERROR);
        }
    }
}
