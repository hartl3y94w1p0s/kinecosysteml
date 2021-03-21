/*
 * Kin Ecosystem
 * Apis for client to server interaction
 *
 * OpenAPI spec version: 1.0.0
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */


package com.kin.ecosystem.network.model;

import java.util.Objects;

import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * a submitted order. it can be pending/completed/failed
 */
public class Order {
    @SerializedName("result")
    private Object result = null;

    /**
     * Gets or Sets status
     */
    @JsonAdapter(StatusEnum.Adapter.class)
    public enum StatusEnum {

        PENDING("pending"),
        COMPLETED("completed"),
        FAILED("failed");

        private String value;

        StatusEnum(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }

        public static StatusEnum fromValue(String text) {
            for (StatusEnum b : StatusEnum.values()) {
                if (String.valueOf(b.value).equals(text)) {
                    return b;
                }
            }
            return null;
        }

        public static class Adapter extends TypeAdapter<StatusEnum> {
            @Override
            public void write(final JsonWriter jsonWriter, final StatusEnum enumeration) throws IOException {
                jsonWriter.value(enumeration.getValue());
            }

            @Override
            public StatusEnum read(final JsonReader jsonReader) throws IOException {
                String value = jsonReader.nextString();
                return StatusEnum.fromValue(String.valueOf(value));
            }
        }
    }

    @SerializedName("status")
    private StatusEnum status = null;
    @SerializedName("id")
    private String orderId = null;
    @SerializedName("completion_date")
    private String completionDate = null;
    @SerializedName("blockchain_data")
    private BlockchainData blockchainData = null;

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
    @SerializedName("title")
    private String title = null;
    @SerializedName("description")
    private String description = null;
    @SerializedName("call_to_action")
    private String callToAction = null;
    @SerializedName("amount")
    private Integer amount = null;

    public Order result(Object result) {
        this.result = result;
        return this;
    }


    /**
     * * empty when no result (pending status, completed earn) * failure_message when status is failed * coupon_code when completed spend
     *
     * @return result
     **/
    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public Order status(StatusEnum status) {
        this.status = status;
        return this;
    }


    /**
     * Get status
     *
     * @return status
     **/
    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    public Order orderId(String orderId) {
        this.orderId = orderId;
        return this;
    }


    /**
     * unique identifier of this item
     *
     * @return orderId
     **/
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Order completionDate(String completionDate) {
        this.completionDate = completionDate;
        return this;
    }


    /**
     * UTC ISO
     *
     * @return completionDate
     **/
    public String getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(String completionDate) {
        this.completionDate = completionDate;
    }

    public Order blockchainData(BlockchainData blockchainData) {
        this.blockchainData = blockchainData;
        return this;
    }


    /**
     * Get blockchainData
     *
     * @return blockchainData
     **/
    public BlockchainData getBlockchainData() {
        return blockchainData;
    }

    public void setBlockchainData(BlockchainData blockchainData) {
        this.blockchainData = blockchainData;
    }

    public Order offerType(OfferTypeEnum offerType) {
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

    public Order title(String title) {
        this.title = title;
        return this;
    }


    /**
     * usually a brand name
     *
     * @return title
     **/
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Order description(String description) {
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

    public Order callToAction(String callToAction) {
        this.callToAction = callToAction;
        return this;
    }


    /**
     * Get callToAction
     *
     * @return callToAction
     **/
    public String getCallToAction() {
        return callToAction;
    }

    public void setCallToAction(String callToAction) {
        this.callToAction = callToAction;
    }

    public Order amount(Integer amount) {
        this.amount = amount;
        return this;
    }


    /**
     * kin amount
     *
     * @return amount
     **/
    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Order order = (Order) o;
        return Objects.equals(this.orderId, order.orderId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(result, status, orderId, completionDate, blockchainData, offerType, title, description, callToAction, amount);
    }
}



