package com.meteor.lazy;

import org.junit.jupiter.api.Test;

public class LazyTest {

    @Test
    void lazyHolderTest() {
        System.out.println("callBefore");
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

    private static class LazyStaticValue {
        public static String getLazyValue() {
            return LazyValueHolder.valaue;
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
