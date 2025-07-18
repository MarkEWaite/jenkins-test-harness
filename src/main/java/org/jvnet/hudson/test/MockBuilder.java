package org.jvnet.hudson.test;

import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.Extension;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import hudson.model.Descriptor;
import hudson.model.Result;
import hudson.tasks.Builder;
import java.io.IOException;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.StaplerRequest2;

/**
 * Forces the build result to be some pre-configured value.
 *
 * @author Kohsuke Kawaguchi
 */
public class MockBuilder extends Builder {
    public final Result result;

    public MockBuilder(Result result) {
        this.result = result;
    }

    @Override
    public boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener)
            throws InterruptedException, IOException {
        listener.getLogger().println("Simulating a specific result code " + result);
        build.setResult(result);
        return true;
    }

    @Override
    public Descriptor<Builder> getDescriptor() {
        return new DescriptorImpl();
    }

    @Extension
    public static final class DescriptorImpl extends Descriptor<Builder> {
        @Override
        public Builder newInstance(StaplerRequest2 req, @NonNull JSONObject data) {
            throw new UnsupportedOperationException();
        }
    }
}
