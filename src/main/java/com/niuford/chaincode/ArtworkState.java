package com.niuford.chaincode;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

import java.util.Date;
import java.util.List;

@DataType
public final class ArtworkState {
    @Property
    private final String artworkId;

    @Property
    private final String imageUrl;

    @Property
    private final String title;

    @Property
    private final String description;

    @Property
    private final String dimension;

    @Property
    private final String price;

    @Property
    private final String currentOwner;

    @Property
    private final List<String> highlights;

    @Property
    private final ArtistState artist;

    @Property
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd MMMMM yyyy")
    private final Date lastModified;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public ArtworkState(@JsonProperty("artworkId") String artworkId,
                        @JsonProperty("imageUrl") String imageUrl,
                        @JsonProperty("title") String title,
                        @JsonProperty("description") String description,
                        @JsonProperty("dimension") String dimension,
                        @JsonProperty("price") String price,
                        @JsonProperty("currentOwner") String currentOwner,
                        @JsonProperty("highlights") List<String> highlights,
                        @JsonProperty("artist") ArtistState artist,
                        @JsonProperty("lastModified") Date lastModified) {
        this.artworkId = artworkId;
        this.imageUrl = imageUrl;
        this.title = title;
        this.description = description;
        this.dimension = dimension;
        this.price = price;
        this.currentOwner = currentOwner;
        this.highlights = highlights;
        this.artist = artist;
        this.lastModified = lastModified;
    }

    public String getArtworkId() {
        return artworkId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDimension() {
        return dimension;
    }

    public String getPrice() {
        return price;
    }

    public String getCurrentOwner() {
        return currentOwner;
    }

    public List<String> getHighlights() {
        return highlights;
    }

    public ArtistState getArtist() {
        return artist;
    }

    public Date getLastModified() {
        return lastModified;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ArtworkState that = (ArtworkState) o;

        if (!artworkId.equals(that.artworkId)) return false;
        if (imageUrl != null ? !imageUrl.equals(that.imageUrl) : that.imageUrl != null) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (dimension != null ? !dimension.equals(that.dimension) : that.dimension != null) return false;
        if (price != null ? !price.equals(that.price) : that.price != null) return false;
        if (currentOwner != null ? !currentOwner.equals(that.currentOwner) : that.currentOwner != null) return false;
        if (highlights != null ? !highlights.equals(that.highlights) : that.highlights != null) return false;
        if (artist != null ? !artist.equals(that.artist) : that.artist != null) return false;
        return lastModified != null ? lastModified.equals(that.lastModified) : that.lastModified == null;
    }

    @Override
    public int hashCode() {
        int result = artworkId.hashCode();
        result = 31 * result + (imageUrl != null ? imageUrl.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (dimension != null ? dimension.hashCode() : 0);
        result = 31 * result + (price != null ? price.hashCode() : 0);
        result = 31 * result + (currentOwner != null ? currentOwner.hashCode() : 0);
        result = 31 * result + (highlights != null ? highlights.hashCode() : 0);
        result = 31 * result + (artist != null ? artist.hashCode() : 0);
        result = 31 * result + (lastModified != null ? lastModified.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ArtworkState{" +
                "artworkId='" + artworkId + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", dimension='" + dimension + '\'' +
                ", price='" + price + '\'' +
                ", currentOwner='" + currentOwner + '\'' +
                ", highlights=" + highlights +
                ", artist=" + artist +
                ", lastModified=" + lastModified +
                '}';
    }
}
