/*
 * The MIT License
 *
 * Copyright (c) 2004-2009, Sun Microsystems, Inc., Kohsuke Kawaguchi
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.jvnet.hudson.test;

import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.BuildListener;
import hudson.model.Descriptor;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;
import java.io.IOException;

/**
 * Partial {@link Builder} implementation for writing a one-off throw-away builder used during tests.
 *
 * <p>
 * Because this builder tends to be written as an inner class, this builder doesn't write itself
 * to a disk when persisted. Configuration screen won't work, either.
 *
 * @author Kohsuke Kawaguchi
 */
public abstract class TestBuilder extends Builder {

    @Override
    public abstract boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener)
            throws InterruptedException, IOException;

    @Override
    public Descriptor<Builder> getDescriptor() {
        // throw new UnsupportedOperationException();
        return new BuildStepDescriptor<>() {
            @Override
            public boolean isApplicable(Class<? extends AbstractProject> jobType) {
                return true;
            }
        };
    }

    protected Object writeReplace() {
        return new Object();
    }

    @FunctionalInterface
    public interface Body {
        void perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener)
                throws InterruptedException, IOException;
    }

    /**
     * More convenient form that can be used with a lambda.
     */
    public static Builder of(Body body) {
        return new TestBuilder() {
            @Override
            public boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener)
                    throws InterruptedException, IOException {
                body.perform(build, launcher, listener);
                return true;
            }
        };
    }
}
