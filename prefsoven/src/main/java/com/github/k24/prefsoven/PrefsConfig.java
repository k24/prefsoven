package com.github.k24.prefsoven;

import com.github.k24.prefsoven.factory.AbstractElementFactory;
import com.github.k24.prefsoven.factory.AbstractFieldFactory;

/**
 * Created by k24 on 2016/02/13.
 */
public class PrefsConfig {
    AbstractFieldFactory fieldFactory;
    AbstractElementFactory elementFactory;

    public PrefsConfig() {
    }

    private PrefsConfig(Builder builder) {
        fieldFactory = builder.fieldFactory;
        elementFactory = builder.elementFactory;
    }

    public static Builder newBuilder() {
        return new Builder();
    }


    public static final class Builder {
        private AbstractFieldFactory fieldFactory;
        private AbstractElementFactory elementFactory;

        private Builder() {
        }

        public Builder fieldFactory(AbstractFieldFactory val) {
            fieldFactory = val;
            return this;
        }

        public Builder elementFactory(AbstractElementFactory val) {
            elementFactory = val;
            return this;
        }

        public PrefsConfig build() {
            return new PrefsConfig(this);
        }
    }
}
