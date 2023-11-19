package practice.springmvc.domain.board;


import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import practice.springmvc.domain.board.notrecommend.NotRecommend;
import practice.springmvc.domain.board.notrecommend.NotRecommendRepository;
import practice.springmvc.domain.board.recommend.Recommend;
import practice.springmvc.domain.board.recommend.RecommendRepository;
import practice.springmvc.domain.member.MemberRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService {

    private final MemberRepository memberRepository;
    private final RecommendRepository recommendRepository;
    private final NotRecommendRepository notRecommendRepository;

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
            Recommend recommend = new Recommend(board.getId(), board.getMember(), LocalDate.now());
            recommendRepository.save(recommend);
            List<Recommend> recommends = Optional.ofNullable(board.getRecommends()).orElseGet(() -> new ArrayList<>());
            recommends.add(recommend);
            board.setRecommends(recommends);
        }
        return board;
    }

    // 작성자 IP와 같지 않은 경우 비추천수 증가
    public Board notRecommend(Board board, HttpServletRequest request) {
        boolean ownIp = isOwnIp(board, request);
        if (!ownIp) {
            NotRecommend notRecommend = new NotRecommend(board.getId(), board.getMember(), LocalDate.now());
            notRecommendRepository.save(notRecommend);
            List<NotRecommend> notRecommends = Optional.ofNullable(board.getNotRecommends()).orElseGet(() -> new ArrayList<>());
            notRecommends.add(notRecommend);
            board.setNotRecommends(notRecommends);
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
        ip = Optional.ofNullable(ip).orElseGet(() -> request.getHeader("Proxy-Client-IP"));
        //웹로직 서버일 경우
        ip = Optional.ofNullable(ip).orElseGet(() -> request.getHeader("WL-Proxy-Client-IP"));

        ip = Optional.ofNullable(ip).orElseGet(() -> request.getRemoteAddr());

        return ip;
    }
}
