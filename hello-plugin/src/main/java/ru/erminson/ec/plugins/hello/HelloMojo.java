package ru.erminson.ec.plugins.hello;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

@Mojo(name = "hello", defaultPhase = LifecyclePhase.INSTALL)
public class HelloMojo extends AbstractMojo {

    @Parameter(defaultValue = "Hello from my custom plugin!", property = "hello.message")
    String message;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info(message);
    }
}
