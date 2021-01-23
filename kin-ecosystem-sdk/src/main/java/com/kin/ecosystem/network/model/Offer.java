package com.kin.ecosystem.network.model;

import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.Objects;

/**
 * offer details for the offer list
 */
public class Offer {
    @SerializedName("id")
    private String id = null;
    @SerializedName("title")
    private String title = null;
    @SerializedName("description")
    private String description = null;
    @SerializedName("image")
    private String image = null;
    @SerializedName("amount")
    private Integer amount = null;
    @SerializedName("content")
    private Object content = null;

    /**
     * Gets or Sets offerType
     */
    @JsonAdapter(OfferTypeEnum.Adapter.class)
    public enum OfferTypeEnum {

        EARN("earn"),
        SPEND("spend");

        private String value;

        OfferTypeEnum(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }

        public static OfferTypeEnum fromValue(String text) {
            for (OfferTypeEnum b : OfferTypeEnum.values()) {
                if (String.valueOf(b.value).equals(text)) {
                    return b;
                }
            }
            return null;
        }

        public static class Adapter extends TypeAdapter<OfferTypeEnum> {
            @Override
            public void write(final JsonWriter jsonWriter, final OfferTypeEnum enumeration) throws IOException {
                jsonWriter.value(enumeration.getValue());
            }

            @Override
            public OfferTypeEnum read(final JsonReader jsonReader) throws IOException {
                String value = jsonReader.nextString();
                return OfferTypeEnum.fromValue(String.valueOf(value));
            }
        }
    }

    @SerializedName("offer_type")
    private OfferTypeEnum offerType = null;

    public Offer id(String id) {
        this.id = id;
        return this;
    }


    /**
     * Get id
     *
     * @return id
     **/
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Offer title(String title) {
        this.title = title;
        return this;
    }


    /**
     * Get title
     *
     * @return title
     **/
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Offer description(String description) {
        this.description = description;
        return this;
    }


    /**
     * Get description
     *
     * @return description
     **/
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Offer image(String image) {
        this.image = image;
        return this;
    }


    /**
     * Get image
     *
     * @return image
     **/
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Offer amount(Integer amount) {
        this.amount = amount;
        return this;
    }


    /**
     * Get amount
     *
     * @return amount
     **/
    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Offer content(Object content) {
        this.content = content;
        return this;
    }

    public boolean hasPollJsonContent() {
        return content instanceof String;
    }

    public String getContentAsJsonString() {
        return (String) content;
    }

    /**
     * Get content
     *
     * @return content
     **/
    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public Offer offerType(OfferTypeEnum offerType) {
        this.offerType = offerType;
        return this;
    }


    /**
     * Get offerType
     *
     * @return offerType
     **/
    public OfferTypeEnum getOfferType() {
        return offerType;
    }

    public void setOfferType(OfferTypeEnum offerType) {
        this.offerType = offerType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Offer offer = (Offer) o;
        return Objects.equals(this.id, offer.id) &&
                Objects.equals(this.title, offer.title) &&
                Objects.equals(this.description, offer.description) &&
                Objects.equals(this.image, offer.image) &&
                Objects.equals(this.amount, offer.amount) &&
                Objects.equals(this.content, offer.content) &&
                Objects.equals(this.offerType, offer.offerType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, image, amount, content, offerType);
    }

}



