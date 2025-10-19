package net.TheIdo1.idos_first_mod.entity.goals;

import net.TheIdo1.idos_first_mod.effect.ModEffects;
import net.TheIdo1.idos_first_mod.entity.custom.SkibEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;

import java.util.EnumSet;

public class DetectHighPlayerGoal extends Goal {

    private final SkibEntity skib;
    private final double speed;           // מהירות ריצה אל המטרה
    private final double followRange;     // טווח חיפוש שחקנים
    private final double stopDistance;    // מרחק עצירה ליד השחקן
    private Player target;
    private int recheckTicker;

    private static final TargetingConditions COND = TargetingConditions.forNonCombat()
            .ignoreLineOfSight() // אם אתה רוצה לדרוש LOS, הסר את זה
            .ignoreInvisibilityTesting();

    public DetectHighPlayerGoal(SkibEntity skib, double speed, double followRange, double stopDistance) {
        this.skib = skib;
        this.speed = speed;
        this.followRange = followRange;
        this.stopDistance = stopDistance;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (recheckTicker-- > 0) return false;
        recheckTicker = 10; // בדיקה כל חצי שנייה בערך

        // מצא שחקן קרוב עם HIGH_EFFECT
        Player nearest = skib.level().getNearestPlayer(
                skib.getX(),
                skib.getY(),
                skib.getZ(),
                followRange,
                p -> p instanceof Player player && player.hasEffect(ModEffects.HIGH_EFFECT)
        );

        if (nearest == null) return false;
        if (nearest.distanceToSqr(skib) > followRange * followRange) return false;

        this.target = nearest;
        return true;
    }

    @Override
    public boolean canContinueToUse() {
        if (target == null || !target.isAlive()) return false;
        if (!target.hasEffect(ModEffects.HIGH_EFFECT)) return false;
        if (target.distanceToSqr(skib) > (followRange * followRange)) return false;
        return true;
    }

    @Override
    public void start() {
        skib.setHeadSpinning(true);
        // לא חובה, אפשר סאונד/מחווה
        // skib.playSound(...);
    }

    @Override
    public void stop() {
        target = null;
        skib.getNavigation().stop();
        skib.setHeadSpinning(false);
    }

    @Override
    public void tick() {
        if (target == null) return;

        skib.setHeadSpinning(true);

        double dist2 = skib.distanceToSqr(target);
        double stop2 = stopDistance * stopDistance;

        if (dist2 > stop2) {
            // התקרב
            skib.getNavigation().moveTo(target, speed);
        } else {
            // מספיק קרוב – עצור
            skib.getNavigation().stop();
        }
    }
}
