package cn.nojeb;

import com.mojang.authlib.*;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.properties.PropertyMap;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilMinecraftSessionService;
import com.mojang.authlib.yggdrasil.response.Response;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.lang.reflect.Field;
import java.net.URL;

public class AuthenticationHandler {

    public static void load() {
        if (System.getProperty("noauth") != null) {
            NoJeb.log.info("Authentication service will be muted.");
            if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
                loadClient();
            }
        }
    }

    @SideOnly(Side.CLIENT)
    static void loadClient() {
        Minecraft minecraft = Minecraft.getMinecraft();
        try {
            Field field = Minecraft.class.getDeclaredField("sessionService");
            field.setAccessible(true);
            field.set(minecraft,
                new OverrideSessionService(
                    ((YggdrasilMinecraftSessionService) field.get(minecraft)).getAuthenticationService())
            );
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static class OverrideSessionService extends YggdrasilMinecraftSessionService {
        protected OverrideSessionService(YggdrasilAuthenticationService authenticationService) {
            super(new YggdrasilAuthenticationService(authenticationService.getProxy(), authenticationService.getClientToken()) {
                @Override
                protected <T extends Response> T makeRequest(URL url, Object input, Class<T> classOfT) throws AuthenticationException {
                    throw new AuthenticationException("Request blocked");
                }
            });
        }
    }

}
