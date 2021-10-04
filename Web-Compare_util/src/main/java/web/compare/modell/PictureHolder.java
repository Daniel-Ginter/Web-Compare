package web.compare.modell;

import web.compare.modell.MetadataHelper;

import java.awt.image.BufferedImage;

public class PictureHolder {
    private MetadataHelper metadata;
    private BufferedImage image;
    private String name;

    public PictureHolder(MetadataHelper metadata, BufferedImage image,String name) {
        this.metadata = metadata;
        this.image = image;
        this.name = name;
    }

    public PictureHolder() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MetadataHelper getMetadata() {
        return metadata;
    }

    public void setMetadata(MetadataHelper metadata) {
        this.metadata = metadata;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }
}
