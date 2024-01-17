package practice.springmvc.web.board;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import practice.springmvc.domain.PageCustom;
import practice.springmvc.domain.PageableCustom;
import practice.springmvc.domain.board.Board;
import practice.springmvc.domain.board.BoardSearchCond;
import practice.springmvc.domain.board.BoardService;
import practice.springmvc.domain.board.dto.BoardDTO;
import practice.springmvc.domain.board.notrecommend.NotRecommend;
import practice.springmvc.domain.board.recommend.Recommend;
import practice.springmvc.domain.member.Member;
import practice.springmvc.web.board.form.BoardSaveForm;
import practice.springmvc.web.board.form.BoardUpdateForm;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final MessageSource ms;

    @GetMapping
    public String boards(@ModelAttribute("boardSearch") BoardSearchCond boardSearch, Model model, @PageableDefault(size = 2) Pageable pageable) {
        PageCustom<BoardDTO> boardList = boardService.findPagingAll(boardSearch, pageable);
        PageableCustom page = boardList.getPageableCustom();

        model.addAttribute("page", page.getPage());
        model.addAttribute("size", page.getSize());
        model.addAttribute("total", page.getTotal());
        model.addAttribute("totalPage", page.getTotalPage());
        model.addAttribute("boards", boardList.getContent());
        model.addAttribute("today", LocalDateTime.now());
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
        board.setRegistDate(LocalDateTime.now());
        board.setUpdateDate(LocalDateTime.now());

        boardService.save(board);
        return "redirect:/board";
    }

    @GetMapping("/{boardId}")
    public String read(@PathVariable Long boardId, Model model, HttpServletRequest request) {
        Board findBoard = boardService.findById(boardId).orElseThrow();
        model.addAttribute("board", boardService.addReadCount(findBoard, request));

        return "boards/board";
    }

    @GetMapping("/{boardId}/recommend")
    public String recommend(@PathVariable Long boardId, Model model, HttpServletRequest request) {
        Board findBoard = boardService.findById(boardId).orElseThrow();
        List<Recommend> findRecommends = findBoard.getRecommends();
        if (findRecommends != null) {
            model.addAttribute("board", boardService.recommend(findBoard, request));
        } else {
            model.addAttribute("board", findBoard);
        }

        return "boards/board";
    }

    @GetMapping("/{boardId}/notrecommend")
    public String notRecommend(@PathVariable Long boardId, Model model, HttpServletRequest request) {
        Board findBoard = boardService.findById(boardId).orElseThrow();
        List<NotRecommend> findNotRecommends = findBoard.getNotRecommends();
        if (findNotRecommends != null) {
            model.addAttribute("board", boardService.notRecommend(findBoard, request));
        } else {
            model.addAttribute("board", findBoard);
        }
        return "boards/board";
    }

    @GetMapping("/{boardId}/edit")
    public String editForm(@PathVariable Long boardId, Model model) {
        Board editBoard = boardService.findById(boardId).orElseThrow();
        model.addAttribute("board", editBoard);
        return "boards/editForm";
    }

    @PostMapping("/{boardId}/edit")
    public String edit(@PathVariable Long boardId, @ModelAttribute("board") BoardUpdateForm form, BindingResult bindingResult) {
        String passParam = form.getMember().getPassword();
        String findPassword = boardService.findById(boardId).orElseThrow().getMember().getPassword();
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
        board.setUpdateDate(LocalDateTime.now());

        boardService.update(boardId, board);
        return "redirect:/board";
    }

}
