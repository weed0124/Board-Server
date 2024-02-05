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
import practice.springmvc.domain.board.dto.BoardDTO;
import practice.springmvc.domain.board.dto.QBoardDTO;

import java.util.List;

import static practice.springmvc.domain.board.QBoard.board;
import static practice.springmvc.domain.board.recommend.QRecommend.*;
import static practice.springmvc.domain.member.QMember.member;

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
                        member.nickname,
                        member.ip,
                        board.createdDate,
                        board.readCount,
                        board.recommends.size(),
                        board.notRecommends.size()
                ))
                .from(board)
                .leftJoin(board.member, member)
                .where(likeTitle(title), likeNickname(nickname))
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
            return board.member.nickname.like("%" + nickname + "%");
        }
        return null;
    }
}
