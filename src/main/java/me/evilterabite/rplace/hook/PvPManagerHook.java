package me.evilterabite.rplace.hook;

import me.NoChance.PvPManager.PvPlayer;
import org.bukkit.entity.Player;

public class PvPManagerHook implements HookHandler.Hook {

    public boolean isInCombat(Player player) {
        return PvPlayer.get(player).isInCombat();
    }

}
