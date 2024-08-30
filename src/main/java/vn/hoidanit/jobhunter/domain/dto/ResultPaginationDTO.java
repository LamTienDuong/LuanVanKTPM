package vn.hoidanit.jobhunter.domain.dto;

public class ResultPaginationDTO {
    private Meta meta;
    private Object result;

    public static class Meta {
        private int page;
        private int pageSize;
        private int pages;
        private long total;

        public Meta() {
        }

        public Meta(int page, int pageSize, int pages, long total) {
            this.page = page;
            this.pageSize = pageSize;
            this.pages = pages;
            this.total = total;
        }

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public int getPages() {
            return pages;
        }

        public void setPages(int pages) {
            this.pages = pages;
        }

        public long getTotal() {
            return total;
        }

        public void setTotal(long total) {
            this.total = total;
        }
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
