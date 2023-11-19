package practice.springmvc.web.board;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import practice.springmvc.domain.board.Board;
import practice.springmvc.domain.board.BoardRepository;
import practice.springmvc.domain.board.BoardService;
import practice.springmvc.domain.board.notrecommend.NotRecommend;
import practice.springmvc.domain.board.notrecommend.NotRecommendRepository;
import practice.springmvc.domain.board.recommend.Recommend;
import practice.springmvc.domain.board.recommend.RecommendRepository;
import practice.springmvc.domain.member.Member;
import practice.springmvc.web.board.form.BoardSaveForm;
import practice.springmvc.web.board.form.BoardUpdateForm;

import java.util.Date;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardRepository boardRepository;
    private final RecommendRepository recommendRepository;
    private final NotRecommendRepository notRecommendRepository;
    private final BoardService boardService;
    private final MessageSource ms;

    @GetMapping
    public String boards(Model model) {
        List<Board> boardList = boardRepository.findAll();
        model.addAttribute("boards", boardList);
        return "boards/boards";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        Board board = new Board();
        Member member = new Member();
        member.setNickname(ms.getMessage("board.default.writer", null, null));
        board.setMember(member);
        model.addAttribute("board", board);
        return "boards/addForm";
    }

    @PostMapping("/add")
    public String saveForm(@Validated @ModelAttribute("board") BoardSaveForm form, HttpServletRequest request, BindingResult bindingResult) {
        String password = form.getMember().getPassword();
        if (password.length() < 4 || password.length() > 12) {
            bindingResult.reject("password.size", new Object[]{4, 12}, null);
        }

        if (bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult);
            return "boards/addForm";
        }

        Board board = new Board();
        board.setTitle(form.getTitle());
        board.setContent(form.getContent());
        board.setMember(new Member(form.getMember().getNickname(), form.getMember().getPassword(), boardService.getRemoteIp(request)));
        board.setRegistDate(new Date());
        board.setUpdateDate(new Date());

        boardRepository.save(board);
        return "redirect:/board";
    }

    @GetMapping("/{boardId}")
    public String read(@PathVariable Long boardId, Model model, HttpServletRequest request) {
        Board findBoard = boardRepository.findById(boardId);
        model.addAttribute("board", boardService.addReadCount(findBoard, request));

        return "boards/board";
    }

    @GetMapping("/{boardId}/recommend")
    public String recommend(@PathVariable Long boardId, Model model, HttpServletRequest request) {
        Board findBoard = boardRepository.findById(boardId);
        List<Recommend> findRecommends = recommendRepository.findByBoardId(findBoard.getId());
        if (findRecommends.size() == 0) {
            model.addAttribute("board", boardService.recommend(findBoard, request));
        } else {
            model.addAttribute("board", findBoard);
        }
        return "boards/board";
    }

    @GetMapping("/{boardId}/notrecommend")
    public String notRecommend(@PathVariable Long boardId, Model model, HttpServletRequest request) {
        Board findBoard = boardRepository.findById(boardId);
        List<NotRecommend> findNotRecommends = notRecommendRepository.findByBoardId(findBoard.getId());
        if (findNotRecommends.size() == 0) {
            model.addAttribute("board", boardService.notRecommend(findBoard, request));
        } else {
            model.addAttribute("board", findBoard);
        }
        return "boards/board";
    }

    @GetMapping("/{boardId}/edit")
    public String editForm(@PathVariable Long boardId, Model model) {
        Board editBoard = boardRepository.findById(boardId);
        model.addAttribute("board", editBoard);
        return "boards/editForm";
    }

    @PostMapping("/{boardId}/edit")
    public String edit(@PathVariable Long boardId, @ModelAttribute("board") BoardUpdateForm form, BindingResult bindingResult) {
        String passParam = form.getMember().getPassword();
        String findPassword = boardRepository.findById(boardId).getMember().getPassword();
        if (!findPassword.equals(passParam)) {
            bindingResult.reject("loginFail", "비밀번호가 맞지 않습니다.");
        }

        if (bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult);
            return "boards/editForm";
        }

        Board board = new Board();
        board.setTitle(form.getTitle());
        board.setContent(form.getContent());
//        board.setWriter(form.getWriter());
        board.setUpdateDate(new Date());

        boardRepository.update(boardId, board);
        return "redirect:/board";
    }

}
