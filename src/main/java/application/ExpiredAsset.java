package application;

public class ExpiredAsset {
    private String asset;
    private String expirationDate;

    public ExpiredAsset(String asset, String expirationDate) {
        this.asset = asset;
        this.expirationDate = expirationDate;
    }

    public String getAsset() {
        return asset;
    }

    public void setAsset(String asset) {
        this.asset = asset;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }
}
