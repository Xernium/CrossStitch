package com.velocitypowered.crossstitch;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;

import java.io.*;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Set;

public class CrossStitch implements DedicatedServerModInitializer {

    private static Set<String> UNSUPPORTED_NAMES = ImmutableSet.of("minecraft:testargument", "brigadier:reallyunsupported");
    private final static Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    @Override
    public void onInitializeServer() {
        Path configDir = FabricLoader.getInstance().getConfigDir();

        if (!configDir.toFile().exists()) {
            configDir.toFile().mkdirs();
        }

        File configFile = configDir.resolve("crossstitch-overrides.json").toFile();

        if (!configFile.exists()) {
            try (JsonWriter jw = new JsonWriter(new FileWriter(configFile))) {
                GSON.toJson(UNSUPPORTED_NAMES, Set.class, jw);
            } catch (IOException err) {
                throw new UncheckedIOException("Invalid config environemnt" , err);
            }

        }

        try (JsonReader jr = new JsonReader(new FileReader(configFile))) {
            UNSUPPORTED_NAMES = ImmutableSet.copyOf((Set<String>) GSON.fromJson(jr, Set.class));
        } catch (IOException err) {
            throw new UncheckedIOException("Invalid config environemnt" , err);
        }
    }

    public static Set<String> getUnsupportedNames() {
        return UNSUPPORTED_NAMES;
    }
}
