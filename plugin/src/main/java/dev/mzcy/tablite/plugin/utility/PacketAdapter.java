package dev.mzcy.tablite.plugin.utility;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import dev.mzcy.tablite.api.profile.FakePlayerProfile;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoRemovePacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket.Action;
import net.minecraft.network.protocol.game.ClientboundTabListPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameType;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.EnumSet;
import java.util.List;
import java.util.UUID;

public class PacketAdapter {

    public static void sendFakePlayer(Player viewer, FakePlayerProfile profile, int ping, int index) {
        ServerPlayer serverViewer = ((CraftPlayer) viewer).getHandle();

        UUID uuid = getUuidForIndex(index);

        GameProfile gameProfile = new GameProfile(uuid, profile.getName());
        if (profile.getSkinTexture() != null) {
            gameProfile.getProperties().put("textures", new Property("textures", profile.getSkinTexture()));
        }

        ServerPlayer fakePlayer = new ServerPlayer(
                serverViewer.server,
                serverViewer.serverLevel(),
                gameProfile,
                serverViewer.clientInformation()
        );

        ClientboundPlayerInfoUpdatePacket addPacket = new ClientboundPlayerInfoUpdatePacket(
                EnumSet.of(Action.ADD_PLAYER, Action.UPDATE_GAME_MODE, Action.UPDATE_LATENCY, Action.UPDATE_DISPLAY_NAME),
                List.of(
                        new ClientboundPlayerInfoUpdatePacket.Entry(
                                fakePlayer.getUUID(),
                                fakePlayer.getGameProfile(),
                                true,
                                ping,
                                GameType.SURVIVAL,
                                Component.literal(profile.getName()),
                                false,
                                index,
                                null
                        )
                )
        );

        serverViewer.connection.send(addPacket);
    }


    public static void removeFakePlayer(Player viewer, int index) {
        ServerPlayer serverViewer = ((CraftPlayer) viewer).getHandle();

        // This assumes the UUID used is deterministic per index
        UUID uuid = getUuidForIndex(index);
        ClientboundPlayerInfoRemovePacket removePacket = new ClientboundPlayerInfoRemovePacket(List.of(uuid));

        serverViewer.connection.send(removePacket);
    }

    public static void sendHeaderFooter(Player viewer, Component header, Component footer) {
        ServerPlayer serverViewer = ((CraftPlayer) viewer).getHandle();
        serverViewer.connection.send(new ClientboundTabListPacket(
                header,
                footer
        ));
    }

    private static UUID getUuidForIndex(int index) {
        return UUID.nameUUIDFromBytes(("tablite-fake-slot-" + index).getBytes());
    }
}
