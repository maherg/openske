package openske.model.assets;

public class AssetAccess {
    protected Asset asset;
    protected AssetAccessor accessor;
    protected AssetAccessType type;

    public AssetAccess(Asset asset, AssetAccessor accessor, AssetAccessType type) {
        super();
        this.asset = asset;
        this.accessor = accessor;
        this.type = type;
    }

    public Asset getAsset() {
        return asset;
    }

    public void setAsset(Asset asset) {
        this.asset = asset;
    }

    public AssetAccessor getAccessor() {
        return accessor;
    }

    public void setAccessor(AssetAccessor accessor) {
        this.accessor = accessor;
    }

    public AssetAccessType getType() {
        return type;
    }

    public void setType(AssetAccessType type) {
        this.type = type;
    }

}
