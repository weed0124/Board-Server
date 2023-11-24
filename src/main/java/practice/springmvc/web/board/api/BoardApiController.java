package practice.springmvc.web.board.api;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import practice.springmvc.domain.board.Board;
import practice.springmvc.domain.board.BoardService;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/board")
@RequiredArgsConstructor
public class BoardApiController {

    private final BoardService boardService;

    @GetMapping("/boards/best")
    public ResponseEntity<Result> bestBoards() {
        List<Board> boardList = boardService.findAll().stream()
                .filter(board -> board.getRecommends().size() >= 1)
                .toList();
        List<BoardApiDTO> collect = getBoardApiDTOS(boardList);
        return ResponseEntity.ok(new Result(collect));
    }

    @GetMapping("/boards/worst")
    public ResponseEntity<Result> worstBoards() {
        List<Board> boardList = boardService.findAll().stream()
                .filter(board -> board.getNotRecommends().size() >= 1)
                .toList();
        List<BoardApiDTO> collect = getBoardApiDTOS(boardList);
        return ResponseEntity.ok(new Result(collect));
    }

    private List<BoardApiDTO> getBoardApiDTOS(List<Board> boardList) {
        List<BoardApiDTO> collect = boardList
                .stream()
                .map(board -> new BoardApiDTO(board.getId(),
                        board.getTitle(),
                        board.getContent(),
                        board.getMember().getNickname(),
                        board.getMember().getIp(),
                        board.getRecommends().size(),
                        board.getNotRecommends().size())).collect(Collectors.toList());
        return collect;
    }

    public void update(Board board, BoardApiDTO boardApiDTO) {
        boardApiDTO.setId(board.getId());
        boardApiDTO.setTitle(board.getTitle());
        boardApiDTO.setContent(board.getContent());
        boardApiDTO.setIp(board.getMember().getIp());
        boardApiDTO.setNickname(board.getMember().getNickname());
    }

    @Getter @Setter
    @AllArgsConstructor
    class Result<T> {
        private T data;
    }

    @Getter @Setter
    @AllArgsConstructor
    class BoardApiDTO {
        Long id;
        String title;
        String content;
        String nickname;
        String ip;
        int recommendSize;
        int notRecommendSize;
    }
}
