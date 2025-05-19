package kr.co.pawong.pwbe.lostPost.application.port.out;

public interface BookmarkInfoPort {

    boolean existsByUserIdAndLostPostId(Long userId, Long lostPostId);

    boolean existsByUserIdAndAdoptionId(Long userId, Long adoptionId);

}
