package model;

import entity.Config;
import org.junit.jupiter.api.Test;

public class ConfigLoaderTest {
    @Test
    public void loadConfigTest(){
        try {
            Config config = new ConfigLoader().load();
        } catch (Exception e){
            assert false;
        }
    }
}
