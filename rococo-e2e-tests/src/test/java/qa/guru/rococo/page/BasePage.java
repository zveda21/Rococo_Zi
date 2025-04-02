package qa.guru.rococo.page;

import qa.guru.rococo.config.Config;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public abstract class BasePage<T extends BasePage<?>> {

    protected static final Config CFG = Config.getInstance();

    public abstract T checkThatPageLoaded();

}
