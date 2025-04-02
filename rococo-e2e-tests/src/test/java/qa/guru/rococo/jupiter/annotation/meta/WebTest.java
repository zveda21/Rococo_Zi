package qa.guru.rococo.jupiter.annotation.meta;

import io.qameta.allure.junit5.AllureJunit5;
import org.junit.jupiter.api.extension.ExtendWith;
import qa.guru.rococo.jupiter.extension.ApiLoginExtension;
import qa.guru.rococo.jupiter.extension.BrowserExtension;
import qa.guru.rococo.jupiter.extension.TestMethodContextExtension;
import qa.guru.rococo.jupiter.extension.UserExtension;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@ExtendWith({
        BrowserExtension.class,
        AllureJunit5.class,
        UserExtension.class,
        ApiLoginExtension.class,
        TestMethodContextExtension.class
})
public @interface WebTest {
}
