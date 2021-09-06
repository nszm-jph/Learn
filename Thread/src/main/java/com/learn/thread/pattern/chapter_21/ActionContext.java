package com.learn.thread.pattern.chapter_21;


public class ActionContext {

    private static final ThreadLocal<Context> context = ThreadLocal.withInitial(Context::new);

    public static Context get() {
        return context.get();
    }

    static class Context {
        private String configuration;
        private String otherResource;

        public String getConfiguration() {
            return configuration;
        }

        public void setConfiguration(String configuration) {
            this.configuration = configuration;
        }

        public String getOtherResource() {
            return otherResource;
        }

        public void setOtherResource(String otherResource) {
            this.otherResource = otherResource;
        }
    }
}
