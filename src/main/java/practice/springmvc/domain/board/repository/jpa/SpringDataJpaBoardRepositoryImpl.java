package practice.springmvc.domain.board.repository.jpa;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;
import practice.springmvc.domain.board.Board;
import practice.springmvc.domain.board.BoardSearchCond;
import practice.springmvc.dto.BoardDTO;
import practice.springmvc.dto.QBoardDTO;

import java.util.List;

import static practice.springmvc.domain.board.QBoard.board;
import static practice.springmvc.domain.board.tag.QTag.*;

public class SpringDataJpaBoardRepositoryImpl implements BoardJPARepository {

    private final EntityManager em;
    private final JPAQueryFactory query;

    public SpringDataJpaBoardRepositoryImpl(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }

    @Override
    public List<Board> findBoardList(BoardSearchCond cond) {
        String title = cond.getTitle();
        String nickname = cond.getNickname();

        return query
                .select(board)
                .from(board)
                .where(likeTitle(title), likeNickname(nickname))
                .fetch();
    }

    @Override
    public Page<BoardDTO> findPagingBoardList(BoardSearchCond cond, Pageable pageable) {
        String title = cond.getTitle();
        String nickname = cond.getNickname();

        QueryResults<BoardDTO> results = query
                .select(new QBoardDTO(
                        board.id,
                        board.title,
                        board.nickname,
                        board.ip,
                        board.createdDate,
                        board.readCount,
                        board.recommends.size(),
                        board.notRecommends.size()
                ))
                .from(board)
                .where(likeTitle(title), likeNickname(nickname))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<BoardDTO> content = results.getResults();
        long total = results.getTotal();
        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<BoardDTO> findPagingBoardListByTagName(String tagName, Pageable pageable) {
        QueryResults<BoardDTO> results = query
                .select(new QBoardDTO(
                        board.id,
                        board.title,
                        board.nickname,
                        board.ip,
                        board.createdDate,
                        board.readCount,
                        board.recommends.size(),
                        board.notRecommends.size(),
                        tag.name,
                        tag.url
                ))
                .from(board)
                .join(tag)
                .on(board.id.eq(tag.board.id))
                .where(likeTagName(tagName))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<BoardDTO> content = results.getResults();
        long total = results.getTotal();
        return new PageImpl<>(content, pageable, total);
    }

    private BooleanExpression likeTitle(String title) {
        if (StringUtils.hasText(title)) {
            return board.title.like("%" + title + "%");
        }
        return null;
    }

    private BooleanExpression likeNickname(String nickname) {
        if (StringUtils.hasText(nickname)) {
            return board.nickname.like("%" + nickname + "%");
        }
        return null;
    }

    private BooleanExpression likeTagName(String tagName) {
        if (StringUtils.hasText(tagName)) {
            return tag.name.like("%" + tagName + "%");
        }
        return null;
    }
}
