package com.github.k24.prefsoven;

import com.github.k24.prefsoven.factory.PrefFieldFactory;

/**
 * Created by k24 on 2016/02/13.
 */
public class PrefsConfig {
    PrefFieldFactory prefFieldFactory;

    public PrefsConfig() {
    }

    private PrefsConfig(Builder builder) {
        prefFieldFactory = builder.prefFieldFactory;
    }

    public static Builder newBuilder() {
        return new Builder();
    }


    public static final class Builder {
        private PrefFieldFactory prefFieldFactory;

        private Builder() {
        }

        public Builder prefFieldFactory(PrefFieldFactory val) {
            prefFieldFactory = val;
            return this;
        }

        public PrefsConfig build() {
            return new PrefsConfig(this);
        }
    }
}
