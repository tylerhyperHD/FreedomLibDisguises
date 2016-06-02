package me.libraryaddict.disguise.disguisetypes;

import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Guardian;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Skeleton.SkeletonType;
import org.bukkit.entity.Zombie;

public enum DisguiseType {

    AREA_EFFECT_CLOUD(3, 0),
    ARMOR_STAND(78),
    ARROW(60, 0),
    BAT,
    BLAZE,
    BOAT(1),
    CAVE_SPIDER,
    CHICKEN,
    COW,
    CREEPER,
    DONKEY,
    DRAGON_FIREBALL(93),
    DROPPED_ITEM(2, 1),
    EGG(62),
    ELDER_GUARDIAN,
    ENDER_CRYSTAL(51),
    ENDER_DRAGON,
    ENDER_PEARL(65),
    ENDER_SIGNAL(72),
    ENDERMAN,
    ENDERMITE,
    EXPERIENCE_ORB,
    FALLING_BLOCK(70, 1),
    FIREBALL(63),
    FIREWORK(76),
    FISHING_HOOK(90),
    GHAST,
    GIANT,
    GUARDIAN,
    HORSE,
    IRON_GOLEM,
    ITEM_FRAME(71),
    LEASH_HITCH(77),
    MAGMA_CUBE,
    MINECART(10),
    MINECART_CHEST(10, 1),
    MINECART_COMMAND(10, 6),
    MINECART_FURNACE(10, 2),
    MINECART_HOPPER(10, 5),
    MINECART_MOB_SPAWNER(10, 4),
    MINECART_TNT(10, 3),
    MULE,
    MUSHROOM_COW,
    OCELOT,
    PAINTING,
    PIG,
    PIG_ZOMBIE,
    PLAYER,
    PRIMED_TNT(50),
    RABBIT,
    SHEEP,
    SHULKER,
    SHULKER_BULLET(67),
    SILVERFISH,
    SKELETON,
    SKELETON_HORSE,
    SLIME,
    SMALL_FIREBALL(63),
    SNOWBALL(61),
    SNOWMAN,
    SPECTRAL_ARROW(91),
    SPIDER,
    SPLASH_POTION(73, 0),
    SQUID,
    TIPPED_ARROW(92),
    THROWN_EXP_BOTTLE(75),
    UNDEAD_HORSE,
    VILLAGER,
    WITCH,
    WITHER,
    WITHER_SKELETON,
    WITHER_SKULL(66),
    WOLF,
    ZOMBIE,
    ZOMBIE_VILLAGER,
    UNKNOWN;

    static {
        // We set the entity type in this so that we can safely ignore disguisetypes which don't exist in older versions of MC.
        // Without erroring up everything.

        for (DisguiseType type : values()) {

            try {
                DisguiseType toUse = type;
                String name;

                switch (type) {
                    // Disguise item frame isn't supported. So we don't give it a entity type which should prevent it from being..
                    // Usable.
                    case ITEM_FRAME:
                        break;
                    case DONKEY:
                    case MULE:
                    case UNDEAD_HORSE:
                    case SKELETON_HORSE:
                        toUse = DisguiseType.HORSE;
                        break;
                    case ZOMBIE_VILLAGER:
                        toUse = DisguiseType.ZOMBIE;
                        break;
                    case WITHER_SKELETON:
                        toUse = DisguiseType.SKELETON;
                        break;
                    case ELDER_GUARDIAN:
                        toUse = DisguiseType.GUARDIAN;
                        break;
                    default:
                        break;
                }

                name = toUse.name();

                type.setEntityType(EntityType.valueOf(name));
            } catch (Throwable ex) {
                // This version of Spigot doesn't have the disguise.
            }
        }
    }

    public static DisguiseType getType(Entity entity) {
        DisguiseType disguiseType = getType(entity.getType());

        switch (disguiseType) {
            case ZOMBIE:

                if (((Zombie) entity).isVillager()) {
                    disguiseType = DisguiseType.ZOMBIE_VILLAGER;
                }

                break;

            case HORSE:

                disguiseType = DisguiseType.valueOf(((Horse) entity).getVariant().name());

                break;

            case SKELETON:

                if (((Skeleton) entity).getSkeletonType() == SkeletonType.WITHER) {
                    disguiseType = DisguiseType.WITHER_SKELETON;
                }

                break;
            case GUARDIAN:

                if (((Guardian) entity).isElder()) {
                    disguiseType = DisguiseType.ELDER_GUARDIAN;
                }

                break;
            default:
                break;
        }

        return disguiseType;

    }

    public static DisguiseType getType(EntityType entityType) {
        try {
            return valueOf(entityType.name().toUpperCase());
        } catch (Throwable ex) {
            return DisguiseType.UNKNOWN;
        }
    }

    private int objectId = -1, defaultData = 0;

    private EntityType entityType;

    private Class<? extends FlagWatcher> watcherClass;

    DisguiseType(int... ints) {
        for (int i = 0; i < ints.length; i++) {
            int value = ints[i];

            switch (i) {
                case 0:
                    objectId = value;

                    break;
                case 1:
                    defaultData = value;

                    break;
                default:
                    break;
            }
        }
    }

    public int getDefaultData() {
        return defaultData;
    }

    public Class<? extends Entity> getEntityClass() {
        if (entityType != null) {
            return getEntityType().getEntityClass();
        }

        return Entity.class;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    /**
     * The TYPE id of this entity. Different from the Object Id send in spawn
     * packets when spawning miscs.
     *
     * @return
     */
    public int getTypeId() {
        return (int) getEntityType().getTypeId();
    }

    /**
     * The object type send in packets when spawning a misc entity. Otherwise,
     * -1.
     *
     * @return
     */
    public int getObjectId() {
        return objectId;
    }

    public Class getWatcherClass() {
        return watcherClass;
    }

    public boolean isMisc() {
        return getEntityType() != null && !getEntityType().isAlive();
    }

    public boolean isMob() {
        return getEntityType() != null && getEntityType().isAlive() && !isPlayer();
    }

    public boolean isPlayer() {
        return this == DisguiseType.PLAYER;
    }

    public boolean isUnknown() {
        return this == DisguiseType.UNKNOWN;
    }

    private void setEntityType(EntityType entityType) {
        this.entityType = entityType;
    }

    public void setWatcherClass(Class<? extends FlagWatcher> c) {
        watcherClass = c;
    }

    public String toReadable() {
        String[] split = name().split("_");

        for (int i = 0; i < split.length; i++) {
            split[i] = split[i].substring(0, 1) + split[i].substring(1).toLowerCase();
        }

        return StringUtils.join(split, " ");
    }
}
