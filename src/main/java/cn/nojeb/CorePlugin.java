package cn.nojeb;

import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public class CorePlugin implements IFMLLoadingPlugin {

    private static boolean deobfEnabled;

    public static boolean isDeobfEnabled() {
        return deobfEnabled;
    }

    @Override
    public String[] getASMTransformerClass() {
        return new String[] {
            "cn.nojeb.NoJebTransformer"
        };
    }

    @Override
    public String getModContainerClass() {
        return "cn.nojeb.ModContainer";
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {
        deobfEnabled = (Boolean) data.get("runtimeDeobfuscationEnabled");
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }

}
