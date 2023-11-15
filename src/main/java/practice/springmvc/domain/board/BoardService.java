package practice.springmvc.domain.board;


import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import practice.springmvc.domain.member.Member;
import practice.springmvc.domain.member.MemberRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService {

    private final MemberRepository memberRepository;

    // 작성자 IP와 같지 않은 경우 조회수 증가
    public Board addReadCount(Board board, HttpServletRequest request) {
        String remoteIp = getRemoteIp(request);
        if (!board.getMember().getIp().equals(remoteIp)) {
            board.setReadCount(board.getReadCount() + 1);
        }

        /*
        if (!board.getIp().equals(remoteIp)) {
            board.setReadCount(board.getReadCount() + 1);
        }
        */
        return board;
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
