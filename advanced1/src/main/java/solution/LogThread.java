package solution;

import java.lang.annotation.*;
import org.spockframework.runtime.extension.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@ExtensionAnnotation(LogThreadExtension.class)
public @interface LogThread {

}
