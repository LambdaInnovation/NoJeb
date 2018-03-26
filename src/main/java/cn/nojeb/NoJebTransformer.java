package cn.nojeb;

import net.minecraft.launchwrapper.IClassTransformer;

public class NoJebTransformer implements IClassTransformer {

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        return basicClass;
    }

}
