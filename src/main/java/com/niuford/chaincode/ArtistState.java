package com.niuford.chaincode;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

@DataType
public final class ArtistState {
    @Property
    private final String name;

    @Property
    private final String imageUrl;

    @Property
    private final String nationality;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public ArtistState(@JsonProperty("name") String name,
                       @JsonProperty("imageUrl") String imageUrl,
                       @JsonProperty("nationality") String nationality) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.nationality = nationality;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getNationality() {
        return nationality;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ArtistState that = (ArtistState) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (imageUrl != null ? !imageUrl.equals(that.imageUrl) : that.imageUrl != null) return false;
        return nationality != null ? nationality.equals(that.nationality) : that.nationality == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (imageUrl != null ? imageUrl.hashCode() : 0);
        result = 31 * result + (nationality != null ? nationality.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ArtistState{" +
                "name='" + name + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", nationality='" + nationality + '\'' +
                '}';
    }
}
