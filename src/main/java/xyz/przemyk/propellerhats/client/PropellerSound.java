package xyz.przemyk.propellerhats.client;

import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.sounds.SoundSource;
import xyz.przemyk.propellerhats.PropHatsMod;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PropellerSound extends AbstractTickableSoundInstance {

    private static final Map<Integer, PropellerSound> PLAYING_FOR = Collections.synchronizedMap(new HashMap<>());
    private final Player player;
    private int fadeOut = -1;

    protected PropellerSound(Player player) {
        super(PropHatsMod.PROPELLER_SOUND_EVENT.get(), SoundSource.PLAYERS, SoundInstance.createUnseededRandom());
        this.player = player;
        this.looping = true;
        PLAYING_FOR.put(player.getId(), this);
    }

    public static boolean isPlaying(int entityId) {
        if (!PLAYING_FOR.containsKey(entityId)) {
            return false;
        }
        PropellerSound sound = PLAYING_FOR.get(entityId);
        return sound != null && !sound.isStopped();
    }

    @Override
    public void tick() {
        if (player.isSpectator()) {
            stop();
        }
        x = player.getX();
        y = player.getY();
        z = player.getZ();
        if (fadeOut < 0 && !PropHatsMod.isFlying(player)) {
            fadeOut = 0;
            synchronized (PLAYING_FOR) {
                PLAYING_FOR.remove(player.getId());
            }
        } else if (fadeOut >= 5) {
            stop();
        } else if (fadeOut >= 0) {
            volume = 1.0F - fadeOut / 5F;
            fadeOut++;
        }
    }
}
