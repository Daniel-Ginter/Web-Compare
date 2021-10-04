package web.compare.modell;

import java.util.Objects;

public class MetadataHelper {
    public String tag;
    public int x;
    public int y;
    public int width;
    public int height;
    public String structure;
    public String outerHtml;
    public String innerHtml;
    public String selector;
    public Boolean visible;

    public MetadataHelper(String tag, String structure, String outerHtml, String innerHtml, String selector, Boolean visible, int x, int y, int width, int height) {
        this.tag = tag;
        this.structure = structure;
        this.outerHtml = outerHtml;
        this.innerHtml = innerHtml;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.selector = selector;
        this.visible = visible;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MetadataHelper that = (MetadataHelper) o;
        return x == that.x
                && y == that.y
                && width == that.width
                && height == that.height
                && Objects.equals(tag, that.tag)
                && Objects.equals(structure, that.structure)
                && Objects.equals(outerHtml, that.outerHtml)
                && Objects.equals(innerHtml, that.innerHtml)
                && Objects.equals(selector, that.selector);
    }

    public boolean structuralEquals(MetadataHelper p) {
        return this.structure.equals(p.structure)
                && this.tag.equals(p.tag)
                && this.outerHtml.equals(p.outerHtml);
    }

    public boolean dimensionalEquals(MetadataHelper p) {
        return x == p.x &&
                y == p.y &&
                width == p.width &&
                height == p.height;
    }

    @Override
    public int hashCode() {
        return Objects.hash(tag, x, y, width, height, structure, outerHtml, innerHtml, selector, visible);
    }

    @Override
    public String toString() {
        return "MetadataHelper{" +
                "tag='" + tag + '\'' +
                ", x=" + x +
                ", y=" + y +
                ", width=" + width +
                ", height=" + height +
                ", structure='" + structure + '\'' +
                ", outerHtml='" + outerHtml + '\'' +
                ", innerHtml='" + innerHtml + '\'' +
                ", selector='" + selector + '\'' +
                ", visible='" + visible + '\'' +
                '}';
    }
}
