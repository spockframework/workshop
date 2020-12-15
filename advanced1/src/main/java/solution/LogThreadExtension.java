package solution;
import org.spockframework.runtime.model.*;
import org.spockframework.runtime.extension.*;

public class LogThreadExtension implements IAnnotationDrivenExtension<LogThread> {
    @Override
    public void visitSpecAnnotation(LogThread annotation, SpecInfo spec) {
        spec.getFeatures().forEach(feature-> feature.addIterationInterceptor(new LogThreadInterceptor()));
    }

    @Override
    public void visitFeatureAnnotation(LogThread annotation, FeatureInfo feature){
        feature.addIterationInterceptor(new LogThreadInterceptor());
    }

}