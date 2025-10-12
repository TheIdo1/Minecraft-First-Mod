package net.TheIdo1.idos_first_mod.entity;

import net.TheIdo1.idos_first_mod.IdosFirstMod;
import net.TheIdo1.idos_first_mod.entity.custom.SkibEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, IdosFirstMod.MOD_ID);

    public static final ResourceKey<EntityType<?>> SKIB_KEY = ResourceKey.create(Registries.ENTITY_TYPE,
            ResourceLocation.fromNamespaceAndPath(IdosFirstMod.MOD_ID, "skib"));
    public static final Supplier<EntityType<SkibEntity>> SKIB =
            ENTITY_TYPES.register("skib", () -> EntityType.Builder.of(SkibEntity::new, MobCategory.CREATURE)
                    .sized(0.6f, 1.15f).build(SKIB_KEY));


    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
