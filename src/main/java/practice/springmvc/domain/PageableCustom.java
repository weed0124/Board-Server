package practice.springmvc.domain;

import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;

import java.io.Serial;
import java.io.Serializable;

@Getter
public class PageableCustom implements Serializable {
    @Serial
    private static final long serialVersionUID = -7047555937637362870L;

    private int page;
    private int size;
    private boolean hasNext;
    private boolean first;
    private boolean last;
    private int total;
    private long totalPage;

    public PageableCustom() {
    }

    public PageableCustom(Page page) {
        this.page = page.getNumber() + 1;
        this.size = page.getSize();
        this.hasNext = page.hasNext();
        this.first = page.isFirst();
        this.last = page.isLast();
        this.total = page.getTotalPages();
        this.totalPage = page.getTotalElements();
    }

    public PageableCustom(Slice slice) {
        this.page = slice.getNumber() + 1;
        this.size = slice.getSize();
        this.hasNext = slice.hasNext();
        this.first = slice.isFirst();
        this.last = slice.isLast();
    }
}
