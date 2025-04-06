package dev.mzcy.tablite.api;

import dev.mzcy.tablite.api.view.TabView;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public interface TabLiteAPI {

    TabLiteAPI api();

    void registerTabView(@NotNull String identifier, @NotNull TabView tabView);

    void showTabView(@NotNull String identifier, @NotNull Player player);

    void hideTabView(@NotNull String identifier, @NotNull Player player);

    @Nullable TabView getTabView(@NotNull String identifier);

    @NotNull TabView createTabView(@NotNull String identifier);

    @NotNull Set<Player> getPlayersWithTabView(@NotNull String identifier);

}
