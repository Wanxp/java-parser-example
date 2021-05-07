package org.wanxp.model;

/**
 * 评论自建模型
 */
public class CommentReportEntry {
    private String type;
    private String text;
    private int lineNumber;
    private boolean isOrphan;

    public CommentReportEntry(String type, String text, int lineNumber, boolean isOrphan) {
        this.type = type;
        this.text = text;
        this.lineNumber = lineNumber;
        this.isOrphan = isOrphan;
    }

    @Override
    public String toString() {
        return "CommentReportEntry{" +
                "type='" + type + '\'' +
                ", text='" + text + '\'' +
                ", lineNumber=" + lineNumber +
                ", isOrphan=" + isOrphan +
                '}';
    }
}
