package practice.springmvc.domain;

import lombok.Getter;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.SliceImpl;

import java.util.List;

@Getter
public class PageCustom<T> {
    private List<T> content;
    private PageableCustom pageableCustom;

    public PageCustom(List<T> content, Pageable pageable, long total) {
        this.content = content;
        this.pageableCustom = new PageableCustom(new PageImpl<>(content, pageable, total));
    }

    public PageCustom(List<T> content, Pageable pageable, boolean hasNext) {
        this.content = content;
        this.pageableCustom = new PageableCustom(new SliceImpl<>(content, pageable, hasNext));
    }
}
