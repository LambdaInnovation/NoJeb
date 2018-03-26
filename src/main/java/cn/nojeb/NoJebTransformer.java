package cn.nojeb;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.*;

import java.util.Objects;

import static org.objectweb.asm.Opcodes.*;

public class NoJebTransformer implements IClassTransformer {
    boolean noSound = System.getProperty("nosound") != null;
    boolean noModel = System.getProperty("nomodel") != null;

    public NoJebTransformer() {
        if (noSound)
            ModContainer.log.info("Sound will be muted.");
        if (noModel)
            ModContainer.log.info("Model will not be loaded.");
    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if (noSound && transformedName.equals("net.minecraft.client.audio.SoundHandler")) {
            return transformSoundHandler(basicClass);
        }
        if (noModel && transformedName.equals("net.minecraft.client.renderer.block.model.ModelManager")) {
            return transformModelManager(basicClass);
        }
        return basicClass;
    }

    private byte[] transformSoundHandler(byte[] bytes) {
        return transformResourceHandler(bytes, "net/minecraft/client/audio/SoundHandler", "onSoundManagerReload");
    }

    private byte[] transformModelManager(byte[] bytes) {
        return transformResourceHandler(bytes, "net/minecraft/client/renderer/block/model/ModelManager",
            "onModelManagerReload");
    }

    private byte[] transformResourceHandler(byte[] bytes, String classSignature, String methodName) {
        ClassWriter cw = new ClassWriter(0);

        // MD: net/minecraft/client/audio/SoundHandler/onResourceManagerReload (Lnet/minecraft/client/resources/IResourceManager;)V cho/a (Lcep;)V
        String testName = "onResourceManagerReload";
        String testDesc = "(Lnet/minecraft/client/resources/IResourceManager;)V";
        ClassVisitor cv = new ClassVisitor(Opcodes.ASM5, cw) {

            @Override
            public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
                if (Objects.equals(name, testName) && Objects.equals(desc, testDesc)) {
                    MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
                    mv.visitCode();
                    mv.visitVarInsn(ALOAD, 1);
                    mv.visitVarInsn(ALOAD, 0);
                    mv.visitMethodInsn(INVOKESTATIC, "cn/nojeb/ResourceLoadHandler", methodName,
                        "(Lnet/minecraft/client/resources/IResourceManager;L" + classSignature + ";)V", false);
                    mv.visitInsn(RETURN);
                    mv.visitMaxs(2, 2);
                    mv.visitEnd();

                    return null;
                } else {
                    return super.visitMethod(access, name, desc, signature, exceptions);
                }
            }
        };
        ClassReader cr = new ClassReader(bytes);
        cr.accept(cv, 0);
        return cw.toByteArray();
    }

}
