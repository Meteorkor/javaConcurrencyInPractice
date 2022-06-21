package com.meteor.lazy;

import org.junit.jupiter.api.Test;

import net.jcip.annotations.ThreadSafe;

@ThreadSafe
public class LazyTest {

    @Test
    void lazyHolderTest() throws InterruptedException {

        System.out.println("callBefore");
        LazyStaticValue.cAB();
        System.out.println("LazyValue.getLazyValue() : " + LazyStaticValue.getLazyValue());
        System.out.println("LazyValue.getLazyValue() : " + LazyStaticValue.getLazyValue());
        System.out.println("callAfter");
    }

    @Test
    void lazyVarTest() {
        LazyValue lazyValue = new LazyValue();
        System.out.println("callBefore");
        System.out.println("lazyValue.getValue() : " + lazyValue.getValue());
        System.out.println("lazyValue.getValue() : " + lazyValue.getValue());
        System.out.println("callAfter");
    }

    private class LazyNormalValue {
        private String value;

        public String getValue() {
            if (value == null) {
                value = getInitValue();
            }
            return value;
        }

        public String getValueDbc() {
            if (value != null) {
                return value;
            }
            synchronized (this) {
                if (value == null) {
                    value = getInitValue();
                }
                return value;
            }
        }

        private String getInitValue() {
            return "TEXT";
        }

    }

    private static class LazyStaticValue {
        public static String getLazyValue() {

            return LazyValueHolder.valaue;
        }

        private static String cAB() {
            return "";
        }

        private static String createText() {
            System.out.println("static INIT");
            return "VALUE";
        }

        private static class LazyValueHolder {
            public static final String valaue = createText();
        }
    }

    private class LazyValue {
        private String value;

        public String getValue() {
            String local = value;
            if (local != null) {
                return local;
            }
            synchronized (this) {
                if (value == null) {
                    value = initMethod();
                }
                return value;
            }
        }

        private String initMethod() {
            System.out.println("INIT!!!");
            return "VALUE";
        }
    }

}
