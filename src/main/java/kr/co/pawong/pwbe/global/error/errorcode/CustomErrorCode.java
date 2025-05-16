package kr.co.pawong.pwbe.global.error.errorcode;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
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

    /**
     * 401 UNAUTHORIZED
     */
    TOKEN_INVALIDATE(UNAUTHORIZED, "토큰이 유효하지 않습니다."),
    USERNAME_NOT_FOUND(UNAUTHORIZED, "username 정보로 유저를 찾을 수 없습니다."),

    /**
     * 403 FORBIDDEN
     */
    FORBIDDEN_POST_MODIFY(FORBIDDEN, "게시글 수정 및 삭제 권한이 없습니다."),

    /**
     * 404 NOT_FOUND
     */
    USER_NOT_FOUND(NOT_FOUND, "유저가 존재하지 않습니다."),
    ADOPTION_NOT_FOUND(NOT_FOUND, "유기동물 정보가 없습니다."),
    LOSTPOST_NOT_FOUND(NOT_FOUND, "실종동물 게시글 정보가 없습니다."),
    LOST_NOT_FOUND(NOT_FOUND, "게시글이 존재하지 않습니다."),

    /**
     * 500 SERVER_ERROR
     */
    SERVER_ERROR(INTERNAL_SERVER_ERROR, "서버와의 연결에 실패하였습니다."),
    DATABASE_ERROR(INTERNAL_SERVER_ERROR, "데이터베이스 연결에 실패하였습니다."),
    // 검색
    SEARCH_ERROR(SERVICE_UNAVAILABLE, "검색 기능이 정상적으로 동작하지 않습니다.")
    ;
    private final HttpStatus httpStatus;
    private final String message;
}
