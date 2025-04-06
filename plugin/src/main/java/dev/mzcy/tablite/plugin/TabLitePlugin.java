package dev.mzcy.tablite.plugin;

import dev.mzcy.tablite.api.TabLiteAPI;
import dev.mzcy.tablite.api.view.TabView;
import dev.mzcy.tablite.plugin.view.TabViewImpl;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class TabLitePlugin extends JavaPlugin implements TabLiteAPI {

    @Getter
    private static TabLitePlugin instance;

    private final Map<String, TabView> tabViews = new HashMap<>();
    private final Map<Player, TabView> playerTabViews = new HashMap<>();

    @Override
    public void onEnable() {
        log.info("TabLitePlugin is starting...");
        instance = this;
        log.info("TabLitePlugin has been enabled.");
    }

    @Override
    public TabLiteAPI api() {
        return instance;
    }

    @Override
    public void registerTabView(@NotNull String identifier, @NotNull TabView tabView) {
        if (tabViews.containsKey(identifier)) {
            log.warn("TabView with identifier {} already exists. Overwriting.", identifier);
        }
        tabViews.put(identifier, tabView);
        log.info("TabView with identifier {} has been registered.", identifier);
    }

    @Override
    public void showTabView(@NotNull String identifier, @NotNull Player player) {
        TabView tabView = tabViews.get(identifier);
        if (tabView == null) {
            log.warn("TabView with identifier {} not found. Cannot show to player {}.", identifier, player.getName());
            return;
        }
        playerTabViews.put(player, tabView);
        log.info("Showing TabView with identifier {} to player {}.", identifier, player.getName());
    }

    @Override
    public void hideTabView(@NotNull String identifier, @NotNull Player player) {
        TabView tabView = tabViews.get(identifier);
        if (tabView == null) {
            log.warn("TabView with identifier {} not found. Cannot hide from player {}.", identifier, player.getName());
            return;
        }
        playerTabViews.remove(player);
        log.info("Hiding TabView with identifier {} from player {}.", identifier, player.getName());
    }

    @Override
    public @Nullable TabView getTabView(@NotNull String identifier) {
        TabView tabView = tabViews.get(identifier);
        if (tabView == null) {
            log.warn("TabView with identifier {} not found.", identifier);
            return null;
        }
        log.info("Retrieved TabView with identifier {}.", identifier);
        return tabView;
    }

    @Override
    public @NotNull TabView createTabView(@NotNull String identifier) {
        if (tabViews.containsKey(identifier)) {
            log.warn("TabView with identifier {} already exists. Cannot create a new one.", identifier);
            return tabViews.get(identifier);
        }
        TabView newTabView = new TabViewImpl(identifier);
        tabViews.put(identifier, newTabView);
        log.info("Created and registered new TabView with identifier {}.", identifier);
        return newTabView;
    }

    @Override
    public @NotNull Set<Player> getPlayersWithTabView(@NotNull String identifier) {
        TabView tabView = tabViews.get(identifier);
        if (tabView == null) {
            log.warn("TabView with identifier {} not found. Cannot retrieve players.", identifier);
            return Set.of();
        }
        Set<Player> players = playerTabViews.entrySet().stream()
                .filter(entry -> entry.getValue().equals(tabView))
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
        log.info("Retrieved players with TabView identifier {}: {}", identifier, players);
        return players;
    }

}