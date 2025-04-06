package dev.mzcy.tablite.api.view;

import dev.mzcy.tablite.api.entry.TabEntry;
import net.minecraft.network.chat.Component;

public interface TabView {

    void setHeader(Component header);

    void setFooter(Component footer);

    void set(int x, int y, TabEntry entry);

    void remove(int x, int y);

    void clear();

    void update();

}
