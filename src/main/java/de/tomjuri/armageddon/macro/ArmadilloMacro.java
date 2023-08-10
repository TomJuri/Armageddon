package de.tomjuri.armageddon.macro;

import de.tomjuri.armageddon.Armageddon;
import de.tomjuri.armageddon.config.ArmageddonConfig;
import de.tomjuri.armageddon.util.*;
import de.tomjuri.macroframework.macro.ConditionalState;
import de.tomjuri.macroframework.macro.Macro;
import de.tomjuri.macroframework.macro.TimedState;
import net.minecraft.entity.monster.EntityZombie;

import java.util.Arrays;

public class ArmadilloMacro extends Macro {

    private static int i = -1;

    private static final TimedState SWITCH_TO_ROD = new TimedState() {
        @Override
        public void execute() {
            Ref.player().inventory.currentItem = ArmageddonConfig.rodSlot - 1;
        }

        @Override
        public long delayAfter() {
            return 100;
        }
    };
    private static final TimedState SUMMON_DILLO = new TimedState() {
        @Override
        public void execute() {
            KeyBindUtil.rightClick();
        }

        @Override
        public long delayAfter() {
            return 100;
        }
    };
    private static final TimedState SWITCH_TO_DRILL = new TimedState() {
        @Override
        public void execute() {
            Ref.player().inventory.currentItem = ArmageddonConfig.drillSlot - 1;
        }

        @Override
        public long delayAfter() {
            return 100;
        }
    };
    private static final ConditionalState NORMALIZE_PITCH = new ConditionalState() {
        @Override
        public void execute() {
            RotationUtil.ease(new RotationUtil.Rotation(Ref.player().rotationYaw, -3f), 100);
        }

        @Override
        public boolean canNext() {
            return Ref.world().loadedEntityList.stream().filter(
                    entity -> entity instanceof EntityZombie && entity.isInvisible() && Ref.player().getDistanceSqToEntity(entity) < 10
            ).findAny().orElse(null) != null;
        }
    };
    private static final ConditionalState MOUNT_DILLO = new ConditionalState() {
        @Override
        public void execute() {
            KeyBindUtil.rightClick();
        }

        @Override
        public boolean canNext() {
            return Ref.player().isRiding();
        }
    };
    private static final TimedState MINE = new TimedState() {
        @Override
        public void execute() {
            KeyBindUtil.jump();
            RotationUtil.easeCertain(new RotationUtil.Rotation(ArmageddonConfig.swipeRange, Ref.player().rotationPitch), ArmageddonConfig.swipeTime);
        }

        @Override
        public long delayAfter() {
            return ArmageddonConfig.swipeTime;
        }
    };
    private static final TimedState SWITCH_TO_ROD_AGAIN = new TimedState() {
        @Override
        public void execute() {
            Ref.player().inventory.currentItem = ArmageddonConfig.rodSlot - 1;
        }

        @Override
        public long delayAfter() {
            return 100;
        }
    };
    private static final ConditionalState DISMOUNT_DILLO = new ConditionalState() {
        @Override
        public void execute() {
            KeyBindUtil.rightClick();
        }

        @Override
        public boolean canNext() {
            return !Ref.player().isRiding();
        }
    };
    private static final TimedState SWITCH_TO_AOTV = new TimedState() {
        @Override
        public void execute() {
            Ref.player().inventory.currentItem = ArmageddonConfig.aotvSlot - 1;
        }

        @Override
        public long delayAfter() {
            return 100;
        }
    };
    private static final TimedState SNEAK = new TimedState() {
        @Override
        public void execute() {
            KeyBindUtil.setKey(Ref.mc().gameSettings.keyBindSneak.getKeyCode(), true);
        }

        @Override
        public long delayAfter() {
            return 100;
        }
    };
    private static final TimedState LOOK_AT_BLOCK = new TimedState() {
        @Override
        public void execute() {
            RotationUtil.ease(AngleUtil.getRotationForBlock(Armageddon.getInstance().getRouteManager().getNext()), ArmageddonConfig.lookAtBlockTime);
        }

        @Override
        public long delayAfter() {
            return ArmageddonConfig.lookAtBlockTime;
        }
    };
    private static final ConditionalState TELEPORT = new ConditionalState() {
        @Override
        public void execute() {
            i = Armageddon.getInstance().getRouteManager().getStandingOn() + 1;
            KeyBindUtil.rightClick();
        }

        @Override
        public boolean canNext() {
            return Armageddon.getInstance().getRouteManager().getStandingOn() != i;
        }
    };
    private static final TimedState UNSNEAK = new TimedState() {
        @Override
        public void execute() {
            KeyBindUtil.setKey(Ref.mc().gameSettings.keyBindSneak.getKeyCode(), false);
        }

        @Override
        public long delayAfter() {
            return 100;
        }
    };

    public ArmadilloMacro() {
        super(
                Arrays.asList(SWITCH_TO_ROD,
                        SUMMON_DILLO,
                        SWITCH_TO_DRILL,
                        NORMALIZE_PITCH,
                        MOUNT_DILLO,
                        MINE,
                        SWITCH_TO_ROD_AGAIN,
                        DISMOUNT_DILLO,
                        SWITCH_TO_AOTV,
                        SNEAK,
                        LOOK_AT_BLOCK,
                        TELEPORT,
                        UNSNEAK)
        );
    }

    @Override
    protected boolean onEnable() {
        if(Armageddon.getInstance().getRouteManager().getStandingOn() == -1) {
            Logger.error("You are not standing on a block on the route. Make sure to AOTV on it, so you are centered.");
            return false;
        }
        Logger.info("Starting macro.");
        return true;
    }

    @Override
    protected void onDisable() {
        Logger.info("Stopping macro.");
    }
}