package net.TheIdo1.idos_first_mod.sound;

import com.google.common.eventbus.EventBus;
import net.TheIdo1.idos_first_mod.IdosFirstMod;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, IdosFirstMod.MOD_ID);

    public static final Supplier<SoundEvent> BONG_LIGHT = registerSoundEvent("bong_light");
    public static final Supplier<SoundEvent> BONG_RIP = registerSoundEvent("bong_rip");
    public static final Supplier<SoundEvent> BONG_FINISH = registerSoundEvent("bong_finish");
    public static final Supplier<SoundEvent> BONG_COUGH = registerSoundEvent("bong_cough");





    private static Supplier<SoundEvent> registerSoundEvent(String name) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(IdosFirstMod.MOD_ID, name);
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(id));
    }

    public static void register(IEventBus eventBus){
        SOUND_EVENTS.register(eventBus);
    }
}
