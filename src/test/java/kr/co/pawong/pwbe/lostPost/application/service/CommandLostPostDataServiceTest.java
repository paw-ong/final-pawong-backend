package kr.co.pawong.pwbe.lostPost.application.service;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import kr.co.pawong.pwbe.global.error.errorcode.CustomErrorCode;
import kr.co.pawong.pwbe.global.error.exception.BaseException;
import kr.co.pawong.pwbe.lostPost.application.port.out.LostPostDataCommandPort;
import kr.co.pawong.pwbe.lostPost.application.port.out.LostPostDataQueryPort;
import kr.co.pawong.pwbe.lostPost.domain.LostPost;
import kr.co.pawong.pwbe.lostPost.enums.PostStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class CommandLostPostDataServiceTest {

    @Mock
    private LostPostDataCommandPort commandPort;

    @Mock
    private LostPostDataQueryPort queryPort;  // update는 내부에서 안 쓰이지만, 생성자 주입을 위해 선언만

    @InjectMocks
    private CommandLostPostDataService service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * 생성 로직 테스트
     */
    @Test
    void 게시글_생성되면_생성된_ID를_반환한다() {
        // given
        LostPost input = LostPost.builder().lostPostId(null).build();
        LostPost saved = LostPost.builder().lostPostId(123L).build();
        when(commandPort.saveLostPost(any())).thenReturn(saved);

        // when
        Long result = service.createLostPost(input, 42L);

        // then
        assertThat(result).isEqualTo(123L);
        verify(commandPort, times(1)).saveLostPost(any());  // 1번만 호출됨
    }

    @Test
    void 게시글_생성시_userId와_status가_ACTIVE로_설정되어_DB에_전달된다() {
        // given
        LostPost input = LostPost.builder().lostPostId(null).build();
        when(commandPort.saveLostPost(any())).thenReturn(LostPost.builder().build());

        // when
        service.createLostPost(input, 42L);

        // then
        ArgumentCaptor<LostPost> captor = ArgumentCaptor.forClass(LostPost.class);
        verify(commandPort).saveLostPost(captor.capture());
        LostPost passed = captor.getValue();

        assertThat(passed.getUserId()).isEqualTo(42L);
        assertThat(passed.getStatus()).isEqualTo(PostStatus.ACTIVE);
    }

    @Test
    void 게시글_생성시_createdAt가_설정되어_DB에_전달된다() {
        // given
        LostPost input = LostPost.builder().lostPostId(null).build();
        when(commandPort.saveLostPost(any())).thenReturn(LostPost.builder().build());

        // when
        service.createLostPost(input, 42L);

        // then
        ArgumentCaptor<LostPost> captor = ArgumentCaptor.forClass(LostPost.class);
        verify(commandPort).saveLostPost(captor.capture());
        LostPost passed = captor.getValue();

        assertThat(passed.getCreatedAt()).isNotNull();
    }

    /**
     * 수정 로직 테스트
     */
    @Test
    void 게시글_수정요청하면_updateLostPostOrThrow를_호출한다() {
        // given
        Long postId = 7L, userId = 99L;
        LostPost dto = LostPost.builder().lostPostId(postId).build();
        LostPost dummyResult = LostPost.builder().lostPostId(postId).build();
        when(commandPort.updateLostPostOrThrow(postId, dto, userId))
                .thenReturn(dummyResult);

        // when
        service.updateLostPost(postId, dto, userId);

        // then
        verify(commandPort, times(1))
                .updateLostPostOrThrow(postId, dto, userId);
    }

    @Test
    void 게시글_수정요청하면_포트가_반환한_ID를_리턴한다() {
        // given
        Long postId = 7L, userId = 99L;
        LostPost dto = LostPost.builder().lostPostId(postId).build();
        LostPost updated = LostPost.builder().lostPostId(888L).build();
        when(commandPort.updateLostPostOrThrow(postId, dto, userId))
                .thenReturn(updated);

        // when
        Long result = service.updateLostPost(postId, dto, userId);

        // then
        assertThat(result).isEqualTo(888L);
    }
    @Test
    void updateLostPost_존재하지_않는_게시글이면_NotFound_예외_던진다() {
        // given
        Long postId = 1L, userId = 2L;
        LostPost dto = LostPost.builder().lostPostId(postId).build();
        doThrow(new BaseException(CustomErrorCode.LOSTPOST_NOT_FOUND))
                .when(commandPort).updateLostPostOrThrow(postId, dto, userId);

        // when / then
        BaseException ex = assertThrows(BaseException.class,
                () -> service.updateLostPost(postId, dto, userId));

        // then
        assertThat(ex.getErrorCode()).isEqualTo(CustomErrorCode.LOSTPOST_NOT_FOUND);
    }
    @Test
    void updateLostPost_작성자가_아니면_Forbidden_예외_던진다() {
        // given
        Long postId = 3L, userId = 4L;
        LostPost dto = LostPost.builder().lostPostId(postId).build();
        doThrow(new BaseException(CustomErrorCode.FORBIDDEN_POST_MODIFY))
                .when(commandPort).updateLostPostOrThrow(postId, dto, userId);

        // when / then
        BaseException ex = assertThrows(BaseException.class,
                () -> service.updateLostPost(postId, dto, userId));

        assertThat(ex.getErrorCode()).isEqualTo(CustomErrorCode.FORBIDDEN_POST_MODIFY);
    }

    /**
     * 삭제 로직 테스트
     */
    @Test
    void 게시글_삭제요청하면_modifyDeleteStatusOrThrow를_호출한다() {
        // given
        Long postId = 55L, userId = 66L;

        // when
        service.deleteLostPost(postId, userId);

        // then
        verify(commandPort, times(1))
                .modifyDeleteStatusOrThrow(postId, userId);
    }

    @Test
    void deleteLostPost_존재하지_않는_게시글이면_NotFound_예외_던진다() {
        // given
        Long postId = 5L, userId = 6L;
        doThrow(new BaseException(CustomErrorCode.LOSTPOST_NOT_FOUND))
                .when(commandPort).modifyDeleteStatusOrThrow(postId, userId);

        // when / then
        BaseException ex = assertThrows(BaseException.class,
                () -> service.deleteLostPost(postId, userId));

        assertThat(ex.getErrorCode()).isEqualTo(CustomErrorCode.LOSTPOST_NOT_FOUND);
    }

    @Test
    void deleteLostPost_작성자가_아니면_Forbidden_예외_던진다() {
        // given
        Long postId = 7L, userId = 8L;
        doThrow(new BaseException(CustomErrorCode.FORBIDDEN_POST_MODIFY))
                .when(commandPort).modifyDeleteStatusOrThrow(postId, userId);

        // when / then
        BaseException ex = assertThrows(BaseException.class,
                () -> service.deleteLostPost(postId, userId));

        assertThat(ex.getErrorCode()).isEqualTo(CustomErrorCode.FORBIDDEN_POST_MODIFY);
    }

}
