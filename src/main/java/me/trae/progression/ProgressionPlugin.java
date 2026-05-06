package me.trae.progression;

import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import io.github.trae.di.annotations.type.Application;
import me.trae.core.CorePlugin;
import me.trae.core.framework.Plugin;

import javax.annotation.Nonnull;

@Application(dependencies = CorePlugin.class)
public class ProgressionPlugin extends Plugin {

    public ProgressionPlugin(@Nonnull final JavaPluginInit javaPluginInit) {
        super(javaPluginInit);
    }
}