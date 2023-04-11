package projekt.transformer;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.sourcegrade.jagr.api.testing.ClassTransformer;

public class UnitTestsTransformer implements ClassTransformer {

    @Override
    public String getName() {
        return "FileSystemIOFactory";
    }

    @Override
    public void transform(final ClassReader reader, final ClassWriter writer) {
        if ("projekt/base/LocationUnitTests".equals(reader.getClassName()) ||
            "projekt/delivery/routing/RegionImplUnitTests".equals(reader.getClassName()) ||
            "projekt/delivery/routing/NodeImplUnitTests".equals(reader.getClassName()) ||
            "projekt/delivery/routing/EdgeImplUnitTests".equals(reader.getClassName())) {
            reader.accept(new CV(Opcodes.ASM9, writer), 0);
        } else {
            reader.accept(writer, 0);
        }
    }

    private static class CV extends ClassVisitor {

        public CV(final int api, final ClassVisitor classVisitor) {
            super(api, classVisitor);
        }

        @Override
        public MethodVisitor visitMethod(final int access, final String name, final String descriptor,
                                         final String signature, final String[] exceptions) {
            if ("initialize".equals(name)) {
                return new MV(api, super.visitMethod(access, name, descriptor, signature, exceptions));
            }
            return super.visitMethod(access, name, descriptor, signature, exceptions);
        }

        private static class MV extends MethodVisitor {

            public MV(final int api, final MethodVisitor methodVisitor) {
                super(api, methodVisitor);
            }

            @Override
            public void visitTypeInsn(final int opcode, final String type) {
                if (opcode == Opcodes.NEW) {
                    if (("projekt/ObjectUnitTests").equals(type)) {
                        super.visitTypeInsn(opcode, "projekt/solution/TutorObjectUnitTests");
                    } else if (("projekt/ComparableUnitTests").equals(type)) {
                        super.visitTypeInsn(opcode, "projekt/solution/TutorComparableUnitTests");
                    }else if (("projekt/base/Location").equals(type)) {
                        super.visitTypeInsn(opcode, "projekt/solution/TutorLocation");
                    }else if (("projekt/delivery/routing/NodeImpl").equals(type)) {
                        super.visitTypeInsn(opcode, "projekt/solution/TutorNode");
                    }else if (("projekt/delivery/routing/EdgeImpl").equals(type)) {
                        super.visitTypeInsn(opcode, "projekt/solution/TutorEdge");
                    }else if (("projekt/delivery/routing/RegionImpl").equals(type)) {
                        super.visitTypeInsn(opcode, "projekt/solution/TutorRegion");
                    }else {
                        super.visitTypeInsn(opcode, type);
                    }
                } else {
                    super.visitTypeInsn(opcode, type);
                }
            }

            @Override
            public void visitMethodInsn(final int opcode, final String owner, final String name,
                                        final String descriptor, final boolean isInterface) {
                if (opcode == Opcodes.INVOKESPECIAL && "<init>".equals(name)) {
                    if (("projekt/ObjectUnitTests").equals(owner)) {
                        super.visitMethodInsn(
                            Opcodes.INVOKESPECIAL,
                            "projekt/solution/TutorObjectUnitTests",
                            name,
                            descriptor,
                            false
                        );
                    } else if (("projekt/ComparableUnitTests").equals(owner)) {
                        super.visitMethodInsn(
                            Opcodes.INVOKESPECIAL,
                            "projekt/solution/TutorComparableUnitTests",
                            name,
                            descriptor,
                            false
                        );
                    } else if (("projekt/base/Location").equals(owner)) {
                        super.visitMethodInsn(
                            Opcodes.INVOKESPECIAL,
                            "projekt/solution/TutorLocation",
                            name,
                            descriptor,
                            false
                        );
                    } else if (("projekt/delivery/routing/NodeImpl").equals(owner)) {
                        super.visitMethodInsn(
                            Opcodes.INVOKESPECIAL,
                            "projekt/solution/TutorNode",
                            name,
                            descriptor,
                            false
                        );
                    } else if (("projekt/delivery/routing/EdgeImpl").equals(owner)) {
                        super.visitMethodInsn(
                            Opcodes.INVOKESPECIAL,
                            "projekt/solution/TutorEdge",
                            name,
                            descriptor,
                            false
                        );
                    } else if (("projekt/delivery/routing/RegionImpl").equals(owner)) {
                        super.visitMethodInsn(
                            Opcodes.INVOKESPECIAL,
                            "projekt/solution/TutorRegion",
                            name,
                            descriptor,
                            false
                        );
                    } else {
                        super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
                    }
                } else {
                    super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
                }
            }
        }
    }

}
