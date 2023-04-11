package projekt.transformer;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.sourcegrade.jagr.api.testing.ClassTransformer;

public class FinalClassTransformer implements ClassTransformer {

    @Override
    public String getName() {
        return "FinalTransformer";
    }

    @Override
    public void transform(ClassReader reader, ClassWriter writer) {
        reader.accept(new CV(Opcodes.ASM9, writer), 0);
    }

    private static int makeNonFinal(int access) {
        return access & ~Opcodes.ACC_FINAL;
    }

    private static class CV extends ClassVisitor {
        public CV(int api, ClassVisitor classVisitor) {
            super(api, classVisitor);
        }

        @Override
        public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
            super.visit(version, makeNonFinal(access), name, signature, superName, interfaces);
        }
    }

}
