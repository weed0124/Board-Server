package practice.springmvc.web.board;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import practice.springmvc.domain.board.Board;
import practice.springmvc.domain.board.BoardRepository;
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
        board.setWriter(ms.getMessage("board.default.writer", null, null));
        model.addAttribute("board", board);
        return "boards/addForm";
    }

    @PostMapping("/add")
    public String saveForm(BoardSaveForm form) {
        Board board = new Board();
        board.setTitle(form.getTitle());
        board.setContent(form.getContent());
        board.setWriter(form.getWriter());
        board.setPassword(form.getPassword());
        board.setRegistDate(new Date());
        board.setUpdateDate(new Date());

        boardRepository.save(board);
        return "redirect:/board";
    }

    @GetMapping("/{boardId}")
    public String read(@PathVariable Long boardId, Model model) {
        Board findBoard = boardRepository.findById(boardId);
        model.addAttribute("board", findBoard);
        return "boards/board";
    }

    @GetMapping("/{boardId}/edit")
    public String editForm(@PathVariable Long boardId, Model model) {
        Board editBoard = boardRepository.findById(boardId);
        model.addAttribute("board", editBoard);
        return "boards/editForm";
    }

    @PostMapping("/{boardId}/edit")
    public String edit(@PathVariable Long boardId, BoardUpdateForm form) {
        Board board = new Board();
        board.setTitle(form.getTitle());
        board.setContent(form.getContent());
        board.setWriter(form.getWriter());
        board.setUpdateDate(new Date());

        boardRepository.update(boardId, board);
        return "redirect:/board";
    }

}
