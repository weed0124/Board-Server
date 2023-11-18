package practice.springmvc.domain.board;


import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import practice.springmvc.domain.member.MemberRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService {

    private final MemberRepository memberRepository;

    // 작성자 IP와 같지 않은 경우 조회수 증가
    public Board addReadCount(Board board, HttpServletRequest request) {
        boolean ownIp = isOwnIp(board, request);
        if (!ownIp) {
            board.setReadCount(board.getReadCount() + 1);
        }

        return board;
    }

    // 작성자 IP와 같지 않은 경우 추천수 증가
    public Board recommend(Board board, HttpServletRequest request) {
        boolean ownIp = isOwnIp(board, request);
        if (!ownIp) {
            board.setRecommendCount(board.getRecommendCount() + 1);
        }

        return board;
    }

    // 작성자 IP와 같지 않은 경우 비추천수 증가
    public Board notRecommend(Board board, HttpServletRequest request) {
        boolean ownIp = isOwnIp(board, request);
        if (!ownIp) {
            board.setNotRecommendCount(board.getNotRecommendCount() + 1);
        }

        return board;
    }

    private boolean isOwnIp(Board board, HttpServletRequest request) {
        String remoteIp = getRemoteIp(request);
        return board.getMember().getIp().equals(remoteIp);
    }

    // IP 조회
    public String getRemoteIp(HttpServletRequest request) {
        String ip = request.getHeader("X-FORWARDED-FOR");

        //proxy 환경일 경우
        if (ip == null || ip.length() == 0) {
            ip = request.getHeader("Proxy-Client-IP");
        }

        //웹로직 서버일 경우
        if (ip == null || ip.length() == 0) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }

        if (ip == null || ip.length() == 0) {
            ip = request.getRemoteAddr() ;
        }

        return ip;
    }
}
