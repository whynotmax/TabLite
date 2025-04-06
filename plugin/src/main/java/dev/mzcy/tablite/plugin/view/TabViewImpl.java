package dev.mzcy.tablite.plugin.view;

import dev.mzcy.tablite.api.entry.TabEntry;
import dev.mzcy.tablite.api.view.TabView;
import dev.mzcy.tablite.plugin.TabLitePlugin;
import dev.mzcy.tablite.plugin.utility.PacketAdapter;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import net.minecraft.network.chat.Component;
import org.bukkit.entity.Player;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TabViewImpl implements TabView {

    private static final int ROWS = 20;
    private static final int COLS = 4;

    final String identifier;

    Component header = Component.empty();
    Component footer = Component.empty();

    TabEntry[][] entries = new TabEntry[ROWS][COLS];

    public TabViewImpl(String identifier) {
        this.identifier = identifier;
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                entries[i][j] = TabEntry.EMPTY;
            }
        }
    }

    @Override
    public void set(int x, int y, TabEntry entry) {
        if (x < 0 || x >= ROWS || y < 0 || y >= COLS) {
            throw new IndexOutOfBoundsException("Invalid coordinates: (" + x + ", " + y + ")");
        }
        entries[x][y] = entry;
    }

    @Override
    public void remove(int x, int y) {
        if (x < 0 || x >= ROWS || y < 0 || y >= COLS) {
            throw new IndexOutOfBoundsException("Invalid coordinates: (" + x + ", " + y + ")");
        }
        entries[x][y] = TabEntry.EMPTY;
    }

    @Override
    public void clear() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                entries[i][j] = TabEntry.EMPTY;
            }
        }
    }

    @Override
    public void update() {
        Set<Player> players = TabLitePlugin.getInstance().getPlayersWithTabView(identifier);
        for (Player player : players) {
            PacketAdapter.sendHeaderFooter(player, header, footer);
        }
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                int index = i * COLS + j;
                TabEntry entry = entries[i][j];
                if (entry == null) continue;
                for (Player player : players) {
                    PacketAdapter.sendFakePlayer(player, entry.toFakePlayerProfile(), entry.getPing(), index);
                }
            }
        }
    }
}
