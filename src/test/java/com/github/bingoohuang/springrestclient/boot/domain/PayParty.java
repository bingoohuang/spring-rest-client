package com.github.bingoohuang.springrestclient.boot.domain;

public class PayParty {
    private String partyId;
    private String partyName;
    private String sellerId;
    private String buyerId;

    public PayParty() {
    }

    public PayParty(String sellerId, String buyerId, String partyId, String partyName) {
        this.sellerId = sellerId;
        this.buyerId = buyerId;
        this.partyId = partyId;
        this.partyName = partyName;
    }

    public String getPartyId() {
        return partyId;
    }

    public void setPartyId(String partyId) {
        this.partyId = partyId;
    }

    public String getPartyName() {
        return partyName;
    }

    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public String getBuyerId() {
        return buyerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PayParty payParty = (PayParty) o;

        if (partyId != null ? !partyId.equals(payParty.partyId) : payParty.partyId != null) return false;
        if (partyName != null ? !partyName.equals(payParty.partyName) : payParty.partyName != null) return false;
        if (sellerId != null ? !sellerId.equals(payParty.sellerId) : payParty.sellerId != null) return false;
        return !(buyerId != null ? !buyerId.equals(payParty.buyerId) : payParty.buyerId != null);

    }

    @Override
    public int hashCode() {
        int result = partyId != null ? partyId.hashCode() : 0;
        result = 31 * result + (partyName != null ? partyName.hashCode() : 0);
        result = 31 * result + (sellerId != null ? sellerId.hashCode() : 0);
        result = 31 * result + (buyerId != null ? buyerId.hashCode() : 0);
        return result;
    }
}
