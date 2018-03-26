package cn.nojeb;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Logger;

@Mod(modid = NoJeb.MODID, version = NoJeb.VERSION)
public class NoJeb
{
    public static final String MODID = "nojeb";
    public static final String VERSION = "@VERSION@";

    /**
     * Whether we are in development (debug) mode.
     */
    public static final boolean DEBUG = VERSION.startsWith("@");

    public static Logger log;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        log = event.getModLog();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        AuthenticationHandler.load();
    }

}
