package practice.springmvc.domain;

import lombok.Getter;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.SliceImpl;

import java.io.Serial;
import java.util.List;

@Getter
public class PageCustom<T> extends PageImpl {

    @Serial
    private static final long serialVersionUID = -9049751420366260662L;

    private List<T> content;
    private PageableCustom pageableCustom;

    public PageCustom(List<T> content, Pageable pageable, long total) {
        super(content);
        this.content = content;
        this.pageableCustom = new PageableCustom(new PageImpl<>(content, pageable, total));
    }

//    public PageCustom(List<T> content, Pageable pageable, boolean hasNext) {
//        super(content);
//        this.content = content;
//        this.pageableCustom = new PageableCustom(new SliceImpl<>(content, pageable, hasNext));
//    }
}
