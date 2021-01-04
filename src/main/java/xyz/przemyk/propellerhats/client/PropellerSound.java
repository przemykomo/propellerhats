package xyz.przemyk.propellerhats.client;

import net.minecraft.client.audio.TickableSound;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundCategory;
import xyz.przemyk.propellerhats.PropHatsMod;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PropellerSound extends TickableSound {

    private static final Map<Integer, PropellerSound> PLAYING_FOR = Collections.synchronizedMap(new HashMap<>());
    private final PlayerEntity player;
    private int fadeOut = -1;

    protected PropellerSound(PlayerEntity player) {
        super(PropHatsMod.PROPELLER_SOUND_EVENT.get(), SoundCategory.PLAYERS);
        this.player = player;
        this.repeat = true;
        PLAYING_FOR.put(player.getEntityId(), this);
    }

    public static boolean isPlaying(int entityId) {
        if (!PLAYING_FOR.containsKey(entityId)) {
            return false;
        }
        PropellerSound sound = PLAYING_FOR.get(entityId);
        return sound != null && !sound.isDonePlaying();
    }

    @Override
    public void tick() {
        if (player.isSpectator()) {
            finishPlaying();
        }
        x = player.getPosX();
        y = player.getPosY();
        z = player.getPosZ();
        if (fadeOut < 0 && !PropHatsMod.isFlying(player)) {
            fadeOut = 0;
            synchronized (PLAYING_FOR) {
                PLAYING_FOR.remove(player.getEntityId());
            }
        } else if (fadeOut >= 5) {
            finishPlaying();
        } else if (fadeOut >= 0) {
            volume = 1.0F - fadeOut / 5F;
            fadeOut++;
        }
    }
}
