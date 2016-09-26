package me.flyness.sentry.transformer;

import javassist.*;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bjlizhitao on 2016/9/14.
 */
public class MethodTransformer implements ClassFileTransformer {
    public static final String prefix = "\nlong startTime = System.currentTimeMillis();\n";
    public static final String sufix = "\nlong endTime = System.currentTimeMillis();\n";
    public static final List<String> methodList = new ArrayList<String>(2);

    static {
        methodList.add("cc.fly.sentry.test.SentryTest.sayHello2");
    }

    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        if (className.startsWith("cc/fly/sentry/test")) {
            className = className.replaceAll("/", ".");

            CtClass ctClass = null;

            try {
                ctClass = ClassPool.getDefault().get(className);
                for (String method : methodList) {
                    if (method.startsWith(className)) {
                        //获取方法名
                        String methodName = method.substring(method.lastIndexOf('.') + 1, method.length());
                        String outputStr = "\nSystem.out.println(\"this method " + methodName + " cost:\" +(endTime - startTime) +\"ms.\");";
                        //得到这方法实例
                        CtMethod ctmethod = ctClass.getDeclaredMethod(methodName);
                        //新定义一个方法叫做比如sayHello$impl
                        String newMethodName = methodName + "$impl";
                        //原来的方法改个名字
                        ctmethod.setName(newMethodName);

                        //创建新的方法，复制原来的方法 ，名字为原来的名字
                        CtMethod newMethod = CtNewMethod.copy(ctmethod, methodName, ctClass, null);
                        //构建新的方法体
                        StringBuilder bodyStr = new StringBuilder();
                        bodyStr.append("{");
                        bodyStr.append(prefix);
                        //调用原有代码，类似于method();($$)表示所有的参数
                        bodyStr.append(newMethodName + "($$);\n");

                        bodyStr.append(sufix);
                        bodyStr.append(outputStr);

                        bodyStr.append("}");
                        //替换新方法
                        newMethod.setBody(bodyStr.toString());
                        //增加新方法
                        ctClass.addMethod(newMethod);
                    }
                }
                return ctClass.toBytecode();
            } catch (NotFoundException e) {
                e.printStackTrace();
            } catch (CannotCompileException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }
}
