package fvtc.edu.teams;

import java.io.Serializable;

public class Team implements Serializable {


    private int id;
    private String name;
    private String city;
    private String cellPhone;
    private float rating;
    private int imgId;
    private boolean isFavorite;
    private Double latitude;
    private Double longitude;

    @Override
    public String toString() {
        return  "id=" + String.valueOf(id) +
                "| name='" + name + '\'' +
                "| city='" + city + '\'' +
                "| cellPhone='" + cellPhone + '\'' +
                "| rating=" + rating +
                "| imgId=" + imgId +
                "| isFavorite=" + isFavorite +
                "| latitude=" + latitude +
                "| longitude=" + longitude;
    }

    public Team(int id,
                String name,
                String city,
                String cellPhone,
                float rating,
                int imgId,
                boolean isFavorite,
                Double latitude,
                Double longitude) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.cellPhone = cellPhone;
        this.rating = rating;
        this.imgId = imgId;
        this.isFavorite = isFavorite;
        this.latitude = latitude;
        this.longitude = longitude;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCellPhone() {
        return cellPhone;
    }

    public void setCellPhone(String cellPhone) {
        this.cellPhone = cellPhone;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public boolean getIsFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
