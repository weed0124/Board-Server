package practice.springmvc.domain.board;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import practice.springmvc.annotation.Trace;
import practice.springmvc.domain.PageCustom;
import practice.springmvc.domain.board.dto.BoardDTO;
import practice.springmvc.domain.board.notrecommend.NotRecommend;
import practice.springmvc.domain.board.notrecommend.NotRecommendService;
import practice.springmvc.domain.board.recommend.Recommend;
import practice.springmvc.domain.board.recommend.RecommendService;
import practice.springmvc.domain.board.repository.jpa.SpringDataJpaBoardRepository;
import practice.springmvc.domain.member.Member;
import practice.springmvc.domain.member.MemberService;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {

    private final SpringDataJpaBoardRepository boardRepository;
    private final RecommendService recommendService;
    private final NotRecommendService notRecommendService;
    private final MemberService memberService;

    @Trace
    public Board save(Board board) {
        return boardRepository.save(board);
    }

    @Trace
    public Optional<Board> findById(Long id) {
        return boardRepository.findById(id);
    }

    @Trace
    public List<Board> findAll(BoardSearchCond cond) {
        return boardRepository.findBoardList(cond);
    }

    @Trace
    public Page<BoardDTO> findPagingAllV2(BoardSearchCond cond, Pageable pageable) {
        return boardRepository.findPagingBoardList(cond, pageable);
    }

    @Trace
    public PageCustom<BoardDTO> findPagingAll(BoardSearchCond cond, Pageable pageable) {
        Page<BoardDTO> page = boardRepository.findPagingBoardList(cond, pageable);
        return new PageCustom<BoardDTO>(page.getContent(), page.getPageable(), page.getTotalElements());
    }

    @Trace
    public void update(Long boardId, Board updateParam) {
        Board findBoard = findById(boardId).orElseThrow();
        findBoard.setTitle(updateParam.getTitle());
        findBoard.setContent(updateParam.getContent());
        findBoard.setNickname(updateParam.getNickname());
        findBoard.setPassword(updateParam.getPassword());
        findBoard.setIp(updateParam.getIp());
    }

    @Trace
    public void delete(Long boardId) {
        boardRepository.deleteById(boardId);
    }


    // 작성자 IP와 같지 않은 경우 조회수 증가
    public Board addReadCount(Board board, HttpServletRequest request) {
        boolean ownIp = isOwnIp(board, request);
        if (!ownIp) {
            addReadCount(board.getId());
        }

        return board;
    }

    private void addReadCount(Long boardId) {
        Board findBoard = boardRepository.findById(boardId).orElseThrow();
        findBoard.setReadCount(findBoard.getReadCount() + 1);
    }

    // 작성자 IP와 같지 않은 경우 추천수 증가
    @Trace
    public Board recommend(Board board, HttpServletRequest request) {
        boolean ownIp = isOwnIp(board, request);
        Period p = Period.between(LocalDate.now(), LocalDate.now());
        if (!ownIp) {
            List<Recommend> recommends = Optional.ofNullable(board.getRecommends()).orElseGet(ArrayList::new);
            List<Recommend> oneDayRecommend = recommends.stream()
                    .filter(rec -> Period.between(rec.getCreatedDate().toLocalDate(), LocalDate.now()).getDays() == 0)
                    .toList();
            if (oneDayRecommend.isEmpty()) {
                Member member = new Member(getRemoteIp(request));
                memberService.save(member);
                Recommend recommend = new Recommend(board, member);
                recommendService.save(recommend);
                board.addRecommend(recommend);
            }
        }
        return board;
    }

    // 작성자 IP와 같지 않은 경우 비추천수 증가
    @Trace
    public Board notRecommend(Board board, HttpServletRequest request) {
        boolean ownIp = isOwnIp(board, request);
        if (!ownIp) {
            List<NotRecommend> notRecommends = Optional.ofNullable(board.getNotRecommends()).orElseGet(ArrayList::new);
            List<NotRecommend> oneDayNotRecommend = notRecommends.stream()
                    .filter(notRec -> Period.between(notRec.getCreatedDate().toLocalDate(), LocalDate.now()).getDays() == 0)
                    .toList();
            if (oneDayNotRecommend.isEmpty()) {
                Member member = new Member(getRemoteIp(request));
                memberService.save(member);
                NotRecommend notRecommend = new NotRecommend(board, member);
                notRecommendService.save(notRecommend);
                board.addNotRecommend(notRecommend);
            }
        }

        return board;
    }

    private boolean isOwnIp(Board board, HttpServletRequest request) {
        String remoteIp = getRemoteIp(request);
        return board.getIp().equals(remoteIp);
    }

    // IP 조회
    public String getRemoteIp(HttpServletRequest request) {
        String ip = request.getHeader("X-FORWARDED-FOR");

        //proxy 환경일 경우
        ip = Optional.ofNullable(ip).orElseGet(() -> request.getHeader("Proxy-Client-IP"));
        //웹로직 서버일 경우
        ip = Optional.ofNullable(ip).orElseGet(() -> request.getHeader("WL-Proxy-Client-IP"));

        ip = Optional.ofNullable(ip).orElseGet(request::getRemoteAddr);

        return ip;
    }
}
