package openske.model.assets;

import java.util.Random;

public enum AssetAccessType {
    NONE, READ, READ_WRITE, READ_EXECUTE, READ_WRITE_EXECUTE, ;
    
    public static AssetAccessType getRandomValue() {
        AssetAccessType[] list = AssetAccessType.values();
        int randomIndex = new Random().nextInt(list.length);
        return list[randomIndex];
    }
}
