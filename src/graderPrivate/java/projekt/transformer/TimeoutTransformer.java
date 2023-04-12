package projekt.transformer;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.sourcegrade.jagr.api.testing.ClassTransformer;

public class TimeoutTransformer implements ClassTransformer {

    @Override
    public String getName() {
        return "TimeoutTransformer";
    }

    @Override
    public void transform(ClassReader reader, ClassWriter writer) {
        if ((reader.getClassName().startsWith("projekt/delivery/service") && !reader.getClassName().equals("projekt/delivery/service/BasicDeliveryService")
            || reader.getClassName().startsWith("projekt/solution"))
            || reader.getClassName().equals("projekt/delivery/routing/DijkstraPathCalculator")
            || reader.getClassName().equals("projekt/delivery/routing/DijkstraPathCalculator$DijkstraNode")
            || reader.getClassName().equals("projekt/delivery/routing/AbstractOccupied")
            || reader.getClassName().equals("projekt/delivery/routing/OccupiedNodeImpl")
            || reader.getClassName().equals("projekt/delivery/routing/OccupiedEdgeImpl")) {
            reader.accept(writer, 0);
        } else {
            reader.accept(new CV(Opcodes.ASM9, writer), 0);
        }
    }

    private static class CV extends ClassVisitor {

        public CV(int api, ClassVisitor classVisitor) {
            super(api, classVisitor);
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
            return new MV(api, super.visitMethod(access, name, descriptor, signature, exceptions));
        }
    }

    private static class MV extends MethodVisitor {

        public MV(int api, MethodVisitor methodVisitor) {
            super(api, methodVisitor);
        }

        @Override
        public void visitCode() {
            visitTimeoutIsns();
            super.visitCode();
        }

        @Override
        public void visitJumpInsn(int opcode, Label label) {
            visitTimeoutIsns();
            super.visitJumpInsn(opcode, label);
        }

        private void visitTimeoutIsns() {
            visitMethodInsn(
                Opcodes.INVOKESTATIC,
                "org/sourcegrade/jagr/core/executor/TimeoutHandler",
                "checkTimeout",
                "()V",
                false
            );
        }
    }
}
