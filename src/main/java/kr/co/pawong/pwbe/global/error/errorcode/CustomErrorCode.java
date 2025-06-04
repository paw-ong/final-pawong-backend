package kr.co.pawong.pwbe.global.error.errorcode;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CustomErrorCode implements ErrorCode {
    /**
     * 400 BAD_REQUEST
     */
    REQUEST_ERROR(BAD_REQUEST, "입력값이 잘못되었습니다."),
    CHATROOM_POST_ERROR(BAD_REQUEST, "채팅방을 생성할 수 없습니다."),
    FCM_INVALID_TOKEN(BAD_REQUEST, "FCM 토큰이 유효하지 않습니다."),
    FCM_TOKEN_MISSING(BAD_REQUEST, "FCM 토큰이 누락되었습니다."),
    FCM_NOTIFICATION_TITLE_MISSING(BAD_REQUEST, "알림 제목이 누락되었습니다."),
    FCM_NOTIFICATION_MESSAGE_MISSING(BAD_REQUEST, "알림 메시지가 누락되었습니다."),
    FCM_INVALID_JSON_FORMAT(BAD_REQUEST, "알림 JSON 형식이 올바르지 않습니다."),
    EMAIL_INVALID_JSON_FORMAT(BAD_REQUEST, "이메일 JSON 형식이 올바르지 않습니다."),
    NOTIFICATION_INVALID_TOKEN(BAD_REQUEST, "유효한 FCM 토큰을 찾을 수 없습니다."),
    NOTIFICATION_INVALID_REQUEST(BAD_REQUEST, "알림 요청 데이터가 유효하지 않습니다."),
    NOTIFICATION_USER_ID_REQUIRED(BAD_REQUEST, "사용자 ID는 필수입니다."),
    NOTIFICATION_TITLE_REQUIRED(BAD_REQUEST, "알림 제목은 필수입니다."),
    NOTIFICATION_MESSAGE_REQUIRED(BAD_REQUEST, "알림 메시지는 필수입니다."),
    NOTIFICATION_TOKEN_REQUIRED(BAD_REQUEST, "FCM 토큰은 필수입니다."),
    NOTIFICATION_TARGET_ID_REQUIRED(BAD_REQUEST, "대상 ID는 필수입니다."),

    /**
     * 401 UNAUTHORIZED
     */
    TOKEN_NOT_EXIST(UNAUTHORIZED, "토큰이 존재하지 않습니다."),
    TOKEN_INVALIDATE(UNAUTHORIZED, "토큰이 유효하지 않습니다."),
    REFRESH_TOKEN_INVALIDATE(UNAUTHORIZED, "리프레쉬 토큰이 유효하지 않습니다."),
    ACCESS_TOKEN_INVALIDATE(UNAUTHORIZED, "액세스 토큰이 유효하지 않습니다."),
    ACCESS_TOKEN_EXPIRED(UNAUTHORIZED, "액세스 토큰이 만료되었습니다."),
    USERNAME_NOT_FOUND(UNAUTHORIZED, "username 정보로 유저를 찾을 수 없습니다."),

    /**
     * 403 FORBIDDEN
     */
    FORBIDDEN_POST_MODIFY(FORBIDDEN, "게시글 수정 및 삭제 권한이 없습니다."),
    FORBIDDEN_CHATROOMS_ACCESS(FORBIDDEN, "해당 공고의 채팅방들을 조회할 권한이 없습니다"),
    FORBIDDEN_CHATROOM_DEACTIVATION(FORBIDDEN, "해당 채팅방을 비활성화 할 권한이 없습니다."),
    FORBIDDEN_CHATMESSAGE_SENDING(FORBIDDEN, "해당 메시지를 채팅방에 보낼 권한이 없습니다."),
    FORBIDDEN_CHATMESSAGE_QUERY(FORBIDDEN, "해당 채팅방의 메시지들을 조회할 권한이 없습니다."),
    /**
     * 404 NOT_FOUND
     */
    USER_NOT_FOUND(NOT_FOUND, "유저가 존재하지 않습니다."),
    ADOPTION_NOT_FOUND(NOT_FOUND, "유기동물 정보가 없습니다."),
    LOSTPOST_NOT_FOUND(NOT_FOUND, "실종동물 게시글 정보가 없습니다."),
    LOST_NOT_FOUND(NOT_FOUND, "게시글이 존재하지 않습니다."),
    CHATROOM_NOT_FOUND(NOT_FOUND, "채팅방이 존재하지 않습니다."),
    CHATMESSAGE_NOT_FOUND(NOT_FOUND, "채팅 메시지가 존재하지 않습니다."),
    NOTIFICATION_NOT_FOUND(NOT_FOUND, "알림이 존재하지 않습니다."),
    REDIS_KEY_NOT_FOUND(NOT_FOUND,"redis 키가 존재하지 않습니다."),

    /**
     * 500 SERVER_ERROR
     */
    SERVER_ERROR(INTERNAL_SERVER_ERROR, "서버와의 연결에 실패하였습니다."),
    DATABASE_ERROR(INTERNAL_SERVER_ERROR, "데이터베이스 연결에 실패하였습니다."),
    ES_SAVE_ERROR(INTERNAL_SERVER_ERROR, "Elasticsearch 저장에 실패하였습니다."),
    ES_DELETE_ERROR(INTERNAL_SERVER_ERROR, "Elasticsearch에서 삭제하는데 실패하였습니다."),
    KAFKA_MESSAGE_PUBLISH_ERROR(INTERNAL_SERVER_ERROR, "Kafka 메시지 발행에 실패하였습니다."),
    KAFKA_CONNECTION_ERROR(INTERNAL_SERVER_ERROR, "Kafka 서버 연결에 실패하였습니다."),
    NOTIFICATION_SAVE_ERROR(INTERNAL_SERVER_ERROR, "알림 저장에 실패하였습니다."),
    NOTIFICATION_FIND_ERROR(INTERNAL_SERVER_ERROR, "알림 조회에 실패하였습니다."),
    NOTIFICATION_SEND_ERROR(INTERNAL_SERVER_ERROR, "알림 발송에 실패하였습니다."),
    NOTIFICATION_CHAT_SEND_ERROR(INTERNAL_SERVER_ERROR, "채팅 알림 발송에 실패하였습니다."),
    NOTIFICATION_ADOPTION_SEND_ERROR(INTERNAL_SERVER_ERROR, "유사 공고 알림 발송에 실패하였습니다."),
    EMAIL_SEND_FAIL(INTERNAL_SERVER_ERROR, "이메일 전송에 실패하였습니다."),
    REDIS_SAVE_ERROR(INTERNAL_SERVER_ERROR,"REDIS 저장에 실패하였습니다."),
    BROKER_FAIL(INTERNAL_SERVER_ERROR, "브로커 전송이 실패하였습니다."),

    // 검색
    SEARCH_ERROR(SERVICE_UNAVAILABLE, "검색 기능이 정상적으로 동작하지 않습니다."),

    // 이메일
    EMAIL_DUPLICATE(CONFLICT, "사용 중인 이메일입니다.");
    private final HttpStatus httpStatus;
    private final String message;
}
