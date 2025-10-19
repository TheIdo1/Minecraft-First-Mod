package net.TheIdo1.idos_first_mod.entity.custom;

import net.TheIdo1.idos_first_mod.entity.ModEntities;
import net.TheIdo1.idos_first_mod.entity.goals.*;
import net.TheIdo1.idos_first_mod.item.BongItem;
import net.TheIdo1.idos_first_mod.item.ModItems;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class SkibEntity extends Animal {

    public final AnimationState idleAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;

    public final AnimationState bongUseAnimationState = new AnimationState();
    private static final EntityDataAccessor<Boolean> USING_BONG =
            SynchedEntityData.defineId(SkibEntity.class, EntityDataSerializers.BOOLEAN);
    private int bongUseTimeout;

    public final AnimationState headSpinAnimationState = new AnimationState();
    private static final EntityDataAccessor<Boolean> HEAD_SPIN =
            SynchedEntityData.defineId(SkibEntity.class, EntityDataSerializers.BOOLEAN);
    private int headSpinTimeout;

    private final Item WEED_ITEM = ModItems.WEED_NUG.asItem();

    public SkibEntity(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
        this.setCustomNameVisible(false);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));

        this.goalSelector.addGoal(1, new PanicGoal(this, 1.25));
        this.goalSelector.addGoal(2, new BreedGoal(this, 1.0));


        this.goalSelector.addGoal(0, new DetectHighPlayerGoal(this, 1.5f, 20, 2));
        this.goalSelector.addGoal(3, new StealBongGoal(this, 1.2f, 10, 2));
        this.goalSelector.addGoal(3, new FindAndFillWaterGoal(this, 1f, 10, 2));
        this.goalSelector.addGoal(3, new GetWeedBongGoal(this, 1f, 10, 2));
        this.goalSelector.addGoal(3, new DumpBongWaterGoal(this));
        this.goalSelector.addGoal(3, new UseBongGoal(this));

        this.goalSelector.addGoal(4, new TemptGoal(this, 1.5f, stack -> stack.is(WEED_ITEM), false));

        this.goalSelector.addGoal(5, new FollowParentGoal(this, 1.25));

        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));

    }

    public static AttributeSupplier.Builder createAttributes(){
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 15d)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.FOLLOW_RANGE, 240)
                .add(Attributes.TEMPT_RANGE, 20);
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(WEED_ITEM);
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel level, AgeableMob otherParent) {
        return ModEntities.SKIB.get().create(level, EntitySpawnReason.BREEDING);
    }

    private void setupAnimationStates(){
        if(this.idleAnimationTimeout <= 0) {
            this.idleAnimationTimeout = 20 * 2;
            this.idleAnimationState.start(this.tickCount);
        } else {
            --this.idleAnimationTimeout;
        }

        if(this.headSpinTimeout <=0){
            if(this.isBaby() || this.isHeadSpinning()){
                this.headSpinTimeout = 70;
                this.headSpinAnimationState.start(this.tickCount);
            }
        } else {
            --this.headSpinTimeout;
        }

        if (this.isUsingBong()) {
            if(this.bongUseTimeout <= 0){
                bongUseTimeout = 30;
                this.bongUseAnimationState.start(this.tickCount);
            } else {
                --bongUseTimeout;
            }
        } else {
            this.bongUseAnimationState.stop();
        }
    }

    @Override
    public void tick() {
        super.tick();
        if(this.level().isClientSide()) {
            this.setupAnimationStates();
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(USING_BONG, false);
        builder.define(HEAD_SPIN, false);
    }

    public boolean isUsingBong() {
        return this.entityData.get(USING_BONG);
    }

    public void setUsingBong(boolean v) {
        this.entityData.set(USING_BONG, v);
    }

    public boolean isHeadSpinning() {
        return this.entityData.get(HEAD_SPIN);
    }
    public void setHeadSpinning(boolean v) {
        this.entityData.set(HEAD_SPIN, v);
    }

}
